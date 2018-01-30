package org.dataportabilityproject.cloud.microsoft.cosmos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dataportabilityproject.spi.cloud.types.PortabilityJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.scassandra.Scassandra;
import org.scassandra.ScassandraFactory;
import org.scassandra.http.client.PreparedStatementExecution;
import org.scassandra.http.client.PrimingRequest;
import org.scassandra.http.client.types.ColumnMetadata;

import java.util.Collections;
import java.util.Map;

import static org.dataportabilityproject.cloud.microsoft.cosmos.CosmosStore.JOB_DELETE;
import static org.dataportabilityproject.cloud.microsoft.cosmos.CosmosStore.JOB_INSERT;
import static org.dataportabilityproject.cloud.microsoft.cosmos.CosmosStore.JOB_QUERY;
import static org.dataportabilityproject.cloud.microsoft.cosmos.CosmosStore.JOB_UPDATE;
import static org.dataportabilityproject.spi.cloud.types.PortabilityJob.State.COMPLETE;
import static org.scassandra.cql.PrimitiveType.VARCHAR;
import static org.scassandra.matchers.Matchers.preparedStatementRecorded;

/**
 *
 */
public class CosmosStoreTest {
    private CosmosStore cosmosStore;
    private Scassandra cassandra;

    @Test
    @Ignore
    public void verifyCreateAndFind() throws Exception {
        PrimingRequest.Then.ThenBuilder thenInsert = PrimingRequest.then();
        thenInsert.withVariableTypes(VARCHAR, VARCHAR);
        PrimingRequest createRequest = PrimingRequest.preparedStatementBuilder().withQuery(JOB_INSERT).withThen(thenInsert).build();

        cassandra.primingClient().prime(createRequest);

        PortabilityJob primeJob = new PortabilityJob();
        primeJob.setId("1");
        Map row = Collections.singletonMap("job_data", new ObjectMapper().writeValueAsString(primeJob));

        PrimingRequest.Then.ThenBuilder thenQuery = PrimingRequest.then();
        PrimingRequest findRequest = PrimingRequest.preparedStatementBuilder()
                .withQuery(JOB_QUERY)
                .withThen(thenQuery.withVariableTypes(VARCHAR).withColumnTypes(ColumnMetadata.column("job_id", VARCHAR)).withRows(row))
                .build();

        cassandra.primingClient().prime(findRequest);

        PrimingRequest.Then.ThenBuilder thenUpdate = PrimingRequest.then();
        PrimingRequest updateRequest = PrimingRequest.preparedStatementBuilder()
                .withQuery(JOB_UPDATE)
                .withThen(thenUpdate.withVariableTypes(VARCHAR).withColumnTypes(ColumnMetadata.column("job_data", VARCHAR)))
                .build();

        cassandra.primingClient().prime(updateRequest);

        PrimingRequest.Then.ThenBuilder thenRemove = PrimingRequest.then();
        thenRemove.withVariableTypes(VARCHAR);
        PrimingRequest removeRequest = PrimingRequest.preparedStatementBuilder().withQuery(JOB_DELETE).withThen(thenRemove).build();
        cassandra.primingClient().prime(removeRequest);

        PortabilityJob createJob = new PortabilityJob();
        cosmosStore.create(createJob);

        PortabilityJob copy = cosmosStore.find(createJob.getId());
        copy.setState(COMPLETE);
        cosmosStore.update(copy);

        cosmosStore.remove(copy.getId());

        PreparedStatementExecution expectedStatement = PreparedStatementExecution.builder()
            .withPreparedStatementText(JOB_DELETE)
            .withConsistency("LOCAL_ONE")
            .withVariables("1")
            .build();

        Assert.assertThat(cassandra.activityClient().retrievePreparedStatementExecutions(), preparedStatementRecorded(expectedStatement));
    }

    @Before
    public void setUp() {
        cassandra = ScassandraFactory.createServer();
        cassandra.start();

        int port = cassandra.getBinaryPort();
        ObjectMapper mapper = new ObjectMapper();
        cosmosStore = new Initializer().getLocal(port, mapper);
    }
}
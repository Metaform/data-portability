package org.dataportabilityproject.cloud.microsoft.cosmos;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dataportabilityproject.spi.cloud.storage.JobStore;
import org.dataportabilityproject.spi.cloud.types.PortabilityJob;
import org.dataportabilityproject.types.transfer.models.DataModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.dataportabilityproject.cloud.microsoft.cosmos.MicrosoftCloudConstants.JOB_TABLE;

/**
 * A {@link JobStore} backed by CosmosDB. This implementation uses the DataStax Cassandra driver.
 */
public class CosmosStore implements JobStore {
    static final String JOB_INSERT = "INSERT INTO  " + JOB_TABLE + " (job_id, job_data) VALUES (?,?)";
    static final String JOB_QUERY = "SELECT * FROM " + JOB_TABLE + " WHERE job_id = ?";
    static final String JOB_DELETE = "DELETE FROM " + JOB_TABLE + " WHERE job_id = ?";
    static final String JOB_UPDATE = "UPDATE " + JOB_TABLE + "SET job_data = ? WHERE job_id = ?";

    private Session session;
    private ObjectMapper mapper;

    public CosmosStore(Session session, ObjectMapper mapper) {
        this.session = session;
        this.mapper = mapper;
    }

    public void close() {
        if (session != null) {
            session.close();
        }
    }

    @Override
    public void create(PortabilityJob job) {
        if (job.getId() != null) {
            throw new IllegalStateException("Job already created: " + job.getId());
        }
        UUID uuid = UUID.randomUUID();
        job.setId(uuid.toString());

        PreparedStatement statement = session.prepare(JOB_INSERT);
        BoundStatement boundStatement = new BoundStatement(statement);
        try {
            boundStatement.setUUID(0, uuid);
            boundStatement.setString(1, mapper.writeValueAsString(job));
            session.execute(boundStatement);
        } catch (JsonProcessingException e) {
            throw new MicrosoftStorageException("Error creating job: " + job.getId(), e);
        }
    }

    @Override
    public void update(PortabilityJob job) {
        if (job.getId() == null) {
            throw new IllegalStateException("Job not persisted: " + job.getId());
        }

        PreparedStatement statement = session.prepare(JOB_UPDATE);
        BoundStatement boundStatement = new BoundStatement(statement);
        try {
            boundStatement.setString(0,mapper.writeValueAsString(job));
            boundStatement.setUUID(1, UUID.fromString(job.getId()));
            session.execute(boundStatement);
        } catch (JsonProcessingException e) {
            throw new MicrosoftStorageException("Error deleting job: " + job.getId(), e);
        }
    }

    @Override
    public PortabilityJob find(String id) {
        PreparedStatement statement = session.prepare(JOB_QUERY);
        BoundStatement boundStatement = new BoundStatement(statement);
        boundStatement.bind(UUID.fromString(id));

        Row row = session.execute(boundStatement).one();
        String serialized = row.getString("job_data");
        try {
            return mapper.readValue(serialized, PortabilityJob.class);
        } catch (IOException e) {
            throw new MicrosoftStorageException("Error deserializng job: " + id, e);
        }
    }

    @Override
    public void remove(String id) {
        PreparedStatement statement = session.prepare(JOB_DELETE);
        BoundStatement boundStatement = new BoundStatement(statement);
        boundStatement.setUUID(0,UUID.fromString(id));
        session.execute(boundStatement);
    }

    @Override
    public <T extends DataModel> T getData(Class<T> type, String id) {
        return null;
    }

    @Override
    public <T extends DataModel> void store(String jobId, T model) {

    }

    @Override
    public void store(String key, String jobId, InputStream stream) {

    }

    @Override
    public InputStream getStream(String key) {
        return null;
    }


}

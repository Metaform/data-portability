package org.dataportabilityproject.transfer.microsoft.contacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import ezvcard.VCard;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.dataportabilityproject.spi.transfer.provider.ExportResult;
import org.dataportabilityproject.spi.transfer.provider.Exporter;
import org.dataportabilityproject.spi.transfer.types.ContinuationData;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformResult;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformerService;
import org.dataportabilityproject.types.transfer.auth.TokenAuthData;
import org.dataportabilityproject.types.transfer.models.DataModel;
import org.dataportabilityproject.types.transfer.models.contacts.ContactsModelWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Exports Microsoft contacts using the Graph API.
 */
public class MicrosoftContactsExporter implements Exporter<TokenAuthData, DataModel> {
    private static final String CONTACTS_URL = "https://graph.microsoft.com/v1.0/me/contacts";
    private static final String ODATA_NEXT = "@odata.nextLink";

    private OkHttpClient client;
    private ObjectMapper objectMapper;
    private TransformerService transformerService;

    public MicrosoftContactsExporter(OkHttpClient client, ObjectMapper objectMapper, TransformerService transformerService) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.transformerService = transformerService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExportResult<DataModel> export(TokenAuthData authData) {
        Request.Builder graphReqBuilder = new Request.Builder().url(CONTACTS_URL);
        graphReqBuilder.header("Authorization", "Bearer " + authData.getToken());

        try (Response graphResponse = client.newCall(graphReqBuilder.build()).execute()) {
            ResponseBody body = graphResponse.body();
            if (body == null) {
                return new ExportResult<>(ExportResult.ResultType.ERROR, "Error retrieving contacts: response body was null");
            }
            String graphBody = new String(body.bytes());
            Map graphMap = objectMapper.reader().forType(Map.class).readValue(graphBody);
            String nextLink = (String) graphMap.get("@odata.nextLink");
            List<Map<String, Object>> rawContacts = (List<Map<String, Object>>) graphMap.get("value");
            if (rawContacts == null) {
                return new ExportResult<>(ExportResult.ResultType.END);
            }
            ContactsModelWrapper wrapper = transform(rawContacts);
            return new ExportResult<>(ExportResult.ResultType.CONTINUE, wrapper);
        } catch (IOException e) {
            e.printStackTrace();  // FIXME log error
            return new ExportResult<>(ExportResult.ResultType.ERROR, "Error retrieving contacts: " + e.getMessage());
        }
    }

    @Override
    public ExportResult<DataModel> export(TokenAuthData authData, ContinuationData continuationData) {
        throw new UnsupportedOperationException();
    }

    private ContactsModelWrapper transform(List<Map<String, Object>> rawContacts) {
        List<String> contacts = new ArrayList<>();

        for (Map<String, Object> rawContact : rawContacts) {
            TransformResult<VCard> result = transformerService.transform(VCard.class, rawContact);
            if (result.hasProblems()) {
                // discard
                // FIXME log problem
                continue;
            }
            String serialized = result.getTransformed().writeJson();
            contacts.add(serialized);
        }

        return new ContactsModelWrapper(contacts);
    }

}

package org.dataportabilityproject.transfer.microsoft.contacts;

import ezvcard.VCard;
import ezvcard.io.json.JCardReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.dataportabilityproject.spi.transfer.provider.ImportResult;
import org.dataportabilityproject.spi.transfer.provider.Importer;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformResult;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformerService;
import org.dataportabilityproject.types.transfer.auth.TokenAuthData;
import org.dataportabilityproject.types.transfer.models.contacts.ContactsModelWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 *
 */
public class MicrosoftContactsImporter implements Importer<TokenAuthData, ContactsModelWrapper> {
    private static final String CONTACTS_URL = "https://graph.microsoft.com/v1.0/me/contacts";
    private static final String BATCH_URL = "https://graph.microsoft.com/beta/$batch";

    private OkHttpClient client;
    private TransformerService transformerService;

    public MicrosoftContactsImporter(OkHttpClient client, TransformerService transformerService) {
        this.client = client;
        this.transformerService = transformerService;
    }

    @Override
    public ImportResult importItem(TokenAuthData authData, ContactsModelWrapper wrapper) {
        JCardReader reader = new JCardReader(wrapper.getVCards());
        try {
            List<VCard> cards = reader.readAll();

            List<String> problems = new ArrayList<>();
            int[] id = new int[]{1};
            List<Map<String, Object>> requests = cards.stream().map(card -> {
                TransformResult<LinkedHashMap> result = transformerService.transform(LinkedHashMap.class, card);
                problems.addAll(result.getProblems());
                Map<?, ?> contact = result.getTransformed();
                Map<String, Object> request = new LinkedHashMap<>();
                request.put("id", id);
                request.put("method", "POST");
                request.put("url", CONTACTS_URL);
                request.put("body", contact);
                id[0]++;
                return request;
            }).collect(toList());

            if (!problems.isEmpty()) {
                // TODO log problems
            }

            Map<String, Object> batch = new LinkedHashMap<>();
            batch.put("requests", requests);

            Request.Builder graphReqBuilder = new Request.Builder().url(BATCH_URL);
            graphReqBuilder.header("Authorization", "Bearer " + authData.getToken());
            try (Response graphResponse = client.newCall(graphReqBuilder.build()).execute()) {
                ResponseBody body = graphResponse.body();
                System.out.println("Received response");
            }

            return new ImportResult(ImportResult.ResultType.OK);
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
            return new ImportResult(ImportResult.ResultType.ERROR, "Error deserializing contacts: " + e.getMessage());
        }
    }
}

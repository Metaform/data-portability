package org.dataportabilityproject.auth.microsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.dataportabilityproject.spi.gateway.types.AuthFlowConfiguration;
import org.dataportabilityproject.types.transfer.auth.TokenAuthData;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import static java.lang.System.getProperty;
import static junit.framework.TestCase.fail;

/**
 *
 */
public class AuthTestDriver {
    private String clientId;
    private String secret;

    private String callbackHost;
    private String callbackBase;
    private String authRetrievalUrl;

    public AuthTestDriver() {
        this.callbackHost = Objects.requireNonNull(getProperty("callbackHost"), "Callback host");
        String callbackPort = Objects.requireNonNull(getProperty("callbackPort"), "Callback port");
        this.clientId = Objects.requireNonNull(getProperty("clientId"), "Client ID");
        this.secret = Objects.requireNonNull(getProperty("secret"), "Client secret");

        callbackBase = "https://" + callbackHost + ":" + callbackPort;
        authRetrievalUrl = callbackBase + "/code";
    }

    // @Test
    public void runAsTest() throws Exception {
        getOAuthTokenCode();
    }

    /**
     * Performs an OAuth flow using the MicrosoftAuthDataGenerator, returning a token.
     *
     * @return the token
     */
    public String getOAuthTokenCode() throws Exception {

        OkHttpClient client = TestHelper.createTestBuilder(callbackHost).build();
        ObjectMapper mapper = new ObjectMapper();

        MicrosoftAuthDataGenerator dataGenerator = new MicrosoftAuthDataGenerator("/response", () -> clientId, () -> secret, client, mapper);

        AuthFlowConfiguration configuration = dataGenerator.generateConfiguration(callbackBase, "1");

        Desktop desktop = Desktop.getDesktop();

        desktop.browse(new URI(configuration.getUrl()));

        // Execute the request and retrieve the auth code.
        String authCode = retrieveAuthCode(client);

        // get the token
        TokenAuthData tokenData = dataGenerator.generateAuthData(callbackBase, authCode, "1", configuration.getInitialAuthData(), null);

        System.out.println("TOKEN: " + tokenData.getToken());
        return tokenData.getToken();
    }

    private String retrieveAuthCode(OkHttpClient client) throws IOException {
        Request.Builder builder = new Request.Builder().url(authRetrievalUrl);

        try (Response authResponse = client.newCall(builder.build()).execute()) {
            ResponseBody authBody = authResponse.body();
            if (authBody == null) {
                fail("AUTH ERROR: " + authResponse.code() + ":" + "<empty body>");
            }
            String authCode = new String(authBody.bytes());

            System.out.println("AUTH: " + authResponse.code() + ":" + authCode);

            return authCode;
        }
    }


}

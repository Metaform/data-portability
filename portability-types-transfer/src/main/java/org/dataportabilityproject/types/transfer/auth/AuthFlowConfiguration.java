package org.dataportabilityproject.types.transfer.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.dataportabilityproject.types.transfer.PortableType;

/**
 * Configuration for an authorization flow. A flow has an initial URL and optional initial authentication data.
 */
public class AuthFlowConfiguration extends PortableType {
    private String url;
    private AuthData initialAuthData;

    /**
     * Ctor used when the flow does not require initial authentication data.
     *
     * @param url the initial URL.
     */
    public AuthFlowConfiguration(String url) {
        this.url = url;
    }

    /**
     * Ctor.
     *
     * @param url the initial URL.
     * @param initialAuthData the initial authentication data
     */
    @JsonCreator
    public AuthFlowConfiguration(@JsonProperty("url") String url, @JsonProperty("initialAuthData") AuthData initialAuthData) {
        this.url = url;
        this.initialAuthData = initialAuthData;
    }

    /**
     * Returns the initial flow URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the initial authentication data or null if the flow does not use it.
     */
    public AuthData getInitialAuthData() {
        return initialAuthData;
    }
}
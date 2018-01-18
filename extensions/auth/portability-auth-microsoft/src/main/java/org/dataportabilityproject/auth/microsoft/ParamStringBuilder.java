package org.dataportabilityproject.auth.microsoft;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Builds a URL query part.
 */
public class QueryPartBuilder {
    private enum State {
        START, START_PARAM, PARAM_VALUE, END_PARAM
    }

    private StringBuilder builder = new StringBuilder("?");

    private State state = State.START;

    public QueryPartBuilder startParam(String name) {
        if (State.END_PARAM == state) {
            builder.append("&");
        }
        builder.append(name).append("=");
        state = State.START_PARAM;
        return this;
    }

    public QueryPartBuilder value(String value) {
        if (State.PARAM_VALUE != state && State.START_PARAM != state) {
            throw new IllegalStateException("Parameter name not specified");
        }
        try {
            if (State.PARAM_VALUE == state) {
                builder.append("%20");
            }
            builder.append(URLEncoder.encode(value, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Invalid query param value: " + value);
        }
        state = State.PARAM_VALUE;
        return this;
    }

    public QueryPartBuilder endParam() {
        state = State.END_PARAM;
        return this;
    }

    public String build() {
        return builder.toString();
    }

}

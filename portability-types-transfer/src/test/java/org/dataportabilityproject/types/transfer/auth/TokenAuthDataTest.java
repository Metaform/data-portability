package org.dataportabilityproject.types.transfer.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TokenAuthDataTest {

    @Test
    public void verifySerializeDeserialize() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String serialized = objectMapper.writeValueAsString(new TokenAuthData("testToken"));
        TokenAuthData deserialized = objectMapper.readValue(serialized, TokenAuthData.class);
        Assert.assertEquals("testToken", deserialized.getToken());
    }
}
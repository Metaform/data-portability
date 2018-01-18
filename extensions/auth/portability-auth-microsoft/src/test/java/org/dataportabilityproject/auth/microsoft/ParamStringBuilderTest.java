package org.dataportabilityproject.auth.microsoft;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class QueryPartBuilderTest {

    @Test
    public void verifySingleParam() {
        QueryPartBuilder builder = new QueryPartBuilder();
        builder.startParam("param1").value("value1").endParam();
        Assert.assertEquals("?param1=value1", builder.build());
    }

    @Test
    public void verifyMultipleParams() {
        QueryPartBuilder builder = new QueryPartBuilder();
        builder.startParam("param1").value("value1").endParam();
        builder.startParam("param2").value("value2").endParam();
        Assert.assertEquals("?param1=value1&param2=value2", builder.build());
    }

    @Test
    public void verifyMultipleValues() {
        QueryPartBuilder builder = new QueryPartBuilder();
        builder.startParam("param1").value("value1").value("value2").endParam();
        Assert.assertEquals("?param1=value1%20value2", builder.build());
    }

    @Test
    public void verifyEncodedValues() {
        QueryPartBuilder builder = new QueryPartBuilder();
        builder.startParam("param1").value("value1&").value("value2&").endParam();
        Assert.assertEquals("?param1=value1%26%20value2%26", builder.build());
    }

}
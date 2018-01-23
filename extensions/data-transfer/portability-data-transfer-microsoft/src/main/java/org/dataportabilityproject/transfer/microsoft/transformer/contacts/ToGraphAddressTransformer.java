package org.dataportabilityproject.transfer.microsoft.transformer.contacts;

import ezvcard.property.Address;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformerContext;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Maps from a VCard Address to a Graph API physical address resource as defined by: https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/resources/physicaladdress.
 */
public class ToGraphAddressTransformer implements BiFunction<Address, TransformerContext, Map<String, String>> {

    @Override
    public Map<String, String> apply(Address address, TransformerContext context) {
        return null;
    }
}
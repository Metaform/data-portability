package org.dataportabilityproject.transfer.microsoft.transformer.contacts;

import ezvcard.property.Address;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformerContext;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Maps from a Graph API physical address resource as defined by: https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/resources/physicaladdress.
 */
public class PhysicalAddressTransformer implements BiFunction<Map<String, String>, TransformerContext, Address> {

    @Override
    public Address apply(Map<String, String> addressMap, TransformerContext context) {
        Address address = new Address();


        return address;
    }
}

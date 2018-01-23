package org.dataportabilityproject.transfer.microsoft.transformer.contacts;

import ezvcard.VCard;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformerContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Maps from a VCard to a Microsoft Graph contacts resource as defined by https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/resources/contact.
 */
public class ToGraphContactTransformer implements BiFunction<VCard, TransformerContext, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(VCard card, TransformerContext context) {
        Map<String, Object> contact = new LinkedHashMap<>();
        contact.put("givenName", card.getStructuredName().getGiven());
        contact.put("surname", card.getStructuredName().getFamily());
        return contact;
    }
}

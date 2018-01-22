package org.dataportabilityproject.transfer.microsoft.transformer.contacts;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.StructuredName;
import org.dataportabilityproject.transfer.microsoft.transformer.TransformerContext;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Transforms from a Microsoft Graph contacts resource to a VCard as defined by https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/resources/contact.
 */
public class VCardTransformer implements BiFunction<Map<String, Object>, TransformerContext, VCard> {

    @SuppressWarnings("unchecked")
    @Override
    public VCard apply(Map<String, Object> map, TransformerContext context) {
        VCard card = new VCard();

        String givenName = (String) map.get("givenName");
        String surname = (String) map.get("surname");
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(surname);
        structuredName.setGiven(givenName);
        card.setStructuredName(structuredName);

        String displayName = (String) map.get("displayName");
        card.setFormattedName(displayName);

        String title = (String) map.get("title");
        card.addTitle(title);

        Map<String, String> businessAddress = (Map<String, String>) map.get("businessAddress");
        if (businessAddress != null) {
            Address address = context.transform(Address.class, businessAddress, context);
            card.addAddress(address);
        }

        return card;
    }
}

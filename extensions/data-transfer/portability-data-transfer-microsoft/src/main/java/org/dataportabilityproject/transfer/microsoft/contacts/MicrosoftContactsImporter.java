package org.dataportabilityproject.transfer.microsoft.contacts;

import ezvcard.VCard;
import ezvcard.io.json.JCardReader;
import org.dataportabilityproject.spi.transfer.provider.ImportResult;
import org.dataportabilityproject.spi.transfer.provider.Importer;
import org.dataportabilityproject.types.transfer.auth.TokenAuthData;
import org.dataportabilityproject.types.transfer.models.contacts.ContactsModelWrapper;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class MicrosoftContactsImporter implements Importer<TokenAuthData, ContactsModelWrapper> {
    private static final String CONTACTS_URL = "https://graph.microsoft.com/v1.0/me/contacts";

    @Override
    public ImportResult importItem(TokenAuthData authData, ContactsModelWrapper wrapper) {
        JCardReader reader = new JCardReader(wrapper.getVCards());
        try {
            List<VCard> cards = reader.readAll();

            return new ImportResult(ImportResult.ResultType.OK);
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
            return new ImportResult(ImportResult.ResultType.ERROR, "Error deserializing contacts: " + e.getMessage());
        }
    }
}

package org.dataportabilityproject.transfer.microsoft.contacts;

import org.dataportabilityproject.spi.transfer.provider.ImportResult;
import org.dataportabilityproject.spi.transfer.provider.Importer;
import org.dataportabilityproject.types.transfer.auth.TokenAuthData;
import org.dataportabilityproject.types.transfer.models.contacts.ContactsModelWrapper;

/**
 *
 */
public class MicrosoftContactsImporter implements Importer<TokenAuthData, ContactsModelWrapper> {

    @Override
    public ImportResult importItem(TokenAuthData authData, ContactsModelWrapper data) {
        return null;
    }
}

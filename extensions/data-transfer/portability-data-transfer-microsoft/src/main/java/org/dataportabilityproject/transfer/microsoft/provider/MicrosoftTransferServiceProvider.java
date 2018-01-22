package org.dataportabilityproject.transfer.microsoft.provider;

import org.dataportabilityproject.spi.transfer.provider.Exporter;
import org.dataportabilityproject.spi.transfer.provider.Importer;
import org.dataportabilityproject.spi.transfer.provider.TransferServiceProvider;
import org.dataportabilityproject.transfer.microsoft.contacts.MicrosoftContactsExporter;

/**
 *
 */
public class MicrosoftTransferServiceProvider implements TransferServiceProvider {
    private static final String CONTACTS = "contacts"; // TODO we should standardize the naming scheme if it isn't already

    private static final MicrosoftContactsExporter CONTACTS_EXPORTER = new MicrosoftContactsExporter();

    @Override
    public String getServiceId() {
        return "microsoft";
    }

    @Override
    public Exporter<?, ?> getExporter(String transferDataType) {
        if (CONTACTS.equals(transferDataType)) {
            return CONTACTS_EXPORTER;
        }
        throw new UnsupportedOperationException("Unsupported transfer type: " + transferDataType);
    }

    @Override
    public Importer<?, ?> getImporter(String transferDataType) {
        throw new UnsupportedOperationException();
    }
}

package org.dataportabilityproject.transfer.microsoft.provider;

import org.dataportabilityproject.spi.transfer.provider.Exporter;
import org.dataportabilityproject.spi.transfer.provider.Importer;
import org.dataportabilityproject.spi.transfer.provider.TransferServiceProvider;

/**
 *
 */
public class MicrosoftTransferServiceProvider implements TransferServiceProvider{

    @Override
    public String getServiceId() {
        return "microsoft";
    }

    @Override
    public Exporter<?, ?> getExporter(String transferDataType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Importer<?, ?> getImporter(String transferDataType) {
        throw new UnsupportedOperationException();
    }
}

package org.dataportabilityproject.transfer.microsoft.contacts;

import org.dataportabilityproject.spi.transfer.provider.ExportResult;
import org.dataportabilityproject.spi.transfer.provider.Exporter;
import org.dataportabilityproject.spi.transfer.types.ContinuationData;
import org.dataportabilityproject.types.transfer.auth.TokenAuthData;
import org.dataportabilityproject.types.transfer.models.DataModel;

/**
 * Exports Microsoft contacts using the Graph API.
 */
public class MicrosoftContactsExporter implements Exporter<TokenAuthData, DataModel> {

    @Override
    public ExportResult<DataModel> export(TokenAuthData authData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExportResult<DataModel> export(TokenAuthData authData, ContinuationData continuationData) {
        throw new UnsupportedOperationException();
    }
}

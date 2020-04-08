package eu.domibus.connector.security.container;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.europa.esig.dss.CommonDocument;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.MimeType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LargeFileBasedDssDocument extends CommonDocument implements DSSDocument {

    private final LargeFilePersistenceService persistenceService;
    private final LargeFileReference reference;

    public LargeFileBasedDssDocument(LargeFilePersistenceService persistenceService,
                                     LargeFileReference reference) {
        this.persistenceService = persistenceService;
        this.reference = reference;
    }

    @Override
    public InputStream openStream() {
        LargeFileReference readableDataSource = persistenceService.getReadableDataSource(this.reference);
        try (InputStream is = readableDataSource.getInputStream()) {
            return is;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MimeType getMimeType() {
        return MimeType.fromMimeTypeString(reference.getContentType());
    }

    public String getName() {
        return reference.getName();
    }

}

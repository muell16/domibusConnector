package eu.domibus.connector.persistence.service.exceptions;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;

public class LargeFileDeletionException extends LargeFileException {

    private DomibusConnectorBigDataReference referenceFailedToDelete;

    public LargeFileDeletionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DomibusConnectorBigDataReference getReferenceFailedToDelete() {
        return referenceFailedToDelete;
    }

    public void setReferenceFailedToDelete(DomibusConnectorBigDataReference referenceFailedToDelete) {
        this.referenceFailedToDelete = referenceFailedToDelete;
    }
}

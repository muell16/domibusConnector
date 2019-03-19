package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public class DomibusConnectorRejectSubmissionException extends DomibusConnectorMessageTransportException {

    public DomibusConnectorRejectSubmissionException(DomibusConnectorMessage message) {
        super(message);
    }

    public DomibusConnectorRejectSubmissionException(DomibusConnectorMessage message, Throwable cause) {
        super(message, cause);
    }

    public DomibusConnectorRejectSubmissionException(DomibusConnectorMessage message, String reasonMessage, Throwable cause) {
        super(message, reasonMessage, cause);
    }

}

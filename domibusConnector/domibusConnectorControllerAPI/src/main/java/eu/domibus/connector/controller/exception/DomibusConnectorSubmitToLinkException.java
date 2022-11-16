package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.ecodex.dc5.message.model.DC5Message;

/**
 * This exception should be thrown by a link implementation
 *  when the submission of the message has failed!
 */
public class DomibusConnectorSubmitToLinkException extends DomibusConnectorMessageTransportException {

    public DomibusConnectorSubmitToLinkException(DC5Message message, DomibusConnectorRejectionReason reason) {
        super(message, reason);
    }

    public DomibusConnectorSubmitToLinkException(DC5Message message, DomibusConnectorRejectionReason reason, Throwable cause) {
        super(message, reason, cause);
    }

    public DomibusConnectorSubmitToLinkException(DC5Message message, DomibusConnectorRejectionReason reason, String reasonMessage, Throwable cause) {
        super(message, reason, reasonMessage, cause);
    }

    public DomibusConnectorSubmitToLinkException(DC5Message message, String errorMessage) {
        super(message, errorMessage);
    }

}

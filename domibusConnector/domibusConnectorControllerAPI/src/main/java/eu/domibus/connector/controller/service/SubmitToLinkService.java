package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Will be called by the connector
 * controller to submit a message to a link
 *  the specific implementation here has to lookup the
 *  correct link partner
 */
public interface SubmitToLinkService {

    void submitToLink(SubmitToLinkEvent event) throws DomibusConnectorSubmitToLinkException;

    @Builder
    @Getter
    @AllArgsConstructor
    public class SubmitToLinkEvent {
        private DC5TransportRequest.TransportRequestId transportRequestId;
        private String linkName;
        private MessageTargetSource target;
    }

}

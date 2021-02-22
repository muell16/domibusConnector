package eu.domibus.connector.controller.processor.util;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindBusinessMessageByMsgId {

    private final DCMessagePersistenceService msgPersistenceService;

    public DomibusConnectorMessage findBusinessMessageByIdAndDirection(DomibusConnectorMessage refMessage, DomibusConnectorMessageDirection direction) {

        DomibusConnectorMessageDetails messageDetails = refMessage.getMessageDetails();
        String refToEbmsId = messageDetails.getRefToMessageId();

        Optional<DomibusConnectorMessage> messageByEbmsIdOrBackendIdAndDirection = msgPersistenceService.findMessageByEbmsIdOrBackendIdAndDirection(refToEbmsId, direction);
        if (messageByEbmsIdOrBackendIdAndDirection.isPresent()) {
            log.debug("Successfully used refToMessageId [{}] to find business msg", refToEbmsId);
            return messageByEbmsIdOrBackendIdAndDirection.get();
        }

        String refToBackendId = messageDetails.getRefToBackendMessageId();
        messageByEbmsIdOrBackendIdAndDirection = msgPersistenceService.findMessageByEbmsIdOrBackendIdAndDirection(refToBackendId, direction);
        if (messageByEbmsIdOrBackendIdAndDirection.isPresent()) {
            log.debug("Successfully used refToBackendMessageId [{}] to find business msg", refToBackendId);
            return messageByEbmsIdOrBackendIdAndDirection.get();
        }

        throw new DomibusConnectorMessageException(refMessage, FindBusinessMessageByMsgId.class, "Was not able to find related message!");

    }

}

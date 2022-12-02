package eu.ecodex.dc5.message;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindBusinessMessageByMsgId {

    private static final Logger LOGGER = LogManager.getLogger(FindBusinessMessageByMsgId.class);
    private final DC5MessageRepo dc5MessageRepo;

    public @NonNull DC5Message findBusinessMessageByIdAndDirection(final DC5Message refMessage, final DomibusConnectorMessageDirection direction) {

        Optional<EbmsMessageId> refToEbmsId = Optional.ofNullable(refMessage.getEbmsData())
                .map(DC5Ebms::getEbmsMessageId);
        Optional<DC5Message> messageByEbmsIdOrBackendIdAndDirection = refToEbmsId.flatMap(r -> dc5MessageRepo.findOneByEbmsMessageIdAndDirectionTarget(r.getEbmsMesssageId(), direction.getTarget()));

        if (messageByEbmsIdOrBackendIdAndDirection.isPresent()) {
            LOGGER.debug("Successfully used refToMessageId [{}] to find business msg", refToEbmsId);
            return messageByEbmsIdOrBackendIdAndDirection.get();
        }

        Optional<BackendMessageId> refToBackendId = Optional.ofNullable(refMessage.getBackendData())
                .map(DC5BackendData::getRefToBackendMessageId);

        messageByEbmsIdOrBackendIdAndDirection = refToBackendId.flatMap(r -> dc5MessageRepo.findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(r.getBackendMessageId(), direction.getTarget()));
        if (messageByEbmsIdOrBackendIdAndDirection.isPresent()) {
            LOGGER.debug("Successfully used refToBackendMessageId [{}] to find business msg", refToBackendId);
            return messageByEbmsIdOrBackendIdAndDirection.get();
        }

        String error = String.format("Was not able to find related message for refToEbmsId [%s] or refToBackendId [%s] and direction [%s]!", refToEbmsId, refToBackendId, direction);
        throw new DomibusConnectorMessageException(refMessage, FindBusinessMessageByMsgId.class, error);

    }

}

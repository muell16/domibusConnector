package eu.ecodex.dc5.message;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindBusinessMessageByMsgId {

    private static final Logger LOGGER = LogManager.getLogger(FindBusinessMessageByMsgId.class);
    private final DC5MessageRepo dc5MessageRepo;

    public @NonNull DC5Message findBusinessMessageByIdAndDirection(final DC5Message refMessage, final DomibusConnectorMessageDirection direction) {

        Optional<EbmsMessageId> refToEbmsId = Optional.ofNullable(refMessage.getEbmsData())
                .map(DC5Ebms::getEbmsMessageId);
        Optional<DC5Message> messageByEbmsIdOrBackendIdAndDirection = refToEbmsId.flatMap(r -> dc5MessageRepo.findOneByEbmsMessageIdAndDirectionTarget(r, direction.getTarget()));

        if (messageByEbmsIdOrBackendIdAndDirection.isPresent()) {
            LOGGER.debug("Successfully used refToMessageId [{}] to find business msg", refToEbmsId);
            return messageByEbmsIdOrBackendIdAndDirection.get();
        }

        Optional<BackendMessageId> refToBackendId = Optional.ofNullable(refMessage.getBackendData())
                .map(DC5BackendData::getRefToBackendMessageId);

        messageByEbmsIdOrBackendIdAndDirection = refToBackendId.flatMap(refToBackend -> dc5MessageRepo.findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(null, refToBackend, direction.getTarget()));
        if (messageByEbmsIdOrBackendIdAndDirection.isPresent()) {
            LOGGER.debug("Successfully used refToBackendMessageId [{}] to find business msg", refToBackendId);
            return messageByEbmsIdOrBackendIdAndDirection.get();
        }

        String error = String.format("Was not able to find related message for refToEbmsId [%s] or refToBackendId [%s] and direction [%s]!", refToEbmsId, refToBackendId, direction);
        throw new DomibusConnectorMessageException(refMessage, FindBusinessMessageByMsgId.class, error);

    }

    public @NonNull List<DC5Message> findBusinessMsgByConversationId(String s) {
        final DC5Message example = DC5Message.builder().ebmsData(DC5Ebms.builder().conversationId(s).build()).build();
        return dc5MessageRepo.findAll(Example.of(example));
    }

    public @NonNull Optional<DC5Message> findBusinessMsgByRefToMsgId(DC5Message msg) {


        final MessageTargetSource target = msg.getSource();
        Objects.requireNonNull(target, "message source cannot be null!");

        final Optional<DC5Message> result0 = Optional.ofNullable(msg.getRefToConnectorMessageId())
                .flatMap(msgId -> {
                    LOGGER.debug("Trying to find related message with ConnectorMessageId: [{}]", msgId);
                    return dc5MessageRepo.findOneByConnectorMessageId(msgId);
                });
        if (result0.isPresent()) {
            LOGGER.debug("Found related business message [{}] via ref to connector message id", result0.get());
            return result0;
        }

        final Optional<DC5Message> result1 = Optional.ofNullable(msg.getEbmsData())
                .flatMap(e -> Optional.ofNullable(e.getEbmsMessageId()))
                .flatMap(ebmsMessageId -> {
                    LOGGER.debug("Trying to find related message with EBMSMessageId: [{}]", ebmsMessageId);
                    return dc5MessageRepo.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, target);
                });
        if (result1.isPresent()) {
            LOGGER.debug("Found related business message [{}] via ebms message id", result1.get());
            return result1;
        }


        final Optional<DC5Message> result3 = Optional.ofNullable(msg.getBackendData())
                .flatMap(b -> Optional.ofNullable(b.getBackendMessageId()))
                .flatMap(backendMessageId -> {
                    LOGGER.debug("Trying to find related message with BackendMessageId: [{}]", backendMessageId);
                    return dc5MessageRepo.findOneByBackendMessageIdAndDirectionTarget(backendMessageId, target);
                });

        if (result3.isPresent()) {
            LOGGER.debug("Found related business message [{}] via backend message id", result3.get());
            return result3;
        }

        LOGGER.debug("Found no related business message!");
        return Optional.empty();
    }


}

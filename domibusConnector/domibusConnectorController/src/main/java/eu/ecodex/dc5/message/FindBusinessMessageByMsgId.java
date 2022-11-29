package eu.ecodex.dc5.message;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import io.micrometer.core.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@NonNullApi
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

    public @NonNull List<DC5Message> findBusinessMsgByConversationId(String s) {
        final DC5Message example = DC5Message.builder().ebmsData(DC5Ebms.builder().conversationId(s).build()).build();
        return dc5MessageRepo.findAll(Example.of(example));
    }

    public @NonNull Optional<DC5Message> findBusinessMsgByRefToMsgId(DC5Message msg) {

        // needs java 9+
//        return dc5MessageRepo.findOneByEbmsMessageIdAndDirectionTarget(msg.getEbmsData().getRefToEbmsMessageId().getEbmsMesssageId(), msg.getDirection().getTarget())
//                .or(() ->
//                        Optional.ofNullable(msg.getBackendData().getRefToBackendMessageId()).flatMap(ref -> dc5MessageRepo.findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(ref.getBackendMessageId(), msg.getDirection().getTarget()))
//                );

        if (msg.getEbmsData().getRefToEbmsMessageId() != null) {
            final Optional<DC5Message> result = dc5MessageRepo.findOneByEbmsMessageIdAndDirectionTarget(msg.getEbmsData().getRefToEbmsMessageId().getEbmsMesssageId(), msg.getDirection().getTarget());
            if (result.isPresent()) {
                return result;
            }
        }
        final BackendMessageId refToBackendMessageId = msg.getBackendData().getRefToBackendMessageId();
        if (refToBackendMessageId != null) {
            final Optional<DC5Message> result2 = dc5MessageRepo.findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(refToBackendMessageId.getBackendMessageId(), msg.getDirection().getTarget());
            if (result2.isPresent()) {
                return result2;
            }
        }

        return Optional.empty();
    }


}

package eu.ecodex.dc5.message;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindBusinessMessageByMsgId {

    private static final Logger LOGGER = LogManager.getLogger(FindBusinessMessageByMsgId.class);
    private final DC5MessageRepo dc5MessageRepo;


    public @NonNull DC5Message findBusinessMessageByIdAndDirection(final DC5Message refMessage, final DomibusConnectorMessageDirection direction) {

        final MessageTargetSource target = direction.getTarget();
        final RefTo refTo = findOptionalBusinessMessageByIdAndDirection(refMessage, target);
        if (refTo.refToMessage == null) {
            final String error = String.format("Was not able to find related message for refToConnectorMessageId [%s] or refToEbmsId [%s] or refToBackendId [%s] and target [%s]!",
                    refTo.refToConnectorMessageId.orElse(null),
                    refTo.refToEbmsId.orElse(null),
                    refTo.refToBackendMessageId.orElse(null),
                    target);
            throw new DomibusConnectorMessageException(refMessage, FindBusinessMessageByMsgId.class, error);
        } else {
            return refTo.refToMessage;
        }
    }

    private static class RefTo {
        private Optional<DC5MessageId> refToConnectorMessageId;
        private Optional<EbmsMessageId> refToEbmsId;
        private Optional<BackendMessageId> refToBackendMessageId;
        private DC5Message refToMessage;
    }

    private @NonNull RefTo findOptionalBusinessMessageByIdAndDirection(final DC5Message refMessage, final MessageTargetSource target) {
        RefTo refTo = new RefTo();

        final Optional<DC5MessageId> refToConnectorMessageId = Optional.ofNullable(refMessage.getRefToConnectorMessageId());
        refTo.refToConnectorMessageId = refToConnectorMessageId;
        final Optional<DC5Message> result0 = refToConnectorMessageId.flatMap(msgId -> {
                    LOGGER.debug("Trying to find related message with ConnectorMessageId: [{}]", msgId);
                    return dc5MessageRepo.findOneByConnectorMessageId(msgId);
                });
        if (result0.isPresent()) {
            LOGGER.debug("Successfully used refToConnectorMessageId [{}] via ref to connector message id", result0.get());
            refTo.refToMessage = result0.get();
            return refTo;
        }

        Optional<EbmsMessageId> refToEbmsId = Optional.ofNullable(refMessage.getEbmsData())
                .map(DC5Ebms::getRefToEbmsMessageId);
        refTo.refToEbmsId = refToEbmsId;
        Optional<DC5Message> result1 = refToEbmsId.flatMap(r -> dc5MessageRepo.findOneByEbmsMessageIdAndDirectionTarget(r, target));

        if (result1.isPresent()) {
            LOGGER.debug("Successfully used refToMessageId [{}] to find business msg", refToEbmsId);
            refTo.refToMessage = result1.get();
            return refTo;
        }

        Optional<BackendMessageId> refToBackendId = Optional.ofNullable(refMessage.getBackendData())
                .map(DC5BackendData::getRefToBackendMessageId);
        refTo.refToBackendMessageId = refToBackendId;
        Optional<DC5Message> result2 = refToBackendId.flatMap(refToBackend -> dc5MessageRepo.findOneByBackendMessageIdAndDirectionTarget(refToBackend, target));
        if (result2.isPresent()) {
            LOGGER.debug("Successfully used refToBackendMessageId [{}] to find business msg", refToBackendId);
            refTo.refToMessage = result2.get();
            return refTo;
        }
        return refTo;
    }

    public @NonNull List<DC5Message> findBusinessMsgByConversationId(String s) {
        final ArrayList<DC5Message> result = new ArrayList<>();
        dc5MessageRepo.findAllByEbmsData_ConversationId(s).forEach(result::add);
        return result;
    }

    public @NonNull Optional<DC5Message> findBusinessMsgByRefToMsgId(DC5Message msg) {
        final MessageTargetSource target;
//        if (MessageModelHelper.isIncomingBusinessMessage(msg)) {
//            target = msg.getSource();
//        } else if (MessageModelHelper.isOutgoingBusinessMessage(msg)) {
//            target = msg.getSource();
//        } else if (MessageModelHelper.isEvidenceMessage(msg)) {
//            target = msg.getSource();
//        } else if (MessageModelHelper.isEvidenceTriggerMessage(msg)) {
//            target = msg.getSource();
//        } else {
//            throw new IllegalArgumentException("ERROR");
//        }
        target = msg.getSource();

        final RefTo refTo = findOptionalBusinessMessageByIdAndDirection(msg, target);
        return Optional.ofNullable(refTo.refToMessage);
    }


}

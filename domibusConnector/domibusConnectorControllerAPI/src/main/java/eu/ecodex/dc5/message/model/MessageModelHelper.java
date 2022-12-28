package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import org.apache.commons.lang3.ArrayUtils;

public class MessageModelHelper {

    public static boolean isEvidenceTriggerMessage(DC5Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is not allowed to be null!");
        }
        return message.getMessageContent() == null && message.getTransportedMessageConfirmation() != null
                && ArrayUtils.isEmpty(message.getTransportedMessageConfirmation().getEvidence());
    }

    public static boolean isEvidenceMessage(DC5Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is not allowed to be null!");
        }
        return message.getMessageContent() == null && message.getTransportedMessageConfirmation() != null
                && !ArrayUtils.isEmpty(message.getTransportedMessageConfirmation().getEvidence());
    }


    public static boolean isOutgoingBusinessMessage(DC5Message msg) {
        return MessageTargetSource.GATEWAY.equals(msg.getTarget()) &&
                msg.getMessageContent() != null &&
                msg.getMessageContent().getBusinessContent() != null;
    }

    public static boolean isIncomingBusinessMessage(DC5Message msg) {
        return MessageTargetSource.BACKEND.equals(msg.getTarget()) &&
                msg.getMessageContent() != null &&
                msg.getMessageContent().getEcodexContent() != null;
    }
}

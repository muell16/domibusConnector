package eu.ecodex.dc5.message.model;

import org.apache.commons.lang3.ArrayUtils;

public class MessageModelHelper {

    public static boolean isEvidenceTriggerMessage(DC5Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is not allowed to be null!");
        }
        return message.getMessageContent() == null && message.getTransportedMessageConfirmations().size() == 1
                && ArrayUtils.isEmpty(message.getTransportedMessageConfirmations().get(0).getEvidence());
    }

    public static boolean isEvidenceMessage(DC5Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is not allowed to be null!");
        }
        return message.getMessageContent() == null && message.getTransportedMessageConfirmations().size() == 1
                && !ArrayUtils.isEmpty(message.getTransportedMessageConfirmations().get(0).getEvidence());
    }


}

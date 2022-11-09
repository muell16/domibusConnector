package eu.ecodex.dc5.core.model;

public class DC5ModelHelper {


    public static boolean isEvidenceTriggerMessage(DC5Msg message) {
        return (!message.getContent().isPresent() && message.getConfirmations().size() == 1
                && !message.getConfirmations().get(0).getEvidenceXml().isPresent());
    }


}

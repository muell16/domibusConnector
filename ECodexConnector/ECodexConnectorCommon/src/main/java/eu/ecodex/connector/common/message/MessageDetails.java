package eu.ecodex.connector.common.message;

import eu.ecodex.connector.common.db.model.ECodexAction;
import eu.ecodex.connector.common.db.model.ECodexParty;
import eu.ecodex.connector.common.db.model.ECodexService;

/**
 * This is an object that contains informations concerning the message. It is
 * mandatory to build a {@link Message} object.
 * 
 * @author riederb
 * 
 */
public class MessageDetails {

    private String nationalMessageId;
    private String ebmsMessageId;
    private String refToMessageId;
    private String conversationId;
    private String originalSender;
    private String finalRecipient;
    private Long dbMessageId;
    private ECodexAction action;
    private ECodexService service;
    private ECodexParty fromParty;
    private ECodexParty toParty;

    public String getNationalMessageId() {
        return nationalMessageId;
    }

    public void setNationalMessageId(String nationalMessageId) {
        this.nationalMessageId = nationalMessageId;
    }

    public String getEbmsMessageId() {
        return ebmsMessageId;
    }

    public void setEbmsMessageId(String ebmsMessageId) {
        this.ebmsMessageId = ebmsMessageId;
    }

    public String getRefToMessageId() {
        return refToMessageId;
    }

    public void setRefToMessageId(String refToMessageId) {
        this.refToMessageId = refToMessageId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getOriginalSender() {
        return originalSender;
    }

    public void setOriginalSender(String originalSender) {
        this.originalSender = originalSender;
    }

    public String getFinalRecipient() {
        return finalRecipient;
    }

    public void setFinalRecipient(String finalRecipient) {
        this.finalRecipient = finalRecipient;
    }

    public Long getDbMessageId() {
        return dbMessageId;
    }

    public void setDbMessageId(Long dbMessageId) {
        this.dbMessageId = dbMessageId;
    }

    public ECodexAction getAction() {
        return action;
    }

    public void setAction(ECodexAction action) {
        this.action = action;
    }

    public ECodexService getService() {
        return service;
    }

    public void setService(ECodexService service) {
        this.service = service;
    }

    public ECodexParty getFromParty() {
        return fromParty;
    }

    public void setFromParty(ECodexParty fromParty) {
        this.fromParty = fromParty;
    }

    public ECodexParty getToParty() {
        return toParty;
    }

    public void setToParty(ECodexParty toParty) {
        this.toParty = toParty;
    }

    public boolean isValidWithoutPDF() {
        if (action != null && !action.isPdfRequired()) {
            return true;
        }
        return false;
    }

}

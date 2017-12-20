package eu.domibus.connector.common.message;

import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.persistence.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.DomibusConnectorService;

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
    private DomibusConnectorAction action;
    private DomibusConnectorService service;
    private DomibusConnectorParty fromParty;
    private DomibusConnectorParty toParty;

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

    public DomibusConnectorAction getAction() {
        return action;
    }

    public void setAction(DomibusConnectorAction action) {
        this.action = action;
    }

    public DomibusConnectorService getService() {
        return service;
    }

    public void setService(DomibusConnectorService service) {
        this.service = service;
    }

    public DomibusConnectorParty getFromParty() {
        return fromParty;
    }

    public void setFromParty(DomibusConnectorParty fromParty) {
        this.fromParty = fromParty;
    }

    public DomibusConnectorParty getToParty() {
        return toParty;
    }

    public void setToParty(DomibusConnectorParty toParty) {
        this.toParty = toParty;
    }

    public boolean isValidWithoutPDF() {
        if (action != null && !action.isPdfRequired()) {
            return true;
        }
        return false;
    }

}

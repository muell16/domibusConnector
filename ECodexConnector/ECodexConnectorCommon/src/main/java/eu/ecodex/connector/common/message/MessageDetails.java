package eu.ecodex.connector.common.message;

import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;

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
    private ServiceEnum service;
    private ActionEnum action;
    private PartnerEnum fromPartner;
    private PartnerEnum toPartner;
    private String originalSenderAddress;
    private String finalRecipientAddress;
    private Long dbMessageId;

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

    public ServiceEnum getService() {
        return service;
    }

    public void setService(ServiceEnum service) {
        this.service = service;
    }

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public PartnerEnum getFromPartner() {
        return fromPartner;
    }

    public void setFromPartner(PartnerEnum fromPartner) {
        this.fromPartner = fromPartner;
    }

    public PartnerEnum getToPartner() {
        return toPartner;
    }

    public void setToPartner(PartnerEnum toPartner) {
        this.toPartner = toPartner;
    }

    public String getOriginalSenderAddress() {
        return originalSenderAddress;
    }

    public void setOriginalSenderAddress(String originalSenderAddress) {
        this.originalSenderAddress = originalSenderAddress;
    }

    public String getFinalRecipientAddress() {
        return finalRecipientAddress;
    }

    public void setFinalRecipientAddress(String finalRecipientAddress) {
        this.finalRecipientAddress = finalRecipientAddress;
    }

    public Long getDbMessageId() {
        return dbMessageId;
    }

    public void setDbMessageId(Long dbMessageId) {
        this.dbMessageId = dbMessageId;
    }

}

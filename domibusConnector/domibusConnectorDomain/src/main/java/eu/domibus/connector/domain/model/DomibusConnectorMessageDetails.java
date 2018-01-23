package eu.domibus.connector.domain.model;

import org.springframework.core.style.ToStringCreator;


/**
 * Holds the routing information for the {@link DomibusConnectorMessage}. The data
 * represented is needed to be able to send the message to other participants.
 * @author riederb
 * @version 1.0
 * @updated 29-Dez-2017 10:12:49
 */
public class DomibusConnectorMessageDetails {

	private String backendMessageId;
	private String ebmsMessageId;
	private String refToMessageId;
	private String conversationId;
	private String originalSender;
	private String finalRecipient;    
	private DomibusConnectorService service;
	private DomibusConnectorAction action;
	private DomibusConnectorParty fromParty;
	private DomibusConnectorParty toParty;

	public DomibusConnectorMessageDetails(){

	}

	public String getBackendMessageId(){
		return this.backendMessageId;
	}

	/**
	 * 
	 * @param backendMessageId    backendMessageId
	 */
	public void setBackendMessageId(String backendMessageId){
		this.backendMessageId = backendMessageId;
	}

	public String getEbmsMessageId(){
		return this.ebmsMessageId;
	}

	/**
	 * 
	 * @param ebmsMessageId    ebmsMessageId
	 */
	public void setEbmsMessageId(String ebmsMessageId){
		this.ebmsMessageId = ebmsMessageId;
	}

	public String getRefToMessageId(){
		return this.refToMessageId;
	}

	/**
	 * 
	 * @param refToMessageId    refToMessageId
	 */
	public void setRefToMessageId(String refToMessageId){
		this.refToMessageId = refToMessageId;
	}

	public String getConversationId(){
		return this.conversationId;
	}

	/**
	 * 
	 * @param conversationId    conversationId
	 */
	public void setConversationId(String conversationId){
		this.conversationId = conversationId;
	}

	public String getOriginalSender(){
		return this.originalSender;
	}

	/**
	 * 
	 * @param originalSender    originalSender
	 */
	public void setOriginalSender(String originalSender){
		this.originalSender = originalSender;
	}

	public String getFinalRecipient(){
		return this.finalRecipient;
	}

	/**
	 * 
	 * @param finalRecipient    finalRecipient
	 */
	public void setFinalRecipient(String finalRecipient){
		this.finalRecipient = finalRecipient;
	}

	public DomibusConnectorService getService(){
		return this.service;
	}

	/**
	 * 
	 * @param service    service
	 */
	public void setService(DomibusConnectorService service){
		this.service = service;
	}

	public DomibusConnectorAction getAction(){
		return this.action;
	}

	/**
	 * 
	 * @param action    action
	 */
	public void setAction(DomibusConnectorAction action){
		this.action = action;
	}

	public DomibusConnectorParty getFromParty(){
		return this.fromParty;
	}

	/**
	 * 
	 * @param fromParty    fromParty
	 */
	public void setFromParty(DomibusConnectorParty fromParty){
		this.fromParty = fromParty;
	}

	public DomibusConnectorParty getToParty(){
		return this.toParty;
	}

	/**
	 * 
	 * @param toParty    toParty
	 */
	public void setToParty(DomibusConnectorParty toParty){
		this.toParty = toParty;
	}
    
    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("ebmsMessageId", this.ebmsMessageId);
        builder.append("backendMessageId", this.backendMessageId);
        builder.append("originalSender", this.originalSender);
        builder.append("finalRecipient", this.finalRecipient);
        builder.append("conversationId", this.conversationId);
        return builder.toString();        
    }

}
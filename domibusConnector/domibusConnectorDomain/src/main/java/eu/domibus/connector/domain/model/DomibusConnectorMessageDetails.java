package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import org.springframework.core.style.ToStringCreator;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;


/**
 * Holds the routing information for the {@link DomibusConnectorMessage}. The data
 * represented is needed to be able to send the message to other participants.
 * @author riederb
 * @version 1.0
 */
public class DomibusConnectorMessageDetails implements Serializable {

	@Nullable
	private String backendMessageId;
	@Nullable
	private String ebmsMessageId;
	@Nullable
	private String refToMessageId;
	@Nullable
	private String conversationId;
	private String originalSender;
	private String finalRecipient;
	private DomibusConnectorService service;
	private DomibusConnectorAction action;
	private DomibusConnectorParty fromParty;
	private DomibusConnectorParty toParty;

	//the backend client name the message is received from or should be delivered to
	@Nullable
	private String connectorBackendClientName;

	@Nullable
	private Date deliveredToGateway;

	@Nullable
	private Date deliveredToBackend;

	@Nullable
	private String causedBy;

	@Nullable
	private Date failed;

	private DomibusConnectorMessageDirection direction;

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

	@Nullable
	public String getConnectorBackendClientName() {
		return connectorBackendClientName;
	}

	public void setConnectorBackendClientName(@Nullable String connectorBackendClientName) {
		this.connectorBackendClientName = connectorBackendClientName;
	}

	@Nullable
	public Date getDeliveredToGateway() {
		return deliveredToGateway;
	}

	public void setDeliveredToGateway(@Nullable Date deliveredToGateway) {
		this.deliveredToGateway = deliveredToGateway;
	}

	@Nullable
	public Date getDeliveredToBackend() {
		return deliveredToBackend;
	}

	public void setDeliveredToBackend(@Nullable Date deliveredToBackend) {
		this.deliveredToBackend = deliveredToBackend;
	}

	@Nullable
	public String getCausedBy() {
		return causedBy;
	}

	public void setCausedBy(@Nullable String causedBy) {
		this.causedBy = causedBy;
	}

	public DomibusConnectorMessageDirection getDirection() {
		return direction;
	}

	public void setDirection(DomibusConnectorMessageDirection direction) {
		this.direction = direction;
	}

	@Nullable
	public Date getFailed() {
		return failed;
	}

	public void setFailed(@Nullable Date failed) {
		this.failed = failed;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("ebmsMessageId", this.ebmsMessageId);
        builder.append("backendMessageId", this.backendMessageId);
        builder.append("refToMessageId", this.refToMessageId);
        builder.append("originalSender", this.originalSender);
        builder.append("finalRecipient", this.finalRecipient);
        builder.append("conversationId", this.conversationId);
        return builder.toString();        
    }

}
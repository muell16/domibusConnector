package eu.domibus.connector.domain.model;

import java.util.List;
import java.util.ArrayList;
import org.springframework.core.style.ToStringCreator;

/**
 * This domain object contains all data of a message. At least the {@link
 * DomibusConnectorMessageDetails} and the {@link DomibusConnectorMessageContent}
 * must be given at the time of creation as they represent the minimum structure
 * of a message. While the message is processed by the domibusConnector, the data
 * inside this structure changes up to the point where the message is completely
 * finished.
 * @author riederb
 * @version 1.0
 * @updated 29-Dez-2017 10:12:48
 */
public class DomibusConnectorMessage {

	private final DomibusConnectorMessageDetails messageDetails;
	private DomibusConnectorMessageContent messageContent;
	private final List<DomibusConnectorMessageAttachment> messageAttachments = new ArrayList<DomibusConnectorMessageAttachment>();
	private final List<DomibusConnectorMessageConfirmation> messageConfirmations = new ArrayList<DomibusConnectorMessageConfirmation>();
	private final List<DomibusConnectorMessageError> messageErrors = new ArrayList<DomibusConnectorMessageError>();
    private String connectorMessageId;

	/**
	 * This constructor initializes an instance of a DomibusConnectorMessage in case
	 * it is not a confirmation message. At least the messageDetails and the
	 * messageContent must be given.
	 * 
	 * @param messageDetails    The details for message routing.
	 * @param messageContent    The content of the message.
     * 
     * 
	 */
	public DomibusConnectorMessage(final DomibusConnectorMessageDetails messageDetails, final DomibusConnectorMessageContent messageContent){
	   this.messageDetails = messageDetails;
	   this.messageContent = messageContent;
	}

    /**
	 * This constructor initializes an instance of a DomibusConnectorMessage in case
	 * it is not a confirmation message. At least the messageDetails and the
	 * messageContent must be given.
	 * 
     * @param connectorMessageId The internal connector message process id
	 * @param messageDetails    The details for message routing.
	 * @param messageContent    The content of the message.
	 */
	public DomibusConnectorMessage(
            final String connectorMessageId,
            final DomibusConnectorMessageDetails messageDetails, 
            final DomibusConnectorMessageContent messageContent){
        this.connectorMessageId = connectorMessageId;
        this.messageDetails = messageDetails;
        this.messageContent = messageContent;
	}
    
	/**
	 * This constructor initializes an instance of a DomibusConnectorMessage in case
	 * it is a confirmation message. At least the messageDetails and the
	 * messageConfirmation must be given.
	 * 
	 * @param messageDetails
	 * @param messageConfirmation
     * 
     * 
	 */
	public DomibusConnectorMessage(final DomibusConnectorMessageDetails messageDetails, final DomibusConnectorMessageConfirmation messageConfirmation){
	   this.messageDetails = messageDetails;
	   addConfirmation(messageConfirmation);
	}
    
    /**
	 * This constructor initializes an instance of a DomibusConnectorMessage in case
	 * it is a confirmation message. At least the messageDetails and the
	 * messageConfirmation must be given.
	 * 
     * @param connectorMessageId internal connector message process id
	 * @param messageDetails
	 * @param messageConfirmation
	 */
    public DomibusConnectorMessage(
            final String connectorMessageId, 
            final DomibusConnectorMessageDetails messageDetails, 
            final DomibusConnectorMessageConfirmation messageConfirmation) {
        this.connectorMessageId = connectorMessageId;
        this.messageDetails = messageDetails;
        addConfirmation(messageConfirmation);
    }
            

	public DomibusConnectorMessageDetails getMessageDetails(){
		return this.messageDetails;
	}

	public DomibusConnectorMessageContent getMessageContent(){
		return this.messageContent;
	}

	public List<DomibusConnectorMessageAttachment> getMessageAttachments(){
		return this.messageAttachments;
	}

	public List<DomibusConnectorMessageConfirmation> getMessageConfirmations(){
		return this.messageConfirmations;
	}

	/**
	 * Method to add a new {@link DomibusConnectorMessageAttachment} to the collection.
	 * The collection is initialized, so no new collection needs to be created or set.
	 * 
	 * @param attachment    attachment
	 */
	public void addAttachment(final DomibusConnectorMessageAttachment attachment){
	   	this.messageAttachments.add(attachment);
	}

	/**
	 * Method to add a new {@link DomibusConnectorMessageConfirmation} to the
	 * collection. The collection is initialized, so no new collection needs to be
	 * created or set.
	 * 
	 * @param confirmation    confirmation
	 */
	public void addConfirmation(final DomibusConnectorMessageConfirmation confirmation){
	   	this.messageConfirmations.add(confirmation);
	}

	public List<DomibusConnectorMessageError> getMessageErrors(){
		return this.messageErrors;
	}

	/**
	 * Method to add a new {@link DomibusConnectorMessageError} to the collection.
	 * This collection is filled during the processing of the message inside the
	 * domibusConnector, or, if there are message related errors reported by the
	 * gateway.
	 * 
	 * @param error    error
	 */
	public void addError(final DomibusConnectorMessageError error){
	   	this.messageErrors.add(error);
	}

    public String getConnectorMessageId() {
        return connectorMessageId;
    }

    public void setConnectorMessageId(String connectorMessageId) {
        this.connectorMessageId = connectorMessageId;
    }

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("connectorMessageId", this.connectorMessageId);        
        return builder.toString();        
    }
    
}
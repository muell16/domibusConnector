package eu.ecodex.dc5.message.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DCMessageProcessSettings;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.model.jpa.BusinessDomainIdConverter;
import eu.domibus.connector.domain.model.jpa.DomibusConnectorMessageIdConverter;
import lombok.*;
import org.springframework.core.style.ToStringCreator;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This domain object contains all data of a message. At least the {@link
 * DC5Ebms} and the {@link DomibusConnectorMessageContent}
 * must be given at the time of creation as they represent the minimum structure
 * of a message. While the message is processed by the domibusConnector, the data
 * inside this structure changes up to the point where the message is completely
 * finished.
 * @author riederb
 */
@Validated
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5Message implements Serializable {

	@GeneratedValue
	@Id
	private long id;

	@Valid
	@Convert(converter = BusinessDomainIdConverter.class)
	private DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId = DomibusConnectorBusinessDomain.getDefaultMessageLaneId();
	@NotNull
	@Valid
	@Convert(converter = DomibusConnectorMessageIdConverter.class)
	private DomibusConnectorMessageId connectorMessageId;

	@NotNull
	@Valid
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, optional = false)
	private DC5Ebms ebmsData;

	@NotNull
	@Valid
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, optional = false)
	private DC5BackendData backendData;

	@Convert(converter = MessageTargetSourceConverter.class)
	private MessageTargetSource target;

	@Convert(converter = MessageTargetSourceConverter.class)
	private MessageTargetSource source;

	private String gatewayLinkName;

	private String backendLinkName;

	@OneToOne(orphanRemoval = true)
	private DomibusConnectorMessageContent messageContent;

	//holds all message confirmations which are transported with this message
	@OneToMany(cascade = CascadeType.ALL)
	@Singular("transportedMessageConfirmation")
	private List<DC5Confirmation> transportedMessageConfirmations = new ArrayList<>();


	//holds all message confirmations which are related to this business message
	@Transient
	@Singular
	@OneToMany()
	private List<DC5Confirmation> relatedMessageConfirmations = new ArrayList<>();
	//holds all errors which occured during message processing...

	@Transient //TODO: move to process
	private final List<DomibusConnectorMessageError> messageProcessErrors = new ArrayList<>();

	@Transient  //TODO: move to process
	private DCMessageProcessSettings dcMessageProcessSettings = new DCMessageProcessSettings();


    @JsonProperty
	public DomibusConnectorMessageId getConnectorMessageId() {
		return connectorMessageId;
	}


	@JsonProperty
	public void setConnectorMessageId(DomibusConnectorMessageId messageId) {
		this.connectorMessageId = messageId;
	}

	public DC5Ebms getEbmsData(){
		return this.ebmsData;
	}

	public void setEbmsData(DC5Ebms messageDetails) {
		this.ebmsData = messageDetails;
	}

	public DomibusConnectorMessageContent getMessageContent(){
		return this.messageContent;
	}

//	public List<DomibusConnectorMessageAttachment> getMessageAttachments(){
//		return this.messageAttachments;
//	}

	public List<DC5Confirmation> getTransportedMessageConfirmations(){
		return this.transportedMessageConfirmations;
	}

	public List<DC5Confirmation> getRelatedMessageConfirmations() {
		return relatedMessageConfirmations;
	}

	public DomibusConnectorBusinessDomain.BusinessDomainId getMessageLaneId() {
		return businessDomainId;
	}

	public void setMessageLaneId(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
		this.businessDomainId = businessDomainId;
	}

	/**
	 * Method to add a new {@link DC5Confirmation} to the
	 * collection.
	 *
	 * The confirmations here are related to the message document/content
	 *
	 * The collection is initialized, so no new collection needs to be
	 * created or set.
	 *
	 * @param confirmation    confirmation
	 * @return for return see: {@link List#add(Object)}
	 */
	public boolean addRelatedMessageConfirmation(final DC5Confirmation confirmation){
		return this.relatedMessageConfirmations.add(confirmation);
	}

	/**
	 * Method to add a new {@link DomibusConnectorMessageAttachment} to the collection.
	 * The collection is initialized, so no new collection needs to be created or set.
	 * 
	 * @param attachment    attachment
	 */
//	public void addAttachment(final DomibusConnectorMessageAttachment attachment){
//	   	this.messageAttachments.add(attachment);
//	}

	/**
	 * Method to add a new {@link DC5Confirmation} to the
	 * collection. This collection holds only Confirmations which are transported
	 * with this message. In case of a business message they are also related
	 * to it.
	 * The collection is initialized, so no new collection needs to be
	 * created or set.
	 * 
	 * @param confirmation    confirmation
	 */
	public boolean addTransportedMessageConfirmation(final DC5Confirmation confirmation){
		if (!this.transportedMessageConfirmations.contains(confirmation)) {
			return this.transportedMessageConfirmations.add(confirmation);
		} else {
			return false; //duplicate
		}
	}

	public DCMessageProcessSettings getDcMessageProcessSettings() {
		return dcMessageProcessSettings;
	}

	public void setDcMessageProcessSettings(DCMessageProcessSettings dcMessageProcessSettings) {
		this.dcMessageProcessSettings = dcMessageProcessSettings;
	}

	@JsonIgnore
	public List<DomibusConnectorMessageError> getMessageProcessErrors(){
		return this.messageProcessErrors;
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
	   	this.messageProcessErrors.add(error);
	}

	@Deprecated
	@JsonIgnore
    public String getConnectorMessageIdAsString() {
		if (connectorMessageId == null) {
			return null;
		}
        return connectorMessageId.getConnectorMessageId();
    }

	@Deprecated
	@JsonIgnore
    public void setConnectorMessageId(String connectorMessageId) {
        this.connectorMessageId = new DomibusConnectorMessageId(connectorMessageId);
    }

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("connectorMessageId", this.connectorMessageId);
        builder.append("messageDetails", this.ebmsData);
        return builder.toString();
	}

	public void setMessageContent(DomibusConnectorMessageContent messageContent) {
		this.messageContent = messageContent;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DomibusConnectorBusinessDomain.BusinessDomainId getBusinessDomainId() {
		return businessDomainId;
	}

	public void setBusinessDomainId(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
		this.businessDomainId = businessDomainId;
	}

	public void setDirection(DomibusConnectorMessageDirection gatewayToBackend) {
		this.setSource(gatewayToBackend.getSource());
		this.setTarget(gatewayToBackend.getTarget());
	}

	public DomibusConnectorMessageDirection getDirection() {
		return DomibusConnectorMessageDirection.fromMessageTargetSource(this.source, this.target);
	}

}
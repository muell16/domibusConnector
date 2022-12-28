package eu.ecodex.dc5.message.model;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DCMessageProcessSettings;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.model.jpa.DomibusConnectorMessageIdConverter;
import eu.ecodex.dc5.domain.model.BusinessDomainIdConverter;
import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import eu.ecodex.dc5.message.validation.OutgoingMessageRules;
import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.annotation.CheckForNull;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This domain object contains all data of a message. At least the {@link
 * DC5Ebms} and the {@link DC5MessageContent}
 * must be given at the time of creation as they represent the minimum structure
 * of a message. While the message is processed by the domibusConnector, the data
 * inside this structure changes up to the point where the message is completely
 * finished.
 *
 * @author riederb
 */
@Valid
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5Message implements Serializable {

    @GeneratedValue
    @Id
    private Long id;

    @CheckForNull
    @Convert(converter = BusinessDomainIdConverter.class)
    @NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must already have a business domain!")
    private DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId = DomibusConnectorBusinessDomain.getDefaultBusinessDomainId();

    @NotNull(message = "A message must have a connectorMessageId!")
    @Valid
    @Convert(converter = DomibusConnectorMessageIdConverter.class)
    private DomibusConnectorMessageId connectorMessageId;

    @CheckForNull
    @Convert(converter = DomibusConnectorMessageIdConverter.class)
    private DomibusConnectorMessageId refToConnectorMessageId;

    @Valid
    @NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have EBMS data!")
    @NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have EBMS data!")
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @CheckForNull
    private DC5Ebms ebmsData = new DC5Ebms();

    @Valid
    @NotNull(groups = OutgoingMessageRules.class, message = "A outgoing message must have backendData!")
    @CheckForNull
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private DC5BackendData backendData = new DC5BackendData();

    @Convert(converter = MessageTargetSourceConverter.class)
    @NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have direction target!")
    @NotNull(groups = OutgoingMessageRules.class, message = "A outgoing message must have direction target!")
    private MessageTargetSource target;

    @Convert(converter = MessageTargetSourceConverter.class)
    @NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have direction source!")
    @NotNull(groups = OutgoingMessageRules.class, message = "A outgoing message must have direction source!")
    private MessageTargetSource source;

    @CheckForNull
    @Convert(converter = DomibusConnectorLinkPartner.LinkPartnerNameConverter.class)
    @NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a gateway link name!")
    private DomibusConnectorLinkPartner.LinkPartnerName gatewayLinkName;

    @CheckForNull
    @Convert(converter = DomibusConnectorLinkPartner.LinkPartnerNameConverter.class)
    @NotNull(groups = OutgoingMessageRules.class, message = "A outgoing message must have a backend link name!")
    private DomibusConnectorLinkPartner.LinkPartnerName backendLinkName;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @CheckForNull
    @NotNull(groups = IncomingBusinessMesssageRules.class, message = "A incoming business message must have a message content")
    private DC5MessageContent messageContent;

    //holds all message confirmations which are transported with this message
    @OneToOne(cascade = CascadeType.ALL)
    @CheckForNull
    @NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a transport confirmation!")
    private DC5Confirmation transportedMessageConfirmation;


    @Transient //TODO: move to process
    private final List<DomibusConnectorMessageError> messageProcessErrors = new ArrayList<>();

    @Transient  //TODO: move to process
    @Builder.Default
    private DCMessageProcessSettings dcMessageProcessSettings = new DCMessageProcessSettings();

    @NotNull
    private LocalDateTime created = LocalDateTime.now();

    //TODO: move to process
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn (name="PROPERTY_NAME", nullable = false)
    @Column(name="PROPERTY_VALUE", length = 2048)
    private Map<String, String> messageProcessingProperties = new HashMap<String, String>();

    @PostLoad
    public void loadDCMessageProcessSettings() {
        dcMessageProcessSettings = DCMessageProcessSettings.of(this.messageProcessingProperties);
    }

    @PreUpdate
    @PrePersist
    public void saveDCMessageProcessSettings() {
        if (this.dcMessageProcessSettings != null) {
            messageProcessingProperties.putAll(this.dcMessageProcessSettings.toProperties());
        }
    }

    @JsonProperty
    public DomibusConnectorMessageId getConnectorMessageId() {
        return connectorMessageId;
    }

    @JsonProperty
    public void setConnectorMessageId(DomibusConnectorMessageId messageId) {
        this.connectorMessageId = messageId;
    }

    public DC5Ebms getEbmsData() {
        return this.ebmsData;
    }

    public void setEbmsData(DC5Ebms messageDetails) {
        this.ebmsData = messageDetails;
    }

    public DC5MessageContent getMessageContent() {
        return this.messageContent;
    }

    public DomibusConnectorBusinessDomain.BusinessDomainId getMessageLaneId() {
        return businessDomainId;
    }

    public void setMessageLaneId(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        this.businessDomainId = businessDomainId;
    }



    /**
     * Method to add a new {@link DomibusConnectorMessageError} to the collection.
     * This collection is filled during the processing of the message inside the
     * domibusConnector, or, if there are message related errors reported by the
     * gateway.
     *
     * @param error error
     */
    public void addError(final DomibusConnectorMessageError error) {
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
        return new ToStringCreator(this)
                .append("connectorMessageId", this.connectorMessageId)
                .append("refToConnectorMessageId", this.refToConnectorMessageId)
                .append("target", this.target)
                .append("source", this.source)
                .append("messageDetails", this.ebmsData)
                .append("backendData", this.backendData)
                .append("messageContent", this.messageContent)
                .toString();

    }

    public void setMessageContent(DC5MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public @NotNull DomibusConnectorMessageDirection getDirection() {
        return DomibusConnectorMessageDirection.fromMessageTargetSource(this.source, this.target);
    }

    public boolean isConfirmationMessage() {
        return MessageModelHelper.isEvidenceMessage(this);
    }

    public boolean isConfirmationTriggerMessage() {
        return MessageModelHelper.isEvidenceTriggerMessage(this);
    }

    public DomibusConnectorLinkPartner.LinkPartnerName getTargetLinkName() {
        if (getTarget() == MessageTargetSource.GATEWAY) {
            return this.gatewayLinkName;
        } else if (getTarget() == MessageTargetSource.BACKEND) {
            return this.backendLinkName;
        } else {
            throw new IllegalArgumentException(
                    String.format("Illegal Message target [%s] set! Only [%s] are accepted",
                            getTarget(),
                            Stream.of(MessageTargetSource.values()).map(Enum::toString).collect(Collectors.joining(",")))
            );
        }
    }

    public boolean isBusinessMessage() {
        return MessageModelHelper.isOutgoingBusinessMessage(this) || MessageModelHelper.isIncomingBusinessMessage(this);
    }

    public boolean isOutgoingBusinessMessage() {
        return MessageModelHelper.isOutgoingBusinessMessage(this);
    }

    public boolean isIncomingBusinessMessage() {
        return MessageModelHelper.isIncomingBusinessMessage(this);
    }

}
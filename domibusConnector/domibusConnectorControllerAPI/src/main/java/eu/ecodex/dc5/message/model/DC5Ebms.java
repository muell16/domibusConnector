package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import eu.ecodex.dc5.message.validation.OutgoingMessageRules;
import lombok.*;
import org.springframework.core.style.ToStringCreator;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * Holds the routing information for the {@link DC5Message}. The data
 * represented is needed to be able to send the message to other participants.
 * @author riederb
 * @version 1.0
 *
 *
 *
 */
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DC5Ebms {

	@Builder(toBuilder = true)
	public DC5Ebms(String conversationId,
				   EbmsMessageId ebmsMessageId,
				   EbmsMessageId refToEbmsMessageId,
				   DC5Action action,
				   DC5Service service,
				   DC5EcxAddress backendAddress,
				   DC5EcxAddress gatewayAddress,
				   DC5Role initiatorRole,
				   DC5Role responderRole
				   ) {
		this.conversationId = conversationId;
		this.ebmsMessageId = ebmsMessageId;
		this.refToEbmsMessageId = refToEbmsMessageId;
		if (action != null) {
			this.action = action.toBuilder().build();
		}
		if (service != null) {
			this.service = service.toBuilder().build();
		}
		if (backendAddress != null) {
			this.backendAddress = backendAddress.toBuilder().build();
		}
		if (gatewayAddress != null) {
			this.gatewayAddress = gatewayAddress.toBuilder().build();
		}
		if (initiatorRole != null) {
			this.initiatorRole = initiatorRole.toBuilder().build();
		}
		if (responderRole != null) {
			this.responderRole = responderRole.toBuilder().build();
		}

	}

	@Id
	@GeneratedValue
	private Long id = null;

	@Column(name = "CREATED")
	private LocalDateTime created;

	@Column(name = "DC5_CONVERSATION_ID", length = 255)
	private String conversationId;

	@Column(name = "EBMS_MESSAGE_ID", length = 255)
	@Convert(converter = EbmsMessageIdConverter.class)
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have EBMS ID")
	private EbmsMessageId ebmsMessageId;

	@Column(name = "DC5_REF_TO_MESSAGE_ID", length = 255)
	@Convert(converter = EbmsMessageIdConverter.class)
	@NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a refToEbmsMessageId")
	private EbmsMessageId refToEbmsMessageId;


	@Valid
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS Action")
	@NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a EBMS Action")
	@ManyToOne(cascade = CascadeType.ALL)
	private DC5Action action;

	@Valid
	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a EBMS Service")
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS Service")
	private DC5Service service;

	//JPA
//	@Embedded
//	@AttributeOverrides({
//			@AttributeOverride( name = "ecxAddress", column = @Column(name = "S_ECX_ADDRESS", length = 255)),
//			@AttributeOverride( name = "party.partyId", column = @Column(name = "S_PARTY_Id", length = 255)),
//			@AttributeOverride( name = "party.partyIdType", column = @Column(name = "S_PARTY_TYPE", length = 255)),
//			@AttributeOverride( name = "role.role", column = @Column(name = "S_ROLE", length = 255)),
//			@AttributeOverride( name = "role.roleType", column = @Column(name = "S_ROLE_TYPE", length = 255)),
//	})
	//lombok
//	@Builder.Default
	//validation rules
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS backend addr block")
	@NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a EBMS backend addr block")
	@ManyToOne(cascade = CascadeType.ALL)
	private DC5EcxAddress backendAddress = new DC5EcxAddress(); //role type is implicit RESPONDER - do I need a ROLE TYPE here?

	//JPA
//	@Embedded
//	@AttributeOverrides({
//			@AttributeOverride( name = "ecxAddress", column = @Column(name = "R_ECX_ADDRESS", length = 255)),
//			@AttributeOverride( name = "party.partyId", column = @Column(name = "R_PARTY_Id", length = 255)),
//			@AttributeOverride( name = "party.partyIdType", column = @Column(name = "R_PARTY_TYPE", length = 255)),
//			@AttributeOverride( name = "role.role", column = @Column(name = "R_ROLE", length = 255)),
//			@AttributeOverride( name = "role.roleType", column = @Column(name = "R_ROLE_TYPE", length = 255)),
//	})
	//lombok
//	@Builder.Default
	//validation Rules
	@NotNull(groups = {IncomingMessageRules.class, OutgoingMessageRules.class}, message = "A incoming or outgoing message must have a EBMS gateway addr block")
	@NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a EBMS gateway addr block")
	@ManyToOne(cascade = CascadeType.ALL)
	private DC5EcxAddress gatewayAddress = new DC5EcxAddress(); //role type is implicit RESPONDER - do I need a ROLE TYPE here?

	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull(groups = {IncomingMessageRules.class}, message = "A incoming message must already have a EBMS initiator Role!")
	private DC5Role initiatorRole = new DC5Role();

	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull(groups = {IncomingMessageRules.class}, message = "A incoming message must already have a EBMS responder Role!")
	private DC5Role responderRole = new DC5Role();

	public EbmsSender getSender(MessageTargetSource target) {
		if (target == MessageTargetSource.BACKEND) {
			return EbmsSender.builder()
					.originalSender(backendAddress.getEcxAddress())
					.party(backendAddress.getParty())
					.role(initiatorRole)
					.build();
		} else if (target == MessageTargetSource.GATEWAY) {
			return EbmsSender.builder()
					.originalSender(backendAddress.getEcxAddress())
					.party(backendAddress.getParty())
					.role(initiatorRole)
					.build();
		} else {
			throw new IllegalArgumentException("Unknonw MessageTargetSource " + target);
		}
	}

	public EbmsReceiver getReceiver(MessageTargetSource target) {
		if (target == MessageTargetSource.GATEWAY) {
			return EbmsReceiver.builder()
					.finalRecipient(gatewayAddress.getEcxAddress())
					.party(gatewayAddress.getParty())
					.role(responderRole)
					.build();
		} else if (target == MessageTargetSource.BACKEND) {
			return EbmsReceiver.builder()
					.finalRecipient(backendAddress.getEcxAddress())
					.party(backendAddress.getParty())
					.role(responderRole)
					.build();
		} else {
			throw new IllegalArgumentException("Unknonw MessageTargetSource " + target);
		}
	}

	@Getter
	@Builder(toBuilder = true)
	@ToString
	public static class EbmsSender {
		private final DC5Party party;
		private final DC5Role role;
		private final String originalSender;
	}

	@Getter
	@Builder(toBuilder = true)
	@ToString
	public static class EbmsReceiver  {
		private final DC5Party party;
		private final DC5Role role;
		private final String finalRecipient;
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
		builder.append("backendAddress", this.backendAddress);
		builder.append("gatewayAddress", this.gatewayAddress);
        builder.append("ebmsMessageId", this.ebmsMessageId);
        builder.append("refToMessageId", this.refToEbmsMessageId);
        builder.append("conversationId", this.conversationId);
		builder.append("service", this.service);
		builder.append("action", this.action);

        return builder.toString();        
    }

}
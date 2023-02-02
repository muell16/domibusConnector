package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
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
@Table(name = "DC5_EBMS_DATA")
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
				   DC5Partner initiator,
				   DC5Partner responder
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
		if (initiator != null) {
			this.initiator = initiator.toBuilder().build();
		}
		if (responder != null) {
			this.responder = responder.toBuilder().build();
		}
	}

	@Id
	@GeneratedValue
	private Long id = null;

	@Column(name = "CREATED")
	private LocalDateTime created;

	@Column(name = "CONVERSATION_ID", length = 255)
	private String conversationId;

	@Column(name = "EBMS_MESSAGE_ID", length = 255)
	@Convert(converter = EbmsMessageIdConverter.class)
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have EBMS ID")
	private EbmsMessageId ebmsMessageId;

	@Column(name = "REF_TO_MESSAGE_ID", length = 255)
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

	@ManyToOne
	@JoinColumn(name = "INITIATOR")
	private DC5Partner initiator;

	@ManyToOne
	@JoinColumn(name = "RESPONDER")
	private DC5Partner responder;

	public EbmsSender getSender(MessageTargetSource target) {
		if (target == MessageTargetSource.BACKEND) {
			return EbmsSender.builder()
					.originalSender(initiator.getPartnerAddress().getEcxAddress())
					.party(initiator.getPartnerAddress().getParty())
					.role(initiator.getPartnerRole())
					.build();
		} else if (target == MessageTargetSource.GATEWAY) {
			return EbmsSender.builder()
					.originalSender(responder.getPartnerAddress().getEcxAddress())
					.party(responder.getPartnerAddress().getParty())
					.role(responder.getPartnerRole())
					.build();
		} else {
			throw new IllegalArgumentException("Unknonw MessageTargetSource " + target);
		}
	}

	public EbmsReceiver getReceiver(MessageTargetSource target) {
		if (target == MessageTargetSource.GATEWAY) {
			return EbmsReceiver.builder()
					.finalRecipient(responder.getPartnerAddress().getEcxAddress())
					.party(responder.getPartnerAddress().getParty())
					.role(responder.getPartnerRole())
					.build();
		} else if (target == MessageTargetSource.BACKEND) {
			return EbmsReceiver.builder()
					.finalRecipient(initiator.getPartnerAddress().getEcxAddress())
					.party(initiator.getPartnerAddress().getParty())
					.role(initiator.getPartnerRole())
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
        builder.append("ebmsMessageId", this.ebmsMessageId);
        builder.append("refToMessageId", this.refToEbmsMessageId);
        builder.append("conversationId", this.conversationId);
		builder.append("service", this.service);
		builder.append("action", this.action);

        return builder.toString();        
    }

}
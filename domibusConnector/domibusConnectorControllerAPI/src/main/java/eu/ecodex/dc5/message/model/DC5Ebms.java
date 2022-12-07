package eu.ecodex.dc5.message.model;

import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DC5Ebms {

	public DC5Ebms.DC5EbmsBuilder toBuilder() {
		DC5EbmsBuilder b = DC5Ebms
				.builder()
				.created(this.created)
				.conversationId(this.conversationId)
				.ebmsMessageId(this.ebmsMessageId)
				.refToEbmsMessageId(this.refToEbmsMessageId);
		if (this.action != null) {
			b.action(this.action.toBuilder().build());
		}
		if (this.service != null) {
			b.service(this.service.toBuilder().build());
		}
		if (this.sender != null) {
			b.sender(this.sender.toBuilder().build());
		}
		if (this.receiver != null) {
			b.receiver(this.receiver.toBuilder().build());
		}
		return b;
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
	private EbmsMessageId refToEbmsMessageId;


	@Valid
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS Action")
	@ManyToOne(cascade = CascadeType.ALL)
	private DC5Action action;

	@Valid
	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS Service")
	private DC5Service service;

	//JPA
	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "ecxAddress", column = @Column(name = "S_ECX_ADDRESS", length = 255)),
			@AttributeOverride( name = "party.partyId", column = @Column(name = "S_PARTY_Id", length = 255)),
			@AttributeOverride( name = "party.partyIdType", column = @Column(name = "S_PARTY_TYPE", length = 255)),
			@AttributeOverride( name = "role.role", column = @Column(name = "S_ROLE", length = 255)),
			@AttributeOverride( name = "role.roleType", column = @Column(name = "S_ROLE_TYPE", length = 255)),
	})
	//lombok
	@Builder.Default
	//validation rules
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS Sender")
	private DC5EcxAddress sender = new DC5EcxAddress(); //role type is implicit RESPONDER - do I need a ROLE TYPE here?

	//JPA
	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "ecxAddress", column = @Column(name = "R_ECX_ADDRESS", length = 255)),
			@AttributeOverride( name = "party.partyId", column = @Column(name = "R_PARTY_Id", length = 255)),
			@AttributeOverride( name = "party.partyIdType", column = @Column(name = "R_PARTY_TYPE", length = 255)),
			@AttributeOverride( name = "role.role", column = @Column(name = "R_ROLE", length = 255)),
			@AttributeOverride( name = "role.roleType", column = @Column(name = "R_ROLE_TYPE", length = 255)),
	})
	//lombok
	@Builder.Default
	//validation Rules
	@NotNull(groups = IncomingMessageRules.class, message = "A incoming message must have a EBMS Receiver")
	private DC5EcxAddress receiver = new DC5EcxAddress(); //role type is implicit RESPONDER - do I need a ROLE TYPE here?


	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
		builder.append("sender", this.sender);
		builder.append("receiver", this.receiver);
        builder.append("ebmsMessageId", this.ebmsMessageId);
        builder.append("refToMessageId", this.refToEbmsMessageId);
        builder.append("conversationId", this.conversationId);

        return builder.toString();        
    }

}
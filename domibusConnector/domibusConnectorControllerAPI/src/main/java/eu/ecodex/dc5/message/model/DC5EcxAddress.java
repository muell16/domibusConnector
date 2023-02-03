package eu.ecodex.dc5.message.model;


import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import eu.ecodex.dc5.message.validation.OutgoingMessageRules;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//@Embeddable

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DC5_EXC_ADDRESS")
public class DC5EcxAddress {

    @Id
    @GeneratedValue
    private Long id;

    @Builder(toBuilder = true)
    public DC5EcxAddress(String ecxAddress, DC5Party party) {
        if (party != null) {
            this.party = party.toBuilder().build();
        }
        this.ecxAddress = ecxAddress;
    }

    @NotBlank(groups = {IncomingMessageRules.class, OutgoingMessageRules.class}, message = "A incoming or outgoing message must have a ecx addr")
    @NotNull(groups = {IncomingMessageRules.class, OutgoingMessageRules.class}, message = "A incoming or outgoing message must have a ecx addr")
    @NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a ecx addr")
    @Column(name = "SENDER_RECEIVER_REF")
    private String ecxAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull(groups = {IncomingMessageRules.class, OutgoingMessageRules.class}, message = "A incoming or outgoing message must have a not null party")
    @NotNull(groups = ConfirmationMessageRules.class, message = "A confirmation message must have a valid party")
    private DC5Party party = new DC5Party();

    public String toString() {
        return new ToStringBuilder(this)
                .append("party", party)
                .append("ecxAddr", ecxAddress)
                .toString();
    }

}

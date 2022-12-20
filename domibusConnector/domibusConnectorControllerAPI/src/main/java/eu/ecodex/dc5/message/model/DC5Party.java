package eu.ecodex.dc5.message.model;


import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import eu.ecodex.dc5.message.validation.OutgoingMessageRules;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class DC5Party {

    @Id
    @GeneratedValue
    private long id;

    @Builder(toBuilder = true)
    public DC5Party(String partyId, String partyIdType) {
        this.partyId = partyId;
        this.partyIdType = partyIdType;
    }

    @NotBlank(groups = OutgoingMessageRules.class, message = "A outgoing message needs at least a partyId")
    @NotBlank(groups = {IncomingMessageRules.class, ConfirmationMessageRules.class},
            message = "A valid party message must have a partyIdType")
    private String partyId;
    @NotBlank(groups = {IncomingMessageRules.class, ConfirmationMessageRules.class},
            message = "A valid party message must have a partyIdType")
    private String partyIdType;

}

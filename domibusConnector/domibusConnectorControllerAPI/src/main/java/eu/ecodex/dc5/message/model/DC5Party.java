package eu.ecodex.dc5.message.model;


import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

@Embeddable
public class DC5Party {

    @NotBlank(groups = IncomingMessageRules.class, message = "A incoming message mus have a partyIdType")
    private String partyId;
    @NotBlank(groups = IncomingMessageRules.class, message = "A incoming message mus have a partyIdType")
    private String partyIdType;

}

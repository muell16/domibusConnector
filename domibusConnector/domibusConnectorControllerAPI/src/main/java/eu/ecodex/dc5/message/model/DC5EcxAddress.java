package eu.ecodex.dc5.message.model;


import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Embeddable

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5EcxAddress {

    @NotBlank(groups = IncomingMessageRules.class, message = "A incoming message mus have a ecx addr")
    @NotNull(groups = IncomingMessageRules.class, message = "A incoming message mus have a ecx addr")
    private String ecxAddress;

    @Embedded
    @NotNull(groups = IncomingMessageRules.class, message = "A incoming message mus have a ecx addr")
    private DC5Party party = new DC5Party();
    @Embedded
    private DC5Role role = new DC5Role();

}

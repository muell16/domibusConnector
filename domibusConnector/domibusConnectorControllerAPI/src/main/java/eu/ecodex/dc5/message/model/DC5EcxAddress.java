package eu.ecodex.dc5.message.model;


import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5EcxAddress {

    private String ecxAddress;

    @Embedded
    private DC5Party party;
    @Embedded
    private DC5Role role;

}

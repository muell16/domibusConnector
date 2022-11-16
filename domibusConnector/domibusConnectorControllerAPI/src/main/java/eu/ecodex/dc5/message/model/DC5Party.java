package eu.ecodex.dc5.message.model;


import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

@Embeddable
public class DC5Party {
    private String partyId;
    private String partyIdType;

}

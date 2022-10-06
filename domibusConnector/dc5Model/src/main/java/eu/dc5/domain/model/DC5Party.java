package eu.dc5.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DC5Party {
    @Column(insertable = false, updatable = false)
    private String partyId;
    @Column(insertable = false, updatable = false)
    private String partyIdType;
}

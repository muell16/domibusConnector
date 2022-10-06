package eu.dc5.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DC5EcxAddress {

    private String ecxAddress;

    @Embedded
    private DC5Party party;
    @Embedded
    private DC5Role role;
}

package eu.dc5.domain.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class DC5Role {
    private String role;
    private String roleType;
}

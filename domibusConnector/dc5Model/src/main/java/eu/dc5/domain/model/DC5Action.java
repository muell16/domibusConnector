package eu.dc5.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class DC5Action {
    private String action;
}

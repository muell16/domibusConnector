package eu.dc5.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("2")
public class DC5TransportStep extends DC5ProcStep {

    private Long id; // TODO: another ID?
    private String message; // TODO: message?
}

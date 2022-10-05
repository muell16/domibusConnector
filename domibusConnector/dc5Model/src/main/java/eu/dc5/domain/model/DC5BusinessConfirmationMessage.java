package eu.dc5.domain.model;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class DC5BusinessConfirmationMessage extends DC5Message {
}

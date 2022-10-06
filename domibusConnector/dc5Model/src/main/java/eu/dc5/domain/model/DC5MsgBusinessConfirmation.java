package eu.dc5.domain.model;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class DC5MsgBusinessConfirmation extends DC5Msg {
}

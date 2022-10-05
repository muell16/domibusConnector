package eu.dc5.domain.model;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1") // this id is inserted into DC5_MESSAGE_TYPE, you can find the class
public class DC5BusinessDocumentMessage extends DC5Message {
}

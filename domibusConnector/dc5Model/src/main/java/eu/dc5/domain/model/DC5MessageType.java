package eu.dc5.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "DC5_MESSAGE_TYPE")
public class DC5MessageType {

    @Id
    @Column(name = "ID", length = 1)
    private byte id;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "CLASSNAME", length = 255)
    private String className;
}

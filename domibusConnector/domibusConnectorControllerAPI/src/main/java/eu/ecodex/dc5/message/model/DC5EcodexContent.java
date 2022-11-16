package eu.ecodex.dc5.message.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DC5EcodexContent {

    @GeneratedValue
    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment asicContainer;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment trustTokenXml;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment businessXml;


}

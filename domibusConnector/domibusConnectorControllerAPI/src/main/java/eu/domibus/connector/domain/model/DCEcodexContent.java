package eu.domibus.connector.domain.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DCEcodexContent {

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

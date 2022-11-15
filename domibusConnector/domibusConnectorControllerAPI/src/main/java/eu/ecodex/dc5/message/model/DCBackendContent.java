package eu.ecodex.dc5.message.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class DCBackendContent {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment businessXml;

    /**
     * The business document here can be of any type,
     * but it has to be supported by ecodex container
     *
     */
    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment businessDocument;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DomibusConnectorMessageAttachment> attachments;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment trustTokenXml;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment trustTokenPDF;


}

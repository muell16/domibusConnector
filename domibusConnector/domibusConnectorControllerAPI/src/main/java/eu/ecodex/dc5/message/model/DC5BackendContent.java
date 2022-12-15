package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class DC5BackendContent {

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
    @NotNull
    private DomibusConnectorMessageAttachment businessDocument;

    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    private List<DomibusConnectorMessageAttachment> attachments;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment trustTokenXml;

    @OneToOne(cascade = CascadeType.ALL)
    private DomibusConnectorMessageAttachment trustTokenPDF;


}

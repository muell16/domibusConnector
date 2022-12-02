package eu.ecodex.dc5.message.model;

import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5EcodexContent {

    @GeneratedValue
    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(groups = IncomingBusinessMesssageRules.class, message = "A incoming business message must have a asic-s container")
    private DomibusConnectorMessageAttachment asicContainer;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(groups = IncomingBusinessMesssageRules.class, message = "A incoming business message must have a trust xml token")
    private DomibusConnectorMessageAttachment trustTokenXml;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(groups = IncomingBusinessMesssageRules.class, message = "A incoming business message must have a business xml")
    private DomibusConnectorMessageAttachment businessXml;


}

package eu.ecodex.dc5.flow.events;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.events.DC5Event;
import lombok.*;


@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class MessageReadyForTransportEvent extends DC5Event {

    private long id;
    private DomibusConnectorLinkPartner.LinkPartnerName linkName;
    private MessageTargetSource target;

}

package eu.ecodex.dc5.flow.events;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.events.DC5Event;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.*;


@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
public class MessageReadyForTransportEvent {

    @NonNull
    private DC5Message message;
    @NonNull
    private DomibusConnectorLinkPartner.LinkPartnerName linkName;
    @NonNull
    private MessageTargetSource target;

}

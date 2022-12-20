package eu.ecodex.dc5.flow.events;


import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.events.DC5Event;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import lombok.*;

import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class MessageTransportEvent {

    @NonNull
    private final DomibusConnectorLinkPartner.LinkPartnerName linkName;
    @NonNull
    private final DC5TransportRequest.TransportRequestId transportId; // transport id
    @NonNull
    private TransportState state;

    private final String remoteTransportId;
    private final String transportSystemId;

    public Optional<String> getRemoteTransportId() {
        return Optional.ofNullable(remoteTransportId);
    }

    public Optional<String> getTransportSystemId() {
        return Optional.ofNullable(transportSystemId);
    }


}

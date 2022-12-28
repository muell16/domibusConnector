package eu.ecodex.dc5.flow.events;

import eu.domibus.connector.domain.enums.TransportState;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class OutgoingBusinessMessageTransportEvent {

    private final DC5Message message;
    private final DC5TransportRequest transportRequest;
    private final TransportState transportState;

}

package eu.ecodex.dc5.flow.events;


import eu.domibus.connector.domain.enums.TransportState;
import lombok.*;

import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class MessageTransportEvent {

    @NonNull
    private final long id; //message
    @NonNull
    private final String linkName;

    private final long transportId; // transport id

    @NonNull
    private TransportState state;

    private final Optional<String> remoteTransportId = Optional.empty();
    private final Optional<String> transportSystemId = Optional.empty();


}

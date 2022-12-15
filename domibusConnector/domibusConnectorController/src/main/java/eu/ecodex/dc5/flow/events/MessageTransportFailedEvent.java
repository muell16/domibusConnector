package eu.ecodex.dc5.flow.events;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class MessageTransportFailedEvent {

    @NonNull
    private final String linkName;

    private final long transportId; // transport id

    private final MessageTargetSource target;

    //TODO add reason
    private final String reason;

}

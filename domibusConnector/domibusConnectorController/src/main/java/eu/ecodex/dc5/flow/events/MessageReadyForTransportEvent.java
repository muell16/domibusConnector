package eu.ecodex.dc5.flow.events;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.events.DC5Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageReadyForTransportEvent extends DC5Event {

    private long id;
    private String linkName;
    private MessageTargetSource target;

}

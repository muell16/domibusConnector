package eu.ecodex.dc5.flow.events;

import eu.ecodex.dc5.core.model.DC5Msg;
import eu.ecodex.dc5.events.DC5Event;
import lombok.*;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewMessageStoredEvent extends DC5Event {

    private long messageId;

}

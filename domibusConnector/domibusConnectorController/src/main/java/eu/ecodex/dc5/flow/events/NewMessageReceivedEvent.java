package eu.ecodex.dc5.flow.events;

import eu.ecodex.dc5.core.model.DC5Msg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NewMessageReceivedEvent {

    private final DC5Msg msg;

}

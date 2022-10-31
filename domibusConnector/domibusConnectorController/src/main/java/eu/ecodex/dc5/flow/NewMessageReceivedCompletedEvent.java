package eu.ecodex.dc5.flow;

import eu.ecodex.dc.core.model.DC5ProcessStep;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NewMessageReceivedCompletedEvent {

    private final String msgProcessId;
    private final DC5ProcessStep receiveMessageStep;

}

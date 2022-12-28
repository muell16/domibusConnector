package eu.ecodex.dc5.process.model;

import eu.ecodex.dc5.process.MessageProcessId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DC5MessageProcessEvent {

    private final MessageProcessId processId;
    private final DC5ProcessEvents event;

}

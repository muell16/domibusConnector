package eu.ecodex.dc5.process;

import lombok.Getter;

@Getter
public class MessageProcessId {

    public final String processId;

    public MessageProcessId(String processId) {
        this.processId = processId;
    }
}

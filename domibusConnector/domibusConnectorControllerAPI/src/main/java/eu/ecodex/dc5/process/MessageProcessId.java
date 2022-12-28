package eu.ecodex.dc5.process;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
public class MessageProcessId {

    public final String processId;

    public MessageProcessId(String processId) {
        this.processId = processId;
    }

    public static MessageProcessId ofString(String s) {
        return new MessageProcessId(s);
    }

    public static MessageProcessId ofRandom() {
        return new MessageProcessId(UUID.randomUUID().toString());
    }
}

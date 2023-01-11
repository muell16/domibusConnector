package eu.ecodex.dc5.process;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class MessageProcessId {

    public final String processId;

    public MessageProcessId(String processId) {
        if (StringUtils.isBlank(processId)) {
            throw new IllegalArgumentException("processId is not allowed to be blank or null!");
        }
        this.processId = processId;
    }

    public static MessageProcessId ofString(String s) {
        return new MessageProcessId(s);
    }

    public static MessageProcessId ofRandom() {
        return new MessageProcessId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageProcessId)) return false;

        MessageProcessId that = (MessageProcessId) o;

        return Objects.equals(processId, that.processId);
    }

    @Override
    public int hashCode() {
        return processId != null ? processId.hashCode() : 0;
    }
}

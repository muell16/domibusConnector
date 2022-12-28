package eu.ecodex.dc5.message.model;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Validated
@Getter
public final class DC5MessageId {

    @NotBlank
    @NonNull
    private final String connectorMessageId;

    @Deprecated //use static ofString method instead!
    public DC5MessageId(String s) {
        Objects.requireNonNull(s, "ConnectorMessageId is not allowed to be null!");
        if (s.length() < 1) {
            throw new IllegalArgumentException("ConnectorMessageId is not allowed to be empty!");
        }
        connectorMessageId = s;
    }


    public static DC5MessageId ofRandom() {
        return new DC5MessageId(UUID.randomUUID().toString());
    }

    public static DC5MessageId ofString(String s) {
        return new DC5MessageId(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5MessageId)) return false;

        DC5MessageId that = (DC5MessageId) o;

        return connectorMessageId.equals(that.connectorMessageId);
    }

    @Override
    public int hashCode() {
        return connectorMessageId.hashCode();
    }

    public String toString() {
        return "ConnectorMessageId: [" + this.connectorMessageId + "]";
    }
}

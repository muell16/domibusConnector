package eu.ecodex.dc5.message.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Validated
@Getter
public final class DomibusConnectorMessageId {

    @NotBlank
    @NonNull
    private final String connectorMessageId;

    @Deprecated //use static ofString method instead!
    public DomibusConnectorMessageId(String s) {
        Objects.requireNonNull(s, "ConnectorMessageId is not allowed to be null!");
        if (s.length() < 1) {
            throw new IllegalArgumentException("ConnectorMessageId is not allowed to be empty!");
        }
        connectorMessageId = s;
    }


    public static DomibusConnectorMessageId ofRandom() {
        return new DomibusConnectorMessageId(UUID.randomUUID().toString());
    }

    public static DomibusConnectorMessageId ofString(String s) {
        return new DomibusConnectorMessageId(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomibusConnectorMessageId)) return false;

        DomibusConnectorMessageId that = (DomibusConnectorMessageId) o;

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

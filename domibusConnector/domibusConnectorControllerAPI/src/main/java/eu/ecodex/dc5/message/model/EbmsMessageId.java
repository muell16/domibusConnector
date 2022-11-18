package eu.ecodex.dc5.message.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(staticName = "ofString")
public final class EbmsMessageId {

    @NotBlank
    @NonNull
    private final String ebmsMesssageId;

    public static EbmsMessageId ofRandom() {
        return EbmsMessageId.ofString(UUID.randomUUID().toString());
    }

    public String toString() {
        return "EbmsMessageId: [" + this.ebmsMesssageId + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EbmsMessageId)) return false;

        EbmsMessageId that = (EbmsMessageId) o;

        return ebmsMesssageId.equals(that.ebmsMesssageId);
    }

    @Override
    public int hashCode() {
        return ebmsMesssageId.hashCode();
    }
}

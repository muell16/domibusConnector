package eu.ecodex.dc5.message.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(onConstructor_ = {@Deprecated})
public final class BackendMessageId {

    @NotBlank
    @NonNull
    private final String backendMessageId;

    public static BackendMessageId ofRandom() {
        return new BackendMessageId(UUID.randomUUID().toString());
    }

    public static BackendMessageId ofString(String s) {
        return new BackendMessageId(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackendMessageId)) return false;

        BackendMessageId that = (BackendMessageId) o;

        return backendMessageId.equals(that.backendMessageId);
    }

    @Override
    public int hashCode() {
        return backendMessageId.hashCode();
    }

    public String toString() {
        return "BackendMessageId: [" + this.backendMessageId + "]";
    }

}

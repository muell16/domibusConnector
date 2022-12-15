package eu.ecodex.dc5.message.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Digest {


    @NonNull
    private final String digestValue; //as hex
    @NonNull
    private final String digestAlgorithm;

    public static String convertToString(Digest attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDigestAlgorithm() + ":" + attribute.getDigestValue();
    }

    public static Digest ofString(String dbData) {
        if (dbData == null) {
            return null;
        }
        int index = dbData.indexOf(":");
        if (index == -1) {
            throw new IllegalArgumentException("Cannot parse given string to Digest");
        }
        return Digest.builder()
                .digestAlgorithm(dbData.substring(0, index))
                .digestValue(dbData.substring(index + 1))
                .build();
    }

    public static Digest ofMessageDigest(MessageDigest messageDigest) {
        return Digest.builder()
                .digestAlgorithm(messageDigest.getAlgorithm())
                .digestValue(Hex.encodeHexString(messageDigest.digest()))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Digest)) return false;

        Digest digest = (Digest) o;

        if (!digestValue.equals(digest.digestValue)) return false;
        return digestAlgorithm.equals(digest.digestAlgorithm);
    }

    @Override
    public int hashCode() {
        int result = digestValue.hashCode();
        result = 31 * result + digestAlgorithm.hashCode();
        return result;
    }
}

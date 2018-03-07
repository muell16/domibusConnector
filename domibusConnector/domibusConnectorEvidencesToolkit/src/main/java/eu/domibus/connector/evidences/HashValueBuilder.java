package eu.domibus.connector.evidences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class HashValueBuilder {

    public enum HashAlgorithm {

        MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512");

        private HashAlgorithm(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        private static HashAlgorithm getHashAlgorithm(String name) {
            for (HashAlgorithm value : values()) {
                if (value.toString().equals(name))
                    return value;
            }
            throw new IllegalArgumentException(new NoSuchAlgorithmException(String.format("There is no such algorithm named [%s]", name)));
        }

    };

    private final HashAlgorithm algorithm;

    private final MessageDigest digester;

    public HashValueBuilder(HashAlgorithm algorithm) {
        try {
            this.algorithm = algorithm;
            digester = MessageDigest.getInstance(this.algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public HashValueBuilder(String algorithm) {
        this(HashAlgorithm.getHashAlgorithm(algorithm));
    }

    public String buildHashValueAsString(byte[] originalMessage) {
        final byte[] resultByte = buildHashValue(originalMessage);
        final String result = new String(Hex.encodeHexString(resultByte));
        return result;
    }

    public byte[] buildHashValue(byte[] originalMessage) {
        digester.reset();
        digester.update(originalMessage);
        final byte[] resultByte = digester.digest();

        return resultByte;
    }

    public HashAlgorithm getAlgorithm() {
        return algorithm;
    }

}

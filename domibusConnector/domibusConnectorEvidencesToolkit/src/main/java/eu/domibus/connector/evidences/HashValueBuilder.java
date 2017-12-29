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

        private static HashAlgorithm getHashAlgorithm(String name) throws NoSuchAlgorithmException {
            for (HashAlgorithm value : values()) {
                if (value.toString().equals(name))
                    return value;
            }
            throw new NoSuchAlgorithmException();
        }

    };

    private final HashAlgorithm algorithm;

    private final MessageDigest digester;

    private HashValueBuilder(HashAlgorithm algorithm) throws NoSuchAlgorithmException {
        this.algorithm = algorithm;
        digester = MessageDigest.getInstance(this.algorithm.toString());
    }

    public HashValueBuilder(@Value(value = "${hash.algorithm}") String algorithm) throws NoSuchAlgorithmException {
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

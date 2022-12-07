package eu.ecodex.dc5.message.model;

import javax.persistence.AttributeConverter;

public class DigestConverter implements AttributeConverter<Digest, String> {

    @Override
    public String convertToDatabaseColumn(Digest attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDigestAlgorithm() + ":" + attribute.getDigestValue();
    }

    @Override
    public Digest convertToEntityAttribute(String dbData) {
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
}

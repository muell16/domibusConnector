package eu.ecodex.dc5.message.model;

import javax.persistence.AttributeConverter;

public class DigestConverter implements AttributeConverter<Digest, String> {

    @Override
    public String convertToDatabaseColumn(Digest attribute) {
        return Digest.convertToString(attribute);
    }

    @Override
    public Digest convertToEntityAttribute(String dbData) {
        return Digest.ofString(dbData);
    }
}

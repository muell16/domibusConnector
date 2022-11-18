package eu.ecodex.dc5.message.model;

import javax.persistence.AttributeConverter;

public class BackendMessageIdConverter implements AttributeConverter<BackendMessageId, String> {

    @Override
    public String convertToDatabaseColumn(BackendMessageId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getBackendMessageId();
    }

    @Override
    public BackendMessageId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new BackendMessageId(dbData);
    }
}

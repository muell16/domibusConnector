package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.enums.MessageTargetSource;

import javax.persistence.AttributeConverter;

public class MessageTargetSourceConverter implements AttributeConverter<MessageTargetSource, String> {

    @Override
    public String convertToDatabaseColumn(MessageTargetSource attribute) {
        return attribute.getDbName();
    }

    @Override
    public MessageTargetSource convertToEntityAttribute(String dbData) {
        return MessageTargetSource.ofOfDbName(dbData);
    }
}

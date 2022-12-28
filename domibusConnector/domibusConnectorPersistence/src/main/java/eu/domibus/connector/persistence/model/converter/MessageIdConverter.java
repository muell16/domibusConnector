package eu.domibus.connector.persistence.model.converter;

import eu.ecodex.dc5.message.model.DC5MessageId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MessageIdConverter implements AttributeConverter<DC5MessageId, String> {

    @Override
    public String convertToDatabaseColumn(DC5MessageId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getConnectorMessageId();
    }

    @Override
    public DC5MessageId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new DC5MessageId(dbData);
    }
}

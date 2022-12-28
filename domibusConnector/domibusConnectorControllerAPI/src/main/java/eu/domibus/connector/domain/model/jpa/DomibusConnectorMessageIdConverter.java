package eu.domibus.connector.domain.model.jpa;

import eu.ecodex.dc5.message.model.DC5MessageId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DomibusConnectorMessageIdConverter implements AttributeConverter<DC5MessageId, String> {


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
        return DC5MessageId.ofString(dbData);
    }
}

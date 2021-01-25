package eu.domibus.connector.persistence.model.converter;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MessageIdConverter implements AttributeConverter<DomibusConnectorMessage.DomibusConnectorMessageId, String> {

    @Override
    public String convertToDatabaseColumn(DomibusConnectorMessage.DomibusConnectorMessageId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getConnectorMessageId();
    }

    @Override
    public DomibusConnectorMessage.DomibusConnectorMessageId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new DomibusConnectorMessage.DomibusConnectorMessageId(dbData);
    }
}

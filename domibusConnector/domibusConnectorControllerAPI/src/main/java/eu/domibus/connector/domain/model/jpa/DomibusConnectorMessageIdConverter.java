package eu.domibus.connector.domain.model.jpa;

import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DomibusConnectorMessageIdConverter implements AttributeConverter<DomibusConnectorMessageId, String> {


    @Override
    public String convertToDatabaseColumn(DomibusConnectorMessageId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getConnectorMessageId();
    }

    @Override
    public DomibusConnectorMessageId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return DomibusConnectorMessageId.ofString(dbData);
    }
}

package eu.domibus.connector.domain.model.jpa;

import eu.domibus.connector.domain.model.DomibusConnectorMessageId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DomibusConnectorMessageIdConverter implements AttributeConverter<DomibusConnectorMessageId, String> {


    @Override
    public String convertToDatabaseColumn(DomibusConnectorMessageId attribute) {
        return attribute.getConnectorMessageId();
    }

    @Override
    public DomibusConnectorMessageId convertToEntityAttribute(String dbData) {
        return new DomibusConnectorMessageId(dbData);
    }
}

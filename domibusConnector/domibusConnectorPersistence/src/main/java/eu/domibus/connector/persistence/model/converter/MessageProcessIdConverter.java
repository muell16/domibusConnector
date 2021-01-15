package eu.domibus.connector.persistence.model.converter;

import eu.domibus.connector.domain.model.DomibusConnectorMessageProcessId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MessageProcessIdConverter implements AttributeConverter<DomibusConnectorMessageProcessId, String> {

    @Override
    public String convertToDatabaseColumn(DomibusConnectorMessageProcessId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getProcessId();
    }

    @Override
    public DomibusConnectorMessageProcessId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new DomibusConnectorMessageProcessId(dbData);
    }
}

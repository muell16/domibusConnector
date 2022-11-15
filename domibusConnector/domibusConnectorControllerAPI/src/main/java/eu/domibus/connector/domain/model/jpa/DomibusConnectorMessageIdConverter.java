package eu.domibus.connector.domain.model.jpa;

import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;

import javax.persistence.AttributeConverter;

//@Converter(autoApply = true)
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

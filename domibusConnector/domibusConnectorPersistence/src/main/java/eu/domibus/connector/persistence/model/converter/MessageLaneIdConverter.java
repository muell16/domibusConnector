package eu.domibus.connector.persistence.model.converter;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.domain.model.DomibusConnectorMessageProcessId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MessageLaneIdConverter implements AttributeConverter<DomibusConnectorMessageLane.MessageLaneId, String> {

    @Override
    public String convertToDatabaseColumn(DomibusConnectorMessageLane.MessageLaneId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getMessageLaneId();
    }

    @Override
    public DomibusConnectorMessageLane.MessageLaneId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new DomibusConnectorMessageLane.MessageLaneId(dbData);
    }
}

package eu.domibus.connector.persistence.model.converter;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.enums.TransportState;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransportStateJpaConverter implements AttributeConverter<TransportState, String> {

    @Override
    public String convertToDatabaseColumn(TransportState attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDbName();
    }

    @Override
    public TransportState convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TransportState.ofDbName(dbData);
    }

}

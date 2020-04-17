package eu.domibus.connector.persistence.model.converter;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.controller.service.TransportStatusService.TransportId;
import eu.domibus.connector.domain.enums.TransportState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransportStepIdJpaConverter implements AttributeConverter<TransportId, String> {

    @Override
    public String convertToDatabaseColumn(TransportId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getTransportId().toString();
    }

    @Override
    public TransportId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new TransportId(dbData);
    }
}

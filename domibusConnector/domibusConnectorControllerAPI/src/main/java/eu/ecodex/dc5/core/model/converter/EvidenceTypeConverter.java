package eu.ecodex.dc5.core.model.converter;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

public class EvidenceTypeConverter implements AttributeConverter<DomibusConnectorEvidenceType, String> {


    @Override
    public String convertToDatabaseColumn(DomibusConnectorEvidenceType attribute) {
        return attribute.name();
    }

    @Override
    public DomibusConnectorEvidenceType convertToEntityAttribute(String dbData) {
        return Stream.of(DomibusConnectorEvidenceType.values())
                .filter(n -> n.name().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No EvidenceType with name [%s] exists", dbData)));
    }
}


package eu.domibus.connector.domain.model.jpa;

import eu.domibus.connector.domain.model.DetachedSignatureMimeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

//@Converter(autoApply = true)
public class DetachedSignatureMimeTypeConverter implements AttributeConverter<DetachedSignatureMimeType, String> {

    @Override
    public String convertToDatabaseColumn(DetachedSignatureMimeType attribute) {
        return attribute.getCode();
    }

    @Override
    public DetachedSignatureMimeType convertToEntityAttribute(String dbData) {
        return Stream.of(DetachedSignatureMimeType.values())
                .filter(i -> i.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("There is no DetachedSignatureType of [%s]", dbData)));
    }

}

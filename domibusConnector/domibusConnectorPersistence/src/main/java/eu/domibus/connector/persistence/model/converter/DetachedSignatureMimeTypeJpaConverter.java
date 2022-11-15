package eu.domibus.connector.persistence.model.converter;

import eu.ecodex.dc5.message.model.DetachedSignatureMimeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DetachedSignatureMimeTypeJpaConverter implements AttributeConverter<DetachedSignatureMimeType, String> {


    @Override
    public String convertToDatabaseColumn(DetachedSignatureMimeType attribute) {
        return attribute.getCode();
    }

    @Override
    public DetachedSignatureMimeType convertToEntityAttribute(String dbData) {
        return DetachedSignatureMimeType.fromCode(dbData);
    }


}

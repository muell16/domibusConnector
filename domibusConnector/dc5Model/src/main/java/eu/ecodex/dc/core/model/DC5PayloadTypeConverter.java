package eu.ecodex.dc.core.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DC5PayloadTypeConverter implements AttributeConverter<DC5PayloadType, Byte> {

    @Override
    public Byte convertToDatabaseColumn(DC5PayloadType state) {
        return state.getCode();
    }

    @Override
    public DC5PayloadType convertToEntityAttribute(Byte code) {
        return Stream.of(DC5PayloadType.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}


package eu.ecodex.dc.core.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DC5BusinessDocumentStateConverter implements AttributeConverter<DC5BusinessDocumentStatesEnum, Byte> {

    @Override
    public Byte convertToDatabaseColumn(DC5BusinessDocumentStatesEnum state) {
        return state.getCode();
    }

    @Override
    public DC5BusinessDocumentStatesEnum convertToEntityAttribute(Byte code) {
        return Stream.of(DC5BusinessDocumentStatesEnum.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}


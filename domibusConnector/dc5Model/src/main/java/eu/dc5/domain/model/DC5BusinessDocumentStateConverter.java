package eu.dc5.domain.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DC5BusinessDocumentStateConverter implements AttributeConverter<DC5BusinessDocumentStates, Byte> {

    @Override
    public Byte convertToDatabaseColumn(DC5BusinessDocumentStates state) {
        return state.getCode();
    }

    @Override
    public DC5BusinessDocumentStates convertToEntityAttribute(Byte code) {
        return Stream.of(DC5BusinessDocumentStates.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}


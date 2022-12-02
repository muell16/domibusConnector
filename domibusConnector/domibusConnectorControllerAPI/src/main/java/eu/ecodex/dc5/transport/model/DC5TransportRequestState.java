package eu.ecodex.dc5.transport.model;


import eu.domibus.connector.domain.enums.TransportState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeConverter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class DC5TransportRequestState {

    @Id
    @GeneratedValue
    private Long id;

    private TransportState transportState;

    private String reason;

    private LocalDateTime created = LocalDateTime.now();

    public static class TransportStateConverter implements AttributeConverter<TransportState, String> {
        @Override
        public String convertToDatabaseColumn(TransportState attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.toString();
        }

        @Override
        public TransportState convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            return TransportState.valueOf(dbData);
        }
    }
}

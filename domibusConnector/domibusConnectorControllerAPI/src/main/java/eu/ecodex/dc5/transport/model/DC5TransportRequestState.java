package eu.ecodex.dc5.transport.model;


import eu.domibus.connector.domain.enums.TransportState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

@Entity
@Table(name = "DC5_TRANSPORT_STATE")
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

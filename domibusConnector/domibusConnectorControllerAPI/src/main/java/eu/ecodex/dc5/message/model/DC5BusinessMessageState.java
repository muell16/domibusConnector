package eu.ecodex.dc5.message.model;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Entity

@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class DC5BusinessMessageState {

    public enum BusinessMessagesStates {
        CREATED, SUBMITTED, RELAYED, REJECTED, CONFIRMED, DELIVERED, RETRIEVED
    }

    public enum BusinessMessageEvents {
        SUBMISSION_REJECTION_RCV(DomibusConnectorEvidenceType.SUBMISSION_REJECTION),
        SUBMISSION_ACCEPTANCE_RCV(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE),
        RELAY_REMMD_FAILURE_RCV(DomibusConnectorEvidenceType.RELAY_REMMD_FAILURE),
        RELAY_REMMD_ACCEPTANCE_RCV(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE),
        NON_DELIVERY_RCV(DomibusConnectorEvidenceType.NON_DELIVERY),
        RELAY_REMMD_REJECTION_RCV(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION),
        DELIVERY_RCV(DomibusConnectorEvidenceType.DELIVERY),
        RETRIEVAL_RCV(DomibusConnectorEvidenceType.RETRIEVAL),
        NON_RETRIEVAL_RCV(DomibusConnectorEvidenceType.NON_RETRIEVAL),
        ADMIN_ABORT(null),
        NEW_MSG(null);

        private final DomibusConnectorEvidenceType confirmationType;

        private BusinessMessageEvents(DomibusConnectorEvidenceType confirmationType) {
            this.confirmationType = confirmationType;
        }

        public static BusinessMessageEvents ofEvidenceType(DomibusConnectorEvidenceType evidenceType) {
            return Stream.of(BusinessMessageEvents.values())
                    .filter(e -> e.confirmationType.equals(evidenceType))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cannot find event for given evidenceType: [" + evidenceType + "]"));
        }
    }





    @Id
    @GeneratedValue
    private Long id;

    private String principal;

    @OneToOne(cascade = CascadeType.ALL)
    private DC5Confirmation confirmation;

    @NonNull
    @Convert(converter = BusinessMessagesStatesConverter.class)
    private BusinessMessagesStates state;

    @NonNull
    @Convert(converter = BusinessMessageEventsConverter.class)
    private BusinessMessageEvents event;

    private LocalDateTime created = LocalDateTime.now();


    public static class BusinessMessagesStatesConverter implements AttributeConverter<BusinessMessagesStates, String> {
        @Override
        public String convertToDatabaseColumn(BusinessMessagesStates attribute) {
            return attribute.name();
        }

        @Override
        public BusinessMessagesStates convertToEntityAttribute(String dbData) {
            return BusinessMessagesStates.valueOf(dbData);
        }
    }

    public static class BusinessMessageEventsConverter implements AttributeConverter<BusinessMessageEvents, String> {

        @Override
        public String convertToDatabaseColumn(BusinessMessageEvents attribute) {
            return attribute.name();
        }

        @Override
        public BusinessMessageEvents convertToEntityAttribute(String dbData) {
            return BusinessMessageEvents.valueOf(dbData);
        }
    }

}

package eu.ecodex.dc5.transport.model;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.MessageTargetSourceConverter;
import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class DC5TransportRequest {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private DC5Message message;

    private String remoteMessageId;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DC5TransportRequestState currentState;

    private String transportSystemId;

    @Convert(converter = TransportIdConverter.class)
    private TransportRequestId transportRequestId;

    @Convert(converter = DomibusConnectorLinkPartner.LinkPartnerNameConverter.class)
    private DomibusConnectorLinkPartner.LinkPartnerName linkName;

    @Convert(converter = MessageTargetSourceConverter.class)
    private MessageTargetSource linkType;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DC5TransportRequestState> states = new ArrayList<>();

    public void changeCurrentState(DC5TransportRequestState currentState) {
        if (currentState.getId() == null) {
            this.currentState = currentState;
            if (states == null) {
                states = new ArrayList<>();
            }
            states.add(currentState);
        } else {
            throw new IllegalArgumentException("Not a new state!");
        }
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class TransportRequestId {

        @NonNull
        private final String transportId;

        public static TransportRequestId ofRandom() {
            return new TransportRequestId(UUID.randomUUID().toString());
        }

        public static TransportRequestId ofString(String id) {
            return new TransportRequestId(id);
        }

        public static TransportRequestId ofId(TransportRequestId id) {
            return new TransportRequestId(id.getTransportId());
        }

        public String toString() {
            return "TransportId: [" +
                    this.transportId +
                    "]";
        }
    }

    public static class TransportIdConverter implements AttributeConverter<TransportRequestId, String> {

        @Override
        public String convertToDatabaseColumn(TransportRequestId attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getTransportId();
        }

        @Override
        public TransportRequestId convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            return TransportRequestId.of(dbData);
        }
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("transportRequestId", this.transportRequestId)
                .append("linkName", this.linkName)
                .append("linkType", this.linkType)
                .append("messageId", this.message.getConnectorMessageId())
                .toString();
    }

}

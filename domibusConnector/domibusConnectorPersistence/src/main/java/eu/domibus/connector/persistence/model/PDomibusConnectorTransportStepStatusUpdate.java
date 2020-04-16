package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.persistence.model.converter.TransportStateJpaConverter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = PDomibusConnectorTransportStepStatusUpdate.TABLE_NAME)
@IdClass(PDomibusConnectorTransportStepStatusUpdateIdClass.class)
public class PDomibusConnectorTransportStepStatusUpdate {

    public static final String TABLE_NAME = "DC_TRANSPORT_STEP_STATUS";

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @Id
    @JoinColumn(name = "TRANSPORT_STEP_ID", referencedColumnName = "ID")
    @MapsId
    private PDomibusConnectorTransportStep transportStep;

    @Id
    @Column(name = "STATE")
    @Convert(converter = TransportStateJpaConverter.class)
    private TransportState transportState;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Lob
    @Column(name = "TEXT")
    private String text;

    @PrePersist
    public void beforePersist() {
        created = LocalDateTime.now();
    }

    public PDomibusConnectorTransportStep getTransportStep() {
        return transportStep;
    }

    public void setTransportStep(PDomibusConnectorTransportStep transportStep) {
        this.transportStep = transportStep;
    }

    public TransportState getTransportState() {
        return transportState;
    }

    public void setTransportState(TransportState transportState) {
        this.transportState = transportState;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

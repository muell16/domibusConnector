package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.enums.TransportState;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "DC_TRANSPORT_STEP_STATUS")
@IdClass(PDomibusConnectorTransportStepStatusUpdateIdClass.class)
public class PDomibusConnectorTransportStepStatusUpdate {


    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "TRANSPORT_STEP_ID")
    private PDomibusConnectorTransportStep transportStep;

    @Id
    @Column(name = "STATE")
    private TransportState transportState;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Lob
    @Column(name = "text")
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

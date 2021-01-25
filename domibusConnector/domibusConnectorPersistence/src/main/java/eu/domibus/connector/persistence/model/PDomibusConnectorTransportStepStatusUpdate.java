package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.persistence.model.converter.TransportStateJpaConverter;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = PDomibusConnectorTransportStepStatusUpdate.TABLE_NAME)
@IdClass(PDomibusConnectorTransportStepStatusUpdateIdClass.class)
public class PDomibusConnectorTransportStepStatusUpdate {

    public static final java.lang.String TABLE_NAME = "DC_TRANSPORT_STEP_STATUS";

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @Id
    @JoinColumn(name = "TRANSPORT_STEP_ID", referencedColumnName = "ID")
    @MapsId
    private PDomibusConnectorTransportStep transportStep;

    @Id
    @Column(name = "STATE")
    //does not work becaus it is part of ID!
    //instead @PrePersist is used
    @Convert(converter = TransportStateJpaConverter.class)
    private String transportStateString;

    private transient TransportState transportState = TransportState.PENDING;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Lob
    @Column(name = "TEXT")
    private java.lang.String text;

    @PrePersist
    @PreUpdate
    public void beforePersist() {
        created = LocalDateTime.now();
        this.transportStateString = TransportStateJpaConverter.converter
                .convertToDatabaseColumn(this.transportState);
    }

    @PostLoad
    @PostConstruct
    public void postConstructLoad() {
        this.transportState = TransportStateJpaConverter.converter
                .convertToEntityAttribute(this.transportStateString);
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

    public java.lang.String getText() {
        return text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }
}

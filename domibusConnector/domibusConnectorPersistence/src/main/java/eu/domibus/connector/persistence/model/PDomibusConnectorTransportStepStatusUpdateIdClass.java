package eu.domibus.connector.persistence.model;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;

import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;


public class PDomibusConnectorTransportStepStatusUpdateIdClass implements Serializable {

    Long transportStep;

    TransportState transportState;

    public Long getTransportStep() {
        return transportStep;
    }

    public void setTransportStep(Long transportStep) {
        this.transportStep = transportStep;
    }

    public TransportState getTransportState() {
        return transportState;
    }

    public void setTransportState(TransportState transportState) {
        this.transportState = transportState;
    }
}

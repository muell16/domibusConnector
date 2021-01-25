package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.enums.TransportState;

import java.io.Serializable;


public class PDomibusConnectorTransportStepStatusUpdateIdClass implements Serializable {

    Long transportStep;

    TransportState transportStateTransportState;

    public Long getTransportStep() {
        return transportStep;
    }

    public void setTransportStep(Long transportStep) {
        this.transportStep = transportStep;
    }

    public TransportState getTransportStateString() {
        return transportStateTransportState;
    }

    public void setTransportStateString(TransportState transportStateTransportState) {
        this.transportStateTransportState = transportStateTransportState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PDomibusConnectorTransportStepStatusUpdateIdClass)) return false;

        PDomibusConnectorTransportStepStatusUpdateIdClass that = (PDomibusConnectorTransportStepStatusUpdateIdClass) o;

        if (transportStep != null ? !transportStep.equals(that.transportStep) : that.transportStep != null)
            return false;
        if (transportStateTransportState != that.transportStateTransportState) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transportStep != null ? transportStep.hashCode() : 0;
        result = 31 * result + (transportStateTransportState != null ? transportStateTransportState.hashCode() : 0);
        return result;
    }
}

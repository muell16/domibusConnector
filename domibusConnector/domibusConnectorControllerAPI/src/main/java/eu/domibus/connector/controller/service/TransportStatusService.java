package eu.domibus.connector.controller.service;


import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import org.springframework.core.style.ToStringCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * This service handles the technical transport state of a message
 *  between the connector and a link partner (gw, client)
 *
 */
public interface TransportStatusService {

    /**
     * Sets the transport status for transports to GW
     * @param transportState the transport status to set, contains also the transport id / connector message id
     */
    public void updateTransportToGatewayStatus(DomibusConnectorTransportState transportState);

    /**
     * Sets the transport status for transport to backendClient
     * @param transportState the transport status to set, contains also the transport id / connector message id
     */
    public void updateTransportToBackendClientStatus(DomibusConnectorTransportState transportState);

    public static class DomibusConnectorTransportState {
        private String transportId;
        private String remoteTransportId; //in case of GW ebms id, in case of backend national id/backend id
        private TransportState status;
        private List<DomibusConnectorMessageError> messageErrorList = new ArrayList<>();

        public TransportState getStatus() {
            return status;
        }

        public void setStatus(TransportState status) {
            this.status = status;
        }

        public String getTransportId() {
            return transportId;
        }

        public void setTransportId(String transportId) {
            this.transportId = transportId;
        }

        public String getRemoteTransportId() {
            return remoteTransportId;
        }

        public void setRemoteTransportId(String remoteTransportId) {
            this.remoteTransportId = remoteTransportId;
        }

        public List<DomibusConnectorMessageError> getMessageErrorList() {
            return messageErrorList;
        }

        public void setMessageErrorList(List<DomibusConnectorMessageError> messageErrorList) {
            this.messageErrorList = messageErrorList;
        }

        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("msgId", this.transportId)
                    .append("remote id", this.remoteTransportId)
                    .append("status", this.status)
                    .toString();
        }
    }

    //ACCEPTED, PENDING, FAILED

    public static enum TransportState {
        ACCEPTED, PENDING, FAILED;
    }

}

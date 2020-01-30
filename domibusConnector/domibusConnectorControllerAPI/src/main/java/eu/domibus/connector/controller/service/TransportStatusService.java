package eu.domibus.connector.controller.service;


import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
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

    public void updateTransportStatus(DomibusConnectorTransportState transportState);

    public static class DomibusConnectorTransportState {
        private String connectorTransportId; //may be the same as the connectorMessageId but must not...
        private String transportImplId; // the id of the transport attempt itself, can be null, eg. a jms id
        private String remoteTransportId; //in case of GW ebms id, in case of backend national id/backend id, only filled if
        private TransportState status;
        private List<DomibusConnectorMessageError> messageErrorList = new ArrayList<>();

        public TransportState getStatus() {
            return status;
        }

        public void setStatus(TransportState status) {
            this.status = status;
        }

        public String getConnectorTransportId() {
            return connectorTransportId;
        }

        public void setConnectorTransportId(String connectorTransportId) {
            this.connectorTransportId = connectorTransportId;
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

        public String getTransportImplId() {
            return transportImplId;
        }

        public void setTransportImplId(String transportImplId) {
            this.transportImplId = transportImplId;
        }

        public void setMessageErrorList(List<DomibusConnectorMessageError> messageErrorList) {
            this.messageErrorList = messageErrorList;
        }


        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("msgId", this.connectorTransportId)
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

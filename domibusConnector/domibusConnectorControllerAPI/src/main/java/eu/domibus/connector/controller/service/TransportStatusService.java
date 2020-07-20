package eu.domibus.connector.controller.service;


import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This service handles the technical transport state of a message
 *  between the connector and a link partner (gw, client)
 *
 */
public interface TransportStatusService {

    /**
     * Sets the transport status for transports to GW
     * @param transportState the transport status to set
     * @param transportId contains the transportId
     */
    public void updateTransportToGatewayStatus(TransportId transportId, DomibusConnectorTransportState transportState);

    /**
     * Sets the transport status for transport to backendClient
     * @param transportState the transport status to set, contains also the transport id / connector message id
     * @param transportId contains the transportId
     */
    public void updateTransportToBackendClientStatus(TransportId transportId, DomibusConnectorTransportState transportState);


    public void updateTransportStatus(DomibusConnectorTransportState transportState);

    /**
     * Creates a new transport for the message
     * @param message
     * @return
     */
    public TransportId createTransportFor(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);

    /**
     * gets the last not finished (PENDING state) transport for this
     * message
     * @param message
     */
    public TransportId createOrGetTransportFor(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);

    public static class TransportId {
        private String transportId;

        public TransportId(String transportId) {
            if (StringUtils.isEmpty(transportId)) {
                throw new IllegalArgumentException("TransportId is not allowed to be null or empty!");
            }
            this.transportId = transportId;
        }

        public String getTransportId() {
            return transportId;
        }

        public void setTransportId(String transportId) {
            this.transportId = transportId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransportId that = (TransportId) o;
            return Objects.equals(transportId, that.transportId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(transportId);
        }

        @Override
        public String toString() {
            return "TransportId{" +
                    "transportId='" + transportId + '\'' +
                    '}';
        }
    }

    public static class DomibusConnectorTransportState {
        private TransportId connectorTransportId; //may be the same as the connectorMessageId but must not...
        private DomibusConnectorMessage.DomibusConnectorMessageId connectorMessageId;
        private String transportImplId; // the id of the transport attempt itself, can be null, eg. a jms id
        private String remoteMessageId; //in case of GW ebms id, in case of backend national id/backend id, only filled if
        private TransportState status;
        private List<DomibusConnectorMessageError> messageErrorList = new ArrayList<>();
        private String text;

        public TransportState getStatus() {
            return status;
        }

        public void setStatus(TransportState status) {
            this.status = status;
        }

        public TransportId getConnectorTransportId() {
            return connectorTransportId;
        }

        public void setConnectorTransportId(TransportId connectorTransportId) {
            this.connectorTransportId = connectorTransportId;
        }

        public DomibusConnectorMessage.DomibusConnectorMessageId getConnectorMessageId() {
            return connectorMessageId;
        }

        public void setConnectorMessageId(DomibusConnectorMessage.DomibusConnectorMessageId connectorMessageId) {
            this.connectorMessageId = connectorMessageId;
        }

        public String getRemoteMessageId() {
            return remoteMessageId;
        }

        public void setRemoteMessageId(String remoteMessageId) {
            this.remoteMessageId = remoteMessageId;
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

        public void addMessageError(DomibusConnectorMessageError error) {
            this.messageErrorList.add(error);
        }


        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("msgId", this.connectorTransportId)
                    .append("remote id", this.remoteMessageId)
                    .append("status", this.status)
                    .toString();
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }


}

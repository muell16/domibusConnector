package eu.domibus.connector.domain.model;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DomibusConnectorTransportStep {

    private TransportStatusService.TransportId transportId;
    private DomibusConnectorMessage.DomibusConnectorMessageId messageId;
    private DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName;
    private int attempt = -1;
    private String transportSystemMessageId;
    private String remoteMessageId;
    private LocalDateTime created;
    private List<DomibusConnectorTransportStepStatusUpdate> statusUpdates = new ArrayList<>();

    public TransportStatusService.TransportId getTransportId() {
        return transportId;
    }

    public void setTransportId(TransportStatusService.TransportId transportId) {
        this.transportId = transportId;
    }

    public DomibusConnectorMessage.DomibusConnectorMessageId getMessageId() {
        return messageId;
    }

    public void setMessageId(DomibusConnectorMessage.DomibusConnectorMessageId messageId) {
        this.messageId = messageId;
    }

    public DomibusConnectorLinkPartner.LinkPartnerName getLinkPartnerName() {
        return linkPartnerName;
    }

    public void setLinkPartnerName(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        this.linkPartnerName = linkPartnerName;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String getTransportSystemMessageId() {
        return transportSystemMessageId;
    }

    public void setTransportSystemMessageId(String transportSystemMessageId) {
        this.transportSystemMessageId = transportSystemMessageId;
    }

    public String getRemoteMessageId() {
        return remoteMessageId;
    }

    public void setRemoteMessageId(String remoteMessageId) {
        this.remoteMessageId = remoteMessageId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<DomibusConnectorTransportStepStatusUpdate> getStatusUpdates() {
        return statusUpdates;
    }

    public void setStatusUpdates(List<DomibusConnectorTransportStepStatusUpdate> statusUpdates) {
        this.statusUpdates = statusUpdates;
    }

    public void addTransportStatus(DomibusConnectorTransportStepStatusUpdate stepStatusUpdate) {
        int max = this.statusUpdates.stream()
                .map(DomibusConnectorTransportStepStatusUpdate::getTransportState)
                .map(TransportState::getPriority)
                .max(Comparator.naturalOrder())
                .orElse(0);

        if (stepStatusUpdate.getTransportState().getPriority() > max) {
            this.statusUpdates.add(stepStatusUpdate);
        } else {
            String error = String.format("Cannot add stepStatusUpdate with state [%s] because there is already a state with higher priority of [%s]!", stepStatusUpdate.getTransportState(), max);
            throw new IllegalArgumentException(error);
        }


    }

    public static class DomibusConnectorTransportStepStatusUpdate {

        private TransportState transportState;

        private LocalDateTime created;

        private String text;

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

}

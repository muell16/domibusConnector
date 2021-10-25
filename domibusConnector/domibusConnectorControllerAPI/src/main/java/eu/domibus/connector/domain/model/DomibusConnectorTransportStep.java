package eu.domibus.connector.domain.model;

import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DomibusConnectorTransportStep {

    private TransportStateService.TransportId transportId;
    private DomibusConnectorMessage transportedMessage;
    private DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName;
    private int attempt = -1;
    private java.lang.String transportSystemMessageId;
    private java.lang.String remoteMessageId;
    private LocalDateTime created;
    private PriorityQueue<DomibusConnectorTransportStepStatusUpdate> statusUpdates = new PriorityQueue<>();
    private LocalDateTime finalStateReached;

    public DomibusConnectorMessage getTransportedMessage() {
        return transportedMessage;
    }

    public void setTransportedMessage(DomibusConnectorMessage transportedMessage) {
        this.transportedMessage = transportedMessage;
    }

    public TransportStateService.TransportId getTransportId() {
        return transportId;
    }

    public void setTransportId(TransportStateService.TransportId transportId) {
        this.transportId = transportId;
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

    public java.lang.String getTransportSystemMessageId() {
        return transportSystemMessageId;
    }

    public void setTransportSystemMessageId(java.lang.String transportSystemMessageId) {
        this.transportSystemMessageId = transportSystemMessageId;
    }

    public java.lang.String getRemoteMessageId() {
        return remoteMessageId;
    }

    public void setRemoteMessageId(java.lang.String remoteMessageId) {
        this.remoteMessageId = remoteMessageId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void addStatusUpdate(DomibusConnectorTransportStepStatusUpdate u) {
        this.statusUpdates.add(u);
    }

    public DomibusConnectorMessageId getConnectorMessageId() {
        return this.transportedMessage.getConnectorMessageId();
    }

    public List<DomibusConnectorTransportStepStatusUpdate> getStatusUpdates() {
        return new ArrayList<>(statusUpdates);
    }

    public void setStatusUpdates(List<DomibusConnectorTransportStepStatusUpdate> statusUpdates) {
        this.statusUpdates.addAll(statusUpdates);
    }

    public void addTransportStatus(DomibusConnectorTransportStepStatusUpdate stepStatusUpdate) {
        if (stepStatusUpdate.getTransportState().getPriority() >= 10) {
            this.finalStateReached = LocalDateTime.now();
        }

        int max = this.statusUpdates.stream()
                .map(DomibusConnectorTransportStepStatusUpdate::getTransportState)
                .map(TransportState::getPriority)
                .max(Comparator.naturalOrder())
                .orElse(0);

        if (stepStatusUpdate.getTransportState().getPriority() > max) {
            this.statusUpdates.add(stepStatusUpdate);
        } else {
            java.lang.String error = java.lang.String.format("Cannot add stepStatusUpdate with state [%s] because there is already a state with higher or equal priority of [%s]!", stepStatusUpdate.getTransportState(), max);
            throw new IllegalArgumentException(error);
        }


    }

    public LocalDateTime getFinalStateReached() {
        return finalStateReached;
    }

    public void setFinalStateReached(LocalDateTime setFinalStateReached) {
        this.finalStateReached = setFinalStateReached;
    }

    public boolean isInPendingState() {
        TransportState state = TransportState.PENDING;
        return isInState(state);
    }

    public boolean isInPendingDownloadedState() {
        TransportState state = TransportState.PENDING_DOWNLOADED;
        return isInState(state);
    }

    public boolean isInAcceptedState() {
        TransportState state = TransportState.ACCEPTED;
        return isInState(state);
    }

    private boolean isInState(TransportState state) {
        DomibusConnectorTransportStepStatusUpdate lastState = this.statusUpdates.peek();
        return lastState != null && lastState.getTransportState() == state;
    }



    public static class DomibusConnectorTransportStepStatusUpdate implements Comparable<DomibusConnectorTransportStepStatusUpdate> {

        private TransportState transportState;

        private LocalDateTime created;

        private java.lang.String text;

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

        @Override
        public int compareTo(DomibusConnectorTransportStepStatusUpdate o) {
            int p1 = 0;
            if (this.transportState != null) {
                p1 = this.transportState.getPriority();
            }
            int p2 = 0;
            if (o != null && o.getTransportState() != null) {
                p2 = o.transportState.getPriority();
            }
            return p1 - p2;
        }
    }

}

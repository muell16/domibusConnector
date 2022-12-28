package eu.domibus.connector.controller.transport;

import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.flow.events.MessageTransportEvent;
import eu.ecodex.dc5.message.model.BackendMessageId;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import eu.ecodex.dc5.message.model.EbmsMessageId;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import eu.ecodex.dc5.transport.model.DC5TransportRequestState;
import eu.ecodex.dc5.transport.repo.DC5TransportRequestRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service
//@Transactional
//@RequiredArgsConstructor
@RequiredArgsConstructor
@Service
public class DomibusConnectorTransportStateService implements TransportStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorTransportStateService.class);


    private final ApplicationEventPublisher eventPublisher;
    private final DC5TransportRequestRepo transportRequestRepo;

    @Override
    @Transactional
    public void updateTransportToGatewayStatus(TransportId transportId, DomibusConnectorTransportState transportState) {

        MessageTransportEvent messageTransportEvent = MessageTransportEvent.builder()
                .linkName(transportState.getLinkPartner().getLinkPartnerName())
                .state(transportState.getStatus())
                .remoteTransportId(transportState.getRemoteMessageId())
                .transportId(DC5TransportRequest.TransportRequestId.ofString(transportId.getTransportId()))
                .build();
        eventPublisher.publishEvent(messageTransportEvent);

    }


    @Override
    @Transactional
    public void updateTransportToBackendClientStatus(TransportId transportId, DomibusConnectorTransportState transportState) {
        MessageTransportEvent messageTransportEvent = MessageTransportEvent.builder()
                .linkName(transportState.getLinkPartner().getLinkPartnerName())
                .state(transportState.getStatus())
                .remoteTransportId(transportState.getRemoteMessageId())
                .transportId(DC5TransportRequest.TransportRequestId.ofString(transportId.getTransportId()))
                .build();
        eventPublisher.publishEvent(messageTransportEvent);
    }

    private static interface SuccessHandler {
        void success(DC5Message message);
    }


    @Override
    public void updateTransportStatus(DomibusConnectorTransportState transportState) {
        MessageTransportEvent messageTransportEvent = MessageTransportEvent.builder()
                .linkName(transportState.getLinkPartner().getLinkPartnerName())
                .state(transportState.getStatus())
                .remoteTransportId(transportState.getRemoteMessageId())
                .transportId(DC5TransportRequest.TransportRequestId.ofString(transportState.getConnectorTransportId().getTransportId()))
                .build();
        eventPublisher.publishEvent(messageTransportEvent);
    }


    @Override
    public List<DomibusConnectorTransportStep> getPendingTransportsForLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        List<DC5TransportRequest> t = transportRequestRepo.findByCurrentState(TransportState.PENDING, linkPartnerName);
        return t.stream().map(this::mapToTransportStep).collect(Collectors.toList());
    }

    @Override
    public Optional<DomibusConnectorTransportStep> getTransportStepById(TransportId transportId) {
        Optional<DC5TransportRequest> byTransportRequestId = transportRequestRepo.findByTransportRequestId(DC5TransportRequest.TransportRequestId.ofString(transportId.getTransportId()));
        return byTransportRequestId.map(this::mapToTransportStep);
    }

    private DomibusConnectorTransportStep mapToTransportStep(DC5TransportRequest t) {
        DomibusConnectorTransportStep step = new DomibusConnectorTransportStep();
        step.setTransportId(new TransportId(t.getTransportRequestId().getTransportId()));
        step.setTransportedMessage(t.getMessage());
        step.setLinkPartnerName(t.getLinkName());
        step.setConnectorMessageId(t.getMessage().getConnectorMessageId());
        return step;
    }


}

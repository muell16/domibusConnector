package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageContentManager;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DomibusConnectorTransportStateService implements TransportStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorTransportStateService.class);

    private DCMessagePersistenceService messagePersistenceService;
    private DomibusConnectorMessageContentManager contentStorageService;
    private DomibusConnectorMessageErrorPersistenceService errorPersistenceService;
    private TransportStepPersistenceService transportStepPersistenceService;

    @Autowired
    public void setTransportStepPersistenceService(TransportStepPersistenceService transportStepPersistenceService) {
        this.transportStepPersistenceService = transportStepPersistenceService;
    }

    @Autowired
    public void setMessagePersistenceService(DCMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setContentStorageService(DomibusConnectorMessageContentManager contentStorageService) {
        this.contentStorageService = contentStorageService;
    }

    @Autowired
    public void setErrorPersistenceService(DomibusConnectorMessageErrorPersistenceService errorPersistenceService) {
        this.errorPersistenceService = errorPersistenceService;
    }


    @Override
    @Transactional
    public void updateTransportToGatewayStatus(TransportId transportId, DomibusConnectorTransportState transportState) {

        this.updateTransportStatus(transportId, transportState, (DomibusConnectorMessage m) -> {
            m.getMessageDetails().setEbmsMessageId(transportState.getRemoteMessageId());
            messagePersistenceService.mergeMessageWithDatabase(m);
            messagePersistenceService.setDeliveredToGateway(m);
        });

    }



    @Override
    @Transactional
    public void updateTransportToBackendClientStatus(TransportId transportId, DomibusConnectorTransportState transportState) {
        this.updateTransportStatus(transportId, transportState, (DomibusConnectorMessage m) -> {
                m.getMessageDetails().setBackendMessageId(transportState.getRemoteMessageId());
                messagePersistenceService.setMessageDeliveredToNationalSystem(m);
                messagePersistenceService.mergeMessageWithDatabase(m);
        });
    }

    private static interface SuccessHandler {
        void success(DomibusConnectorMessage message);
    }


    @Transactional
    public void updateTransportStatus(TransportId transportId, DomibusConnectorTransportState transportState, SuccessHandler successHandler) {
        if (transportId == null) {
            throw new IllegalArgumentException("TransportId is not allowed to be null!");
        }
        transportState.setConnectorTransportId(transportId);
        if (transportState == null) {
            throw new IllegalArgumentException("TransportState is not allowed to be null!");
        }
        DomibusConnectorTransportStep transportStep = transportStepPersistenceService.getTransportStepByTransportId(transportId);


        if (StringUtils.isEmpty(transportStep.getRemoteMessageId())) {
            transportStep.setRemoteMessageId(transportState.getRemoteMessageId());
        }
        if (StringUtils.isEmpty(transportStep.getTransportSystemMessageId())) {
            transportStep.setTransportSystemMessageId(transportState.getTransportImplId());
        }
        DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate statusUpdate = new DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate();
        statusUpdate.setCreated(LocalDateTime.now());
        statusUpdate.setTransportState(transportState.getStatus());
        transportStep.addTransportStatus(statusUpdate);
        transportStepPersistenceService.update(transportStep);


        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(transportStep.getMessageId().getConnectorMessageId());
        if (message == null) {
            //cannot update a transport for a null message maybe it's a evidence message, but they don't have
            // a relation to connector message id yet...so cannot set transport state for them!
            LOGGER.debug("#updateTransportToBackendStatus:: No message with transport id [{}] was found within database!", transportState.getConnectorTransportId());
            return;
        }

        if (transportState.getStatus() == TransportState.ACCEPTED) {
            if (message != null) {
                successHandler.success(message);
            }
        } else if (transportState.getStatus() == TransportState.FAILED) {
            //TODO: reject message async... -> inform backend async of rejection!
            transportState.getMessageErrorList().stream().forEach( error ->
                    errorPersistenceService.persistMessageError(transportState.getConnectorTransportId().getTransportId(), error)
            );
            if (message != null) {
                messagePersistenceService.rejectMessage(message);
            }
        }

        //TODO: put this into seperate method - delete only if state FAILED or FINISHED...
//        try {
//            if (!isEvidenceMessage(message)) {
//                try {
//                    contentStorageService.cleanForMessage(message);
//                    LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully deleted message content of message [{}]", message.getConnectorMessageId());
//                } catch (Exception e) {
//                    LOGGER.warn(LoggingMarker.BUSINESS_LOG, "Was not able to delete message content of message", e);
//                }
//            } else {
//                LOGGER.debug("#updateTransportToBackendStatus:: Message is an evidence message, no content deletion will be triggered!");
//            }
//        } catch (Exception e) {
//            LOGGER.error(String.format("Exception occured while cleaning up after message successfully handed over with transport id [%s]", transportId), e);
//        }
    }


    @Override
    @Transactional
    public void updateTransportStatus(DomibusConnectorTransportState transportState) {
        this.updateTransportStatus(transportState.getConnectorTransportId(), transportState, (m) -> {});
    }

    @Override
    @Transactional
    public TransportId createTransportFor(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {

        DomibusConnectorTransportStep transportStep = new DomibusConnectorTransportStep();
        transportStep.setLinkPartnerName(linkPartnerName);
        transportStep.setCreated(LocalDateTime.now());
        transportStep.setMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId(message.getConnectorMessageId()));

        transportStep = transportStepPersistenceService.createNewTransportStep(transportStep);
        LOGGER.debug("#createTransportFor:: created new transport step within database with id [{}]", transportStep.getTransportId());
        return transportStep.getTransportId();
    }

    @Override
    @Transactional
    public TransportId createOrGetTransportFor(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        //TODO: check for active transport...
        return createTransportFor(message, linkPartnerName);
    }

    @Override
    public List<DomibusConnectorTransportStep> getPendingTransportsForLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        return transportStepPersistenceService.findPendingStepBy(linkPartnerName);
    }

}

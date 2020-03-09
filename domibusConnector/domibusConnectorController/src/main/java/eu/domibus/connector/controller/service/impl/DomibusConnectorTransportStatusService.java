package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.persistence.dao.DomibusConnectorTransportStepDao;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static eu.domibus.connector.domain.model.helper.DomainModelHelper.isEvidenceMessage;

@Service
@Transactional
public class DomibusConnectorTransportStatusService implements TransportStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorTransportStatusService.class);

    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    private DomibusConnectorPersistAllBigDataOfMessageService contentStorageService;
    private DomibusConnectorMessageErrorPersistenceService errorPersistenceService;

    @Autowired
    TransportStepPersistenceService transportStepPersistenceService;

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setContentStorageService(DomibusConnectorPersistAllBigDataOfMessageService contentStorageService) {
        this.contentStorageService = contentStorageService;
    }

    @Autowired
    public void setErrorPersistenceService(DomibusConnectorMessageErrorPersistenceService errorPersistenceService) {
        this.errorPersistenceService = errorPersistenceService;
    }



    @Override
    @Transactional
    public void updateTransportToGatewayStatus(DomibusConnectorTransportState transportState) {
        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(transportState.getConnectorTransportId().getTransportId());
        if (message == null) {
            //cannot update a transport for a null message maybe it's a evidence message, but they don't have
            // a relation to connector message id yet...so cannot set transport state for them!
            LOGGER.debug("#updateTransportToGatewayStatus:: No message with transport id [{}] was found within database, maybe it's an evidence message", transportState.getConnectorTransportId());
            return;
        }

        if (transportState.getStatus() == TransportState.ACCEPTED) {
            if (message != null) {
                message.getMessageDetails().setEbmsMessageId(transportState.getRemoteTransportId());
                messagePersistenceService.mergeMessageWithDatabase(message);
                messagePersistenceService.setDeliveredToGateway(message);
            }
        } else if (transportState.getStatus() == TransportState.FAILED) {
            //TODO: reject message async... -> inform backend async of rejection!
            transportState.getMessageErrorList().stream().forEach( error ->
                    errorPersistenceService.persistMessageError(transportState.getConnectorTransportId().getTransportId(), error)
            );
        }

        //TODO: async call!
        if (!isEvidenceMessage(message)) {
            try {
                contentStorageService.cleanForMessage(message);
                LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully deleted message content of outgoing message [{}]", message.getConnectorMessageId());
            } catch (RuntimeException e) {
                LOGGER.warn(LoggingMarker.BUSINESS_LOG, "Was not able to delete message content of outgoing message", e);
            }
        } else {
            LOGGER.debug("#updateTransportToGatewayStatus:: Message is an evidence message, no content deletion will be triggered!");
        }

    }


    @Override
    public void updateTransportToBackendClientStatus(DomibusConnectorTransportState transportState) {




//        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(transportState.getTransportId());
//        messagePersistenceService.confirmMessage(message);
//        message.getMessageDetails().setEbmsMessageId(transportState.getRemoteTransportId());
//        messagePersistenceService.mergeMessageWithDatabase(message);

        //TODO: trigger content deletion, failure handling, ...
    }

    @Override
    public void updateTransportStatus(DomibusConnectorTransportState transportState) {
        //TODO: update transport state!

        DomibusConnectorTransportStep transportStep = transportStepPersistenceService.getTransportStepByTransportId(transportState.getConnectorTransportId());

        if (StringUtils.isEmpty(transportStep.getRemoteMessageId())) {
            transportStep.setRemoteMessageId(transportState.getRemoteTransportId());
        }
        if (StringUtils.isEmpty(transportStep.getTransportSystemMessageId())) {
            transportStep.setTransportSystemMessageId(transportState.getTransportImplId());
        }
        DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate statusUpdate = new DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate();
        statusUpdate.setCreated(LocalDateTime.now());
        statusUpdate.setTransportState(transportState.getStatus());


        transportStep.addTransportStatus(statusUpdate);

        transportStepPersistenceService.update(transportStep);


//        transportStep.setRemoteMessageId(transportState.getRemoteTransportId());

    }

    @Override
    public TransportId createTransportFor(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {

        DomibusConnectorTransportStep transportStep = new DomibusConnectorTransportStep();
        transportStep.setLinkPartnerName(linkPartnerName);
        transportStep.setCreated(LocalDateTime.now());
        transportStep.setMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId(message.getConnectorMessageId()));

        transportStep = transportStepPersistenceService.createNewTransportStep(transportStep);

        return transportStep.getTransportId();
    }

    @Override
    public TransportId createOrGetTransportFor(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        return createTransportFor(message, linkPartnerName);
    }

}

package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static eu.domibus.connector.domain.model.helper.DomainModelHelper.isEvidenceMessage;

@Service
@Transactional
public class DomibusConnectorTransportStatusService implements TransportStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorTransportStatusService.class);

    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    private DomibusConnectorPersistAllBigDataOfMessageService contentStorageService;
    private DomibusConnectorMessageErrorPersistenceService errorPersistenceService;

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
        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(transportState.getTransportId());

        if (transportState.getStatus() == TransportState.ACCEPTED) {
            message.getMessageDetails().setEbmsMessageId(transportState.getRemoteTransportId());
            messagePersistenceService.mergeMessageWithDatabase(message);
            messagePersistenceService.setDeliveredToGateway(message);
        } else if (transportState.getStatus() == TransportState.FAILED) {
            //TODO: reject message...
            transportState.getMessageErrorList().stream().forEach( error ->
                    errorPersistenceService.persistMessageError(transportState.getTransportId(), error)
            );
        }

        if (!isEvidenceMessage(message)) {
            try {
                contentStorageService.cleanForMessage(message);
                LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully deleted message content of outgoing message [{}]", message.getConnectorMessageId());
            } catch (RuntimeException e) {
                LOGGER.warn(LoggingMarker.BUSINESS_LOG, "Was not able to delete message content of incoming message", e);
            }
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

}

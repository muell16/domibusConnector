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
public class DomibusConnectorTransportStatusService implements TransportStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorTransportStatusService.class);

    @Autowired
    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    private DomibusConnectorPersistAllBigDataOfMessageService contentStorageService;

    private DomibusConnectorMessageErrorPersistenceService errorPersistenceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setTransportStatusForTransportToGateway(DomibusConnectorTransportState transportState) {

        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(transportState.getTransportId());
        if (transportState.getStatus() == TransportState.ACCEPTED) {
            message.getMessageDetails().setEbmsMessageId(transportState.getRemoteTransportId());
            messagePersistenceService.mergeMessageWithDatabase(message);
        } else if (transportState.getStatus() == TransportState.FAILED) {
            //TODO: reject message...
            transportState.getMessageErrorList().stream().forEach( error ->
                    errorPersistenceService.persistMessageError(transportState.getTransportId(), error)
            );
        }

//        if (!isEvidenceMessage(message)) {
//            try {
//                contentStorageService.cleanForMessage(message);
//                LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully deleted message content of outgoing message [{}]", message.getConnectorMessageId());
//            } catch (RuntimeException e) {
//                LOGGER.warn(LoggingMarker.BUSINESS_LOG, "Was not able to delete message content of incoming message", e);
//            }
//        }
//          TODO: async trigger message deletion....!


    }


    @Override
    public void setTransportStatusForTransportToBackendClient(DomibusConnectorTransportState transportState) {

//        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(transportState.getTransportId());
//        messagePersistenceService.confirmMessage(message);
//        message.getMessageDetails().setEbmsMessageId(transportState.getRemoteTransportId());
//        messagePersistenceService.mergeMessageWithDatabase(message);

        //TODO: trigger content deletion, failure handling, ...
    }

}

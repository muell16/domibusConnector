package eu.domibus.connector.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.exception.ImplementationMissingException;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.nbc.DomibusConnectorRemoteNationalBackendService;

public class IncomingEvidenceService implements EvidenceService {

    static Logger LOGGER = LoggerFactory.getLogger(IncomingEvidenceService.class);

    DomibusConnectorPersistenceService persistenceService;
    DomibusConnectorRemoteNationalBackendService nationalBackendClient;

    public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setNationalBackendClient(DomibusConnectorRemoteNationalBackendService nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    @Override
    public void handleEvidence(DomibusConnectorMessage confirmationMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        String messageID = confirmationMessage.getMessageDetails().getRefToMessageId();

        DomibusConnectorMessage originalMessage = persistenceService.findMessageByEbmsId(messageID);

        DomibusConnectorMessageConfirmation confirmation = confirmationMessage.getMessageConfirmations().get(0);

        if (isMessageAlreadyRejected(originalMessage)) {
            persistenceService.rejectMessage(originalMessage);
            throw new DomibusConnectorMessageException(originalMessage, "Received evidence of type "
                    + confirmation.getEvidenceType().toString() + " for an already rejected Message with ebms ID "
                    + messageID, this.getClass());
        }

        originalMessage.addConfirmation(confirmation);

        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                confirmation.getEvidenceType());

        try {
            nationalBackendClient.deliverLastEvidenceForMessage(confirmationMessage);
        } catch (DomibusConnectorNationalBackendClientException e) {
            throw new DomibusConnectorMessageException(originalMessage,
                    "Could not deliver Evidence to national backend system! ", e, this.getClass());
        } catch (ImplementationMissingException e) {
            throw new DomibusConnectorControllerException(e);
        }

        try {
            persistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());
        } catch (PersistenceException persistenceException) {
            LOGGER.error("Persistence Exception occured", persistenceException);
        }

        //TODO:        
//        if (originalMessage.getDbMessage().getDirection().equals(MessageDirection.NAT_TO_GW)
//                && originalMessage.getDbMessage().getConfirmed()==null && originalMessage.getDbMessage().getRejected()==null) {
//        	if (confirmation.getEvidenceType().equals(EvidenceType.RELAY_REMMD_ACCEPTANCE)
//                    || confirmation.getEvidenceType().equals(EvidenceType.DELIVERY)) {
//        		persistenceService.confirmMessage(originalMessage);
//        	}
//        }

//        LOGGER.info("Successfully processed evidence of type {} to message {}", confirmation.getEvidenceType(),
//                originalMessage.getDbMessageId());
    }

    //better in Domain, message should know that by itself
    private boolean isMessageAlreadyRejected(DomibusConnectorMessage message) {
//        if (message.getDbMessage().getRejected() != null) {
//            return true;
//        } //TODO!
        if (message.getMessageConfirmations() != null) {
            for (DomibusConnectorMessageConfirmation confirmation : message.getMessageConfirmations()) {
                if (confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION)
                        || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.NON_DELIVERY)
                        || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.NON_RETRIEVAL)) {
                	persistenceService.rejectMessage(message);
                    return true;
                }
            }
        }
        return false;
    }

}

package eu.ecodex.connector.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class IncomingEvidenceService implements EvidenceService {

    static Logger LOGGER = LoggerFactory.getLogger(IncomingEvidenceService.class);

    ECodexConnectorPersistenceService persistenceService;
    ECodexConnectorNationalBackendClient nationalBackendClient;

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setNationalBackendClient(ECodexConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    @Override
    public void handleEvidence(Message confirmationMessage) throws ECodexConnectorControllerException {

        String messageID = confirmationMessage.getMessageDetails().getRefToMessageId();

        Message originalMessage = persistenceService.findMessageByEbmsId(messageID);

        MessageConfirmation confirmation = confirmationMessage.getConfirmations().get(0);

        if (isMessageAlreadyRejected(originalMessage)) {
            persistenceService.rejectMessage(originalMessage);
            throw new ECodexConnectorControllerException("Received evidence of type "
                    + confirmation.getEvidenceType().toString() + " for an already rejected Message with ebms ID "
                    + messageID);
        }

        originalMessage.addConfirmation(confirmation);

        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                confirmation.getEvidenceType());

        try {
            nationalBackendClient.deliverLastEvidenceForMessage(confirmationMessage);
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException("Could not deliver Evidence to national backend system! ", e);
        } catch (ImplementationMissingException e) {
            throw new ECodexConnectorControllerException(e);
        }

        persistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());

        if (originalMessage.getDbMessage().getDirection().equals(ECodexMessageDirection.NAT_TO_GW)
                && (confirmation.getEvidenceType().equals(ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE) || confirmation
                        .getEvidenceType().equals(ECodexEvidenceType.RELAY_REMMD_REJECTION))) {
            persistenceService.confirmMessage(originalMessage);
        }

        LOGGER.info("Successfully processed evidence of type {} to message {}", confirmation.getEvidenceType(),
                originalMessage.getDbMessage().getId());
    }

    private boolean isMessageAlreadyRejected(Message message) {
        if (message.getDbMessage().getRejected() != null) {
            return true;
        }
        if (message.getConfirmations() != null) {
            for (MessageConfirmation confirmation : message.getConfirmations()) {
                if (confirmation.getEvidenceType().equals(ECodexEvidenceType.RELAY_REMMD_REJECTION)
                        || confirmation.getEvidenceType().equals(ECodexEvidenceType.NON_DELIVERY)
                        || confirmation.getEvidenceType().equals(ECodexEvidenceType.NON_RETRIEVAL)) {
                    return true;
                }
            }
        }
        return false;
    }

}

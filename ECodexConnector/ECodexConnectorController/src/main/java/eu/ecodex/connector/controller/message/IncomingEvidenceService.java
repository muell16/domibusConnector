package eu.ecodex.connector.controller.message;

import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class IncomingEvidenceService implements EvidenceService {

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
    }

}

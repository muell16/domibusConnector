package eu.ecodex.connector.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.db.model.ECodexAction;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.evidences.ECodexConnectorEvidencesToolkit;
import eu.ecodex.connector.evidences.exception.ECodexConnectorEvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;
import eu.ecodex.connector.gwc.ECodexConnectorGatewayWebserviceClient;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class OutgoingEvidenceService implements EvidenceService {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingEvidenceService.class);

    ECodexConnectorPersistenceService persistenceService;
    ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient;
    ECodexConnectorEvidencesToolkit evidencesToolkit;

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setGatewayWebserviceClient(ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

    public void setEvidencesToolkit(ECodexConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Override
    public void handleEvidence(Message confirmationMessage) throws ECodexConnectorControllerException {
        String messageID = confirmationMessage.getMessageDetails().getRefToMessageId();

        Message originalMessage = persistenceService.findMessageByEbmsId(messageID);
        ECodexEvidenceType evidenceType = confirmationMessage.getConfirmations().get(0).getEvidenceType();

        MessageDetails details = new MessageDetails();
        ECodexAction action = createEvidenceAction(evidenceType);
        details.setAction(action);
        details.setService(confirmationMessage.getMessageDetails().getService());
        details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
        details.setConversationId(originalMessage.getMessageDetails().getConversationId());
        details.setFromParty(confirmationMessage.getMessageDetails().getFromParty());
        details.setToParty(confirmationMessage.getMessageDetails().getToParty());

        MessageConfirmation confirmation = null;
        try {
            confirmation = generateEvidence(evidenceType, originalMessage);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            throw new ECodexConnectorControllerException("Could not handle Evidence to Message " + messageID, e);
        }

        originalMessage.addConfirmation(confirmation);
        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                evidenceType);

        Message evidenceMessage = new Message(details, confirmation);

        try {
            gatewayWebserviceClient.sendMessage(evidenceMessage);
        } catch (ECodexConnectorGatewayWebserviceClientException gwse) {
            throw new ECodexConnectorControllerException("Could not send ECodex Evidence Message to Gateway! ", gwse);
        }

        persistenceService.setEvidenceDeliveredToGateway(originalMessage, evidenceType);

        LOGGER.info("Successfully sent evidence of type {} for message {} to gateway.", confirmation.getEvidenceType(),
                originalMessage.getDbMessage().getId());
    }

    private MessageConfirmation generateEvidence(ECodexEvidenceType type, Message originalMessage)
            throws ECodexConnectorEvidencesToolkitException, ECodexConnectorControllerException {
        switch (type) {
        case DELIVERY:
            return evidencesToolkit.createDeliveryEvidence(originalMessage);
        case NON_DELIVERY:
            return evidencesToolkit.createNonDeliveryEvidence(RejectionReason.OTHER, originalMessage);
        case RETRIEVAL:
            return evidencesToolkit.createRetrievalEvidence(originalMessage);
        case NON_RETRIEVAL:
            return evidencesToolkit.createNonRetrievalEvidence(RejectionReason.OTHER, originalMessage);
        default:
            throw new ECodexConnectorControllerException("Illegal Evidence type to be generated!");
        }
    }

    private ECodexAction createEvidenceAction(ECodexEvidenceType type) throws ECodexConnectorControllerException {
        switch (type) {
        case DELIVERY:
            return persistenceService.getDeliveryNonDeliveryToRecipientAction();
        case NON_DELIVERY:
            return persistenceService.getDeliveryNonDeliveryToRecipientAction();
        case RETRIEVAL:
            return persistenceService.getRetrievalNonRetrievalToRecipientAction();
        case NON_RETRIEVAL:
            return persistenceService.getRetrievalNonRetrievalToRecipientAction();
        default:
            throw new ECodexConnectorControllerException("Illegal Evidence type! No Action found!");
        }
    }

}

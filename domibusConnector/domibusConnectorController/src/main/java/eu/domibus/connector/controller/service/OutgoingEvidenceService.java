package eu.domibus.connector.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.Action;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;

public class OutgoingEvidenceService implements EvidenceService {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingEvidenceService.class);

    DomibusConnectorPersistenceService persistenceService;
    DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient;
    DomibusConnectorEvidencesToolkit evidencesToolkit;

    public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setGatewayWebserviceClient(DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Override
    public void handleEvidence(Message confirmationMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        String messageID = confirmationMessage.getMessageDetails().getRefToMessageId();

        Message originalMessage = persistenceService.findMessageByEbmsId(messageID);
        EvidenceType evidenceType = confirmationMessage.getConfirmations().get(0).getEvidenceType();

        MessageDetails details = new MessageDetails();
        Action action = createEvidenceAction(evidenceType);
        details.setAction(action);
        details.setService(confirmationMessage.getMessageDetails().getService());
        details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
        details.setConversationId(originalMessage.getMessageDetails().getConversationId());
        details.setFromParty(confirmationMessage.getMessageDetails().getFromParty());
        details.setToParty(confirmationMessage.getMessageDetails().getToParty());

        MessageConfirmation confirmation = null;
        try {
            confirmation = generateEvidence(evidenceType, originalMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw new DomibusConnectorMessageException(originalMessage, "Could not handle Evidence to Message "
                    + messageID, e, this.getClass());
        }

        originalMessage.addConfirmation(confirmation);
        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                evidenceType);

        Message evidenceMessage = new Message(details, confirmation);

        try {
            gatewayWebserviceClient.sendMessage(evidenceMessage);
        } catch (DomibusConnectorGatewayWebserviceClientException gwse) {
            throw new DomibusConnectorMessageException(originalMessage, "Could not send Evidence Message to Gateway! ",
                    gwse, this.getClass());
        }

        persistenceService.setEvidenceDeliveredToGateway(originalMessage, evidenceType);

        //TODO!
//        if (originalMessage.getDbMessage().getConfirmed() == null) {
//            persistenceService.confirmMessage(originalMessage);
//        }

        LOGGER.info("Successfully sent evidence of type {} for message {} to gateway.", confirmation.getEvidenceType(),
                originalMessage.getDbMessageId());
    }

    private MessageConfirmation generateEvidence(EvidenceType type, Message originalMessage)
            throws DomibusConnectorEvidencesToolkitException, DomibusConnectorMessageException {
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
            throw new DomibusConnectorMessageException(originalMessage, "Illegal Evidence type " + type
                    + " to be generated!", this.getClass());
        }
    }

    private Action createEvidenceAction(EvidenceType type) throws DomibusConnectorControllerException {
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
            throw new DomibusConnectorControllerException("Illegal Evidence type " + type + "! No Action found!");
        }
    }

}

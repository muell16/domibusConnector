package eu.domibus.connector.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;

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
    public void handleEvidence(DomibusConnectorMessage confirmationMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        String messageID = confirmationMessage.getMessageDetails().getRefToMessageId();

        DomibusConnectorMessage originalMessage = persistenceService.findMessageByEbmsId(messageID);
        DomibusConnectorEvidenceType evidenceType = confirmationMessage.getMessageConfirmations().get(0).getEvidenceType();

        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        DomibusConnectorAction action = createEvidenceAction(evidenceType);
        details.setAction(action);
        details.setService(confirmationMessage.getMessageDetails().getService());
        details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
        details.setConversationId(originalMessage.getMessageDetails().getConversationId());
        details.setFromParty(confirmationMessage.getMessageDetails().getFromParty());
        details.setToParty(confirmationMessage.getMessageDetails().getToParty());

        DomibusConnectorMessageConfirmation confirmation = null;
        try {
            confirmation = generateEvidence(evidenceType, originalMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw new DomibusConnectorMessageException(originalMessage, "Could not handle Evidence to Message "
                    + messageID, e, this.getClass());
        }

        originalMessage.addConfirmation(confirmation);
        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                evidenceType);

        DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, confirmation);

        try {
            gatewayWebserviceClient.sendMessage(evidenceMessage);
        } catch (DomibusConnectorGatewayWebserviceClientException gwse) {
            throw new DomibusConnectorMessageException(originalMessage, "Could not send Evidence Message to Gateway! ",
                    gwse, this.getClass());
        }

        try {
            persistenceService.setEvidenceDeliveredToGateway(originalMessage, evidenceType);
        } catch(PersistenceException persistenceException) {
            //TODO: exception
            LOGGER.error("persistence Exception occured", persistenceException);
        }

        //TODO!
        //if (originalMessage.getDbMessage().getConfirmed() == null) {        
        //    persistenceService.confirmMessage(originalMessage);
        //}

        LOGGER.info("Successfully sent evidence of type {} for message {} to gateway.", confirmation.getEvidenceType(), originalMessage);
    }

    private DomibusConnectorMessageConfirmation generateEvidence(DomibusConnectorEvidenceType type, DomibusConnectorMessage originalMessage)
            throws DomibusConnectorEvidencesToolkitException, DomibusConnectorMessageException {
            return evidencesToolkit.createEvidence(type, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
        
    }

    private DomibusConnectorAction createEvidenceAction(DomibusConnectorEvidenceType type) throws DomibusConnectorControllerException {
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

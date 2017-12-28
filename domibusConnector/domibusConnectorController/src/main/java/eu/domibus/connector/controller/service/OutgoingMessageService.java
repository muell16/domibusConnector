package eu.domibus.connector.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.MessageDirection;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;
import eu.domibus.connector.mapping.exception.DomibusConnectorContentMapperException;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

public class OutgoingMessageService extends AbstractMessageService implements MessageService {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingMessageService.class);

    @Override
    public void handleMessage(Message message) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

            persistenceService.persistMessageIntoDatabase(message, MessageDirection.NAT_TO_GW);

        
        if (connectorProperties.isUseContentMapper()) {
            try {
                contentMapper.mapNationalToInternational(message);
            } catch (DomibusConnectorContentMapperException | ImplementationMissingException cme) {
                createSubmissionRejectionAndReturnIt(message, cme.getMessage());
                throw new DomibusConnectorMessageException(message, cme.getMessage(), cme, this.getClass());
            }
            persistenceService.mergeMessageWithDatabase(message);
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            try {
                securityToolkit.buildContainer(message);
            } catch (DomibusConnectorSecurityException se) {
                createSubmissionRejectionAndReturnIt(message, se.getMessage());
                throw new DomibusConnectorMessageException(message, se.getMessage(), se, this.getClass());
            }
        }

        MessageConfirmation confirmation = null;
        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
            	confirmation = evidencesToolkit.createEvidence(EvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
                // immediately persist new evidence into database
                persistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation.getEvidence(),
                        EvidenceType.SUBMISSION_ACCEPTANCE);

            } catch (DomibusConnectorEvidencesToolkitException ete) {
                createSubmissionRejectionAndReturnIt(message, ete.getMessage());
                throw new DomibusConnectorMessageException(message,
                        "Could not generate evidence submission acceptance! ", ete, this.getClass());
            }

        }

        try {
            gatewayWebserviceClient.sendMessage(message);
        } catch (DomibusConnectorGatewayWebserviceClientException gwse) {
            createSubmissionRejectionAndReturnIt(message, gwse.getMessage());
            throw new DomibusConnectorMessageException(message, "Could not send Message to Gateway! ", gwse,
                    this.getClass());
        }

        persistenceService.setMessageDeliveredToGateway(message);
        persistenceService.setEvidenceDeliveredToGateway(message, confirmation.getEvidenceType());

        try {
            Message returnMessage = buildEvidenceMessage(confirmation, message);
            nationalBackendClient.deliverLastEvidenceForMessage(returnMessage);
        } catch (DomibusConnectorNationalBackendClientException | ImplementationMissingException e) {
            throw new DomibusConnectorMessageException(message,
                    "Could not send submission acceptance back to national connector! ", e, this.getClass());
        }

        persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());

        LOGGER.info("Successfully sent message with id {} to gateway.", message.getDbMessageId());

    }


    private void createSubmissionRejectionAndReturnIt(Message message, String errorMessage){

    	MessageConfirmation confirmation = null;
        try {
        	confirmation = evidencesToolkit.createEvidence(EvidenceType.SUBMISSION_REJECTION, message, RejectionReason.OTHER,
                    errorMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            new DomibusConnectorMessageException(message, "Could not even generate submission rejection! ", e,
                    this.getClass());
            LOGGER.error("Could not even generate submission rejection! ", e);
            return;
        }

        try {
            // immediately persist new evidence into database
            persistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation.getEvidence(),
                    EvidenceType.SUBMISSION_REJECTION);
        } catch (Exception e) {
            new DomibusConnectorMessageException(message, "Could not persist evidence of type SUBMISSION_REJECTION! ",
                    e, this.getClass());
            LOGGER.error("Could not persist evidence of type SUBMISSION_REJECTION! ", e);
            return;
        }

        try {
            Message returnMessage = buildEvidenceMessage(confirmation, message);
            nationalBackendClient.deliverLastEvidenceForMessage(returnMessage);
            persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());

            persistenceService.rejectMessage(message);
        } catch (DomibusConnectorNationalBackendClientException | ImplementationMissingException e) {
            new DomibusConnectorMessageException(message, "Exception while trying to send submission rejection. ", e,
                    this.getClass());
            LOGGER.error("Exception while trying to send submission rejection. ", e);
            return;
        }

    }

    private Message buildEvidenceMessage(MessageConfirmation confirmation, Message originalMessage) {
        MessageDetails details = new MessageDetails();
        details.setRefToMessageId(originalMessage.getMessageDetails().getNationalMessageId());
        details.setService(originalMessage.getMessageDetails().getService());

        Action action = persistenceService.getAction("SubmissionAcceptanceRejection");
        details.setAction(action);

        Message returnMessage = new Message(details, confirmation);

        return returnMessage;
    }

}

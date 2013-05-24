package eu.ecodex.connector.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.exception.PersistenceException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.evidences.exception.ECodexConnectorEvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class OutgoingMessageService extends AbstractMessageService implements MessageService {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingMessageService.class);

    @Override
    public void handleMessage(Message message) throws ECodexConnectorControllerException {

        try {
            persistenceService.persistMessageIntoDatabase(message, ECodexMessageDirection.NAT_TO_GW);
        } catch (PersistenceException e1) {
            // createSubmissionRejectionAndReturnIt(message, hashValue);
            throw new ECodexConnectorControllerException(e1);
        }

        String hashValue = null;
        try {
            hashValue = buildAndPersistHashValue(message);
        } catch (ECodexConnectorControllerException e) {
            createSubmissionRejectionAndReturnIt(message, "0");
        }

        if (connectorProperties.isUseContentMapper()) {
            try {
                contentMapper.mapNationalToInternational(message);
            } catch (ECodexConnectorContentMapperException cme) {
                createSubmissionRejectionAndReturnIt(message, hashValue);
                throw new ECodexConnectorControllerException(cme);
            } catch (ImplementationMissingException ime) {
                createSubmissionRejectionAndReturnIt(message, hashValue);
                throw new ECodexConnectorControllerException(ime);
            }
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            securityToolkit.buildContainer(message);
        }

        MessageConfirmation confirmation = null;
        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
                byte[] submissionAcceptance = evidencesToolkit.createSubmissionAcceptance(message, hashValue);
                // immediately persist new evidence into database
                persistenceService.persistEvidenceForMessageIntoDatabase(message, submissionAcceptance,
                        ECodexEvidenceType.SUBMISSION_ACCEPTANCE);

                confirmation = new MessageConfirmation(ECodexEvidenceType.SUBMISSION_ACCEPTANCE, submissionAcceptance);
            } catch (ECodexConnectorEvidencesToolkitException ete) {
                createSubmissionRejectionAndReturnIt(message, hashValue);
                throw new ECodexConnectorControllerException("Could not generate evidence for submission acceptance! ",
                        ete);
            }

        }

        try {
            gatewayWebserviceClient.sendMessage(message);
        } catch (ECodexConnectorGatewayWebserviceClientException gwse) {
            createSubmissionRejectionAndReturnIt(message, hashValue);
            throw new ECodexConnectorControllerException("Could not send ECodex Message to Gateway! ", gwse);
        }

        persistenceService.setMessageDeliveredToGateway(message);
        persistenceService.setEvidenceDeliveredToGateway(message, confirmation.getEvidenceType());

        try {
            Message returnMessage = buildEvidenceMessage(confirmation, message);
            nationalBackendClient.deliverLastEvidenceForMessage(returnMessage);
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException(
                    "Could not send submission acceptance back to national connector! ", e);
        } catch (ImplementationMissingException e) {
            throw new ECodexConnectorControllerException(
                    "Could not send submission acceptance back to national connector! ", e);
        }

        persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());

    }

    private String buildAndPersistHashValue(Message message) throws ECodexConnectorControllerException {

        String hash = null;
        if (message.getMessageContent().getPdfDocument() != null
                && message.getMessageContent().getPdfDocument().length > 0) {

            // whatever the source for the hash will be - by now it is the pdf
            // document
            hash = hashValueBuilder.buildHashValueAsString(message.getMessageContent().getPdfDocument());
        } else {
            throw new ECodexConnectorControllerException("The PDF content is null or empty! Message must not be sent!");
        }

        // now persist the hash value into the database entry for the
        // message
        message.getDbMessage().setHashValue(hash);
        persistenceService.mergeMessageWithDatabase(message);

        return hash;
    }

    private void createSubmissionRejectionAndReturnIt(Message message, String hashValue)
            throws ECodexConnectorControllerException {
        try {
            byte[] submissionRejection = evidencesToolkit.createSubmissionRejection(RejectionReason.OTHER, message,
                    hashValue);

            // immediately persist new evidence into database
            persistenceService.persistEvidenceForMessageIntoDatabase(message, submissionRejection,
                    ECodexEvidenceType.SUBMISSION_REJECTION);

            MessageConfirmation confirmation = new MessageConfirmation(ECodexEvidenceType.SUBMISSION_REJECTION,
                    submissionRejection);

            Message returnMessage = buildEvidenceMessage(confirmation, message);
            nationalBackendClient.deliverLastEvidenceForMessage(returnMessage);
            persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());

            persistenceService.rejectMessage(message);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            LOGGER.error("Could not even generate submission rejection! ", e);
            return;
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException("Exception while trying to send submission rejection. ", e);
        } catch (ImplementationMissingException ime) {
            throw new ECodexConnectorControllerException("Exception while trying to send submission rejection. ", ime);
        }

    }

    private Message buildEvidenceMessage(MessageConfirmation confirmation, Message originalMessage) {
        MessageDetails details = new MessageDetails();
        details.setRefToMessageId(originalMessage.getMessageDetails().getNationalMessageId());

        Message returnMessage = new Message(details, confirmation);

        return returnMessage;
    }

}

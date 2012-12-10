package eu.ecodex.connector.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
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
    public void handleMessage(String messageId) throws ECodexConnectorControllerException {
        MessageDetails details = new MessageDetails();
        details.setNationalMessageId(messageId);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        try {
            nationalBackendClient.requestMessage(message);
        } catch (ECodexConnectorNationalBackendClientException e1) {
            throw new ECodexConnectorControllerException(
                    "Exception while trying to receive message from national system. ", e1);
        } catch (ImplementationMissingException ime) {
            throw new ECodexConnectorControllerException(
                    "Exception while trying to receive message from national system. ", ime);
        }

        ECodexMessage dbMessage = dbMessageService.createAndPersistDBMessage(message, ECodexMessageDirection.NAT_TO_GW);
        byte[] hashValue = buildAndPersistHashValue(message, dbMessage);

        if (connectorProperties.isUseContentMapper()) {
            try {
                byte[] xmlContent = contentMapper.mapNationalToInternational(message.getMessageContent()
                        .getXmlContent());
                message.getMessageContent().setXmlContent(xmlContent);
            } catch (ECodexConnectorContentMapperException cme) {
                createSubmissionRejectionAndReturnIt(message, hashValue);
                cme.printStackTrace();
            } catch (ImplementationMissingException ime) {
                createSubmissionRejectionAndReturnIt(message, hashValue);
                ime.printStackTrace();
            }
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            // TODO: Integration of SecurityToolkit to build ASIC-S container
            // and TrustOKToken
            LOGGER.warn("SecurityToolkit not available yet! Must send message unsecure!");
        }

        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
                byte[] submissionAcceptance = evidencesToolkit.createSubmissionAcceptance(message, hashValue);
                // immediately persist new evidence into database
                dbMessageService.createAndPersistDBEvidenceForDBMessage(dbMessage, submissionAcceptance,
                        ECodexEvidenceType.SUBMISSION_ACCEPTANCE);
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
    }

    private byte[] buildAndPersistHashValue(Message message, ECodexMessage dbMessage) {
        // whatever the source for the hash will be - by now it is the pdf
        // document
        byte[] hash = hashValueBuilder.buildHashValue(message.getMessageContent().getPdfDocument());

        // now persist the hash value into the database entry for the
        // message
        dbMessage.setHashValue(new String(hash));
        dbMessageService.mergeDBMessage(dbMessage);

        return hash;
    }

    private void createSubmissionRejectionAndReturnIt(Message message, byte[] hashValue)
            throws ECodexConnectorControllerException {
        try {
            byte[] submissionRejection = evidencesToolkit.createSubmissionRejection(RejectionReason.OTHER, message,
                    hashValue);
            MessageConfirmation confirmation = new MessageConfirmation(message.getMessageDetails()
                    .getNationalMessageId(), ECodexEvidenceType.SUBMISSION_REJECTION, submissionRejection);
            nationalBackendClient.deliverLastEvidenceForMessage(confirmation);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            LOGGER.error("Could not even generate submission rejection! ", e);
            return;
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException("Exception while trying to send submission rejection. ", e);
        } catch (ImplementationMissingException ime) {
            throw new ECodexConnectorControllerException("Exception while trying to send submission rejection. ", ime);
        }

    }

}

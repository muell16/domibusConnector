package eu.domibus.connector.controller.service;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.enums.MessageDirection;
import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.exception.PersistenceException;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
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

        try {
            persistenceService.persistMessageIntoDatabase(message, MessageDirection.NAT_TO_GW);
        } catch (PersistenceException e1) {
            // createSubmissionRejectionAndReturnIt(message, hashValue);
            throw new DomibusConnectorControllerException(e1);
        }

        String hashValue = null;

        if (connectorProperties.isUseContentMapper()) {
            try {
                contentMapper.mapNationalToInternational(message);
            } catch (DomibusConnectorContentMapperException | ImplementationMissingException cme) {
                createSubmissionRejectionAndReturnIt(message, hashValue, cme.getMessage());
                throw new DomibusConnectorMessageException(message, cme.getMessage(), cme, this.getClass());
            }
            persistenceService.mergeMessageWithDatabase(message);
        }

        try {
            hashValue = checkPDFandBuildHashValue(message, hashValue);
        } catch (DomibusConnectorControllerException e) {
            createSubmissionRejectionAndReturnIt(message, hashValue, e.getMessage());
            throw new DomibusConnectorMessageException(message, e.getMessage(), e, this.getClass());
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            try {
                securityToolkit.buildContainer(message);
            } catch (DomibusConnectorSecurityException se) {
                createSubmissionRejectionAndReturnIt(message, hashValue, se.getMessage());
                throw new DomibusConnectorMessageException(message, se.getMessage(), se, this.getClass());
            }
        }

        MessageConfirmation confirmation = null;
        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
                byte[] submissionAcceptance = evidencesToolkit.createSubmissionAcceptance(message, hashValue);
                // immediately persist new evidence into database
                persistenceService.persistEvidenceForMessageIntoDatabase(message, submissionAcceptance,
                        EvidenceType.SUBMISSION_ACCEPTANCE);

                confirmation = new MessageConfirmation(EvidenceType.SUBMISSION_ACCEPTANCE, submissionAcceptance);
            } catch (DomibusConnectorEvidencesToolkitException ete) {
                createSubmissionRejectionAndReturnIt(message, hashValue, ete.getMessage());
                throw new DomibusConnectorMessageException(message,
                        "Could not generate evidence submission acceptance! ", ete, this.getClass());
            }

        }

        try {
            gatewayWebserviceClient.sendMessage(message);
        } catch (DomibusConnectorGatewayWebserviceClientException gwse) {
            createSubmissionRejectionAndReturnIt(message, hashValue, gwse.getMessage());
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

        LOGGER.info("Successfully sent message with id {} to gateway.", message.getDbMessage().getId());

    }

    private String checkPDFandBuildHashValue(Message message, String hashValue)
            throws DomibusConnectorControllerException {
        if (ArrayUtils.isEmpty(message.getMessageContent().getPdfDocument())) {
            DomibusConnectorAction action = message.getMessageDetails().getAction();
            if (action == null) {
                throw new DomibusConnectorControllerException("Action still null after mapping!");
            }
            if (action.isPdfRequired()) {
                throw new DomibusConnectorControllerException(
                        "There is no PDF document in the message though the Action " + action.getAction()
                                + " requires one!");
            }
        } else {
            try {
                hashValue = hashValueBuilder.buildHashValueAsString(message.getMessageContent().getPdfDocument());

                if (StringUtils.isNotEmpty(hashValue)) {
                    message.getDbMessage().setHashValue(hashValue);
                    persistenceService.mergeMessageWithDatabase(message);
                }
            } catch (Exception e) {
                throw new DomibusConnectorControllerException("Could not build hash code though the PDF is not empty!",
                        e);
            }
        }
        return hashValue;
    }

    private void createSubmissionRejectionAndReturnIt(Message message, String hashValue, String errorMessage){

        byte[] submissionRejection = null;
        try {
            submissionRejection = evidencesToolkit.createSubmissionRejection(RejectionReason.OTHER, message, hashValue,
                    errorMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            new DomibusConnectorMessageException(message, "Could not even generate submission rejection! ", e,
                    this.getClass());
            LOGGER.error("Could not even generate submission rejection! ", e);
            return;
        }

        try {
            // immediately persist new evidence into database
            persistenceService.persistEvidenceForMessageIntoDatabase(message, submissionRejection,
                    EvidenceType.SUBMISSION_REJECTION);
        } catch (Exception e) {
            new DomibusConnectorMessageException(message, "Could not persist evidence of type SUBMISSION_REJECTION! ",
                    e, this.getClass());
            LOGGER.error("Could not persist evidence of type SUBMISSION_REJECTION! ", e);
            return;
        }

        MessageConfirmation confirmation = new MessageConfirmation(EvidenceType.SUBMISSION_REJECTION,
                submissionRejection);

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

        DomibusConnectorAction action = persistenceService.getAction("SubmissionAcceptanceRejection");
        details.setAction(action);

        Message returnMessage = new Message(details, confirmation);

        return returnMessage;
    }

}

package eu.domibus.connector.controller;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.service.EvidenceService;
import eu.domibus.connector.controller.service.MessageService;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageContent;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.nbc.DomibusConnectorNationalBackendClient;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;

public class DomibusConnectorOutgoingController implements DomibusConnectorController, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6828785110582970077L;

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorOutgoingController.class);

    private DomibusConnectorNationalBackendClient nationalBackendClient;
    private MessageService outgoingMessageService;
    private EvidenceService outgoingEvidenceService;

    public void setNationalBackendClient(DomibusConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    public void setOutgoingMessageService(MessageService outgoingMessageService) {
        this.outgoingMessageService = outgoingMessageService;
    }

    public void setOutgoingEvidenceService(EvidenceService outgoingEvidenceService) {
        this.outgoingEvidenceService = outgoingEvidenceService;
    }

    @Override
    public void execute() throws DomibusConnectorControllerException {
        LOGGER.debug("Job for handling outgoing messages triggered.");
        Date start = new Date();

        LOGGER.debug("Handling messages....");
        try {
            handleMessages();
        } catch (DomibusConnectorControllerException e) {
            throw new DomibusConnectorControllerException("Exception while proceeding job handleOutgoingMessages: ", e);
        }
        LOGGER.debug("Handling confirmations....");
        try {
            handleEvidences();
        } catch (DomibusConnectorControllerException e) {
            throw new DomibusConnectorControllerException("Exception while proceeding job handleOutgoingMessages: ", e);
        }
        LOGGER.debug("Job for handling outgoing messages finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));
    }

    private void handleMessages() throws DomibusConnectorControllerException {
        String[] messages = null;
        try {
            messages = nationalBackendClient.requestMessagesUnsent();
        } catch (DomibusConnectorNationalBackendClientException nbce) {
            throw new DomibusConnectorControllerException(
                    "Exception while trying to get messages list from national system. ", nbce);
        } catch (ImplementationMissingException ime) {
            throw new DomibusConnectorControllerException(
                    "Exception while trying to get messages list from national system. ", ime);
        } catch (Exception e) {
            throw new DomibusConnectorControllerException(
                    "Exception while trying to get messages list from national system. ", e);
        }

        if (messages != null && messages.length > 0) {

            LOGGER.info("Found {} outgoing messages on national system to handle...", messages.length);

            for (String messageId : messages) {

                try {
                    handleMessage(messageId);
                } catch (DomibusConnectorMessageException | DomibusConnectorControllerException e) {
                    LOGGER.error("Error handling message with id " + messageId, e);
                }
            }
        } else {
            LOGGER.debug("There are no unsent outgoing messages on national system!");
        }
    }

    private void handleMessage(String messageId) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        MessageDetails details = new MessageDetails();
        details.setNationalMessageId(messageId);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        try {
            nationalBackendClient.requestMessage(message);
        } catch (Exception e1) {
            throw new DomibusConnectorControllerException("Exception while trying to receive message with id "
                    + messageId + " from national system. ", e1);
        }

        try {
            outgoingMessageService.handleMessage(message);
        } catch (DomibusConnectorControllerException ce) {
            LOGGER.error(ce.getMessage());
            throw ce;
        }
    }

    private void handleEvidences() throws DomibusConnectorControllerException {
        LOGGER.debug("Started to check national implementation for pending confirmations!");

        Message[] confirmationMessages = null;
        try {
            confirmationMessages = nationalBackendClient.requestConfirmations();
        } catch (Exception e) {
            throw new DomibusConnectorControllerException(
                    "Exception while trying to get confirmations from national system. ", e);
        }

        if (confirmationMessages != null && confirmationMessages.length > 0) {

            LOGGER.info("Found {} confirmations on national system to handle...", confirmationMessages.length);

            for (Message confirmationMessage : confirmationMessages) {

                try {
                    outgoingEvidenceService.handleEvidence(confirmationMessage);
                } catch (DomibusConnectorMessageException | DomibusConnectorControllerException ce) {
                    LOGGER.error(ce.getMessage());
                }
            }
        }
    }

}

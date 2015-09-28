package eu.domibus.connector.nbc;

import java.util.List;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.common.message.MessageError;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;

/**
 * Interface which contains methods for a national backend client which has to
 * be implemented. There is no implementation of this interface by default.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorNationalBackendClient {

    /**
     * This method delivers a message received by the gateway. The message
     * content is already transformed into a national format, if there is a
     * content mapper configured and implemented.
     * 
     * @param message
     *            A {@link Message} object with all data concerning the message.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public void deliverMessage(Message message) throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * If there is a new evidence generated for a message and sent to the
     * connector, this new evidence must be sent to the national system.
     * 
     * @param confirmationMessage
     *            is the {@link Message} object which contains
     *            {@link MessageDetails} with the refToMessageId. It is the
     *            national id of the initial Message sent to a partner gateway.
     *            Additionally it contains a {@link MessageConfirmation} with
     *            the evidence last sent by the partner gateway or the
     *            connector.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public void deliverLastEvidenceForMessage(Message confirmationMessage)
            throws DomibusConnectorNationalBackendClientException, ImplementationMissingException;

    /**
     * Requests all messages from the national system that are not yet handled
     * by this connector instance, therefore not sent over the gateway.
     * 
     * @return an Array of messageId's that are queued in the national backend
     *         system and have to be handled by the connector.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public String[] requestMessagesUnsent() throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Requests a certain message that has to be handled by the connector and
     * sent over the gateway.
     * 
     * @param message
     *            A {@link Message} object with all data concerning the message.
     *            This object contains {@link MessageDetails} which holds the
     *            messageId of the message that is requested.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public void requestMessage(Message message) throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Requests all new confirmations for messages delivered to the national
     * backend system before. If the national system marks a message as
     * delivered, or retrieved a {@link MessageConfirmation} should be created
     * and packed into a {@link Message} object. This should contain
     * {@link MessageDetails} where the refToMessageId should be the id of the
     * initial message.
     * 
     * @return an Array of {@link Message} Objects which contain informations on
     *         what message is in which confirmation state.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public Message[] requestConfirmations() throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * This method calls the getMessageStatus method on the gateway backend
     * service directly. It returns, if provided, the message status on the
     * gateway. To implement this method {@link MessageStatusService} must be
     * injected. This method is not triggered by a timer configured job!
     * 
     * @param message
     *            A {@link Message} object that must contain either the
     *            ebmsMessageId or the nationalMessageId in the
     *            {@link MessageDetails} of the message which status is
     *            requested.
     * @return A simple String represantation of the message status provided by
     *         the gateway.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public String requestMessageStatusFromGateway(Message message)
            throws DomibusConnectorNationalBackendClientException, ImplementationMissingException;

    /**
     * This method collects all persisted errors from the connector database. It
     * also calls a backend webservice method to the gateway to request message
     * errors from the gateway, if provided. To implement this method
     * {@link MessageStatusService} must be injected. This method is not
     * triggered by a timer configured job!
     * 
     * @param message
     *            A {@link Message} object that must contain either the
     *            ebmsMessageId or the nationalMessageId in the
     *            {@link MessageDetails} of the message which status is
     *            requested.
     * @return A List of {@link MessageError} objects containing all
     *         informations on persisted errors to the given message.
     * @throws DomibusConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public List<MessageError> requestMessageErrors(Message message)
            throws DomibusConnectorNationalBackendClientException, ImplementationMissingException;
}

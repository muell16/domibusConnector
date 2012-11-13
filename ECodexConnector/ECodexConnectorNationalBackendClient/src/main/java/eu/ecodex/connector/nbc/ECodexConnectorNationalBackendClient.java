package eu.ecodex.connector.nbc;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

/**
 * Interface which contains methods for a national backend client which has to
 * be implemented. There is no implementation of this interface by default.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorNationalBackendClient {

    /**
     * This method delivers a message received by the gateway. The message
     * content is already transformed into a national format, if there is a
     * content mapper configured and implemented.
     * 
     * @param message
     *            A {@link Message} object with all data concerning the message.
     * @throws ECodexConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public void deliverMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * If there is a new evidence generated for a message and sent to the
     * connector, this new evidence must be sent to the national system.
     * 
     * @return A {@link MessageConfirmation} object containing the messageId,
     *         the evidence was generated for, the {@link ConfirmationStateEnum}
     *         and the evidence itself.
     * @throws ECodexConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public MessageConfirmation deliverLastEvidenceForMessage() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Requests all messages from the national system that are not yet handled
     * by this connector instance, therefore not sent over the gateway.
     * 
     * @return an Array of messageId's that are queued in the national backend
     *         system and have to be handled by the connector.
     * @throws ECodexConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public String[] requestMessagesUnsent() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Requests a certain message that has to be handled by the connector and
     * sent over the gateway.
     * 
     * @param message
     *            A {@link Message} object with all data concerning the message.
     *            This object contains {@link MessageDetails} which holds the
     *            messageId of the message that is requested.
     * @throws ECodexConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public void requestMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Requests all new confirmations for messages delivered to the national
     * backend system before. If the national system marks a message as
     * delivered, or retrieved a {@link MessageConfirmation} should be created
     * and queued.
     * 
     * @return an Array of {@link MessageConfirmation} Objects which contain
     *         informations on what message is in which confirmation state.
     * @throws ECodexConnectorNationalBackendClientException
     * @throws ImplementationMissingException
     */
    public MessageConfirmation[] requestConfirmations() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;
}

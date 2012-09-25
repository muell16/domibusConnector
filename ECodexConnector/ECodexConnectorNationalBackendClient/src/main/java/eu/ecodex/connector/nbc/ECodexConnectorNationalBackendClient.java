package eu.ecodex.connector.nbc;

import eu.ecodex.connector.common.EncryptedDocumentPackage;
import eu.ecodex.connector.common.MessageState;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
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
     * Method to deliver a message to the national backend system.
     * 
     * @param documentPackage
     *            - an encrypted document package which contains the message
     *            itself, an asic-s container and the trustOkToken as XML.
     * @return the immediate state of the message within the national backend
     *         system if available.
     * @throws ImplementationMissingException
     */
    public MessageState deliverMessage(EncryptedDocumentPackage documentPackage)
            throws ECodexConnectorNationalBackendClientException, ImplementationMissingException;

    /**
     * Method to request the state of a message formerly sent to the national
     * backend system.
     * 
     * @param messageId
     *            - the ID of the message of which the state is requested.
     * @return an enum value which represents the state of the message within
     *         the national backend system.
     * @throws ImplementationMissingException
     */
    public MessageState requestState(String messageId) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Method to request all messages from the national backend system that are
     * not yet handled.
     * 
     * @return an array of encrypted document package which contains the message
     *         itself, an asic-s container and the trustOkToken as XML.
     * @throws ImplementationMissingException
     */
    public EncryptedDocumentPackage[] requestAllPendingMessages() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException;

    /**
     * Method to request a message from the national backend system to send over
     * gateway.
     * 
     * @param messageId
     *            - The message ID of the message. Has to be requested by method
     *            checkPendingMessges()
     * @return an encrypted document package which contains the message itself,
     *         an asic-s container and the trustOkToken as XML.
     * @throws ImplementationMissingException
     */
    public EncryptedDocumentPackage requestMessage(String messageId)
            throws ECodexConnectorNationalBackendClientException, ImplementationMissingException;

}

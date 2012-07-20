package eu.ecodex.connector.nbc;

import eu.ecodex.connector.common.EncryptedDocumentPackage;
import eu.ecodex.connector.common.MessageState;
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
     */
    public MessageState deliverMessage(EncryptedDocumentPackage documentPackage)
            throws ECodexConnectorNationalBackendClientException;

    /**
     * Method to request the state of a message formerly sent to the national
     * backend system.
     * 
     * @param messageId
     *            - the ID of the message of which the state is requested.
     * @return an enum value which represents the state of the message within
     *         the national backend system.
     */
    public MessageState requestState(String messageId) throws ECodexConnectorNationalBackendClientException;

    /**
     * Method to ask the national backend system if there are pending messages
     * to send.
     * 
     * @return a String array which contains the message ID's of the messages to
     *         send over gateway.
     */
    public String[] checkPendingMessages() throws ECodexConnectorNationalBackendClientException;

    /**
     * Method to request a message from the national backend system to send over
     * gateway.
     * 
     * @param messageId
     *            - The message ID of the message. Has to be requested by method
     *            checkPendingMessges()
     * @return an encrypted document package which contains the message itself,
     *         an asic-s container and the trustOkToken as XML.
     */
    public EncryptedDocumentPackage requestMessage(String messageId)
            throws ECodexConnectorNationalBackendClientException;

}

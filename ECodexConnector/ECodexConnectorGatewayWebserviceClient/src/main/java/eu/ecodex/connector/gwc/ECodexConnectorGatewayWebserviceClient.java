package eu.ecodex.connector.gwc;

import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

/**
 * WebService Client Interface to communicate with the gateway.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorGatewayWebserviceClient {

	/**
	 * WebService method to send XML content to gateway. Payload of eCodex
	 * Message will be passed as a URI Reference.
	 * 
	 * @param The
	 *            message containing the URI where the message can be found.
	 */
	public void sendMessageWithReference(byte[] content) throws ECodexConnectorGatewayWebserviceClientException;

	/**
	 * WebService method to send XML content to gateway. Should contain eCodex
	 * Message as well.
	 * 
	 * @param content
	 *            including payload
	 */
	public void sendMessage(byte[] content) throws ECodexConnectorGatewayWebserviceClientException;

	/**
	 * WebService method to receive all message ID's of messages on the gateway,
	 * which are not yet downloaded by the connector.
	 * 
	 * @return All MessageIds of pending messages on the gateway.
	 */
	public String[] listPendingMessages() throws ECodexConnectorGatewayWebserviceClientException;

	/**
	 * WebService method to recieve message from gateway.
	 * 
	 * @param messageId
	 *            of the message to receive.
	 * @return The message from the gateway.
	 */
	public byte[] downloadMessage(String messageId) throws ECodexConnectorGatewayWebserviceClientException;
}

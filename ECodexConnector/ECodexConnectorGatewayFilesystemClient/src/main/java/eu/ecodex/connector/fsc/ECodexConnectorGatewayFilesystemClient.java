package eu.ecodex.connector.fsc;


/**
 * Interface which contains methods for a filesystem client. 
 * 
 * @author muell16
 *
 */
public interface ECodexConnectorGatewayFilesystemClient 
{
	/**
	 * Method to list all message ID's of messages in the messagefolder,
	 * 
	 * @return All MessageIds of pending messages in the messagefolder.
	 */
	public String[] listPendingMessages();

	/**
	 * Method to recieve message from gateway.
	 * 
	 * @param messageId
	 *            of the message to receive.
	 * @return The message from the gateway.
	 */
	public byte[] downloadMessage(String messageId);
}

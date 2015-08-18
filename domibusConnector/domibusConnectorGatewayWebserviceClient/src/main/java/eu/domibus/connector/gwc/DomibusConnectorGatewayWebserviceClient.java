package eu.domibus.connector.gwc;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.gwc.exception.DomibusConnectorGatewayWebserviceClientException;

/**
 * WebService Client Interface to communicate with the gateway.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorGatewayWebserviceClient {

    /**
     * WebService method to send XML content to gateway. Payload of eCodex
     * Message will be passed as a URI Reference.
     * 
     * @param The
     *            message containing the URI where the message can be found.
     */
    public void sendMessageWithReference(Message message) throws DomibusConnectorGatewayWebserviceClientException;

    /**
     * WebService method to send XML content to gateway. Should contain eCodex
     * Message as well.
     * 
     * @param content
     *            including payload
     */
    public void sendMessage(Message message) throws DomibusConnectorGatewayWebserviceClientException;

    /**
     * WebService method to receive all message ID's of messages on the gateway,
     * which are not yet downloaded by the connector.
     * 
     * @return All MessageIds of pending messages on the gateway.
     */
    public String[] listPendingMessages() throws DomibusConnectorGatewayWebserviceClientException;

    /**
     * WebService method to recieve message from gateway.
     * 
     * @param messageId
     *            of the message to receive.
     * @return The {@link Message} from the gateway.
     */
    public Message downloadMessage(String messageId) throws DomibusConnectorGatewayWebserviceClientException;

}

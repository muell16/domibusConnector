package eu.domibus.connector.common.gwc;

import eu.domibus.connector.domain.Message;
import java.util.Collection;


/**
 * WebService Client Interface to communicate with the gateway.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorGatewayWebserviceClient {

    /**
     * WebService method to send XML content to gateway. Should contain eCodex
     * Message as well.
     * 
     * @param content
     *            including payload
     */
    public void sendMessage(Message message) throws DomibusConnectorGatewayWebserviceClientException;

//    /**
//     * WebService method to receive all message ID's of messages on the gateway,
//     * which are not yet downloaded by the connector.
//     * 
//     * @return All MessageIds of pending messages on the gateway.
//     */
//    public String[] listPendingMessages() throws DomibusConnectorGatewayWebserviceClientException;
//
//    /**
//     * WebService method to recieve message from gateway.
//     * 
//     * @param messageId
//     *            of the message to receive.
//     * @return The {@link Message} from the gateway.
//     */
//    public Message downloadMessage(String messageId) throws DomibusConnectorGatewayWebserviceClientException;
    
    /**
     * WebService method to recieve all pending messages from gateway.
     * 
     * @return The Collection of all {@link Message} objects from the gateway. May be empty or null.
     */
    public Collection<Message> requestPendingMessages() throws DomibusConnectorGatewayWebserviceClientException;

    /**
     * WebService method to receive the errors of a certain message on gateway.
     * 
     * @param message
     *            A {@link Message} object that must contain the ebms message id
     *            in the {@link MessageDetails} of the message which errors are
     *            requested.
     * @throws DomibusConnectorGatewayWebserviceClientException
     */
    void getMessageErrorsFromGateway(Message message) throws DomibusConnectorGatewayWebserviceClientException;

    /**
     * WebService method to receive the status of a certain message on gateway.
     * 
     * @param message
     *            A {@link Message} object that must contain the ebms message id
     *            in the {@link MessageDetails} of the message which status is
     *            requested.
     * @return A simple String represantation of the message status provided by
     *         the gateway.
     * @throws DomibusConnectorGatewayWebserviceClientException
     */
    String getMessageStatusOnGateway(Message message) throws DomibusConnectorGatewayWebserviceClientException;

}

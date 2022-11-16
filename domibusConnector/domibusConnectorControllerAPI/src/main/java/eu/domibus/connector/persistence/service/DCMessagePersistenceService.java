
package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DCMessagePersistenceService {

    boolean checkMessageConfirmed(DC5Message message);

    default boolean checkMessageConfirmedOrRejected(DC5Message message) {
        return checkMessageConfirmedOrRejected(message.getConnectorMessageId());
    }

    boolean checkMessageConfirmedOrRejected(DomibusConnectorMessageId message);

    boolean checkMessageRejected(DC5Message message);

    /**
     * marks the message as rejected
     * @throws IllegalArgumentException is thrown, if the message is null,
     *  or the message does not contain a connector id
     * @throws RuntimeException - if the message is not successfully marked as
     * rejected
     * @param message - the message
     */
    void rejectMessage(DC5Message message);

    /**
     * marks the message as confirmed
     * @throws IllegalArgumentException  is thrown, if the message is null,
     *  or the message does not contain a db id
     * @throws RuntimeException - if the message is not successfully marked as
     * confirmed
     * @param message - the message to confirm
     */
    void confirmMessage(DC5Message message);

    /**
     * all messages which are going to the national system
     * @return the list of unconfirmed messages
     */
    List<DC5Message> findIncomingUnconfirmedMessages();

    DC5Message findMessageByConnectorMessageId(String connectorMessageId);

    /**
     *
     *
     * @param ebmsMessageId - the ebmsId of the message
     * @param messageDirection - the direction of the message
     * @return the found message or an empty Optional if no message found with this ebmsId and direction
     */
    Optional<DC5Message> findMessageByEbmsIdAndDirection(String ebmsMessageId, DomibusConnectorMessageDirection messageDirection);

    /**
     * finds the message by the national id and direction
     * the nationalId is not set if the message was received from the gw
     * @param nationalMessageId - the nationalMessageId
     * @param messageDirection - the direction of the message
     * @return the found message or an empty Optional if no message found with this nationalMessageId and direction
     */
    Optional<DC5Message> findMessageByNationalIdAndDirection(String nationalMessageId, DomibusConnectorMessageDirection messageDirection);


    /**
     *
     *
     * @param ebmsMessageId - the ebmsId of the message
     * @param messageDirection - the direction of the message
     * @return the found message or an empty Optional if no message found with this ebmsId and direction
     */
    Optional<DC5Message> findMessageByEbmsIdOrBackendIdAndDirection(String ebmsMessageId, DomibusConnectorMessageDirection messageDirection);

    /**
     * returns all messages related to the
     * conversation id
     * @param conversationId - the conversation id
     * @return - a list of messages, if there are no messages found
     *  the list will be empty
     */
    List<DC5Message> findMessagesByConversationId(String conversationId);

    /**
     *
     * @return a list of Messages or an emtpy List if nothing found
     */
    List<DC5Message> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutDelivery();

    /**
     *
     * @return a list of Messages or an emtpy List if nothing found
     */
    List<DC5Message> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();

    /**
     * all messages which are going to the GW
     * @return the list of unconfirmed messages
     */
    List<DC5Message> findOutgoingUnconfirmedMessages();

    /**
     * Only updates
     * <ul>
     *   <li>action</li>
     *   <li>service</li>
     *   <li>fromParty</li>
     *   <li>toParty</li>
     *   <li>finalRecipient</li>
     *   <li>originalRecipient</li>
     * </ul>
     *  of the provided message details
     *
     * also stores/updates message content
     * <ul>
     *  <li>all attachments</li>
     *  <li>message content xml content</li>
     *  <li>message message content document with signature</li>
     * </ul>
     *
     *
     * @param message - the message
     * @return the message with eventually updated fields
     * @throws PersistenceException in case of an error
     */
    @Deprecated
    DC5Message mergeMessageWithDatabase(@Nonnull DC5Message message) throws PersistenceException;

    /**
     * stores a new message into storage
     *
     * @deprecated  the method persistBusinessMessageIntoDatabase should be used instead
     * @param message - the message
     * @param direction - direction of the message
     * @return the message with eventually updated fields
     * @throws PersistenceException - in case of failures with persistence
     *
     */
    @Deprecated
    DC5Message persistMessageIntoDatabase(@Nonnull DC5Message message, DomibusConnectorMessageDirection direction) throws PersistenceException;

    /**
     * Marks the message as delivered to the gateway
     * @param message - the message, which should be marked
     */
    void setDeliveredToGateway(DC5Message message);

    /**
     * Marks the message as delivered to national backend
     * @param message - the message, which should be marked
     */
    void setMessageDeliveredToNationalSystem(DC5Message message);

    void updateMessageDetails(DC5Message message);

    /**
     * stores a business messsage into database
     *  -) the message details
     *  -) the message content
     *  -) the message attachments
     *
     * @param message - the connector message
     */
    void persistBusinessMessageIntoDatabase(DC5Message message);
}

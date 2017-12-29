package eu.domibus.connector.persistence.service;

import javax.annotation.Nonnull;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageError;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;



@Transactional
public interface DomibusConnectorPersistenceService {

    /**
     * stores a new message into storage
     * @param message - the message
     * @param direction - direction of the message
     * @throws PersistenceException - in case of failures with persistence
     * 
     */
    void persistMessageIntoDatabase(@Nonnull Message message, DomibusConnectorMessageDirection direction) throws PersistenceException;

    void mergeMessageWithDatabase(@Nonnull Message message) throws PersistenceException;

    /**
     * Creates the evidence in storage
     * 
     * @param message - the message related to the evidence
     * @param evidence - the evidence as byte[]
     * @param evidenceType - the type of the evidence
     */
    void persistEvidenceForMessageIntoDatabase(@Nonnull Message message, @Nonnull byte[] evidence, @Nonnull DomibusConnectorEvidenceType evidenceType);

    Message findMessageByNationalId(String nationalMessageId);

    Message findMessageByEbmsId(String ebmsMessageId);

    /**
     * Merges the message into the storage @see #mergeMessageWithDatabase
     * and updates the delivered to gateway 
     * field with the current time of the corresponding
     * evidence (the corresponding evidence is identified by the evidence type)
     * 
     * @param message - the message
     * @param evidenceType - the evidenceType
     * @throws eu.domibus.connector.persistence.service.PersistenceException - if the message could not be updated
     * in storage
     * 
     * 
     */
    void setEvidenceDeliveredToGateway(@Nonnull Message message, @Nonnull DomibusConnectorEvidenceType evidenceType) throws PersistenceException;

    /**
     * Merges the message into the storage @see #mergeMessageWithDatabase
     * and updates the delivered to national_backend 
     * field with the current time of the corresponding
     * evidence (the corresponding evidence is identified by the evidence type)
     * 
     * @param message - the message
     * @param evidenceType - the evidenceType
     * @throws eu.domibus.connector.persistence.service.PersistenceException - if the message
     * could not be updated in storage
     * 
     */
    void setEvidenceDeliveredToNationalSystem(Message message, DomibusConnectorEvidenceType evidenceType) throws PersistenceException;

    /**
     * Marks the message as delivered to the gateway
     * @param message - the message, which should be marked
     */
    void setMessageDeliveredToGateway(Message message);

    /**
     * Marks the message as delivered to national backend
     * @param message - the message, which should be marked
     */
    void setMessageDeliveredToNationalSystem(Message message);

    List<Message> findOutgoingUnconfirmedMessages();

    List<Message> findIncomingUnconfirmedMessages();

    //TODO: improve Exceptions
    /**
     * marks the message as confirmed
     * @throws IllegalArgumentException  is thrown, if the message is null,
     *  or the message does not contain a db id
     * @throws RuntimeException - if the message is not sucessfully marked as
     * confirmed
     * @param message 
     */
    void confirmMessage(Message message);

    //TODO: improve Exceptions
    /**
     * marks the message as rejected
     * @throws IllegalArgumentException is thrown, if the message is null,
     *  or the message does not contain a db id
     * @trows RuntimeException - if the message is not successfully marked as
     * rejected
     * @param message - the message
     */
    void rejectMessage(Message message);

    Action getAction(String action);

    Action getRelayREMMDAcceptanceRejectionAction();

    Action getRelayREMMDFailure();

    Action getDeliveryNonDeliveryToRecipientAction();

    Action getRetrievalNonRetrievalToRecipientAction();

    Service getService(String service);

    Party getParty(String partyId, String role);

    Party getPartyByPartyId(String partyId);

    /**
     * returns all messages related to the
     * conversation id
     * @param conversationId - the conversation id
     * @return - a list of messages, if there are no messages found
     *  the list will be empty
     */
    List<Message> findMessagesByConversationId(String conversationId);

    void persistMessageError(MessageError messageError);

    @Nonnull List<MessageError> getMessageErrors(@Nonnull Message message) throws Exception;

    void persistMessageErrorFromException(Message message, Throwable ex, Class<?> source); 

    /**
     * 
     * @return a list of Messages or an emtpy List if nothing found
     */
	List<Message> findOutgoingMessagesNotRejectedAndWithoutDelivery();

    /**
     * 
     * @return a list of Messages or an emtpy List if nothing found
     */
	List<Message> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
}

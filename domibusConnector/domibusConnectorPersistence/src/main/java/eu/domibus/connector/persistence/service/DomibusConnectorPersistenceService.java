package eu.domibus.connector.persistence.service;

import javax.annotation.Nonnull;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.model.DomibusConnectorParty;

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
    void persistMessageIntoDatabase(@Nonnull DomibusConnectorMessage message, DomibusConnectorMessageDirection direction) throws PersistenceException;

    /**
     * merges changes of a message into database
     * 
     * @param message - the message
     * @throws PersistenceException  - in case of persistence errors
     */
    void mergeMessageWithDatabase(@Nonnull DomibusConnectorMessage message) throws PersistenceException;

    /**
     * Creates the evidence in storage
     * 
     * @param message - the message related to the evidence
     * @param evidence - the evidence as byte[]
     * @param evidenceType - the type of the evidence
     */
    void persistEvidenceForMessageIntoDatabase(@Nonnull DomibusConnectorMessage message, @Nonnull byte[] evidence, @Nonnull DomibusConnectorEvidenceType evidenceType);

    DomibusConnectorMessage findMessageByNationalId(String nationalMessageId);

    DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId);

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
    void setEvidenceDeliveredToGateway(@Nonnull DomibusConnectorMessage message, @Nonnull DomibusConnectorEvidenceType evidenceType) throws PersistenceException;

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
    void setEvidenceDeliveredToNationalSystem(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) throws PersistenceException;

    /**
     * Marks the message as delivered to the gateway
     * @param message - the message, which should be marked
     */
    void setMessageDeliveredToGateway(DomibusConnectorMessage message);

    /**
     * Marks the message as delivered to national backend
     * @param message - the message, which should be marked
     */
    void setMessageDeliveredToNationalSystem(DomibusConnectorMessage message);

    List<DomibusConnectorMessage> findOutgoingUnconfirmedMessages();

    List<DomibusConnectorMessage> findIncomingUnconfirmedMessages();
    
    /**
     * returns all messages related to the
     * conversation id
     * @param conversationId - the conversation id
     * @return - a list of messages, if there are no messages found
     *  the list will be empty
     */
    List<DomibusConnectorMessage> findMessagesByConversationId(String conversationId);

    //TODO: improve Exceptions
    /**
     * marks the message as confirmed
     * @throws IllegalArgumentException  is thrown, if the message is null,
     *  or the message does not contain a db id
     * @throws RuntimeException - if the message is not sucessfully marked as
     * confirmed
     * @param message 
     */
    void confirmMessage(DomibusConnectorMessage message);

    //TODO: improve Exceptions
    /**
     * marks the message as rejected
     * @throws IllegalArgumentException is thrown, if the message is null,
     *  or the message does not contain a db id
     * @trows RuntimeException - if the message is not successfully marked as
     * rejected
     * @param message - the message
     */
    void rejectMessage(DomibusConnectorMessage message);

    DomibusConnectorAction getAction(String action);

    DomibusConnectorAction getRelayREMMDAcceptanceRejectionAction();

    DomibusConnectorAction getRelayREMMDFailure();

    DomibusConnectorAction getDeliveryNonDeliveryToRecipientAction();

    DomibusConnectorAction getRetrievalNonRetrievalToRecipientAction();

    DomibusConnectorService getService(String service);

    DomibusConnectorParty getParty(String partyId, String role);

    DomibusConnectorParty getPartyByPartyId(String partyId);



    void persistMessageError(DomibusConnectorMessageError messageError);

    @Nonnull List<DomibusConnectorMessageError> getMessageErrors(@Nonnull DomibusConnectorMessage message) throws Exception;

    void persistMessageErrorFromException(DomibusConnectorMessage message, Throwable ex, Class<?> source); 

    /**
     * 
     * @return a list of Messages or an emtpy List if nothing found
     */
	List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery();

    /**
     * 
     * @return a list of Messages or an emtpy List if nothing found
     */
	List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
}

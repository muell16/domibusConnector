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

	
	  DomibusConnectorMessage findMessageByConnectorMessageId(String connectorMessageId);
	  
	  boolean checkMessageConfirmedOrRejected(DomibusConnectorMessage message);
	  
	  boolean checkMessageRejected(DomibusConnectorMessage message);
	
    /**
     * stores a new message into storage
     * @param message - the message
     * @param direction - direction of the message
     * @throws PersistenceException - in case of failures with persistence
     * 
     */
    void persistMessageIntoDatabase(@Nonnull DomibusConnectorMessage message, DomibusConnectorMessageDirection direction) throws PersistenceException;

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
     * @throws PersistenceException 
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

    
  
    
    
    /**
     * finds the message by the national id
     * the nationalId is not set if the message was received from the gw
     * @param nationalMessageId
     * @return 
     */
    DomibusConnectorMessage findMessageByNationalId(String nationalMessageId);

    /**
     * TODO!
     * @param ebmsMessageId
     * @return 
     */
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

    /**
     * all messages which are going to the GW
     * @return the list of unconfirmed messages
     */
    List<DomibusConnectorMessage> findOutgoingUnconfirmedMessages();

    
    /**
     * all messages which are going to the national system
     * @return the list of unconfirmed messages
     */
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


    /**
     * persists an error to the message
     * @param messageError 
     */
    void persistMessageError(DomibusConnectorMessageError messageError);

    /**
     * finds all errors related to the message
     * @param message the message 
     * @return the error list
     * @throws PersistenceException if there are errors accessing or reading from persistence layer
     */
    @Nonnull List<DomibusConnectorMessageError> getMessageErrors(@Nonnull DomibusConnectorMessage message) throws PersistenceException;

    /**
     * creates a MessageError from an exception and persists this message error
     * @param message - the message the exception is related to
     * @param ex - the exception
     * @param source - class/service where the exception occured
     */
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

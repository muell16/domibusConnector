package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageError;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.MessageDirection;

import java.util.List;



public interface DomibusConnectorPersistenceService {

    void persistMessageIntoDatabase(Message message, MessageDirection direction);

    void mergeMessageWithDatabase(Message message);

    void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, EvidenceType evidenceType);

    Message findMessageByNationalId(String nationalMessageId);

    Message findMessageByEbmsId(String ebmsMessageId);

    void setEvidenceDeliveredToGateway(Message message, EvidenceType evidenceType);

    void setEvidenceDeliveredToNationalSystem(Message message, EvidenceType evidenceType);

    void setMessageDeliveredToGateway(Message message);

    void setMessageDeliveredToNationalSystem(Message message);

    List<Message> findOutgoingUnconfirmedMessages();

    List<Message> findIncomingUnconfirmedMessages();

    void confirmMessage(Message message);

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

    List<MessageError> getMessageErrors(Message message) throws Exception;

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

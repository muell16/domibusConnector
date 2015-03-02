package eu.ecodex.connector.common.db.service;

import java.util.List;

import eu.ecodex.connector.common.db.model.ECodexAction;
import eu.ecodex.connector.common.db.model.ECodexParty;
import eu.ecodex.connector.common.db.model.ECodexService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.exception.PersistenceException;
import eu.ecodex.connector.common.message.Message;

public interface ECodexConnectorPersistenceService {

    void persistMessageIntoDatabase(Message message, ECodexMessageDirection direction) throws PersistenceException;

    void mergeMessageWithDatabase(Message message);

    void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, ECodexEvidenceType evidenceType);

    Message findMessageByNationalId(String nationalMessageId);

    Message findMessageByEbmsId(String ebmsMessageId);

    void setEvidenceDeliveredToGateway(Message message, ECodexEvidenceType evidenceType);

    void setEvidenceDeliveredToNationalSystem(Message message, ECodexEvidenceType evidenceType);

    void setMessageDeliveredToGateway(Message message);

    void setMessageDeliveredToNationalSystem(Message message);

    List<Message> findOutgoingUnconfirmedMessages();

    List<Message> findIncomingUnconfirmedMessages();

    void confirmMessage(Message message);

    void rejectMessage(Message message);

    ECodexAction getAction(String action);

    ECodexAction getRelayREMMDAcceptanceRejectionAction();
    
    ECodexAction getRelayREMMDFailure();

    ECodexAction getDeliveryNonDeliveryToRecipientAction();

    ECodexAction getRetrievalNonRetrievalToRecipientAction();

    ECodexService getService(String service);

    ECodexParty getParty(String partyId, String role);

    ECodexParty getPartyByPartyId(String partyId);
}

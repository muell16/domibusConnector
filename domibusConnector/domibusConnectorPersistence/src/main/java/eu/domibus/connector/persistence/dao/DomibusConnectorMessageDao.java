package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
@Transactional
public interface DomibusConnectorMessageDao extends CrudRepository<PDomibusConnectorMessage, Long> {

    public PDomibusConnectorMessage findOneByBackendMessageId(String backendId);
    
    public PDomibusConnectorMessage findOneByEbmsMessageId(String ebmsMessageId);
    
    public PDomibusConnectorMessage findOneByConnectorMessageId(String messageConnectorId);
    
    public List<PDomibusConnectorMessage> findByConversationId(String conversationId);
    
    @Query("select m from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = 'NAT_TO_GW' and m.deliveredToGateway is not null ")
    public List<PDomibusConnectorMessage> findOutgoingUnconfirmedMessages();
        
    @Query("select m from DomibusConnectorMessage m where m.rejected is null and m.direction = 'NAT_TO_GW' and m.deliveredToGateway is not null "
        + "and not exists (select 1 from DomibusConnectorEvidence e where e.message = m and (e.type='DELIVERY' or e.type='NON_DELIVERY'))")
    public List<PDomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery();
    
    @Query("select m from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = 'NAT_TO_GW' and m.deliveredToGateway is not null "
        + "and not exists (select 1 from DomibusConnectorEvidence e where e.message = m and (e.type='RELAY_REMMD_ACCEPTANCE' or e.type='RELAY_REMMD_REJECTION'))")
    public List<PDomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        
    @Query("select m from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = 'GW_TO_NAT' and m.deliveredToGateway is not null")
    public List<PDomibusConnectorMessage> findIncomingUnconfirmedMessages();
        
    @Modifying
    @Query("update DomibusConnectorMessage m set confirmed=CURRENT_TIMESTAMP, rejected=NULL where m.id = ?1")
    public int confirmMessage(Long messageId);
    
    @Modifying
    @Query("update DomibusConnectorMessage m set rejected=CURRENT_TIMESTAMP, confirmed=NULL where m.id = ?1")
    public int rejectMessage(Long messageId);

    @Modifying
    @Query("update DomibusConnectorMessage m set m.deliveredToGateway=CURRENT_TIMESTAMP where m.id = ?")
    public int setMessageDeliveredToGateway(Long dbMessageId);
    
    @Modifying
    @Query("update DomibusConnectorMessage m set m.deliveredToNationalSystem=CURRENT_TIMESTAMP where m.id = ?")
    public int setMessageDeliveredToBackend(Long dbMessageId);
    
    
}

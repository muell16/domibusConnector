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
    
    @Query("SELECT m FROM PDomibusConnectorMessage m WHERE m.confirmed is null AND m.rejected is null AND m.direction = 'NAT_TO_GW' AND m.deliveredToGateway is not null ")
    public List<PDomibusConnectorMessage> findOutgoingUnconfirmedMessages();
        
    @Query("SELECT m FROM PDomibusConnectorMessage m WHERE m.rejected is null AND m.direction = 'NAT_TO_GW' AND m.deliveredToGateway is not null "
        + "AND not exists (SELECT 1 FROM PDomibusConnectorEvidence e WHERE e.message = m AND (e.type='DELIVERY' or e.type='NON_DELIVERY'))")
    public List<PDomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery();
    
    @Query("SELECT m FROM PDomibusConnectorMessage m WHERE m.confirmed is null AND m.rejected is null AND m.direction = 'NAT_TO_GW' AND m.deliveredToGateway is not null "
        + "AND not exists (SELECT 1 FROM PDomibusConnectorEvidence e WHERE e.message = m AND (e.type='RELAY_REMMD_ACCEPTANCE' or e.type='RELAY_REMMD_REJECTION'))")
    public List<PDomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        
    @Query("SELECT m FROM PDomibusConnectorMessage m WHERE m.confirmed is null AND m.rejected is null AND m.direction = 'GW_TO_NAT' AND m.deliveredToGateway is not null")
    public List<PDomibusConnectorMessage> findIncomingUnconfirmedMessages();
        
    // if DB fields confirmed OR rejected are NOT NULL -> then true
    @Query("SELECT case when (count(m) > 0) then true else false end "
            + "FROM PDomibusConnectorMessage m "
            + "WHERE m.id = ?1 AND (m.confirmed is not null OR m.rejected is not null)")
    public boolean checkMessageConfirmedOrRejected(Long messageId);    

    // if DB field rejected is NOT NULL -> then true
    @Query("SELECT case when (count(m) > 0) then true else false end FROM PDomibusConnectorMessage m WHERE m.id = ?1 AND m.rejected is not null")
    public boolean checkMessageRejected(Long messageId);     
    
    // if DB field confirmend is NOT NULL -> then true
    @Query("SELECT case when (count(m) > 0)  then true else false end FROM PDomibusConnectorMessage m WHERE m.id = ?1 AND m.confirmed is not null")
    public boolean checkMessageConfirmed(Long messageId);
    
    @Modifying
    @Query("update PDomibusConnectorMessage m set confirmed=CURRENT_TIMESTAMP, rejected=NULL WHERE m.id = ?1")
    public int confirmMessage(Long messageId);
    
    @Modifying
    @Query("update PDomibusConnectorMessage m set rejected=CURRENT_TIMESTAMP, confirmed=NULL WHERE m.id = ?1")
    public int rejectMessage(Long messageId);

    @Modifying
    @Query("update PDomibusConnectorMessage m set m.deliveredToGateway=CURRENT_TIMESTAMP WHERE m.id = ?")
    public int setMessageDeliveredToGateway(Long dbMessageId);
    
    @Modifying
    @Query("update PDomibusConnectorMessage m set m.deliveredToNationalSystem=CURRENT_TIMESTAMP WHERE m.id = ?")
    public int setMessageDeliveredToBackend(Long dbMessageId);
    
    
}

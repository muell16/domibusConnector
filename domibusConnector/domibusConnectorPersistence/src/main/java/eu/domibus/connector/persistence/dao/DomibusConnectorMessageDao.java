/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
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
public interface DomibusConnectorMessageDao extends CrudRepository<DomibusConnectorMessage, Long> {

    public DomibusConnectorMessage findOneByNationalMessageId(String nationalId);
    
    public DomibusConnectorMessage findOneByEbmsMessageId(String ebmsMessageId);
    
    public List<DomibusConnectorMessage> findByConversationId(String conversationId);
    
    @Query("select m from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = 'NAT_TO_GW' and m.deliveredToGateway is not null ")
    public List<DomibusConnectorMessage> findOutgoingUnconfirmedMessages();
        
    @Query("select m from DomibusConnectorMessage m where m.rejected is null and m.direction = 'NAT_TO_GW' and m.deliveredToGateway is not null "
        + "and not exists (select 1 from DomibusConnectorEvidence e where e.message = m and (e.type='DELIVERY' or e.type='NON_DELIVERY'))")
    public List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery();
    
    @Query("select m from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = 'NAT_TO_GW' and m.deliveredToGateway is not null "
        + "and not exists (select 1 from DomibusConnectorEvidence e where e.message = m and (e.type='RELAY_REMMD_ACCEPTANCE' or e.type='RELAY_REMMD_REJECTION'))")
    public List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        
    @Query("select m from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = 'GW_TO_NAT' and m.deliveredToGateway is not null")
    public List<DomibusConnectorMessage> findIncomingUnconfirmedMessages();
        
    @Modifying
    @Query("update DomibusConnectorMessage m set confirmed=CURRENT_TIMESTAMP, rejected=NULL where m.id = ?1")
    public int confirmMessage(Long messageId);
    
    @Modifying
    @Query("update DomibusConnectorMessage m set rejected=CURRENT_TIMESTAMP, confirmed=NULL where m.id = ?1")
    public int rejectMessage(Long messageId);

    @Modifying
    @Query("update DomibusConnectorMessage m set m.deliveredToGateway=CURRENT_TIMESTAMP")
    public void setMessageDeliveredToGateway(Long dbMessageId);
    
    @Modifying
    @Query("update DomibusConnectorMessage m set m.deliveredToBackend=CURRENT_TIMESTAMP")
    public void setMessageDeliveredToBackend(Long dbMessageId);
    
    
}

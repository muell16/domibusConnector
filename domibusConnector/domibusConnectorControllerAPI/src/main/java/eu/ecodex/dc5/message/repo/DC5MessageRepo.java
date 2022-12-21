package eu.ecodex.dc5.message.repo;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.BackendMessageId;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.ecodex.dc5.message.model.EbmsMessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Message, Long> {


    @Query("SELECT m FROM DC5Message m WHERE (m.ebmsData.ebmsMessageId = ?1 OR m.backendData.backendMessageId = ?2) AND m.target = ?3 ")
    public Optional<DC5Message> findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(EbmsMessageId id, BackendMessageId backendMessageId, MessageTargetSource directionTarget);

    @Query("SELECT m FROM DC5Message m WHERE m.ebmsData.ebmsMessageId = ?1 AND m.target = ?2 ")
    public Optional<DC5Message> findOneByEbmsMessageIdAndDirectionTarget(EbmsMessageId id, MessageTargetSource directionTarget);

    @Query("SELECT m FROM DC5Message m WHERE m.backendData.backendMessageId = ?1 AND m.target = ?2 ")
    public Optional<DC5Message> findOneByBackendMessageIdAndDirectionTarget(BackendMessageId id, MessageTargetSource directionTarget);

    @Query("SELECT m FROM DC5Message m WHERE m.connectorMessageId = ?1")
    DC5Message getByConnectorMessageId(DomibusConnectorMessageId take);

    @Query("SELECT m FROM DC5Message m WHERE m.connectorMessageId = ?1")
    Optional<DC5Message> findOneByConnectorMessageId(DomibusConnectorMessageId msgId);

    List<DC5Message> findAllByOrderByCreated();

    List<DC5Message> findAllByBackendLinkName(DomibusConnectorLinkPartner.LinkPartnerName backendName);
}

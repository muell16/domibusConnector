package eu.ecodex.dc5.message.repo;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.BackendMessageId;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.ecodex.dc5.message.model.EbmsMessageId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Message, Long>, QueryByExampleExecutor<DC5Message> {

    @Override
    @DC5MessageEntityGraphFetchAll
    List<DC5Message> findAll();

    @Query("SELECT m FROM DC5Message m WHERE m.ebmsData.ebmsMessageId = ?1 AND m.target = ?2 ")
    @DC5MessageEntityGraphFetchAll
    Optional<DC5Message> findOneByEbmsMessageIdAndDirectionTarget(EbmsMessageId id, MessageTargetSource directionTarget);

    @Query("SELECT m FROM DC5Message m WHERE m.backendData.backendMessageId = ?1 AND m.target = ?2 ")
    @DC5MessageEntityGraphFetchAll
    Optional<DC5Message> findOneByBackendMessageIdAndDirectionTarget(BackendMessageId id, MessageTargetSource directionTarget);

    @Query("SELECT m FROM DC5Message m WHERE m.connectorMessageId = ?1")
    @DC5MessageEntityGraphFetchAll
    DC5Message findByConnectorMessageId(DC5MessageId take);

    @Query("SELECT m FROM DC5Message m WHERE m.connectorMessageId = ?1")
    @DC5MessageEntityGraphFetchAll
    Optional<DC5Message> findOneByConnectorMessageId(DC5MessageId msgId);


    @DC5MessageEntityGraphFetchAll
    List<DC5Message> findAllByEbmsData_ConversationId(String conversationId);

    @DC5MessageEntityGraphFetchAll
    DC5Message getByConnectorMessageId(DC5MessageId msgId);

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service",
            "ebmsData.initiator",
            "ebmsData.initiator.partnerRole",
            "ebmsData.initiator.partnerAddress",
            "ebmsData.initiator.partnerAddress.party",
            "ebmsData.responder.partnerRole",
            "ebmsData.responder.partnerAddress",
            "ebmsData.responder.partnerAddress.party",
            "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
    @Documented
    public @interface DC5MessageEntityGraphFetchAll {}
}

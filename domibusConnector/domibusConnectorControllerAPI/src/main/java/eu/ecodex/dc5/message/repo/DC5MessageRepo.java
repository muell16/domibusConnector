package eu.ecodex.dc5.message.repo;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.model.BackendMessageId;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.ecodex.dc5.message.model.EbmsMessageId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface DC5MessageRepo
        extends
        PagingAndSortingRepository<DC5Message, Long>
{

    @Override
    @DC5MessageEntityGraphFetchAll
    List<DC5Message> findAll();

    @Override
    @DC5MessageEntityGraphFetchAll
    Page<DC5Message> findAll(Pageable pageable);

    @Query("SELECT m FROM DC5Message m " +
           " LEFT JOIN m.backendData bd " +
           " JOIN m.ebmsData ed " +
           " WHERE (?1 IS NULL or m.connectorMessageId LIKE CONCAT('%',?1,'%')) "
         + " AND (?2 IS NULL or m.ebmsData.ebmsMessageId LIKE CONCAT('%',?2,'%')) "
         + " AND (?3 IS NULL or m.backendData.backendMessageId LIKE CONCAT('%',?3,'%')) "
         + " AND (?4 IS NULL or m.ebmsData.conversationId LIKE CONCAT('%',?4,'%')) "
    )
    @DC5MessageEntityGraphFetchAll
    Page<DC5Message> findByWebFilter(@Nullable String connectorMsgId,
                                     @Nullable String ebmsId,
                                     @Nullable String backendMsgId,
                                     @Nullable String conversationId,
                                     Pageable pageable);

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
//            "content-entity-graph"},
            "messageContent.currentState", "messageContent.messageStates"},
            type = EntityGraph.EntityGraphType.LOAD)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
    @Documented
    public @interface DC5MessageEntityGraphFetchAll {}
}

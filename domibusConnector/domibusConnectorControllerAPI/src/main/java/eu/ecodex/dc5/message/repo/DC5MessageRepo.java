package eu.ecodex.dc5.message.repo;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.BackendMessageId;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.ecodex.dc5.message.model.EbmsMessageId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Message, Long> {

    @Override
    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    List<DC5Message> findAll();

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM DC5Message m WHERE (m.ebmsData.ebmsMessageId = ?1 OR m.backendData.backendMessageId = ?2) AND m.target = ?3 ")
    Optional<DC5Message> findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(EbmsMessageId id, BackendMessageId backendMessageId, MessageTargetSource directionTarget);

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM DC5Message m WHERE m.ebmsData.ebmsMessageId = ?1 AND m.target = ?2 ")
    Optional<DC5Message> findOneByEbmsMessageIdAndDirectionTarget(EbmsMessageId id, MessageTargetSource directionTarget);

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM DC5Message m WHERE m.backendData.backendMessageId = ?1 AND m.target = ?2 ")
    Optional<DC5Message> findOneByBackendMessageIdAndDirectionTarget(BackendMessageId id, MessageTargetSource directionTarget);

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM DC5Message m WHERE m.connectorMessageId = ?1")
    DC5Message findByConnectorMessageId(DomibusConnectorMessageId take);

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM DC5Message m WHERE m.connectorMessageId = ?1")
    Optional<DC5Message> findOneByConnectorMessageId(DomibusConnectorMessageId msgId);

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    List<DC5Message> findAllByOrderByCreated();

    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    //@EntityGraph(value = "msg-view-data-graph", type = EntityGraph.EntityGraphType.LOAD)
    List<DC5Message> findAllByBackendLinkName(DomibusConnectorLinkPartner.LinkPartnerName backendName);
    @EntityGraph(attributePaths = {"ebmsData.action", "ebmsData.service", "ebmsData.backendAddress", "ebmsData.gatewayAddress.party", "ebmsData.initiatorRole", "ebmsData.responderRole", "messageContent.currentState", "messageContent.messageStates"}, type = EntityGraph.EntityGraphType.LOAD)
    List<DC5Message> findAllByEbmsData_ConversationId(String conversationId);
}

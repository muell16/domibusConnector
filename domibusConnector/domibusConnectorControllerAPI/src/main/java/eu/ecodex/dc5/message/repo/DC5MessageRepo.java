package eu.ecodex.dc5.message.repo;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.message.model.DC5Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Message, Long> {


    @Query("SELECT m FROM DC5Message m WHERE (m.ebmsData.ebmsMessageId = ?1 OR m.backendData.backendMessageId = ?1) AND m.target = ?2 ")
    public Optional<DC5Message> findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(String id, MessageTargetSource directionTarget);

    @Query("SELECT m FROM DC5Message m WHERE m.ebmsData.ebmsMessageId = ?1 AND m.target = ?2 ")
    public Optional<DC5Message> findOneByEbmsMessageIdAndDirectionTarget(String id, MessageTargetSource directionTarget);

}

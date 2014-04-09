package eu.ecodex.webadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.ecodex.connector.common.db.model.ECodexMessage;

public interface ECodexMessageRepository extends JpaRepository<ECodexMessage, Long> {

    ECodexMessage findMessageByNationalId(String nationalMessageId);

    ECodexMessage findMessageByEbmsId(String ebmsMessageId);

}

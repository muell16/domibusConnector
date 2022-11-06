package eu.ecodex.dc5.core.repository;

import eu.ecodex.dc5.core.model.DC5MsgProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5MsgProcessRepo extends JpaRepository<DC5MsgProcess, Long> {
}

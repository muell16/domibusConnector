package eu.ecodex.dc5.message.repo;

import eu.ecodex.dc5.message.model.DC5Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Message, Long> {
}

package eu.ecodex.dc5.transport.repo;

import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5TransportRequestRepo extends JpaRepository<DC5TransportRequest, Long> {
}

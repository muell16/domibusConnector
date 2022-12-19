package eu.ecodex.dc5.transport.repo;

import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DC5TransportRequestRepo extends JpaRepository<DC5TransportRequest, Long> {

    @Query("SELECT r FROM DC5TransportRequest r WHERE r.transportRequestId = ?1 ")
    Optional<DC5TransportRequest> findByTransportRequestId(DC5TransportRequest.TransportRequestId transportRequestId);

}

package eu.ecodex.dc5.transport.repo;

import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DC5TransportRequestRepo extends JpaRepository<DC5TransportRequest, Long> {

    @Query("SELECT r FROM DC5TransportRequest r WHERE r.transportRequestId = ?1 ")
    Optional<DC5TransportRequest> findByTransportRequestId(DC5TransportRequest.TransportRequestId transportRequestId);

    @Query("SELECT r FROM DC5TransportRequest r WHERE r.transportRequestId = ?1 ")
    DC5TransportRequest getById(DC5TransportRequest.TransportRequestId transportRequestId);

    @Query("SELECT r FROM DC5TransportRequest r WHERE r.currentState.transportState = ?1 and r.linkName = ?2")
    List<DC5TransportRequest> findByCurrentState(TransportState pending, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);

}

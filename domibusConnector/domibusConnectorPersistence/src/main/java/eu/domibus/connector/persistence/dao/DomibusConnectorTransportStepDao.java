package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomibusConnectorTransportStepDao extends JpaRepository<PDomibusConnectorTransportStep, Long> {

    @Query("SELECT MAX(step.attempt) FROM PDomibusConnectorTransportStep step " +
            "WHERE step.connectorMessageId = ?1 AND step.linkPartnerName = ?2"
            )
    Optional<Integer> getHighestAttemptBy(String messageId, String linkPartnerName);


//    @Query("SELECT PDomibusConnectorTransportStep FROM PDomibusConnectorTransportStep step " +
//            "WHERE step.message.connectorMessageId = ?1 AND step.linkPartnerName = ?2 AND step.attempt = ?3"
//    )
    @Query("SELECT step FROM PDomibusConnectorTransportStep step WHERE step.connectorMessageId = ?1")
    Optional<PDomibusConnectorTransportStep> findbyMsgLinkPartnerAndAttempt(String msgId, String partnerName, int attempt);

    @Query("SELECT step FROM PDomibusConnectorTransportStep step WHERE step.transportId = ?1")
    Optional<PDomibusConnectorTransportStep> findByTransportId(TransportStateService.TransportId transportId);


    @Query("SELECT step FROM PDomibusConnectorTransportStep step WHERE step.linkPartnerName = ?1")
    List<PDomibusConnectorTransportStep> findByMsgLinkPartnerAndState(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);

}

package eu.ecodex.dc5.link.repository;

import eu.ecodex.dc5.link.model.DC5LinkPartnerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomibusConnectorLinkPartnerDao extends JpaRepository<DC5LinkPartnerJpaEntity, Long> {

    Optional<DC5LinkPartnerJpaEntity> findOneBackendByLinkNameAndEnabledIsTrue(String name);

    Optional<DC5LinkPartnerJpaEntity> findOneByLinkName(String linkName);

    @Query("SELECT max(e.id) from DC5LinkPartnerJpaEntity e")
    Long findHighestId();

    List<DC5LinkPartnerJpaEntity> findAllByEnabledIsTrue();
}

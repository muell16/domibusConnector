package eu.ecodex.dc5.domain.repo;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomibusConnectorBusinessDomainDao extends JpaRepository<DC5BusinessDomainJpaEntity, Long> {

    public Optional<DC5BusinessDomainJpaEntity> findByName(DC5BusinessDomain.BusinessDomainId name);

}

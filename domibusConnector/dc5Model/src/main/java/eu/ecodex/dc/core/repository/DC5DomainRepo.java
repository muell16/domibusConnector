package eu.ecodex.dc.core.repository;

import eu.ecodex.dc.core.model.DC5Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5DomainRepo extends JpaRepository<DC5Domain, Long> {

}

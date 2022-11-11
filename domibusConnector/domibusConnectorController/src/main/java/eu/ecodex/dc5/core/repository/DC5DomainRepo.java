package eu.ecodex.dc5.core.repository;

import eu.ecodex.dc5.core.model.DC5Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DC5DomainRepo extends JpaRepository<DC5Domain, Long> {


}

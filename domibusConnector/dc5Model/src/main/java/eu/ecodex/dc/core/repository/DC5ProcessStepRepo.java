package eu.ecodex.dc.core.repository;

import eu.ecodex.dc.core.model.DC5ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5ProcessStepRepo extends JpaRepository<DC5ProcessStep, Long> {

}

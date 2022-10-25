package eu.ecodex.dc.core.repository;

import eu.ecodex.dc.core.model.DC5Msg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Msg, Long> {

}

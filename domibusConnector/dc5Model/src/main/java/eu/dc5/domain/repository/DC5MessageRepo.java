package eu.dc5.domain.repository;

import eu.dc5.domain.model.DC5Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5MessageRepo extends JpaRepository<DC5Message, Long> {

}

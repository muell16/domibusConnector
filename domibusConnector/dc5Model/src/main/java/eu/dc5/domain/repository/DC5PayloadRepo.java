package eu.dc5.domain.repository;

import eu.dc5.domain.model.DC5Payload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DC5PayloadRepo extends JpaRepository<DC5Payload, Long> {
}
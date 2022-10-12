package eu.dc5.domain.repository;

import eu.dc5.domain.model.DC5Ebms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DC5EbmsRepo extends JpaRepository<DC5Ebms, Long> {
    Optional<DC5Ebms> findByEbmsMessageId(String ebmsMessageId);
}
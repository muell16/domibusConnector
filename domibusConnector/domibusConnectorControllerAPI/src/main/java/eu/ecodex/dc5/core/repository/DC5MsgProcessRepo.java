package eu.ecodex.dc5.core.repository;

import eu.ecodex.dc5.core.model.DC5MsgProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface DC5MsgProcessRepo extends JpaRepository<DC5MsgProcess, Long> {
    Optional<DC5MsgProcess> findByProcessId(String processId);
    @NotNull DC5MsgProcess getDC5MsgProcessByProcessId(String processId);
}

package eu.ecodex.dc5.util.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.ecodex.dc5.util.model.DC5ConfigItem;

@Repository
public interface DC5ConfigItemDao extends CrudRepository<DC5ConfigItem, Long> {

	public Optional<DC5ConfigItem> findByUuid(String uuid);
}

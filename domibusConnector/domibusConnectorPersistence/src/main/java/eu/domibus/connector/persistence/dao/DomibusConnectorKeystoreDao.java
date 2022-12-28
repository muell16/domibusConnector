package eu.domibus.connector.persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.domibus.connector.persistence.model.DC5ConfigItem;

@Repository
public interface DomibusConnectorKeystoreDao extends CrudRepository<DC5ConfigItem, Long> {

	public Optional<DC5ConfigItem> findByUuid(String uuid);
}

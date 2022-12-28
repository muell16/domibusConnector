package eu.domibus.connector.persistence.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.domibus.connector.persistence.model.DC5User;
import eu.domibus.connector.persistence.model.DC5UserPassword;

@Repository
public interface DomibusConnectorUserPasswordDao extends CrudRepository<DC5UserPassword, Long> {

	@Query("SELECT p FROM DC5UserPassword p WHERE p.user=?1 AND p.currentPassword=true")
	DC5UserPassword findCurrentByUser(DC5User user);
}

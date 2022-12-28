package eu.domibus.connector.persistence.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.domibus.connector.persistence.model.DC5User;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
public interface DomibusConnectorUserDao extends CrudRepository<DC5User, Long> {

	public DC5User findOneByUsernameIgnoreCase(String username);
}

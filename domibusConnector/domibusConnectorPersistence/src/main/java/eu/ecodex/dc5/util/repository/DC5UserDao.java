package eu.ecodex.dc5.util.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.ecodex.dc5.util.model.DC5User;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
public interface DC5UserDao extends CrudRepository<DC5User, Long> {

	public DC5User findOneByUsernameIgnoreCase(String username);
}

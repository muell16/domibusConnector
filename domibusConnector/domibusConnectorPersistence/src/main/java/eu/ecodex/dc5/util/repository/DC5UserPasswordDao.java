package eu.ecodex.dc5.util.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.ecodex.dc5.util.model.DC5User;
import eu.ecodex.dc5.util.model.DC5UserPassword;

@Repository
public interface DC5UserPasswordDao extends CrudRepository<DC5UserPassword, Long> {

	@Query("SELECT p FROM DC5UserPassword p WHERE p.user=?1 AND p.currentPassword=true")
	DC5UserPassword findCurrentByUser(DC5User user);
}

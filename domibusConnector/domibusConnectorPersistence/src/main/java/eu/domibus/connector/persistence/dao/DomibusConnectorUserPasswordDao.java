package eu.domibus.connector.persistence.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.domibus.connector.persistence.model.PDomibusConnectorUserPassword;

@Repository
public interface DomibusConnectorUserPasswordDao extends CrudRepository<PDomibusConnectorUserPassword, Long> {

}

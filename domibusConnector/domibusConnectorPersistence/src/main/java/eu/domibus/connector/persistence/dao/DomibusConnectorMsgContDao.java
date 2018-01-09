package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
@Transactional
public interface DomibusConnectorMsgContDao extends CrudRepository<PDomibusConnectorMsgCont, Long> {

    @Modifying
    @Query("delete PDomibusConnectorMsgCont c where c.message = ?")
    public void deleteByMessage(PDomibusConnectorMessage message);
    
    public List<PDomibusConnectorMsgCont> findByMessage(PDomibusConnectorMessage message);

}

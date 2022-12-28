package eu.domibus.connector.persistence.dao;

import eu.ecodex.dc5.link.model.DC5LinkConfigJpaEntity;
import eu.ecodex.dc5.link.repository.DomibusConnectorLinkConfigurationDao;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Optional;

//@CommonPersistenceTest
//@DataSet(value = "/database/testdata/dbunit/DomibusConnectorLinkConfiguration.xml", strategy = CLEAN_INSERT)
@Disabled
class DomibusConnectorLinkConfigurationDaoTest {

    @Autowired
    DomibusConnectorLinkConfigurationDao dao;

    @Test
    void testCreateNewLink() {
        DC5LinkConfigJpaEntity linkConfig = new DC5LinkConfigJpaEntity();

        linkConfig.setConfigName("Config3");

        HashMap<String, String> props = new HashMap<>();
        props.put("test","test");

        dao.save(linkConfig);

        //TODO: check db
    }

    @Test
    public void findById() {
        Optional<DC5LinkConfigJpaEntity> linkConfig = dao.findById(2l);
    }


}
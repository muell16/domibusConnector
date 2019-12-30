package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Optional;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorLinkInfo.xml", strategy = CLEAN_INSERT)
class DomibusConnectorLinkInfoDaoTest {

    @Autowired
    DomibusConnectorLinkInfoDao dao;

    @Test
    void testCreateNewLink() {
        PDomibusConnectorLinkInfo linkInfo = new PDomibusConnectorLinkInfo();
        linkInfo.setDescription("test description");
        linkInfo.setLinkName("name");
//
//        HashMap<String, String> props = new HashMap<>();
//        props.put("test","test");
//        linkInfo.setProperties(props);

        dao.save(linkInfo);

        //TODO: check db
    }

//    @Test
//    public void findById() {
//        Optional<PDomibusConnectorLinkConfiguration> linkConfig = dao.findById(2);
//    }


    @Test
    void findOneBackendByLinkNameAndEnabledIsTrue() {
        Optional<PDomibusConnectorLinkInfo> test = dao.findOneBackendByLinkNameAndEnabledIsTrue("test");
        assertThat(test).isNotEmpty();
    }

    @Test
    void findOneByLinkName() {
        Optional<PDomibusConnectorLinkInfo> test2 = dao.findOneByLinkName("test2");
        assertThat(test2).isNotEmpty();

        //TODO: check link configuration...

    }


    @Test
    void findHighestId() {
        Long highestId = dao.findHighestId();
        assertThat(highestId).isEqualTo(1001);
    }
}
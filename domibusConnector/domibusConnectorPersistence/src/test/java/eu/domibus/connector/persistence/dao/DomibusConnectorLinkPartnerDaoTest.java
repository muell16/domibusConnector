package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkPartner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Optional;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorLinkPartner.xml", strategy = CLEAN_INSERT)
class DomibusConnectorLinkPartnerDaoTest {

    @Autowired
    DomibusConnectorLinkPartnerDao dao;

    @Test
    void testCreateNewLink() {
        PDomibusConnectorLinkPartner linkInfo = new PDomibusConnectorLinkPartner();
        linkInfo.setDescription("test description");
        linkInfo.setLinkName("name");
//
        HashMap<String, String> props = new HashMap<>();
        props.put("test","test");
        linkInfo.setProperties(props);

        dao.save(linkInfo);

        //TODO: check db
    }



    @Test
    void findOneBackendByLinkNameAndEnabledIsTrue() {
        Optional<PDomibusConnectorLinkPartner> test = dao.findOneBackendByLinkNameAndEnabledIsTrue("test");
        assertThat(test).isNotEmpty();
    }

    @Test
    void findOneByLinkName() {
        Optional<PDomibusConnectorLinkPartner> test2 = dao.findOneByLinkName("test2");
        assertThat(test2).isNotEmpty();

        assertThat(test2.get().getProperties()).as("must have property entry with [test=test]").hasEntrySatisfying("test", (k) -> k.equals("test"));

    }


    @Test
    void findHighestId() {
        Long highestId = dao.findHighestId();
        assertThat(highestId).isEqualTo(1001);
    }
}
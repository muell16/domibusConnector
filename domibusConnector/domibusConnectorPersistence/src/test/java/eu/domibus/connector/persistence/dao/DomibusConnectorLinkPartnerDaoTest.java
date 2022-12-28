package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.persistence.model.DC5LinkPartnerJpaEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@CommonPersistenceTest
//@DataSet(value = "/database/testdata/dbunit/DomibusConnectorLinkPartner.xml", strategy = CLEAN_INSERT)
@Disabled("Tests is failing on win10 maven build")
class DomibusConnectorLinkPartnerDaoTest {

    @Autowired
    DomibusConnectorLinkPartnerDao dao;

    @Test
    void testCreateNewLink() {
        DC5LinkPartnerJpaEntity linkInfo = new DC5LinkPartnerJpaEntity();
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
    void findByExample() {
        DC5LinkPartnerJpaEntity linkPartner = new DC5LinkPartnerJpaEntity();
        linkPartner.setLinkType(LinkType.GATEWAY);
        Example<DC5LinkPartnerJpaEntity> example = Example.of(linkPartner);

        List<DC5LinkPartnerJpaEntity> all = dao.findAll(example);

        assertThat(all).hasSize(1);
    }

    @Test
    void findOneBackendByLinkNameAndEnabledIsTrue() {
        Optional<DC5LinkPartnerJpaEntity> test = dao.findOneBackendByLinkNameAndEnabledIsTrue("test");
        assertThat(test).isNotEmpty();
    }

    @Test
    void findOneByLinkName() {
        Optional<DC5LinkPartnerJpaEntity> test2 = dao.findOneByLinkName("test2");
        assertThat(test2).isNotEmpty();

        assertThat(test2.get().getProperties()).as("must have property entry with [test=test]").hasEntrySatisfying("test", (k) -> k.equals("test"));

    }


    @Test
    void findHighestId() {
        Long highestId = dao.findHighestId();
        assertThat(highestId).isEqualTo(1001);
    }
}
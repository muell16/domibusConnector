package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest
@Disabled
class DCBusinessDomainPersistenceServiceImplTest {

    @Autowired
    DCBusinessDomainPersistenceService businessDomainPersistenceService;

    @Test
    @Order(1)
    void testFindById() {
        Optional<DC5BusinessDomain> byId = businessDomainPersistenceService.findById(DC5BusinessDomain.getDefaultBusinessDomainId());
        assertThat(byId).isPresent();
        assertThat(byId).get().extracting(DC5BusinessDomain::getConfigurationSource).isEqualTo(ConfigurationSource.DB);
    }

    @Test
    @Order(1)
    void testFindById_notExistant() {
        Optional<DC5BusinessDomain> byId = businessDomainPersistenceService.findById(new DC5BusinessDomain.BusinessDomainId("not_existant"));
        assertThat(byId).isEmpty();

    }

    @Test
    @Order(2)
    void findAll() {
        List<DC5BusinessDomain> all = businessDomainPersistenceService.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    @Order(3)
    void testUpdate() {
        Optional<DC5BusinessDomain> byId = businessDomainPersistenceService.findById(DC5BusinessDomain.getDefaultBusinessDomainId());

        DC5BusinessDomain DC5BusinessDomain = byId.get();
        DC5BusinessDomain.setDescription("Hallo Welt");
        DC5BusinessDomain.getProperties().put("test1", "test1");
        DC5BusinessDomain.getProperties().put("prop1", "test2");
        DC5BusinessDomain.getProperties().put("prop2.prop2", "test3");

        businessDomainPersistenceService.update(DC5BusinessDomain);

        Optional<DC5BusinessDomain> changed = businessDomainPersistenceService.findById(DC5BusinessDomain.getDefaultBusinessDomainId());
        DC5BusinessDomain changedBd = changed.get();

        assertThat(changedBd.getDescription()).isEqualTo("Hallo Welt");
        assertThat(changedBd.getProperties()).hasSize(3);

    }

    @Test
    @Order(4)
    void testUpdateNotExistant_shouldThrow() {
        DC5BusinessDomain DC5BusinessDomain = new DC5BusinessDomain();
        DC5BusinessDomain.setId(new DC5BusinessDomain.BusinessDomainId("doesnotexist"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            businessDomainPersistenceService.update(DC5BusinessDomain);
        });
    }
}

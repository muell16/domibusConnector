package eu.domibus.connector.domain.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
@ActiveProfiles("test")
@Disabled
class JpaConfigurationTest {

    @Autowired
    EntityManager em;

    @Test
    public void test() {
        assertThat(em).isNotNull();
    }

}
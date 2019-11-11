
package eu.domibus.connector.backend;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendContextLoadsITCase {

    
    @Test
    public void testContextLoads() {
        ConfigurableApplicationContext startUpSpringApplication =
                StartBackendOnly.startUpSpringApplication(new String[] {"backendlink-ws", "storage-db"}, new String[]
                        {"server.port=0",
                                "spring.liquibase.enabled=false",
                                "connector.backend.ws.key.store.path=classpath:/connector.jks",
                                "connector.persistence.big-data-impl-class=eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl"});
        assertThat(startUpSpringApplication).isNotNull();
    }
    
}

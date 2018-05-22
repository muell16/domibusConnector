
package eu.domibus.connector.backend;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendContextLoadsITCase {

    
    @Test
    public void testContextLoads() {
        ConfigurableApplicationContext startUpSpringApplication =
                StartBackendOnly.startUpSpringApplication(new String[] {"backendlink-ws"}, new String[]
                        {"server.port=0", "liquibase.enabled=false", "connector.backend.ws.key.store.path=classpath:/connector.jks" });
        assertThat(startUpSpringApplication).isNotNull();
    }
    
}

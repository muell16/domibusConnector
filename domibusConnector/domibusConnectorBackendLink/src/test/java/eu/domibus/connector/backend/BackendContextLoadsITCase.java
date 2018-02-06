
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
        ConfigurableApplicationContext startUpSpringApplication = StartBackendOnly.startUpSpringApplication(new String[] {});
        assertThat(startUpSpringApplication).isNotNull();
    }
    
}

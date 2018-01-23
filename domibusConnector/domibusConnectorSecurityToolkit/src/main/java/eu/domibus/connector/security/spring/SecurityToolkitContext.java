
package eu.domibus.connector.security.spring;

import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkitDefaultImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
public class SecurityToolkitContext {

    
    @ConditionalOnMissingBean
    @Bean
    public DomibusConnectorSecurityToolkit domibusConnectorSecurityToolkit() {
        return new DomibusConnectorSecurityToolkitDefaultImpl();
    }
    
}

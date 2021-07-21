
package eu.domibus.connector.security.spring;

import eu.domibus.connector.security.aes.DomibusConnectorAESTokenValidationCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
//@EnableConfigurationProperties(SecurityToolkitConfigurationProperties.class)
public class SecurityToolkitContext {


}

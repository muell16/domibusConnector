
package eu.domibus.connector.ws.backend.link.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;

/**
 * Configure the backend link web services
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@ImportResource({"classpath:services/DomibusConnectorBackendWebServiceConfig.xml"})
@EnableJms
@PropertySource("classpath:/eu/domibus/connector/backend/config/backend-default-config.properties")
public class WSBackendLinkContextConfiguration {
  
}

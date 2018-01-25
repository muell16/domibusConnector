
package eu.domibus.connector.gateway.link.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@ConfigurationProperties(prefix = "connector.webservice")
public class WebserviceProperties {
    
    private String servletMapping = "/services/";

}

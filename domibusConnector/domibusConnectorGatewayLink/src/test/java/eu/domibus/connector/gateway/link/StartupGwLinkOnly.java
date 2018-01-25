
package eu.domibus.connector.gateway.link;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

/**
 * starts up GW Link only, as web application with web service
 * and mocks controller DomibusConnectorGatewayDeliveryService
 * which just prints out the received messages
 * 
 * Useful for testing configuration, or run simple tests with soapUi
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(scanBasePackages="eu.domibus.connector.gateway.link")
public class StartupGwLinkOnly {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartupGwLinkOnly.class);

    public static void main(String [] args) {
        SpringApplication application = new SpringApplicationBuilder()
                .sources(StartupGwLinkOnly.class)
                .profiles("gwlinkws")     
                .build();
        
        application.run(args);
    }
    
    @Bean
    public DomibusConnectorGatewayDeliveryService mockedDomibusConnectorGatewayDeliveryService() {
        return (DomibusConnectorMessage message) -> {
            LOGGER.info("Received following message: [{}]", message);
        };
    }
    
    
}

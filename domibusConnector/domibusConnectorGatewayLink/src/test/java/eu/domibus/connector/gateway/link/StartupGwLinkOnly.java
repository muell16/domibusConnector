
package eu.domibus.connector.gateway.link;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        startGwContext(new String[]{}, new String[]{});
    }

    public static ConfigurableApplicationContext startGwContext(String[] profiles, String[] properties) {
        SpringApplication application = new SpringApplicationBuilder()
                .sources(StartupGwLinkOnly.class)
                .web(true)
                .profiles(profiles)
                .properties(properties)
                .build();
        return application.run();
    }

    public static List<DomibusConnectorMessage> getFromGwReceivedMessagesList(ConfigurableApplicationContext ctx) {
        return (List<DomibusConnectorMessage>) ctx.getBean("fromGwReceivedMessagesList");
    }

    public static DomibusConnectorGatewaySubmissionWebService getDomibusConnectorGatewaySubmissionWebServiceClient(ConfigurableApplicationContext ctx) {
        return ctx.getBean("gwSubmissionClient", DomibusConnectorGatewaySubmissionWebService.class);
    }

    @Bean("fromGwReceivedMessagesList")
    public List<DomibusConnectorMessage> fromGwReceivedMessagesList() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    @Bean
    public DomibusConnectorGatewayDeliveryService mockedDomibusConnectorGatewayDeliveryService() {
        return new DomibusConnectorGatewayDeliveryService() {
            @Override
            public void deliverMessageFromGateway(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
                LOGGER.info("Received following message: [{}]",message);
                fromGwReceivedMessagesList().add(message);
            }
        };
    }
    
    
}

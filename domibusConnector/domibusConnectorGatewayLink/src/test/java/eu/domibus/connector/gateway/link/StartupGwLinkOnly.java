
package eu.domibus.connector.gateway.link;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    public static final String TRANSPORT_STATE_BLOCKING_QUEUE_NAME = "transportStateQeue";
    public static final String FROM_GW_RCV_MESSAGES_LIST_NAME = "fromGwReceivedMessagesList";

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
        return (List<DomibusConnectorMessage>) ctx.getBean(FROM_GW_RCV_MESSAGES_LIST_NAME);
    }

    public static DomibusConnectorGatewaySubmissionWebService getDomibusConnectorGatewaySubmissionWebServiceClient(ConfigurableApplicationContext ctx) {
        return ctx.getBean("gwSubmissionClient", DomibusConnectorGatewaySubmissionWebService.class);
    }

    public static LinkedBlockingQueue<TransportStatusService.DomibusConnectorTransportState> getSetTransportStates(ConfigurableApplicationContext ctx) {
        return (LinkedBlockingQueue<TransportStatusService.DomibusConnectorTransportState>) ctx.getBean(TRANSPORT_STATE_BLOCKING_QUEUE_NAME);
    }

    @Bean(FROM_GW_RCV_MESSAGES_LIST_NAME)
    public List<DomibusConnectorMessage> fromGwReceivedMessagesList() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    @Bean(TRANSPORT_STATE_BLOCKING_QUEUE_NAME)
    public LinkedBlockingQueue<TransportStatusService.DomibusConnectorTransportState> transportStatesQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorGatewayDeliveryService mockedDomibusConnectorGatewayDeliveryService() {
        return new DomibusConnectorGatewayDeliveryService() {
            @Override
            public void deliverMessageFromGatewayToController(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
                LOGGER.info("Received following message: [{}]",message);
                fromGwReceivedMessagesList().add(message);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public TransportStatusService mockedTransportStatusService() {
        return new TransportStatusService() {

            @Override
            public void setTransportStatusForTransportToGateway(DomibusConnectorTransportState transportState) {
                LOGGER.info("setting transport: [{}]", transportState);
                transportStatesQueue().add(transportState);
            }

            @Override
            public void setTransportStatusForTransportToBackendClient(DomibusConnectorTransportState transportState) {
                throw new IllegalStateException("Not implemented!");
            }
        };
    }
    
    
}

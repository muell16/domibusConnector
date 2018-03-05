package test.eu.domibus.connector.gateway.link.testgw;


import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * IMPLEMENTATION OF THE GW WEB SERIVCE INTERFACE
 * FOR TESTING PURPOSE
 */
@SpringBootApplication(scanBasePackageClasses = {TestGW.class})
@ImportResource("classpath:/test/eu/domibus/connector/gateway/link/testgw/TestGatewayContext.xml")
public class TestGW {


    public static ConfigurableApplicationContext startContext(String[] properties) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        SpringApplication springApp = builder.sources(TestGW.class)
                .web(true)
                .properties(properties)
                .build();

        return springApp.run();
    }

    public static List<DomibusConnectorMessageType> getToGwSubmittedMessages(ConfigurableApplicationContext context) {
        return (List<DomibusConnectorMessageType>) context.getBean("toGwSubmittedMessagesList");
    }

    public static DomibusConnectorGatewayDeliveryWebService getConnectorDeliveryClient(ConfigurableApplicationContext ctx) {
        return (DomibusConnectorGatewayDeliveryWebService) ctx.getBean("connectorDeliveryClient");
    }

    @Bean("toGwSubmittedMessagesList")
    public List<DomibusConnectorMessageType> deliveredMessagesList() {
        return Collections.synchronizedList(new ArrayList<>());
    }


    @Bean("testGwSubmissionService")
    public DomibusConnectorGatewaySubmissionWebService testGwSubmissionService() {
        return new DomibusConnectorGatewaySubmissionWebService() {

            @Override
            public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType deliverMessageRequest) {
                List<DomibusConnectorMessageType> messageList = deliveredMessagesList();

                messageList.add(deliverMessageRequest);

                DomibsConnectorAcknowledgementType acknowledgementType = new DomibsConnectorAcknowledgementType();

                String messageId = UUID.randomUUID().toString() + "_TESTGW";

                acknowledgementType.setResultMessage("resultMessage");
                acknowledgementType.setResult(true);
                acknowledgementType.setMessageId(messageId);

                return acknowledgementType;

            }
        };
    }

}

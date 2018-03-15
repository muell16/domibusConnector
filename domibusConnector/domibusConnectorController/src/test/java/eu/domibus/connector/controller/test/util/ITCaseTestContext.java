package eu.domibus.connector.controller.test.util;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication(scanBasePackages = {
        "eu.domibus.connector.controller",
        "eu.domibus.connector.persistence", //load persistence
        "eu.domibus.connector.evidences",   //load evidences toolkit
        "eu.domibus.connector.security"     //load security toolkit
})
@Profile("ITCaseTestContext")
public class ITCaseTestContext {

    private final static Logger LOGGER = LoggerFactory.getLogger(ITCaseTestContext.class);

//
//    //@EnableConfigurationProperties
//    protected static class TestConfiguration {

        public static final String TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME = "togwdeliveredmessages";
        public static final String TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME = "tobackenddeliveredmessages";

        public interface DomibusConnectorGatewaySubmissionServiceInterceptor {
            public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException;
        }

//        public interface DomibusConnectorBackendDeliveryServiceInterceptor {
//            public void deliverMessageToBackend(DomibusConnectorMessage message);
//        }


        @Bean
        public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean(TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
        @Qualifier(TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
        public List<DomibusConnectorMessage> toBackendDeliveredMessages() {
            return Collections.synchronizedList(new ArrayList<>());
        }

        @Bean(TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
        @Qualifier(TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
        public List<DomibusConnectorMessage> toGatewayDeliveredMessages() {
            return Collections.synchronizedList(new ArrayList<>());
        }

        @Bean
        public DomibusConnectorGatewaySubmissionServiceInterceptor domibusConnectorGatewaySubmissionServiceInterceptor() {
            return Mockito.mock(DomibusConnectorGatewaySubmissionServiceInterceptor.class);
        }


        @Bean
        public DomibusConnectorBackendDeliveryService domibusConnectorBackendDeliveryService() {
            return new DomibusConnectorBackendDeliveryService() {
                @Override
                public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
                    LOGGER.info("Delivered Message [{}] to Backend");
                    toBackendDeliveredMessages().add(message);
                }
            };
        }

        @Bean
        public DomibusConnectorGatewaySubmissionService sendMessageToGwService() {
            return new DomibusConnectorGatewaySubmissionService() {
                @Override
                public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {
                    domibusConnectorGatewaySubmissionServiceInterceptor().submitToGateway(message);
                    LOGGER.info("Delivered Message [{}] to Gateway");
                    toGatewayDeliveredMessages().add(message);
                }
            };
        }

}

package eu.domibus.connector.controller.test.util;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@SpringBootApplication(scanBasePackages = {
        "eu.domibus.connector.controller",
        "eu.domibus.connector.common",      //load common
        "eu.domibus.connector.persistence", //load persistence
        "eu.domibus.connector.evidences",   //load evidences toolkit
        "eu.domibus.connector.security"     //load security toolkit,
},
    excludeName = {"org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration"}
)
@Profile("ITCaseTestContext")
public class ITCaseTestContext {

    private final static Logger LOGGER = LoggerFactory.getLogger(ITCaseTestContext.class);


    public static final String TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME = "togwdeliveredmessages";
    public static final String TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME = "tobackenddeliveredmessages";

    /**
     * Use this interface to tamper with the test...
     */
    public interface DomibusConnectorGatewaySubmissionServiceInterceptor {
        public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException;
    }


    /**
     * Use this interface to tamper with the test...
     */
    public interface DomibusConnectorBackendDeliveryServiceInterceptor {
        void deliveryToBackend(DomibusConnectorMessage message);
    }

    @Autowired
    PlatformTransactionManager txManager;



    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    @Qualifier(TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    public BlockingQueue<DomibusConnectorMessage> toBackendDeliveredMessages() {
        return new ArrayBlockingQueue<>(100);
    }

    @Bean(TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    @Qualifier(TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    public BlockingQueue<DomibusConnectorMessage> toGatewayDeliveredMessages() {
        return new ArrayBlockingQueue<>(100);
    }

    @Bean
    public DomibusConnectorGatewaySubmissionServiceInterceptor domibusConnectorGatewaySubmissionServiceInterceptor() {
        return Mockito.mock(DomibusConnectorGatewaySubmissionServiceInterceptor.class);
    }

    @Bean
    public DomibusConnectorBackendDeliveryServiceInterceptor domibusConnectorBackendDeliveryServiceInterceptor() {
        return Mockito.mock(DomibusConnectorBackendDeliveryServiceInterceptor.class);
    }

    @Bean
    public QueueBasedDomibusConnectorBackendDeliveryService domibusConnectorBackendDeliveryService() {
        return new QueueBasedDomibusConnectorBackendDeliveryService();
    }

    @Bean
    public QueueBasedDomibusConnectorGatewaySubmissionService sendMessageToGwService() {
        return new QueueBasedDomibusConnectorGatewaySubmissionService();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    public static class QueueBasedDomibusConnectorBackendDeliveryService implements DomibusConnectorBackendDeliveryService {

        @Autowired
        TransportStatusService transportStatusService;

        @Autowired
        DomibusConnectorBackendDeliveryServiceInterceptor interceptor;

        @Autowired
        @Qualifier(TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
        public BlockingQueue<DomibusConnectorMessage> toBackendDeliveredMessages; // = new ArrayBlockingQueue<>(100);;

        @Override
        public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
            interceptor.deliveryToBackend(message);

            LOGGER.info("Delivered Message [{}] to Backend");


            TransportStatusService.TransportId dummyBackend = transportStatusService.createTransportFor(message, new DomibusConnectorLinkPartner.LinkPartnerName("dummy_backend"));
            TransportStatusService.DomibusConnectorTransportState state = new TransportStatusService.DomibusConnectorTransportState();
            state.setConnectorTransportId(dummyBackend);
            state.setStatus(TransportState.ACCEPTED);

            String backendId = "BACKEND_" + UUID.randomUUID().toString();
            state.setRemoteMessageId(backendId); //assigned backend message id
            state.setTransportImplId("mem_" + UUID.randomUUID().toString()); //set a transport id
            transportStatusService.updateTransportToBackendClientStatus(dummyBackend, state);

            DomibusConnectorMessage msg = DomibusConnectorMessageBuilder.createBuilder()
                    .copyPropertiesFrom(message)
                    .build();
            msg.getMessageDetails().setBackendMessageId(backendId);

            toBackendDeliveredMessages.add(msg);

        }

        synchronized public void clearQueue() {
            toBackendDeliveredMessages.clear();
        }

        public synchronized BlockingQueue<DomibusConnectorMessage> getQueue() {
            return this.toBackendDeliveredMessages;
        }
    }

    public static class QueueBasedDomibusConnectorGatewaySubmissionService implements DomibusConnectorGatewaySubmissionService {

        @Autowired
        TransportStatusService transportStatusService;

        @Autowired
        DomibusConnectorGatewaySubmissionServiceInterceptor interceptor;

        @Autowired
        @Qualifier(TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
        public BlockingQueue<DomibusConnectorMessage> toGatewayDeliveredMessages; // = new ArrayBlockingQueue<>(100);

        @Override
        synchronized public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {
            interceptor.submitToGateway(message);
            LOGGER.info("Delivered Message [{}] to Gateway", message);

            TransportStatusService.TransportId dummyGW = transportStatusService.createTransportFor(message, new DomibusConnectorLinkPartner.LinkPartnerName("dummy_gw"));
            TransportStatusService.DomibusConnectorTransportState state = new TransportStatusService.DomibusConnectorTransportState();
            state.setConnectorTransportId(dummyGW);
//            state.setConnectorMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId(message.getConnectorMessageId()));
            state.setStatus(TransportState.ACCEPTED);
            String ebmsId = "EBMS_" + UUID.randomUUID().toString();
            state.setRemoteMessageId(ebmsId); //assigned EBMS ID
            state.setTransportImplId("mem_" + UUID.randomUUID().toString()); //set a transport id
            transportStatusService.updateTransportToGatewayStatus(dummyGW , state);

            DomibusConnectorMessage msg = DomibusConnectorMessageBuilder.createBuilder()
                    .copyPropertiesFrom(message)
                    .build();
            msg.getMessageDetails().setEbmsMessageId(ebmsId);

            toGatewayDeliveredMessages.add(msg);
        }

        synchronized public void clearQueue() {
            toGatewayDeliveredMessages.clear();
        }

        public synchronized BlockingQueue<DomibusConnectorMessage> getQueue() {
            return this.toGatewayDeliveredMessages;
        }

    }

}

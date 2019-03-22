
package eu.domibus.connector.backend;

import eu.domibus.connector.controller.exception.DomibusConnectorRejectDeliveryException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorDeliveryRejectionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(
    scanBasePackages={"eu.domibus.connector.backend", "eu.domibus.connector.persistence"}
)  
@Configuration
//@Profile("TestBackendContext")
public class TestBackendContext {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestBackendContext.class);

    public static final String SUBMITTED_MESSAGES_LIST_BEAN_NAME = "submittedMessages";
    public static final String REJECTED_DELIVERIES_LIST_BEAN = "rejectedDeliveries";
    public static final String AS_DELIVERED_SET_LIST_BEAN = "asDeliveredSetMessages";
    
    @Bean(SUBMITTED_MESSAGES_LIST_BEAN_NAME)
    public BlockingQueue<DomibusConnectorMessage> createList() {
        return new LinkedBlockingQueue<>();
    }

    @Bean(AS_DELIVERED_SET_LIST_BEAN)
    public BlockingQueue<DomibusConnectorMessage> createSetAsDeliveredList() {
        return new LinkedBlockingQueue<>();
    }

    @Bean(REJECTED_DELIVERIES_LIST_BEAN)
    public BlockingQueue<DomibusConnectorRejectDeliveryException> createRejectedDeliveriesList() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public DomibusConnectorDeliveryRejectionService domibusConnectorDeliveryRejectionService() {
        return new DomibusConnectorDeliveryRejectionService() {
            @Override
            public void rejectDelivery(DomibusConnectorRejectDeliveryException cause) {
                BlockingQueue<DomibusConnectorRejectDeliveryException> rejectedDeliveriesList = createRejectedDeliveriesList();
                rejectedDeliveriesList.add(cause);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorMessageIdGenerator domibusConnectorMessageIdGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    
    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorBackendSubmissionService dummySubmissionService() {
        final BlockingQueue<DomibusConnectorMessage>  submittedMessages = createList();
        final BlockingQueue<DomibusConnectorMessage> asDeliveredSet = createSetAsDeliveredList();
        DomibusConnectorBackendSubmissionService submissionService = new DomibusConnectorBackendSubmissionService() {
            @Override
            public void submitToController(DomibusConnectorMessage message) {
                LOGGER.warn("message to dummySubmissionService controller submitted: [{}]", message);
                submittedMessages.add(message);
            }


        };
        return submissionService;
    }



}


package eu.domibus.connector.backend;

import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
@Profile("TestBackendContext")
public class TestBackendContext {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestBackendContext.class);

    public static final String SUBMITTED_MESSAGES_LIST_BEAN_NAME = "submittedMessages";
    
    @Bean("submittedMessages")
    public List<DomibusConnectorMessage> createList() {
        return Collections.synchronizedList(new ArrayList());
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorMessageIdGenerator domibusConnectorMessageIdGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    
    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorBackendSubmissionService dummySubmissionService() {
        final List<DomibusConnectorMessage>  submittedMessages = createList(); 
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

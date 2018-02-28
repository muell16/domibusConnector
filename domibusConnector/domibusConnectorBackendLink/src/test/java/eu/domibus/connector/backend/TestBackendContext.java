
package eu.domibus.connector.backend;

import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(
    scanBasePackages={"eu.domibus.connector.backend.ws", "eu.domibus.connector.backend", "eu.domibus.connector.persistence"},
    exclude = {
//        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class
    }
)  
@Configuration
public class TestBackendContext {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestBackendContext.class);
    
    
    @Bean("submittedMessages")
    public List<DomibusConnectorMessage> createList() {
        return Collections.synchronizedList(new ArrayList());
    }
    
    
    @Bean
    public DomibusConnectorBackendSubmissionService dummySubmissionService() {
        final List<DomibusConnectorMessage>  submittedMessages = createList(); 
        DomibusConnectorBackendSubmissionService submissionService = new DomibusConnectorBackendSubmissionService() {
            @Override
            public void submitToController(DomibusConnectorMessage message) {
                LOGGER.debug("message to controller submitted: [{}]", message);
                submittedMessages.add(message);
            }                        
        };
        return submissionService;
    }
    
}

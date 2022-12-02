package eu.ecodex.dc5.flow;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DC5FlowConfiguration {


    @Bean
    public DomibusConnectorMessageIdGenerator domibusConnectorMessageIdGenerator() {
        return DomibusConnectorMessageId::ofRandom;
    }
}

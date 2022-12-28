package eu.domibus.connector.controller.spring;

import java.util.UUID;

import eu.ecodex.dc5.message.model.DC5MessageId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;

/**
 *  Default connectorMessage ID generator
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
public class MessageIdGeneratorConfigurer {


    @ConditionalOnMissingBean(DomibusConnectorMessageIdGenerator.class)
    @Bean
    public DomibusConnectorMessageIdGenerator domibusConnectorMessageIdGenerator() {
        return () -> new DC5MessageId(String.format("%s@%s", UUID.randomUUID(), "domibus.connector.eu"));
    }
    
    
}

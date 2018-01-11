package eu.domibus.connector.controller.spring;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import java.util.Date;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
public class MessageIdGeneratorConfigurer {

    
    
    @ConditionalOnMissingBean(DomibusConnectorMessageIdGenerator.class)
    @Bean
    public DomibusConnectorMessageIdGenerator domibusConnectorMessageIdGenerator() {
        return () -> String.format("%s-%s", UUID.randomUUID(), new Date());
    }
    
    
}

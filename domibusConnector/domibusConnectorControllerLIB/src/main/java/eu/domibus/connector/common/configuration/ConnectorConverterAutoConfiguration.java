package eu.domibus.connector.common.configuration;

import eu.domibus.connector.common.annotations.ConnectorPropertyConverter;
import eu.domibus.connector.common.converters.BusinessDomainIdConverter;
import eu.domibus.connector.common.converters.EvidenceActionConverter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorConverterAutoConfiguration {

    @Bean
    @ConnectorPropertyConverter
    public EvidenceActionConverter stringToEvidenceActionConverter() {
        return new EvidenceActionConverter();
    }

    @Bean
    @ConnectorPropertyConverter
    public BusinessDomainIdConverter stringToBusinessDomainId() { return new BusinessDomainIdConverter(); }

}

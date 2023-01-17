package test.context;

import eu.domibus.connector.common.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServicePassthroughImpl;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.scope.BusinessDomainScopeConfiguration;
import eu.ecodex.dc5.domain.validation.DCDomainValidationService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ComponentScan(basePackages = {"eu.domibus.connector.security", "eu.domibus.connector.common", "eu.domibus.connector.dss"})
@EnableAutoConfiguration( exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@Configuration
//@Import({BasicDssConfigurationProperties.class})
@Import({BusinessDomainScopeConfiguration.class})
@EnableConfigurationProperties({ConnectorConfigurationProperties.class})
public class SecurityToolkitTestContext {

    private static DC5BusinessDomain businessDomain =  DC5BusinessDomain.builder()
//            .properties()
            .build();


    @Bean
    public LargeFilePersistenceService bigDataPersistenceService() {
        LargeFilePersistenceServicePassthroughImpl service = new LargeFilePersistenceServicePassthroughImpl();
        return Mockito.spy(service);
    }

    @Bean
    public DCBusinessDomainManager dcBusinessDomainManager() {
        DCBusinessDomainManager mock = Mockito.mock(DCBusinessDomainManager.class);
        Mockito.when(mock.getValidDomains()).thenReturn(Stream.of(businessDomain).collect(Collectors.toList()));
        Mockito.when(mock.getDomain(Mockito.any())).thenReturn(Optional.of(businessDomain));
        return mock;
    }

    @Bean
    public DCBusinessDomainPersistenceService dcBusinessDomainPersistenceService() {
        DCBusinessDomainPersistenceService mock = Mockito.mock(DCBusinessDomainPersistenceService.class);
        Mockito.when(mock.findById(eq(DC5BusinessDomain.getDefaultBusinessDomainId())))
                .thenReturn(Optional.of(DC5BusinessDomain.getDefaultBusinessDomain()));
        return mock;
    }

    @Bean
    public DCDomainValidationService dcDomainValidationService() {
        DCDomainValidationService mock = Mockito.mock(DCDomainValidationService.class);
        Mockito.when(mock.validateDomain(any())).thenReturn(DCBusinessDomainManager.DomainValidResult.builder().build());
        return mock;
    }

}

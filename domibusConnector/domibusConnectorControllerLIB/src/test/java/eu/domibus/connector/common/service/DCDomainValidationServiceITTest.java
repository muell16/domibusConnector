package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.DomainValidationRule;
import eu.ecodex.dc5.domain.validation.DCDomainValidationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DCDomainValidationServiceITTest.Rule1Test.class, DCDomainValidationServiceITTest.Rule2Test.class})
@RequiredArgsConstructor
class DCDomainValidationServiceITTest {

    @Component
    static class Rule1Test implements DomainValidationRule {

        @Override
        public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain domain) {
            return DCBusinessDomainManager.DomainValidResult.builder().warning("test warning").build();
        }
    }

    @Component
    static class Rule2Test implements DomainValidationRule {

        @Override
        public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain domain) {
            return DCBusinessDomainManager.DomainValidResult.builder().errors(Collections.singletonList("an error")).build();
        }
    }

    private final ApplicationContext context;

    @MockBean
    DCBusinessDomainPersistenceService dcBusinessDomainPersistenceService;

    @Test
    void given_the_validation_rules_raise_an_error_then_the_domain_validation_result_will_be_false() {
        // Arrange
        final DCDomainValidationService sut = new DCDomainValidationService(context);
        final DomibusConnectorBusinessDomain.BusinessDomainId anyId = new DomibusConnectorBusinessDomain.BusinessDomainId("any id");
        final DomibusConnectorBusinessDomain domain = new DomibusConnectorBusinessDomain();
        domain.setId(anyId);


        // Act

        final DCBusinessDomainManager.DomainValidResult validationResult = sut.validateDomain(domain);

        // Assert
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).contains("an error");
        assertThat(validationResult.getWarnings()).contains("test warning");
    }
}
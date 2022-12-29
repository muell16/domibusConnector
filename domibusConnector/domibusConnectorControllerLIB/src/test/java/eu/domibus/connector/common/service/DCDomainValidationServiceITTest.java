package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
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
        public DCBusinessDomainManager.DomainValidResult validate(DC5BusinessDomain domain) {
            return DCBusinessDomainManager.DomainValidResult.builder().warning("test warning").build();
        }
    }

    @Component
    static class Rule2Test implements DomainValidationRule {

        @Override
        public DCBusinessDomainManager.DomainValidResult validate(DC5BusinessDomain domain) {
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
        final DC5BusinessDomain.BusinessDomainId anyId = new DC5BusinessDomain.BusinessDomainId("any id");
        final DC5BusinessDomain domain = new DC5BusinessDomain();
        domain.setId(anyId);


        // Act

        final DCBusinessDomainManager.DomainValidResult validationResult = sut.validateDomain(domain);

        // Assert
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).contains("an error");
        assertThat(validationResult.getWarnings()).contains("test warning");
    }
}
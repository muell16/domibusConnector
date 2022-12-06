package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.DomainValidationRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DCDomainValidationServiceTest {

    @Mock
    private ApplicationContext context;

    @Test
    void given_the_validation_rules_raise_an_error_then_the_domain_validation_result_will_be_false() {
        // Arrange
        final HashMap<String, DomainValidationRule> rules = new HashMap<>();
        rules.put("foo", createDummyRuleWithErrors());

        Mockito.when(context.getBeansOfType(DomainValidationRule.class)).thenReturn(rules);

        final DCDomainValidationService sut = new DCDomainValidationService(context);

        // Act
        final DCBusinessDomainManager.DomainValidResult validationResult = sut.validateDomain(new DomibusConnectorBusinessDomain.BusinessDomainId("any id"));

        // Assert
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).contains("an error");
    }

    @Test
    void given_all_rules_do_not_raise_an_error_then_the_domain_validation_result_will_be_true() {
        // Arrange
        final HashMap<String, DomainValidationRule> rules = new HashMap<>();
        rules.put("foo", createDummyRuleWithoutErrors());

        Mockito.when(context.getBeansOfType(DomainValidationRule.class)).thenReturn(rules);

        final DCDomainValidationService sut = new DCDomainValidationService(context);

        // Act
        final DCBusinessDomainManager.DomainValidResult validationResult = sut.validateDomain(new DomibusConnectorBusinessDomain.BusinessDomainId("any id"));

        // Assert
        assertThat(validationResult.isValid()).isTrue();
    }

    private DomainValidationRule createDummyRuleWithErrors() {
        return id -> DCBusinessDomainManager.DomainValidResult.builder().errors(Collections.singletonList("an error")).build();
    }

    private DomainValidationRule createDummyRuleWithoutErrors() {
        return id -> DCBusinessDomainManager.DomainValidResult.builder().build();
    }
}
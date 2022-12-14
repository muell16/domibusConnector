package eu.domibus.connector.common.service;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.DomainValidationRule;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BusinessScopedConfigurationPropertiesValidationRule implements DomainValidationRule {

    private static final Logger LOGGER = LogManager.getLogger(BusinessScopedConfigurationPropertiesValidationRule.class);

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final BusinessScopedConfigurationPropertiesRegistrar.BusinessScopedConfigurationPropertiesListHolder holder;
    public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain domain) {

        final List<? extends ConstraintViolation<?>> hibernateConstraintViolations = holder.getBusinessScopeConfigurationPropertyBeanNames().stream()
                .map((s) -> {
                    try {
                        return Class.forName(s);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(clazz -> configurationPropertyManagerService.loadConfigurationOnlyFromMap(domain.getProperties(), clazz))
                .map(configurationPropertyManagerService::validateConfiguration)
                .flatMap(Collection::stream)
                .peek(v -> LOGGER.info("Domain Validation Error!"))
                .peek(v -> LOGGER.info("Message: " + v.getMessage()))
                .peek(v -> LOGGER.info("Invalid Value: " + v.getInvalidValue()))
                .peek(v -> LOGGER.info("Property Path: " + v.getPropertyPath()))
                .collect(Collectors.toList());

        return DCBusinessDomainManager.DomainValidResult.builder().errors(hibernateConstraintViolations).build();
    }
}

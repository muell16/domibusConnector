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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class BusinessScopedConfigurationPropertiesValidationRule implements DomainValidationRule {

    private static final Logger LOGGER = LogManager.getLogger(BusinessScopedConfigurationPropertiesValidationRule.class);

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final Optional<BusinessScopedConfigurationPropertiesRegistrar.BusinessScopedConfigurationPropertiesListHolder> holder;
    public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain domain) {

        Stream<? extends ConstraintViolation<?>> hibernateConstraintViolations = holder
                .map(h -> h.getBusinessScopeConfigurationPropertyBeanNames().stream())
                .orElse(Stream.empty())

                .map((s) -> {
                    try {
                        return Class.forName(s);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(clazz -> configurationPropertyManagerService.loadConfigurationOnlyFromMap(domain.getProperties(), clazz))
                .map(configurationPropertyManagerService::validateConfiguration)
                .flatMap(Collection::stream);
        if (LOGGER.isDebugEnabled()) {
            hibernateConstraintViolations = hibernateConstraintViolations.map(v -> {
                LOGGER.debug("Domain Validation Error!\n" +
                        "\tMessage: [{}]\n" +
                        "\tInvalid Value: [{}]\n" +
                        "\tProperty Path: [{}]\n",
                v.getMessage(), v.getInvalidValue(), v.getPropertyPath());
                return v;
            });
        }

        return DCBusinessDomainManager.DomainValidResult
                .builder()
                .errors(hibernateConstraintViolations.collect(Collectors.toList()))
                .build();
    }
}

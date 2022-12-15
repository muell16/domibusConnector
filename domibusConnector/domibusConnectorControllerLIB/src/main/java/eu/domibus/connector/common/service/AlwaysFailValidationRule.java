package eu.domibus.connector.common.service;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.DomainValidationRule;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Profile("fail-always-rule")
@RequiredArgsConstructor
@Component
public class AlwaysFailValidationRule implements DomainValidationRule {

    private static final Logger LOGGER = LogManager.getLogger(AlwaysFailValidationRule.class);

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final BusinessScopedConfigurationPropertiesRegistrar.BusinessScopedConfigurationPropertiesListHolder holder;
    public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain domain) {

        final ArrayList<Object> errors = new ArrayList<>();
        errors.add("This is a fail1");
        errors.add("This is a fail2");
        errors.add("This is a fail3");
        errors.add("This is a fail4");
        errors.add("This is a failurrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        return DCBusinessDomainManager.DomainValidResult.builder().errors(errors).warning("You have been warned!").build();
    }
}

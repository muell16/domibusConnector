package eu.domibus.connector.common.service;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ConfigurationPropertiesValidator implements DCBusinessDomainManagerImpl.ValidationRule {

    private final BusinessScopedConfigurationPropertiesValidator configRegistrar;


    private final ConfigurationPropertyManagerService configurationPropertyManagerService;

    public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain.BusinessDomainId id) {

        List<Set<? extends ConstraintViolation<?>>> collect1 = configRegistrar.getConfigClassNames()
                .stream()
                .map((s) -> {
                    try {
                        return Class.forName(s);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(clazz -> configurationPropertyManagerService.loadConfiguration(id, clazz))
                .map(obj -> configurationPropertyManagerService.validateConfiguration(id, obj))
                .collect(Collectors.toList());

        //TODO: convert collect1 into DomainValidResult
        return null;
    }
}

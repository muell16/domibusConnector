package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.DomainValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
class DCDomainValidationService {
    private final ApplicationContext applicationContext;



    DCBusinessDomainManager.DomainValidResult validateDomain(DomibusConnectorBusinessDomain.BusinessDomainId id) {
        Map<String, DomainValidationRule> beansOfType = applicationContext.getBeansOfType(DomainValidationRule.class);

        final ArrayList<Object> errors = new ArrayList<>();
        final ArrayList<Object> warnings = new ArrayList<>();

        for (DomainValidationRule rule : beansOfType.values()) {
            final DCBusinessDomainManager.DomainValidResult validationResult = rule.validate(id);
            errors.addAll(validationResult.getErrors());
            warnings.addAll(validationResult.getWarnings());
        }

        return DCBusinessDomainManager.DomainValidResult.builder()
                .errors(errors)
                .warnings(warnings)
                .build();
    }
}


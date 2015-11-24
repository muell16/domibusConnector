package eu.domibus.connector.validation.rule;

import java.util.Set;

import eu.domibus.connector.validation.result.ValidationResult;

public abstract class DomibusConnectorContentValidationRule {

    public String fieldName;

    public Set<DomibusConnectorContentValidationRule> subRules;

    public abstract Set<ValidationResult> validate(byte[] content);
}

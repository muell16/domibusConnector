package eu.domibus.connector.validation.result;

import eu.domibus.connector.validation.rule.DomibusConnectorContentValidationRule;

/**
 * This object represents informations created by
 * {@link DomibusConnectorContentValidationRule} rules which are packed together
 * during the validation of a business document.
 * 
 * @author riederb
 * 
 */
public class ValidationResult {

    private final ValidationResultSeverity severity;
    private final Class<? extends DomibusConnectorContentValidationRule> validationRule;
    private final String validationMessage;

    /**
     * Constructor with a given {@link DomibusConnectorContentValidationRule}
     * extending Class object where the result is created and the
     * validationMessage by the rule.
     * 
     * @param severity
     * @param validationRule
     * @param validationMessage
     */
    public ValidationResult(ValidationResultSeverity severity,
            Class<? extends DomibusConnectorContentValidationRule> validationRule, String validationMessage) {
        super();
        this.severity = severity;
        this.validationRule = validationRule;
        this.validationMessage = validationMessage;
    }

    /**
     * Constructor with a given {@link DomibusConnectorContentValidationRule}
     * extending Class object where the result is created and the
     * validationMessage by the rule. As no {@link ValidationResultSeverity}
     * severity is given here the least severity INFO is chosen by default.
     * 
     * @param validationRule
     * @param validationMessage
     */
    public ValidationResult(Class<? extends DomibusConnectorContentValidationRule> validationRule,
            String validationMessage) {
        super();
        this.severity = ValidationResultSeverity.INFO;
        this.validationRule = validationRule;
        this.validationMessage = validationMessage;
    }

    public ValidationResultSeverity getSeverity() {
        return severity;
    }

    public Class<? extends DomibusConnectorContentValidationRule> getValidationRule() {
        return validationRule;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

}

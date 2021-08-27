package eu.domibus.connector.security.configuration.validation;

import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.security.configuration.DCBusinessDocumentValidationConfigurationProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CheckAllowedAdvancedElectronicSystemTypeValidator implements ConstraintValidator<CheckAllowedAdvancedElectronicSystemType, DCBusinessDocumentValidationConfigurationProperties> {

    @Override
    public void initialize(CheckAllowedAdvancedElectronicSystemType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DCBusinessDocumentValidationConfigurationProperties value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean valid = true;
        context.disableDefaultConstraintViolation();
        List<AdvancedElectronicSystemType> allowedAdvancedSystemTypes = value.getAllowedAdvancedSystemTypes();
        if (allowedAdvancedSystemTypes.contains(AdvancedElectronicSystemType.SIGNATURE_BASED) && value.getSignatureValidation() == null) {
            context.buildConstraintViolationWithTemplate("AllowedAdvancedSystemTypes contains SIGNATURE_BASED so signature-validation must be configured")
                    .addPropertyNode("signature-validation")
                    .addConstraintViolation();
            valid = false;
        }
        if (allowedAdvancedSystemTypes.contains(AdvancedElectronicSystemType.AUTHENTICATION_BASED) && value.getAuthenticationValidation() == null) {
            context.buildConstraintViolationWithTemplate("AllowedAdvancedSystemTypes contains AUTHENTICATION_BASED so authentication-validation must be configured")
                    .addPropertyNode("authentication-validation")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }

}

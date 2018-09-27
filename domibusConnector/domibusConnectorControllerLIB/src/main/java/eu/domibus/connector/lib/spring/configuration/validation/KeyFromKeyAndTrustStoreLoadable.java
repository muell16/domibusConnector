package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreAndTrustStoreConfigurationProperties;

import javax.validation.*;
import java.util.Set;

public class KeyFromKeyAndTrustStoreLoadable implements ConstraintValidator<CheckKeyIsLoadableFromKeyStore, KeyAndKeyStoreAndTrustStoreConfigurationProperties> {

    private Validator validator;

    @Override
    public void initialize(CheckKeyIsLoadableFromKeyStore constraintAnnotation) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public boolean isValid(KeyAndKeyStoreAndTrustStoreConfigurationProperties value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Set<ConstraintViolation<KeyAndKeyStoreAndTrustStoreConfigurationProperties>> path = validator.validateProperty(value, "key");
        path.addAll(validator.validateProperty(value, "keyStore"));
        if (!path.isEmpty()) {
            return false;
        }

        context.disableDefaultConstraintViolation();

        return HelperMethods.checkKeyIsLoadable(context, value.getKeyStore(), value.getKey());

    }
}

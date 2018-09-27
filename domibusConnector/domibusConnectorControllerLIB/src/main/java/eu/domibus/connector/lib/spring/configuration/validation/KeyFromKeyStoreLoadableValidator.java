package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;

import javax.validation.*;
import java.util.Set;

public class KeyFromKeyStoreLoadableValidator  implements ConstraintValidator<CheckKeyIsLoadableFromKeyStore, KeyAndKeyStoreConfigurationProperties> {

    private Validator validator;

    @Override
    public void initialize(CheckKeyIsLoadableFromKeyStore constraintAnnotation) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public boolean isValid(KeyAndKeyStoreConfigurationProperties value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> path = validator.validateProperty(value, "key");
        path.addAll(validator.validateProperty(value, "store"));
        if (!path.isEmpty()) {
            return false;
        }

        context.disableDefaultConstraintViolation();


        return HelperMethods.checkKeyIsLoadable(context, value.getStore(), value.getKey());
    }





}

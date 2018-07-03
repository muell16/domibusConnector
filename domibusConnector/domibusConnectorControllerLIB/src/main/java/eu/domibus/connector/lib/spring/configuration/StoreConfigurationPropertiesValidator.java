package eu.domibus.connector.lib.spring.configuration;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class StoreConfigurationPropertiesValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return StoreConfigurationProperties.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StoreConfigurationProperties validate = (StoreConfigurationProperties) target;
//        ValidationUtils.rejectIfEmpty(errors, validate.getPath(), "");
    }
}

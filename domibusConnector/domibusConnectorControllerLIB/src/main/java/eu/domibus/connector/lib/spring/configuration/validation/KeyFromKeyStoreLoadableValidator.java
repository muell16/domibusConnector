package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.core.io.Resource;

import javax.validation.*;
import java.security.*;
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


        String alias = value.getKey().getAlias();
        String password = value.getKey().getPassword();
        char[] passwordArray = password.toCharArray();
        KeyStore keyStore = value.getStore().loadKeyStore();

        try {
            Key key = keyStore.getKey(alias, passwordArray);
            if (key != null) {
                return true;
            } else {
                String error = String.format("key alias [%s] does not exist in key store!", alias);
                context.buildConstraintViolationWithTemplate(error).addPropertyNode("alias").addConstraintViolation();
                return false;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            String error = String.format("key with alias [%s] could not recovered! KeyStoreException!", alias, password);
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            String error = String.format("key with alias [%s] could not recovered! No such algorithm exception", alias, password);
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        } catch (UnrecoverableKeyException e) {
            String error = String.format("key with alias [%s] could not recovered! Check if the password [%s] is correct", alias, password);
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }

        return false;
    }



}

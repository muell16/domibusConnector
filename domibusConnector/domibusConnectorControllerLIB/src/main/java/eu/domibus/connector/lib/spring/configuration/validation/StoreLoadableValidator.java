package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;

import javax.validation.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Set;

public class StoreLoadableValidator implements ConstraintValidator<CheckStoreIsLoadable, StoreConfigurationProperties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreLoadableValidator.class);

    private Validator validator;

    @Override
    public void initialize(CheckStoreIsLoadable constraintAnnotation) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public boolean isValid(StoreConfigurationProperties value, ConstraintValidatorContext context) {
        try {
            if (value == null) {
                return true;
            }
            Set<ConstraintViolation<StoreConfigurationProperties>> path = validator.validateProperty(value, "path");
            if (!path.isEmpty()) {
                return false;
            }
            try {
                value.loadKeyStore();
            } catch (StoreConfigurationProperties.CannotLoadKeyStoreException exception) {
                //TODO: nice message!
                context.buildConstraintViolationWithTemplate(exception.getCause().getMessage()).addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("exception occured", e);
            //throw new RuntimeException(e);
//            context.buildConstraintViolationWithTemplate(e.getCause().getMessage()).addConstraintViolation();
            return false;
        }


//        private KeyStore loadKeyStore() {
//            validatePathReadable();
//            char[] pwdArray = password.toCharArray();
//            try (InputStream inputStream = getPath().getInputStream()) {
//                KeyStore keyStore = KeyStore.getInstance("JKS");
//                keyStore.load(inputStream, pwdArray);
//                return keyStore;
//            } catch (KeyStoreException e) {
//                throw new StoreConfigurationProperties.ValidationException("KeyStoreException occured during open keyStore", e);
//            } catch (IOException e) {
//                throw new StoreConfigurationProperties.ValidationException("IOException occured during open", e);
//            } catch (CertificateException | NoSuchAlgorithmException e) {
//                throw new StoreConfigurationProperties.ValidationException("Exception occured during open keyStore", e);
//            }
//        }



        return true;
    }
}

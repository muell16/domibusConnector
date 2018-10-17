package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;

import javax.validation.ConstraintValidatorContext;
import java.security.*;

public class HelperMethods {

    public static boolean checkKeyIsLoadable(ConstraintValidatorContext context, StoreConfigurationProperties storeConfig, KeyConfigurationProperties keyConfig) {
        String alias = keyConfig.getAlias();
        String password = keyConfig.getPassword();
        char[] passwordArray = password.toCharArray();

        KeyStore keyStore;
        try {
            keyStore = storeConfig.loadKeyStore();
        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation(); //TODO: add PropertyNode
            return false;
        }

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

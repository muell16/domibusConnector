package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreAndTrustStoreConfigurationProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static eu.domibus.connector.lib.spring.configuration.validation.ConstraintViolationSetHelper.printSet;
import static org.assertj.core.api.Assertions.assertThat;

public class KeyFromKeyAndTrustStoreLoadableTest {

    private static Validator validator;

    private KeyAndKeyStoreAndTrustStoreConfigurationProperties props;

    @BeforeAll
    public static void beforeClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        props = new KeyAndKeyStoreAndTrustStoreConfigurationProperties();

        props.setKeyStore(ConstraintViolationSetHelper.generateTestStore());
        props.setPrivateKey(ConstraintViolationSetHelper.generateTestKeyConfig());
        props.setTrustStore(ConstraintViolationSetHelper.generateTestStore());

    }


    @Test
    public void isValid() {
        Set<ConstraintViolation<KeyAndKeyStoreAndTrustStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(0);
    }

    @Test
    public void aliasNotNotReadable() {

        props.getPrivateKey().setAlias("WRONG_ALIAS");

        Set<ConstraintViolation<KeyAndKeyStoreAndTrustStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }

    @Test
    public void aliasNotNotSet() {

        props.getPrivateKey().setAlias(null);

        Set<ConstraintViolation<KeyAndKeyStoreAndTrustStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }


}
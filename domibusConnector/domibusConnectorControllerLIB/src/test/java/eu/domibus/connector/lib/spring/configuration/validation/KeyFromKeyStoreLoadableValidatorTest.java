package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static eu.domibus.connector.lib.spring.configuration.validation.ConstraintViolationSetHelper.printSet;
import static org.assertj.core.api.Assertions.assertThat;


public class KeyFromKeyStoreLoadableValidatorTest {

    private static Validator validator;

    private KeyAndKeyStoreConfigurationProperties props;

    @BeforeClass
    public static void beforeClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Before
    public void setUp() {
        props = new KeyAndKeyStoreConfigurationProperties();
        props.setStore(ConstraintViolationSetHelper.generateTestStore());
        props.setKey(ConstraintViolationSetHelper.generateTestKeyConfig());
    }


    @Test
    public void isValid() throws Exception {
        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(0);
    }

    @Test
    public void isValid_wrongStorePath_shouldNotValidate() {
        props.getStore().setPath(new ClassPathResource("/does/not/exist"));

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }


    @Test
    public void isValid_wrongKeyAlias_shouldNotValidate() {
        props.getKey().setAlias("WRONG_ALIAS");

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }

    @Test
    public void isValid_wrongKeyPassword_shouldNotValidate() {
        props.getKey().setPassword("WRONG_PASSWORD");

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }


    @Test
    public void isValid_keyInformationIsNull() {
        props.setKey(null);

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }

    @Test
    public void isValid_keyStoreInformationIsNull() {
        props.setStore(null);

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate = validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }



}
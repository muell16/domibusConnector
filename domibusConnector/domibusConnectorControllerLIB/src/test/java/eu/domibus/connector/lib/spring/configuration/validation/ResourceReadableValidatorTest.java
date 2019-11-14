package eu.domibus.connector.lib.spring.configuration.validation;




import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceReadableValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testResourceIsNull_shouldNotValidate() {
        TestEntity t = new TestEntity();

        Set<ConstraintViolation<TestEntity>> validate = validator.validate(t);

        validate.stream().forEach(
                a -> System.out.println(a.getMessage())
        );
        assertThat(validate).hasSize(1);

    }

    @Test
    public void testResourceConfiguredPathDoesNotExist_shouldNotValidate() {
        Resource res = new FileSystemResource("/dhjafjkljadflkjdaskldfaskjhdfs");
        TestEntity t = new TestEntity();
        t.setResource(res);

        Set<ConstraintViolation<TestEntity>> validate = validator.validate(t);

        validate.stream().forEach(
                a -> System.out.println(a.getMessage())
        );
        assertThat(validate).hasSize(1);

    }

    public static class TestEntity {

        @CheckResourceIsReadable
        Resource resource;

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }
    }




}
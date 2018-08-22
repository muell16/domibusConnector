package eu.domibus.connector.lib.spring.configuration.validation;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class FolderWriteableValidatorTest {


    private static Validator validator;

    @BeforeClass
    public static void beforeClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testDirectoryExists() {
        FilePathTestClass pathTestClass = new FilePathTestClass(new File("./" + UUID.randomUUID()));
        Set<ConstraintViolation<FilePathTestClass>> validate = validator.validate(pathTestClass);

        assertThat(validate).hasSize(2);
    }

    private static class FilePathTestClass {

        @CheckFolderWriteable
        private File filePath;

        public FilePathTestClass(File f) {
            this.filePath = f;
        }

        public File getFilePath() {
            return filePath;
        }

        public void setFilePath(File filePath) {
            this.filePath = filePath;
        }
    }


}
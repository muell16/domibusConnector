package eu.domibus.connector.lib.spring.configuration.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

public class FolderWriteableValidator implements ConstraintValidator<CheckFolderWriteable, File> {

    @Override
    public boolean isValid(File file, ConstraintValidatorContext context) {
        if (file == null) {
            return true;
        }
        if (!file.exists()) {
            String message = String.format("Provided file path [%s] does not exist! Check if the path is correct and exists!", file.getAbsolutePath());
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        if (!file.isDirectory()) {
            String message = String.format("Provided file path [%s] is not a directory! Check the path!", file.getAbsolutePath());
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        if (!file.canWrite()) {
            String message = String.format("Cannot write to provided path [%s]!", file.getAbsolutePath());
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}

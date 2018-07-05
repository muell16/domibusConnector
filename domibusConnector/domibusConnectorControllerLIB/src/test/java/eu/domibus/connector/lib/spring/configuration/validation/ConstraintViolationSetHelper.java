package eu.domibus.connector.lib.spring.configuration.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ConstraintViolationSetHelper {

    public static <T> void printSet(Set<ConstraintViolation<T>> constraintViolationSet) {
        constraintViolationSet.stream().forEach(c -> System.out.println("propertyPath: " + c.getPropertyPath() + " msg: " + c.getMessage()));
    }

}

package eu.domibus.connector.lib.spring.configuration.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE, FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = {KeyFromKeyStoreLoadableValidator.class, })
@Documented
@NotNull
public @interface CheckKeyIsLoadableFromKeyStore {

    String message() default "Cannot load key from configured key store!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

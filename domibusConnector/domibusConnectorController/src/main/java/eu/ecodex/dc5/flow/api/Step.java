package eu.ecodex.dc5.flow.api;

import org.springframework.transaction.TransactionDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Step {

    public String name();

    public int txPropagation() default TransactionDefinition.PROPAGATION_MANDATORY;

}

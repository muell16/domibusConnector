package eu.domibus.connector.common.annotations;



import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@BusinessDomainScoped
public @interface BusinessDomainConfigurationProperties {

}

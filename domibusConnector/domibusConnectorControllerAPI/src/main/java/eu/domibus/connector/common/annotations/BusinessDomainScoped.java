package eu.domibus.connector.common.annotations;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Scope(BusinessDomainScoped.DC_BUSINESS_DOMAIN_SCOPE_NAME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessDomainScoped {

    public static final String DC_BUSINESS_DOMAIN_SCOPE_NAME = "dcBusinessDomain";

}

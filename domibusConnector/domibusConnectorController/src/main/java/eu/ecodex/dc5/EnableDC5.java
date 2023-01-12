package eu.ecodex.dc5;

import eu.domibus.connector.common.service.BusinessScopedConfigurationPropertiesRegistrar;
import eu.ecodex.dc5.domain.scope.BusinessDomainScopeConfiguration;
import org.moduliths.Modulithic;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@Import(DC5CoreJpaConfiguration.class)
//@ImportAutoConfiguration()
@EntityScan
@EnableTransactionManagement(order = 200)
@EnableAspectJAutoProxy
@Modulithic( //
        sharedModules = { //
                "eu.ecodex.dc5.message", //
                "eu.ecodex.dc5.core", //
                "eu.ecodex.dc5.flow", //
                "eu.ecodex.dc5.events", //
                "eu.ecodex.dc5.process", //
                "eu.ecodex.dc5.domain",
                "eu.domibus.connector.common"
                }, //
        additionalPackages = {"eu.ecodex.dc5", "eu.domibus.connector.common"}, //
        useFullyQualifiedModuleNames = true)
@SpringBootApplication(scanBasePackages = {"eu.ecodex.dc5",
        "eu.domibus.connector.common",
        "eu.domibus.connector.firststartup",
        "eu.domibus.connector.controller.routing",
        "eu.domibus.connector.evidences",
        "eu.domibus.connector.controller.spring",
        "eu.domibus.connector.link",
        "eu.domibus.connector.lib",
        "eu.domibus.connector.persistence.service",
        "eu.domibus.connector.controller.transport",
        "eu.domibus.connector.domain.transformer",
        "eu.domibus.connector.common.service",
        "eu.domibus.connector.security",
        "eu.domibus.connector.dss",
        "eu.domibus.connector.controller.processor",
        "eu.domibus.connector.controller.service",
}, exclude = {
        UserDetailsServiceAutoConfiguration.class,
})
@Import({BusinessScopedConfigurationPropertiesRegistrar.class, BusinessDomainScopeConfiguration.class})
public @interface EnableDC5 {


    @AliasFor(annotation = Modulithic.class, attribute = "systemName")
    String value() default "";

}



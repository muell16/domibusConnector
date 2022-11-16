package eu.ecodex.dc5;

import eu.ecodex.dc5.core.DC5JpaConfiguration;
import org.moduliths.Modulithic;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DC5JpaConfiguration.class)
//@ImportAutoConfiguration()
@EntityScan
@EnableTransactionManagement(order = 200)
@Modulithic( //
        sharedModules = { //
                "eu.ecodex.dc5.message", //
                "eu.ecodex.dc5.core", //
                "eu.ecodex.dc5.flow", //
                "eu.ecodex.dc5.events", //
                "eu.ecodex.dc5.process"
                }, //
        additionalPackages = "eu.ecodex.dc5", //
        useFullyQualifiedModuleNames = true)
@SpringBootApplication
public @interface EnableDC5 {


    @AliasFor(annotation = Modulithic.class, attribute = "systemName")
    String value() default "";

}



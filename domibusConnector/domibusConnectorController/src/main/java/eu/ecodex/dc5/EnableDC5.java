package eu.ecodex.dc5;

import eu.ecodex.dc5.core.DC5JpaConfiguration;
import org.moduliths.Modulithic;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DC5JpaConfiguration.class)
//@ImportAutoConfiguration()
@EntityScan
@Modulithic( //
        sharedModules = { //
                "eu.ecodex.dc.core", //
                "eu.ecodex.dc5.flow", //
                }, //
        additionalPackages = "eu.ecodex.dc", //
        useFullyQualifiedModuleNames = true)
@SpringBootApplication
public @interface EnableDC5 {


    @AliasFor(annotation = Modulithic.class, attribute = "systemName")
    String value() default "";

}



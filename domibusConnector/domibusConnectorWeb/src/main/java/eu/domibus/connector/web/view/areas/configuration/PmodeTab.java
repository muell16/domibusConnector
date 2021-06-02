package eu.domibus.connector.web.view.areas.configuration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@UIScope
@SpringComponent
@Qualifier(PmodeTab.PMODE_TAB)
public @interface PmodeTab {

    public static String PMODE_TAB = "PmodeTab";

    String title();

}

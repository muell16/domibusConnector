package eu.domibus.connector.web.view.areas.configuration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@UIScope
@SpringComponent
public @interface TabMetadata {
    String title();
    String tabGroup();
}

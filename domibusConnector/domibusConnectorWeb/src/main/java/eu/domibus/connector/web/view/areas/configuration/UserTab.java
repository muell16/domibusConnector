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
@Qualifier(UserTab.USER_TAB)
public @interface UserTab {

    public static String USER_TAB = "UserTab";

    String title();

}

package eu.domibus.connector.web.view.areas.users;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationOverviewView;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = UserOverview.ROUTE, layout = UserLayout.class)
@RoleRequired(role = "ADMIN")
public class UserOverview extends VerticalLayout {

    public static final String ROUTE = "";

    public UserOverview() {
        Label l = new Label("User");
        this.add(l);
    }
}

package eu.domibus.connector.web.view.areas.configuration;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.utils.RoleRequired;

import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.environment.EnvironmentConfiguration;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = ConfigurationOverviewView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class ConfigurationOverviewView extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "";

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getUI().navigate(EnvironmentConfiguration.ROUTE);
    }

//    public ConfigurationOverviewView() {
//        Label label = new Label();
//        label.setText("Configuration area, use the tabs above to choose the configuration topic");
//        add(label);
//    }

}

package eu.domibus.connector.ui.view.areas.testing;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.ui.view.areas.pmodes.Import;

@UIScope
@Component
@Route(value = ConnectorTestsOverview.ROUTE, layout = ConnectorTestsLayout.class)
public class ConnectorTestsOverview extends VerticalLayout implements BeforeEnterObserver{

    public static final String ROUTE = "";

    // Always redirect to Import
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.forwardTo(ConnectorTestMessageList.class);
    }

}

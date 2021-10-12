package eu.domibus.connector.ui.view.areas.monitoring;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = JmsMonitoringView.ROUTE, layout = MonitoringLayout.class)
@Order(1)
@TabMetadata(title = "JMS DLQ", tabGroup = MonitoringLayout.TAB_GROUP_NAME)
public class JmsMonitoringView extends VerticalLayout implements AfterNavigationObserver {
    public static final String ROUTE = "jms";

    public JmsMonitoringView() {
        final Div div = new Div(new Text("Hello World!"));
        add(div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}

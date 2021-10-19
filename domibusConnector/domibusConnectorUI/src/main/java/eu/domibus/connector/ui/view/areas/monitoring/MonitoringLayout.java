package eu.domibus.connector.ui.view.areas.monitoring;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.utils.DCTabHandler;
import eu.domibus.connector.ui.view.MainLayout;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@UIScope
@Component
@RoutePrefix(MonitoringLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class MonitoringLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    public static final String ROUTE_PREFIX = "manage";
    public static final String TAB_GROUP_NAME = "Manage";

    private DCTabHandler dcTabHandler = new DCTabHandler();

    public final ApplicationContext applicationContext;

    public MonitoringLayout(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
        dcTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);
        add(dcTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        dcTabHandler.beforeEnter(event);
    }
}

package eu.domibus.connector.ui.view.areas.users;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.ui.utils.DCTabHandler;
import eu.domibus.connector.ui.view.MainLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@UIScope
@org.springframework.stereotype.Component
@RoutePrefix(UserLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class UserLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    protected final static Logger LOGGER = LoggerFactory.getLogger(UserLayout.class);

    public static final String ROUTE_PREFIX = "user";

    public static final String TAB_GROUP_NAME = "User";

    private DCTabHandler DCTabHandler = new DCTabHandler();

    ApplicationContext applicationContext;

    public UserLayout(ApplicationContext applicationContext )
    {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
        DCTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);

        add(DCTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        DCTabHandler.beforeEnter(event);
    }
}

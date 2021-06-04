package eu.domibus.connector.web.view.areas.users;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.utils.TabKraken;
import eu.domibus.connector.web.view.MainLayout;
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

    private TabKraken tabKraken = new TabKraken();

    ApplicationContext applicationContext;

    public UserLayout(ApplicationContext applicationContext )
    {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
        tabKraken.createTabs(applicationContext, TAB_GROUP_NAME);

        add(tabKraken.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabKraken.beforeEnter(event);
    }
}

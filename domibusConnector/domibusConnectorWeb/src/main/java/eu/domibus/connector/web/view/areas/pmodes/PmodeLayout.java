package eu.domibus.connector.web.view.areas.pmodes;

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
@RoutePrefix(PmodeLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class PmodeLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    protected final static Logger LOGGER = LoggerFactory.getLogger(PmodeLayout.class);

    public static final String ROUTE_PREFIX = "pmode";
    public static final String TAB_GROUP_NAME = "Pmode";

    private TabKraken tabKraken = new TabKraken();

    private ApplicationContext applicationContext;

    public PmodeLayout(ApplicationContext applicationContext )
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
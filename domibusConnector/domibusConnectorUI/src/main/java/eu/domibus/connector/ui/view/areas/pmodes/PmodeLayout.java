package eu.domibus.connector.ui.view.areas.pmodes;

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
@RoutePrefix(PmodeLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class PmodeLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    protected final static Logger LOGGER = LoggerFactory.getLogger(PmodeLayout.class);

    public static final String ROUTE_PREFIX = "pmode";
    public static final String TAB_GROUP_NAME = "Pmode";

    private DCTabHandler DCTabHandler = new DCTabHandler();

    private ApplicationContext applicationContext;

    public PmodeLayout(ApplicationContext applicationContext )
    {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
    	
    	setSizeFull();
    	
        DCTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);
        add(DCTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        DCTabHandler.beforeEnter(event);
    }
}
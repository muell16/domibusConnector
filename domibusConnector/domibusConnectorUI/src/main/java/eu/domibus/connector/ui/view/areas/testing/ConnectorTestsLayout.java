package eu.domibus.connector.ui.view.areas.testing;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.ui.utils.DCTabHandler;
import eu.domibus.connector.ui.view.MainLayout;
import eu.domibus.connector.ui.view.areas.pmodes.PmodeLayout;

@UIScope
@org.springframework.stereotype.Component
@RoutePrefix(ConnectorTestsLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class ConnectorTestsLayout  extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

	protected final static Logger LOGGER = LoggerFactory.getLogger(PmodeLayout.class);

    public static final String ROUTE_PREFIX = "c2ctests";
    public static final String TAB_GROUP_NAME = "ConnectorTests";

    private DCTabHandler DCTabHandler = new DCTabHandler();

    private ApplicationContext applicationContext;
    
	public ConnectorTestsLayout(ApplicationContext applicationContext ) {
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

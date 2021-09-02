package eu.domibus.connector.ui.view.areas.messages;

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
@RoutePrefix(MessageLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class MessageLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    protected final static Logger LOGGER = LoggerFactory.getLogger(MessageLayout.class);

    public static final String ROUTE_PREFIX = "message";
    public static final String TAB_GROUP_NAME = "Message";

    private final DCTabHandler DCTabHandler = new DCTabHandler();

    private final ApplicationContext applicationContext;

    public MessageLayout(ApplicationContext applicationContext )
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

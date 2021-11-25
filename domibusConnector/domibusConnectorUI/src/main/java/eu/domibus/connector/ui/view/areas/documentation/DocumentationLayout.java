package eu.domibus.connector.ui.view.areas.documentation;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.utils.DCTabHandler;
import eu.domibus.connector.ui.view.MainLayout;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@UIScope
@org.springframework.stereotype.Component
@RoutePrefix(DocumentationLayout.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class DocumentationLayout extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    public static final String ROUTE_PREFIX = "documentation";
    public static final String TAB_GROUP_NAME = "Documentation";

    private eu.domibus.connector.ui.utils.DCTabHandler DCTabHandler = new DCTabHandler();

    ApplicationContext applicationContext;

    public DocumentationLayout(ApplicationContext applicationContext )
    {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
//        setAlignItems(Alignment.CENTER);
//        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        DCTabHandler.createTabs(applicationContext, TAB_GROUP_NAME);
        add(DCTabHandler.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        DCTabHandler.beforeEnter(event);
    }
}

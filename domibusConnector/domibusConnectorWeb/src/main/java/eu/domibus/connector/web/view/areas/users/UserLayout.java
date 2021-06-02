package eu.domibus.connector.web.view.areas.users;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;
import eu.domibus.connector.web.utils.TabKraken;
import eu.domibus.connector.web.view.MainLayout;
import eu.domibus.connector.web.view.areas.configuration.UserTab;
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

    private TabKraken tabKraken = new TabKraken();

    DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;
    ApplicationContext applicationContext;

    public UserLayout(DomibusConnectorPropertiesPersistenceService propertiesPersistenceService,
                      ApplicationContext applicationContext )
    {
        this.propertiesPersistenceService = propertiesPersistenceService;
        this.applicationContext = applicationContext;


    }

    @PostConstruct
    void init() {
        applicationContext.getBeansWithAnnotation(UserTab.class)
                .forEach((key, value) -> {
                    Component component = (Component) value;
                    UserTab annotation = component.getClass().getAnnotation(UserTab.class);
                    LOGGER.debug("Adding configuration tab [{}] with title [{}]", component, annotation.title());
                    tabKraken.createTab()
                            .withLabel(annotation.title())
                            .addForComponent(component.getClass());
                });

        add(tabKraken.getTabs());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabKraken.beforeEnter(event);
    }
}

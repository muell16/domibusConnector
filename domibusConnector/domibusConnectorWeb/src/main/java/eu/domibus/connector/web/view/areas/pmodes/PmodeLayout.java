package eu.domibus.connector.web.view.areas.pmodes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;
import eu.domibus.connector.web.utils.TabKraken;
import eu.domibus.connector.web.view.MainLayout;
import eu.domibus.connector.web.view.areas.configuration.PmodeTab;
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

    private TabKraken tabKraken = new TabKraken();

    DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;
    ApplicationContext applicationContext;

    public PmodeLayout(DomibusConnectorPropertiesPersistenceService propertiesPersistenceService,
                       ApplicationContext applicationContext )
    {
        this.propertiesPersistenceService = propertiesPersistenceService;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void init() {
        applicationContext.getBeansWithAnnotation(PmodeTab.class)
                .forEach((key, value) -> {
                    Component component = (Component) value;
                    PmodeTab annotation = component.getClass().getAnnotation(PmodeTab.class);
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
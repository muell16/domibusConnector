package eu.domibus.connector.ui.view.areas.configuration.security;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.security.configuration.DCEcodexContainerProperties;
import eu.domibus.connector.ui.utils.binder.SpringBeanValidationBinderFactory;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationPanelFactory;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;

import eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig.ImportBusinessDocConfig;
import eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig.ImportEcodexContainerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@UIScope
@Route(value = EcxContainerConfigPanel.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@TabMetadata(title = "ECodex Container Configuration", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Order(6)
public class EcxContainerConfigPanel extends VerticalLayout {

    public static final String ROUTE = "ecxContainer";
    
    public static final String TITLE = "ECodex Container Configuration";
	public static final String HELP_ID = "ui/configuration/ecodex_container_configuration.html";

    public EcxContainerConfigPanel(ConfigurationPanelFactory configurationPanelFactory,
                                   ObjectProvider<ImportEcodexContainerConfig> importEcodexContainerConfig,
                                   EcxContainerConfigForm form) {
        ConfigurationPanelFactory.ConfigurationPanel<DCEcodexContainerProperties> configurationPanel
                = configurationPanelFactory.createConfigurationPanel(form, DCEcodexContainerProperties.class);

        Button b = new Button("Import old config");
        b.addClickListener(event -> {
            importEcodexContainerConfig.getIfAvailable().open();
        });
        this.add(b);
        this.add(configurationPanel);

    }

}

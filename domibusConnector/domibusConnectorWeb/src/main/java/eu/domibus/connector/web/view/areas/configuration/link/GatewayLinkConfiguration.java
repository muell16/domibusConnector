package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.persistence.service.DCLinkPersistenceService;
import eu.domibus.connector.web.utils.RoleRequired;

import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationTab;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@UIScope
@ConfigurationTab(title = "Gateway Configuration")
@Route(value = GatewayLinkConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
//@Profile(DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME)
public class GatewayLinkConfiguration extends LinkConfiguration {

    public static final String ROUTE = "gwlink";

    public GatewayLinkConfiguration(DCLinkFacade dcLinkFacade, ApplicationContext applicationContext) {
        super(dcLinkFacade, applicationContext, LinkType.GATEWAY);
    }


}

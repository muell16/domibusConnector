package eu.domibus.connector.web.areas.configuration.link;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.link.service.DCLinkPluginConfiguration;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.areas.configuration.ConfigurationTab;
import eu.domibus.connector.web.areas.configuration.ConfigurationLayout;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@UIScope
@ConfigurationTab(title = "Gateway Configuration")
@Route(value = GatewayLinkConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@Profile(DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME)
public class GatewayLinkConfiguration extends LinkConfiguration {

    public static final String ROUTE = "gateway";

    @Override
    protected LinkType getLinkType() {
        return LinkType.GATEWAY;
    }


}

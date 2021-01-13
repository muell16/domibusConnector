package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.web.utils.RoleRequired;

import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationTab;
import org.springframework.stereotype.Component;

@Component
@UIScope
@ConfigurationTab(title = "Backend Configuration")
@Route(value = BackendLinkConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
//@Profile(DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME)
public class BackendLinkConfiguration extends LinkConfiguration {

    public static final String ROUTE = "backendlink";

    @Override
    protected LinkType getLinkType() {
        return LinkType.BACKEND;
    }


}

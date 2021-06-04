package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@UIScope
@TabMetadata(title = "Backend Configuration", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Route(value = BackendLinkConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
//@Profile(DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME)
public class BackendLinkConfiguration extends LinkConfiguration {

    public static final String ROUTE = "backendlink";

    public BackendLinkConfiguration(DCLinkFacade dcLinkFacade, ApplicationContext applicationContext) {
        super(dcLinkFacade, applicationContext, LinkType.BACKEND);
    }

}

package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationTab;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationLayout;
import org.springframework.stereotype.Component;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
@ConfigurationTab(getTabTitle = "Gateway Configuration")
@Route(value = GatewayLinkConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class GatewayLinkConfiguration extends LinkConfiguration {

    public static final String ROUTE = "gateway";

    @Override
    protected LinkType getLinkType() {
        return LinkType.GATEWAY;
    }


}

package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationTab;
import org.springframework.stereotype.Component;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
@ConfigurationTab(getTabTitle = "Backend Configuration")
@Route(value = BackendLinkConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class BackendLinkConfiguration extends LinkConfiguration {

    public static final String ROUTE = "backend";

    @Override
    protected LinkType getLinkType() {
        return LinkType.BACKEND;
    }


}

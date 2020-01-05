package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
@ConfigurationTab
@Route("configuration/gateway")
public class GatewayLinkConfiguration extends LinkConfiguration {


    @Override
    protected LinkType getLinkType() {
        return LinkType.GATEWAY;
    }


}

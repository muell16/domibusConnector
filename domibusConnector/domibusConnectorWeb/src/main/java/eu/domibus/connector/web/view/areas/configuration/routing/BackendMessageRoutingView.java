package eu.domibus.connector.web.view.areas.configuration.routing;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.controller.routing.DCMessageRoutingConfigurationProperties;
import eu.domibus.connector.controller.routing.DCRoutingConfigurationManager;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@UIScope
@TabMetadata(title = "Backend Message Routing", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Route(value = BackendMessageRoutingView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class BackendMessageRoutingView extends VerticalLayout {

    public static final String ROUTE = "backendrouting";

    private final DCRoutingConfigurationManager dcRoutingConfigurationManager;

    private Grid<DCMessageRoutingConfigurationProperties.RoutingRule> routingRuleGrid;

    public BackendMessageRoutingView(DCRoutingConfigurationManager dcRoutingConfigurationManager) {
        this.dcRoutingConfigurationManager = dcRoutingConfigurationManager;
        initUI();
    }

    private void initUI() {
        Label l = new Label("Backend Routing Config: Read only!");
        add(l);

        Label routingDescription = new Label("Routing Priorities:\n1.) refToMessageId\n2.) ConversationId\n3.) RoutingRule\n4.) Default Backend");
        add(routingDescription);

        TextField defaultBackendNameTextField = new TextField();
        defaultBackendNameTextField.setReadOnly(true);
        defaultBackendNameTextField.setLabel("DefaultBackend");
        defaultBackendNameTextField.setValue(dcRoutingConfigurationManager.getDefaultBackendName());

        add(defaultBackendNameTextField);

        routingRuleGrid = new Grid<>();
        routingRuleGrid.addColumn(DCMessageRoutingConfigurationProperties.RoutingRule::getLinkName).setHeader("Backend Name");
        routingRuleGrid.addColumn(rule -> rule.getMatchClause().getMatchRule()).setHeader("matching string");
        routingRuleGrid.addColumn(rule -> rule.getMatchClause().getExpression()).setHeader("matching expression");

        List<DCMessageRoutingConfigurationProperties.RoutingRule> backendRoutingRules = dcRoutingConfigurationManager.getBackendRoutingRules();
        routingRuleGrid.setItems(backendRoutingRules);

        this.add(routingRuleGrid);
    }

    //TODO: for validation purpose check DCLinkFacade if backendName is a configured backend

}

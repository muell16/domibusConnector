package eu.domibus.connector.web.view.areas.configuration.routing;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.controller.routing.DCRoutingRulesManagerImpl;
import eu.domibus.connector.controller.routing.RoutingRule;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@UIScope
@TabMetadata(title = "Backend Message Routing", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Route(value = BackendMessageRoutingView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class BackendMessageRoutingView extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "backendrouting";

    private final DCRoutingRulesManagerImpl dcRoutingRulesManagerImpl;
    private final DCLinkFacade dcLinkFacade;

    private Grid<RoutingRule> routingRuleGrid;

    public BackendMessageRoutingView(DCRoutingRulesManagerImpl dcRoutingRulesManagerImpl, DCLinkFacade dcLinkFacade) {
        this.dcRoutingRulesManagerImpl = dcRoutingRulesManagerImpl;
        this.dcLinkFacade = dcLinkFacade;
        initUI();
    }

    private void initUI() {
        Label l = new Label("Here is the configuration where routing rules are configured that define how messages are routed to backend(s).");
        add(l);

        LumoLabel routingDescription = new LumoLabel();
        routingDescription.setText("General routing priorities:");
        routingDescription.getStyle().set("font-size", "20px");
		
        routingDescription.getStyle().set("font-style", "italic");
        add(routingDescription);

        Accordion routingPriorities = new Accordion();
        
        routingPriorities.add("1. refToMessageId", new LumoLabel("If the message contains a refToMessageId then the backend where the original message was sent from is chosen."));
        routingPriorities.add("2. conversationId", new LumoLabel("If the message is part of a conversation the backend where prior messages of the conversation was sent from is chosen."));
        routingPriorities.add("3. routing Rules", new LumoLabel("This is the part configured on this page. \\nIf there is a rule that applies to the message, the backend configured within the rule is chosen."));
        routingPriorities.add("4. default Backend", new LumoLabel("If none of the above is applicable, the default backend is chosen."));
        
        add(routingPriorities);
        
        TextField defaultBackendNameTextField = new TextField();
        defaultBackendNameTextField.setReadOnly(true);
        defaultBackendNameTextField.setLabel("Configured default backend name");
        defaultBackendNameTextField.setValue(dcRoutingRulesManagerImpl.getDefaultBackendName(DomibusConnectorBusinessDomain.getDefaultMessageLaneId()));

        add(defaultBackendNameTextField);

        routingRuleGrid = new Grid<>();
        
        routingRuleGrid.addColumn(RoutingRule::getLinkName).setHeader("Backend Name");
        routingRuleGrid.addColumn(rule -> rule.getMatchClause().getMatchRule()).setHeader("matching string");
        routingRuleGrid.addColumn(rule -> rule.getMatchClause().getExpression()).setHeader("matching expression");
        routingRuleGrid.addComponentColumn(rule -> getDeleteRoutingRuleLink(rule)).setWidth("50px");
        
        this.add(routingRuleGrid);
        
        Button createNewRoutingRule = new Button("Create new roulting rule");
        createNewRoutingRule.addClickListener(e -> {
        	Dialog createRoutingRuleDialog = new Dialog();
        	createNewRoutingRule.setText("Create new roulting rule");
        	//TODO: Here to create a Dialog panel where the routing rule can be created via dropdown boxes:
        	// -- Box to select backend for which the rule should apply. Only backends should be selectable that do not have a rule already.
        	//    Also the connector backend must be excluded.
        	// -- Box to select first Token
        	// -- Box to select 'equals' of 'startsWith'
        	// -- Box to enter value for regex query. Maybe from PMode data.
        	// -- Box to select either 'and' or 'or'
        	// -- If 'and' or 'or' chosen repeat steps 2-4.
        	// -- Save button which validates data, saves the Rule into DB and closes Dialog panel.
//        	createRoutingRuleDialog.add(delButton);
        	createRoutingRuleDialog.open();
        });
        add(createNewRoutingRule);
        
        Button saveAllRoutingRules = new Button("Save all roulting rules");
        saveAllRoutingRules.addClickListener(e -> {
        	Dialog saveRoutingRulesDialog = new Dialog();
        	saveAllRoutingRules.setText("Save all roulting rules");
        	//TODO: Here to create a Dialog panel which warns the user that all backend rules existing are stored into database and 
        	//      those from the properties file will not apply anymore.
        	// On confirmation, save all backend rules to database and reload in context.
//        	createRoutingRuleDialog.add(delButton);
        	saveRoutingRulesDialog.open();
        });
        add(saveAllRoutingRules);
    }

	private Button getDeleteRoutingRuleLink(RoutingRule rule) {
		Button deleteRoutingRuleButton = new Button(new Icon(VaadinIcon.ERASER));
		deleteRoutingRuleButton.addClickListener(e -> {
			Dialog deleteRoutingRuleDialog = new Dialog();
			Button delButton = new Button("Delete RoutingRule");
			delButton.addClickListener(e1 -> {
				//TODO: Warn user about taking effect.
				//      On confirmation delete rule and reload rules in application context.
				deleteRoutingRuleDialog.close();
			});
			deleteRoutingRuleDialog.add(delButton);
			deleteRoutingRuleDialog.open();
			
		});
		return deleteRoutingRuleButton;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent arg0) {

		Collection<RoutingRule> backendRoutingRules = dcRoutingRulesManagerImpl.getBackendRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
		routingRuleGrid.setItems(backendRoutingRules);
		
	}

    //TODO: for validation purpose check DCLinkFacade if backendName is a configured backend

}

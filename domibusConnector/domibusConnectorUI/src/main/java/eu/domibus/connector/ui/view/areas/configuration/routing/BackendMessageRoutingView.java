package eu.domibus.connector.ui.view.areas.configuration.routing;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.routing.DCMessageRoutingConfigurationProperties;
import eu.domibus.connector.controller.routing.DCRoutingRulesManagerImpl;
import eu.domibus.connector.controller.routing.RoutingRule;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.ui.component.LumoLabel;
import eu.domibus.connector.ui.service.WebPModeService;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@UIScope
@TabMetadata(title = "Backend Message Routing", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Route(value = BackendMessageRoutingView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class BackendMessageRoutingView extends VerticalLayout implements AfterNavigationObserver {

    private static final Logger LOGGER = LogManager.getLogger(BackendMessageRoutingView.class);

    public static final String ROUTE = "backendrouting";

    private final DCRoutingRulesManagerImpl dcRoutingRulesManagerImpl;
    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
//    private final DCLinkFacade dcLinkFacade;
//    private final WebPModeService webPModeService;
    private final ObjectFactory<RoutingRuleForm> routingRuleFormObjectFactory;

    private Grid<RoutingRule> routingRuleGrid;

    private Map<String, RoutingRule> currentRoutingRules;

    public BackendMessageRoutingView(DCRoutingRulesManagerImpl dcRoutingRulesManagerImpl,
                                     ObjectFactory<RoutingRuleForm> routingRuleFormObjectFactory,
                                     ConfigurationPropertyManagerService configurationPropertyManagerService,
                                     DCLinkFacade dcLinkFacade,
                                     WebPModeService webPModeService) {
        this.routingRuleFormObjectFactory = routingRuleFormObjectFactory;
        this.dcRoutingRulesManagerImpl = dcRoutingRulesManagerImpl;
        this.configurationPropertyManagerService = configurationPropertyManagerService;
//        this.dcLinkFacade = dcLinkFacade;
//        this.webPModeService = webPModeService;
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

        routingRuleGrid = new Grid<>(RoutingRule.class);
        routingRuleGrid.addColumn(getButtonRenderer());

        
        this.add(routingRuleGrid);
        
        Button createNewRoutingRule = new Button("Create new roulting rule");
        createNewRoutingRule.addClickListener(this::createNewRoutingRuleClicked);
        add(createNewRoutingRule);

        Button saveChangesButton = new Button("Save Changes");
        saveChangesButton.addClickListener(this::saveChangesButtonClicked);
        add(saveChangesButton);

    }

    private void saveChangesButtonClicked(ClickEvent<Button> buttonClickEvent) {
        this.saveChanges();
    }

    private Renderer<RoutingRule> getButtonRenderer() {
        return new ComponentRenderer<>(
                (RoutingRule routingRule) -> {
                    HorizontalLayout layout = new HorizontalLayout();
                    //edit Button
                    Button editButton = new Button();
                    editButton.setIcon(VaadinIcon.WRENCH.create());
                    editButton.addClickListener(clickEvent -> {
                        editRoutingRule(routingRule);
                    });
                    layout.add(editButton);
                    //delete button
                    Button deleteButton = new Button();
                    deleteButton.setIcon(VaadinIcon.TRASH.create());
                    deleteButton.addClickListener(clickEvent -> {
                        deleteRoutingRule(routingRule);
                    });
                    layout.add(deleteButton);
                    return layout;
                });

    }

    private void createNewRoutingRuleClicked(ClickEvent<Button> buttonClickEvent) {
        final Dialog d = new Dialog();
        d.setModal(true);
        d.setHeight("80%");
        d.setWidth("80%");
        d.setOpened(true);
        d.setCloseOnOutsideClick(false);
        d.setCloseOnEsc(false);

        Label title = new Label("Add new Routing Rule");

        HorizontalLayout saveCancelButton = new HorizontalLayout();
        saveCancelButton.add(title);

        d.add(saveCancelButton);

        Label statusLabel = new Label();

        RoutingRuleForm rrf = this.routingRuleFormObjectFactory.getObject();
        d.add(rrf);
        d.add(statusLabel);

        RoutingRule r = new RoutingRule();

        final Binder<RoutingRule> routingRuleBinder = new Binder<>(RoutingRule.class);
        routingRuleBinder.bindInstanceFields(rrf);
        routingRuleBinder.setBean(r);
        routingRuleBinder.setStatusLabel(statusLabel);

        Button saveButton = new Button(VaadinIcon.CHECK.create());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create());

        cancelButton.addClickListener(e -> {
            //do nothing...
            d.close();
        });

        RoutingRule updatedRoutingRule = new RoutingRule();
        saveButton.addClickListener(e -> {
            //TODO: save routing rule...
//            routingRuleBinder.readBean();
            BinderValidationStatus<RoutingRule> validate = routingRuleBinder.validate();
            LOGGER.info("Validation result: [{}]", validate);
            boolean b = routingRuleBinder.writeBeanIfValid(updatedRoutingRule);
            if (b) {
                d.close();
                updateAndSaveRoutingRule(updatedRoutingRule);
            }

        });

        saveCancelButton.add(saveButton);
        saveCancelButton.add(cancelButton);

        saveChanges();
    }
    
    private void editRoutingRule(RoutingRule r) {
        final Dialog d = new Dialog();
        d.setModal(true);
        d.setHeight("80%");
        d.setWidth("80%");
        d.setOpened(true);
        d.setCloseOnOutsideClick(false);
        d.setCloseOnEsc(false);

        Label title = new Label("Edit Routing Rule");

        HorizontalLayout saveCancelButton = new HorizontalLayout();
        saveCancelButton.add(title);

        d.add(saveCancelButton);

        Label statusLabel = new Label();

        RoutingRuleForm rrf = this.routingRuleFormObjectFactory.getObject();
        d.add(rrf);
        d.add(statusLabel);

        final Binder<RoutingRule> routingRuleBinder = new Binder<>(RoutingRule.class);
        routingRuleBinder.bindInstanceFields(rrf);
        routingRuleBinder.readBean(r);
        routingRuleBinder.setStatusLabel(statusLabel);


        Button saveButton = new Button(VaadinIcon.CHECK.create());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create());

        cancelButton.addClickListener(e -> {
            //do nothing...
            d.close();
        });

        RoutingRule updatedRoutingRule = new RoutingRule();
        saveButton.addClickListener(e -> {
            //TODO: save routing rule...
//            routingRuleBinder.readBean();
            BinderValidationStatus<RoutingRule> validate = routingRuleBinder.validate();
            LOGGER.info("Validation result: [{}]", validate);
            boolean b = routingRuleBinder.writeBeanIfValid(updatedRoutingRule);
            if (b) {
                d.close();
                updateAndSaveRoutingRule(updatedRoutingRule);
            }

        });

        saveCancelButton.add(saveButton);
        saveCancelButton.add(cancelButton);

        saveChanges();
    }

    private void updateAndSaveRoutingRule(RoutingRule rr) {
        this.currentRoutingRules.remove(rr.getRoutingRuleId());
        this.currentRoutingRules.put(rr.getRoutingRuleId(), rr);
        this.routingRuleGrid.setItems(this.currentRoutingRules.values());
    }

    private void deleteRoutingRule(RoutingRule r) {
        Dialog d = new Dialog();
        d.setModal(true);
        d.setHeight("80%");
        d.setWidth("80%");
        d.setOpened(true);

        Label l = new Label("Delete Routing Rule?");
        d.add(l);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button acceptButton = new Button(VaadinIcon.CHECK.create());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create());
        d.add(horizontalLayout);
        horizontalLayout.add(acceptButton);
        horizontalLayout.add(cancelButton);

        acceptButton.addClickListener(e -> {
            currentRoutingRules.remove(r.getRoutingRuleId());
            this.routingRuleGrid.setItems(currentRoutingRules.values());
            d.close();
        });
        cancelButton.addClickListener(e -> {
            d.close();
        });

        saveChanges();
    }



	private void saveChanges() {
        DCMessageRoutingConfigurationProperties routingConfigurationProperties = configurationPropertyManagerService.loadConfiguration(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(),
                DCMessageRoutingConfigurationProperties.class);

        //update only routing rules from source environment
        Map<String, RoutingRule> newRoutingRules = currentRoutingRules.values()
                .stream()
                .filter(r -> r.getConfigurationSource() != ConfigurationSource.IMPL)
                .collect(Collectors.toMap(RoutingRule::getRoutingRuleId, Function.identity()));
        routingConfigurationProperties.setBackendRules(newRoutingRules);
        configurationPropertyManagerService.updateConfiguration(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), routingConfigurationProperties);
    }

    private void resetChanges() {
        DCMessageRoutingConfigurationProperties routingConfigurationProperties = configurationPropertyManagerService.loadConfiguration(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(),
                DCMessageRoutingConfigurationProperties.class);

    }

	@Override
	public void afterNavigation(AfterNavigationEvent arg0) {

        //returns all routing rules (configured within db AND PropertyFiles/SpringEnvironment)
//		Collection<RoutingRule> backendRoutingRules = dcRoutingRulesManagerImpl.getBackendRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
//
//        this.currentRoutingRules = backendRoutingRules
//                .stream()
//                .collect(Collectors.toMap(RoutingRule::getRoutingRuleId, Function.identity()));
//        routingRuleGrid.setItems(this.currentRoutingRules.values());
//<<<<<<< Updated upstream
////        this.currentRoutingRules = dcRoutingRulesManagerImpl.getBackendRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
////        routingRuleGrid.setItems(this.currentRoutingRules);
//
//=======
//        this.currentRoutingRules = dcRoutingRulesManagerImpl.getBackendRoutingRules(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
//        routingRuleGrid.setItems(this.currentRoutingRules.values());
//>>>>>>> Stashed changes
		
	}

    //TODO: for validation purpose check DCLinkFacade if backendName is a configured backend
    // warn when backend exists, but deactivated
    // error when backend does not exist

}

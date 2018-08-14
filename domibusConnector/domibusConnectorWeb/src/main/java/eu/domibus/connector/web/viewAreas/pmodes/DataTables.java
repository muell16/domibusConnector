package eu.domibus.connector.web.viewAreas.pmodes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.service.WebPModeService;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class DataTables extends VerticalLayout {
	
	WebPModeService pmodeService;
	
	Grid<DomibusConnectorParty> partyGrid;
	Grid<DomibusConnectorAction> actionGrid;
	Grid<DomibusConnectorService> serviceGrid;
	

	public DataTables(@Autowired WebPModeService pmodeService) {
		this.pmodeService = pmodeService;
		
		Div parties = createPartiesDiv();
		
		Div actions = createActionsDiv();
		
		Div services = createServicesDiv();
		
		VerticalLayout main = new VerticalLayout(parties, actions, services);
		main.setAlignItems(Alignment.STRETCH);
		main.setHeight("100%");
		add(main);
		setHeight("100vh");
		setWidth("100vw");
	}

	private Div createServicesDiv() {
		Div services = new Div();
		services.setWidth("100vw");
		Label servicesLabel = new Label();
		servicesLabel.setText("DOMIBUS_CONNECTOR_SERVICE:");
		services.add(servicesLabel);
		
		serviceGrid = new Grid<DomibusConnectorService>();
		
		List<DomibusConnectorService> serviceList = this.pmodeService.getServiceList();
		serviceGrid.setItems(serviceList);
		serviceGrid.addComponentColumn(domibusConnectorService -> getDeleteServiceLink(domibusConnectorService)).setWidth("40px");
		serviceGrid.addColumn(DomibusConnectorService::getService).setHeader("Service").setWidth("500px").setSortable(true).setResizable(true);
		serviceGrid.addColumn(DomibusConnectorService::getServiceType).setHeader("Service Type").setWidth("500px").setSortable(true).setResizable(true);
		serviceGrid.setItemDetailsRenderer(new ComponentRenderer<>(domibusConnectorService -> {
		    return editServiceLayout(domibusConnectorService);}));
		serviceGrid.setWidth("1140px");
		serviceGrid.setHeight("300px");
		serviceGrid.setMultiSort(true);
		
		services.add(serviceGrid);
		return services;
	}

	private Div createActionsDiv() {
		Div actions = new Div();
		actions.setWidth("100vw");
		Label actionsLabel = new Label();
		actionsLabel.setText("DOMIBUS_CONNECTOR_ACTION:");
		actions.add(actionsLabel);
		
		actionGrid = new Grid<DomibusConnectorAction>();
		
		List<DomibusConnectorAction> actionList = pmodeService.getActionList();
		actionGrid.setItems(actionList);
		actionGrid.addComponentColumn(domibusConnectorAction -> getDeleteActionLink(domibusConnectorAction)).setWidth("40px");
		actionGrid.addColumn(DomibusConnectorAction::getAction).setHeader("Action").setWidth("1000px").setSortable(true).setResizable(true);
		actionGrid.setItemDetailsRenderer(new ComponentRenderer<>(domibusConnectorAction -> {
		    return editActionLayout(domibusConnectorAction);}));
		actionGrid.setWidth("1140px");
		actionGrid.setHeight("300px");
		actionGrid.setMultiSort(true);
		
		actions.add(actionGrid);
		return actions;
	}

	private Div createPartiesDiv() {
		Div parties = new Div();
		parties.setWidth("100vw");
		Label partiesLabel = new Label();
		partiesLabel.setText("DOMIBUS_CONNECTOR_PARTY:");
		parties.add(partiesLabel);
		
		partyGrid = new Grid<DomibusConnectorParty>();
		
		List<DomibusConnectorParty> partyList = this.pmodeService.getPartyList();
		partyGrid.setItems(partyList);
		partyGrid.addComponentColumn(domibusConnectorParty -> getDeletePartyLink(domibusConnectorParty)).setWidth("40px");
		partyGrid.addColumn(DomibusConnectorParty::getPartyId).setHeader("Party ID").setWidth("250px").setSortable(true).setResizable(true);
		partyGrid.addColumn(DomibusConnectorParty::getPartyIdType).setHeader("Party ID Type").setWidth("400px").setSortable(true).setResizable(true);
		partyGrid.addColumn(DomibusConnectorParty::getRole).setHeader("Party Role").setWidth("350px").setSortable(true).setResizable(true);
		partyGrid.setItemDetailsRenderer(new ComponentRenderer<>(domibusConnectorParty -> {
		    return editPartyLayout(domibusConnectorParty);}));
		partyGrid.setWidth("1140px");
		partyGrid.setHeight("300px");
		partyGrid.setMultiSort(true);
		
		
		parties.add(partyGrid);
		return parties;
	}

	private VerticalLayout editPartyLayout(DomibusConnectorParty domibusConnectorParty) {
		VerticalLayout layout = new VerticalLayout();
		
		DomibusConnectorParty newParty = domibusConnectorParty;
		
		TextField partyId = new TextField("Party ID");
		partyId.setValue(domibusConnectorParty.getPartyId());
		partyId.addValueChangeListener(e -> newParty.setPartyId(e.getValue()));
		layout.add(partyId);
		
		TextField partyIdType = new TextField("Party ID Type");
		partyIdType.setValue(domibusConnectorParty.getPartyIdType());
		partyIdType.addValueChangeListener(e -> newParty.setPartyIdType(e.getValue()));
		layout.add(partyIdType);
		
		TextField partyRole = new TextField("Party Role");
		partyRole.setValue(domibusConnectorParty.getRole());
		partyRole.addValueChangeListener(e -> newParty.setRole(e.getValue()));
		layout.add(partyRole);
		
		Button edit = new Button(new Icon(VaadinIcon.EDIT));
		edit.getElement().setAttribute("title", "Save Party");
		edit.addClickListener(e -> {pmodeService.updateParty(domibusConnectorParty, newParty);
				reloadParties();});
		
		layout.add(edit);
		return layout;
	}
	
	private VerticalLayout editActionLayout(DomibusConnectorAction domibusConnectorAction) {
		VerticalLayout layout = new VerticalLayout();
		
		DomibusConnectorAction newAction = domibusConnectorAction;
		
		TextField action = new TextField("Action");
		action.setValue(domibusConnectorAction.getAction());
		action.addValueChangeListener(e -> newAction.setAction(e.getValue()));
		layout.add(action);
		
		
		Button edit = new Button(new Icon(VaadinIcon.EDIT));
		edit.getElement().setAttribute("title", "Save Action");
		edit.addClickListener(e -> {pmodeService.updateAction(domibusConnectorAction, newAction);
				reloadActions();});
		
		layout.add(edit);
		return layout;
	}
	
	private VerticalLayout editServiceLayout(DomibusConnectorService domibusConnectorService) {
		VerticalLayout layout = new VerticalLayout();
		
		DomibusConnectorService newService = domibusConnectorService;
		
		TextField service = new TextField("Service");
		service.setValue(domibusConnectorService.getService());
		service.addValueChangeListener(e -> newService.setService(e.getValue()));
		layout.add(service);
		
		TextField serviceType = new TextField("Service ID Type");
		serviceType.setValue(domibusConnectorService.getServiceType());
		serviceType.addValueChangeListener(e -> newService.setServiceType(e.getValue()));
		layout.add(serviceType);
		
		Button edit = new Button(new Icon(VaadinIcon.EDIT));
		edit.getElement().setAttribute("title", "Save Service");
		edit.addClickListener(e -> {pmodeService.updateService(domibusConnectorService, newService);
				reloadServices();});
		
		layout.add(edit);
		return layout;
	}
	
	private void reloadParties() {
		partyGrid.setItems(this.pmodeService.getPartyList());
		
	}
	
	private void reloadActions() {
		actionGrid.setItems(this.pmodeService.getActionList());
		
	}
	
	private void reloadServices() {
		serviceGrid.setItems(this.pmodeService.getServiceList());
		
	}
	
	private Button getDeletePartyLink(DomibusConnectorParty party) {
		Button delete = new Button(new Icon(VaadinIcon.TRASH));
		delete.getElement().setAttribute("title", "Delete Party");
		delete.addClickListener(e -> {pmodeService.deleteParty(party);
		reloadParties();});
		return delete;
	}
	
	private Button getDeleteActionLink(DomibusConnectorAction action) {
		Button delete = new Button(new Icon(VaadinIcon.TRASH));
		delete.getElement().setAttribute("title", "Delete Action");
		delete.addClickListener(e -> {pmodeService.deleteAction(action);
		reloadActions();});
		return delete;
	}
	
	private Button getDeleteServiceLink(DomibusConnectorService service) {
		Button delete = new Button(new Icon(VaadinIcon.TRASH));
		delete.getElement().setAttribute("title", "Delete Service");
		delete.addClickListener(e -> {pmodeService.deleteService(service);
		reloadServices();});
		return delete;
	}
	
	

}

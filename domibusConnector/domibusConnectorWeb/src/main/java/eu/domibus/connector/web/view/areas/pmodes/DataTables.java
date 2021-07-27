package eu.domibus.connector.web.view.areas.pmodes;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties.CannotLoadKeyStoreException;
import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.service.WebKeystoreService.CertificateInfo;
import eu.domibus.connector.web.service.WebPModeService;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationUtil;

@Component
@UIScope
@Route(value = DataTables.ROUTE, layout = PmodeLayout.class)
@TabMetadata(title = "PMode-Set Data", tabGroup = PmodeLayout.TAB_GROUP_NAME)
public class DataTables extends VerticalLayout {

	public static final String ROUTE = "pmodedata";

	WebPModeService pmodeService;

	DomibusConnectorPModeSet activePModeSet;

	Grid<DomibusConnectorParty> partyGrid;
	Grid<DomibusConnectorAction> actionGrid;
	Grid<DomibusConnectorService> serviceGrid;


	public DataTables(@Autowired WebPModeService pmodeService, @Autowired ConfigurationUtil util) {
		this.pmodeService = pmodeService;

		//CAVE: activePModeSet can be null!!
		activePModeSet = this.pmodeService.getCurrentPModeSet(DomibusConnectorBusinessDomain.getDefaultMessageLaneId()).orElse(null);

		VerticalLayout activePModeSetDiv = createActivePmodeSetDiv(util);
		
		VerticalLayout histPModeSetsDiv = createPModeHistory();

		VerticalLayout main = new VerticalLayout(activePModeSetDiv, histPModeSetsDiv);
		main.setAlignItems(Alignment.STRETCH);
		main.setHeight("100%");
		add(main);
		setHeight("100vh");
		setWidth("100vw");
	}

	private VerticalLayout createActivePmodeSetDiv(ConfigurationUtil util) {
		VerticalLayout activePModeSetDiv = new VerticalLayout();
		activePModeSetDiv.setWidth("100vw");

		LumoLabel activePModeSetLabel = createChapterText("Active PMode Set data:");
		
		activePModeSetDiv.add(activePModeSetLabel);

		LumoLabel uploadedAt = new LumoLabel("Active PMode Set uploaded at: "+activePModeSet.getCreateDate().toString());
		activePModeSetDiv.add(uploadedAt);
		
		LumoLabel downloadActivePModesButton = new LumoLabel("Download active PModes");
		final StreamResource resource = new StreamResource("activePModes-"+activePModeSet.getCreateDate().toString()+".xml",
				() -> new ByteArrayInputStream(activePModeSet.getpModes()));
		
		Anchor downloadPModesAnchor = new Anchor();
		downloadPModesAnchor.setHref(resource);
		downloadPModesAnchor.getElement().setAttribute("download", true);
		downloadPModesAnchor.setTarget("_blank");
		downloadPModesAnchor.setTitle("Download active PModes");
		downloadPModesAnchor.add(downloadActivePModesButton);
		activePModeSetDiv.add(downloadPModesAnchor);

		TextArea description = new TextArea("Description:");
		description.setValue(activePModeSet.getDescription());
		activePModeSetDiv.add(description);
		description.setRequired(true);

		Button updateDescription = new Button("Update PMode-Set description");
		updateDescription.addClickListener(e -> {
			activePModeSet.setDescription(description.getValue());
			pmodeService.updateActivePModeSetDescription(activePModeSet);

		});
		activePModeSetDiv.add(updateDescription);

		
		activePModeSetDiv.add(createServicesDiv());
		activePModeSetDiv.add(createActionsDiv());
		activePModeSetDiv.add(createPartiesDiv());
		activePModeSetDiv.add(createConnectorstoreDiv(util));

		return activePModeSetDiv;
	}
	
	private VerticalLayout createPModeHistory() {
		VerticalLayout histPModeSetDiv = new VerticalLayout();
		histPModeSetDiv.setWidth("100vw");

		LumoLabel histPModeSetLabel = createChapterText("Previous PMode Sets:");
		
		histPModeSetDiv.add(histPModeSetLabel);
		
		Grid<DomibusConnectorPModeSet> pModesGrid = new Grid<DomibusConnectorPModeSet>();

		List<DomibusConnectorPModeSet> inactivePModesList = this.pmodeService.getInactivePModeSets();
		pModesGrid.setItems(inactivePModesList);
		pModesGrid.addColumn(DomibusConnectorPModeSet::getCreateDate).setHeader("Created date").setWidth("500px").setSortable(true).setResizable(true);
		pModesGrid.addColumn(DomibusConnectorPModeSet::getDescription).setHeader("Description").setWidth("500px").setSortable(true).setResizable(true);
		pModesGrid.addComponentColumn(domibusConnectorPModeSet -> createDownloadPModesAnchor(domibusConnectorPModeSet)).setHeader("PModes").setWidth("200px").setSortable(false).setResizable(true);
		pModesGrid.setWidth("1220px");
		pModesGrid.setHeight("320px");
		pModesGrid.setMultiSort(true);

		histPModeSetDiv.add(pModesGrid);
		
		return histPModeSetDiv;
	}
	
	private Anchor createDownloadPModesAnchor(DomibusConnectorPModeSet pModeSet) {
		LumoLabel downloadPModesButton = new LumoLabel("download");
		Anchor downloadPModesAnchor = new Anchor();

		if(pModeSet.getpModes()!=null && pModeSet.getCreateDate()!=null) {
			final StreamResource resource = new StreamResource("pModes-"+pModeSet.getCreateDate().toString()+".xml",
				() -> new ByteArrayInputStream(pModeSet.getpModes()));
		
			downloadPModesAnchor.setHref(resource);
		}else {
			downloadPModesAnchor.setEnabled(false);
		}
		downloadPModesAnchor.getElement().setAttribute("download", true);
		downloadPModesAnchor.setTarget("_blank");
		downloadPModesAnchor.setTitle("Download PModes");
		downloadPModesAnchor.add(downloadPModesButton);
		
		return downloadPModesAnchor;
	}
	
	private VerticalLayout createConnectorstoreDiv(ConfigurationUtil util) {
		VerticalLayout connectorstore = new VerticalLayout();
		
		LumoLabel connectorstoreLabel = createGridTitleText("Connectorstore contents:");
		connectorstore.add(connectorstoreLabel);
		
		try {
			Grid<CertificateInfo> connectorstoreInformationGrid = util.createKeystoreInformationGrid(
					new ByteArrayInputStream(activePModeSet.getConnectorstore().getKeystoreBytes()), 
					activePModeSet.getConnectorstore().getPasswordPlain());
			
			connectorstore.add(connectorstoreInformationGrid);
		}catch(DCKeyStoreService.CannotLoadKeyStoreException e) {
			LumoLabel resultLabel = new LumoLabel();
			String text = e.getMessage();
			if(e.getCause()!=null) {
				text += e.getCause().getMessage();
			}
			resultLabel.setText("Cannot load connectorstore! "+text);
			resultLabel.getStyle().set("color", "red");
			connectorstore.add(resultLabel);
		}
		
		TextField password = new TextField("Connectorstore password:");
		password.setValue(activePModeSet.getConnectorstore().getPasswordPlain());
		connectorstore.add(password);
		
		Button updatePassword = new Button("Update connectorstore password");
		updatePassword.addClickListener(e -> {
			activePModeSet.getConnectorstore().setPasswordPlain(password.getValue());
			try {
				pmodeService.updateConnectorstorePassword(activePModeSet, password.getValue());
				reloadPage();
			}catch(Exception e1) {
				LumoLabel resultLabel = new LumoLabel();
				String text = e1.getMessage();
				if(e1.getCause()!=null) {
					text += e1.getCause().getMessage();
				}
				resultLabel.setText("Exception updating password! "+text);
				resultLabel.getStyle().set("color", "red");
				connectorstore.add(resultLabel);
			}

		});
		connectorstore.add(updatePassword);
		
		return connectorstore;
	}

	private Div createServicesDiv() {
		Div services = new Div();

		LumoLabel servicesLabel = createGridTitleText("Services within active PMode-Set:");
		services.add(servicesLabel);

		serviceGrid = new Grid<DomibusConnectorService>();

		List<DomibusConnectorService> serviceList = this.pmodeService.getServiceList();
		serviceGrid.setItems(serviceList);
		serviceGrid.addColumn(DomibusConnectorService::getService).setHeader("Service").setWidth("500px").setSortable(true).setResizable(true);
		serviceGrid.addColumn(DomibusConnectorService::getServiceType).setHeader("Service Type").setWidth("500px").setSortable(true).setResizable(true);
		serviceGrid.setWidth("1020px");
		serviceGrid.setHeight("320px");
		serviceGrid.setMultiSort(true);

		services.add(serviceGrid);
		return services;
	}

	private Div createActionsDiv() {
		Div actions = new Div();

		LumoLabel actionsLabel = createGridTitleText("Actions within active PMode-Set:");
		actions.add(actionsLabel);

		actionGrid = new Grid<DomibusConnectorAction>();

		List<DomibusConnectorAction> actionList = pmodeService.getActionList();
		actionGrid.setItems(actionList);
		actionGrid.addColumn(DomibusConnectorAction::getAction).setHeader("Action").setWidth("600px").setSortable(true).setResizable(true);
		actionGrid.setWidth("620px");
		actionGrid.setHeight("320px");
		actionGrid.setMultiSort(true);

		actions.add(actionGrid);
		return actions;
	}

	private Div createPartiesDiv() {
		Div parties = new Div();
		
		LumoLabel partiesLabel = createGridTitleText("Parties within active PMode-Set:");
		parties.add(partiesLabel);

		partyGrid = new Grid<DomibusConnectorParty>();

		List<DomibusConnectorParty> partyList = this.pmodeService.getPartyList();
		partyGrid.setItems(partyList);
		partyGrid.addColumn(DomibusConnectorParty::getPartyId).setHeader("Party ID").setWidth("250px").setSortable(true).setResizable(true);
		partyGrid.addColumn(DomibusConnectorParty::getPartyIdType).setHeader("Party ID Type").setWidth("500px").setSortable(true).setResizable(true);
		partyGrid.addColumn(DomibusConnectorParty::getRole).setHeader("Party Role").setWidth("500px").setSortable(true).setResizable(true);
		partyGrid.setWidth("1270px");
		partyGrid.setHeight("320px");
		partyGrid.setMultiSort(true);


		parties.add(partyGrid);
		return parties;
	}



//	public void reloadParties() {
//		partyGrid.setItems(this.pmodeService.getPartyList());
//
//	}
//
//	public void reloadActions() {
//		actionGrid.setItems(this.pmodeService.getActionList());
//
//	}
//
//	public void reloadServices() {
//		serviceGrid.setItems(this.pmodeService.getServiceList());
//
//	}
	
	private void reloadPage() {
		UI.getCurrent().getPage().reload();
	}


	private LumoLabel createChapterText(String text) {
		LumoLabel label = new LumoLabel();
		label.setText(text);
		label.getStyle().set("font-size", "20px");
		
		label.getStyle().set("font-style", "bold");
		return label;
	}
	
	private LumoLabel createGridTitleText(String text) {
		LumoLabel label = new LumoLabel();
		label.setText(text);
		label.getStyle().set("font-size", "20px");
		
		label.getStyle().set("font-style", "italic");
		return label;
	}

}

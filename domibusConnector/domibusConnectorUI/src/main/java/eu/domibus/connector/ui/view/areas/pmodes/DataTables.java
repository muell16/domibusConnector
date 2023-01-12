package eu.domibus.connector.ui.view.areas.pmodes;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.ui.component.DomainSelect;
import eu.domibus.connector.ui.component.LumoLabel;
import eu.domibus.connector.ui.layout.DCVerticalLayoutWithTitleAndHelpButton;
import eu.domibus.connector.ui.service.WebPModeService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.ecodex.dc5.pmode.DC5PmodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

@Component
@UIScope
@Route(value = DataTables.ROUTE, layout = PmodeLayout.class)
@Order(2)
@TabMetadata(title = "PMode-Set Data", tabGroup = PmodeLayout.TAB_GROUP_NAME)
public class DataTables extends DCVerticalLayoutWithTitleAndHelpButton implements AfterNavigationObserver {

    public static final String ROUTE = "pmodedata";

    public static final String TITLE = "PMode-Set Data";
    public static final String HELP_ID = "ui/pmodes/pmodeset_data.html";

    private final WebPModeService pmodeService;
    private final DomainSelect domainSelect;

    private DC5PmodeService.DomibusConnectorPModeSet activePModeSet;

    private LumoLabel uploadedAt;
    private LumoLabel noActivePModeSet;

    private Anchor downloadPModesAnchor = new Anchor();
    private Div areaNoActivePModeSetDiv = new Div();
    private VerticalLayout activePModeSetLayout = new VerticalLayout();

    private Label description = new Label("Description:");

    private Grid<DC5PmodeService.PModeParty> partyGrid;
    private Grid<DC5PmodeService.PModeAction> actionGrid;
    private Grid<DC5PmodeService.PModeService> serviceGrid;

    public DataTables(@Autowired WebPModeService pmodeService, DomainSelect domainSelect) {
        super(HELP_ID, TITLE);

        this.pmodeService = pmodeService;
        this.domainSelect = domainSelect;
        domainSelect.addValueChangeListener(comboBoxBusinessDomainIdComponentValueChangeEvent -> {
            if (comboBoxBusinessDomainIdComponentValueChangeEvent.isFromClient()) {
                this.refreshUI();
            }
        });

        //CAVE: activePModeSet can be null!!
        activePModeSet = this.pmodeService.getCurrentPModeSet(DC5BusinessDomain.getDefaultBusinessDomainId()).orElse(null);

        createActivePmodeSetDiv();


        VerticalLayout main = new VerticalLayout(activePModeSetLayout);
        main.setAlignItems(Alignment.STRETCH);
        main.setHeight("100%");
        add(main);
        setHeight("100vh");
        setWidth("100vw");
    }

    private void createActivePmodeSetDiv() {
        activePModeSetLayout.setWidth("100vw");

        noActivePModeSet = createChapterText("No active PModes-Set found! Please import PModes and Connectorstore!");
        noActivePModeSet.getStyle().set("color", "red");

        activePModeSetLayout.add(areaNoActivePModeSetDiv);

        LumoLabel activePModeSetLabel = createChapterText("Active PMode Set data:");

        activePModeSetLayout.add(domainSelect);

        activePModeSetLayout.add(activePModeSetLabel);

        LumoLabel uploadedAtHeader = new LumoLabel("Active PMode Set uploaded at: ");
        uploadedAt = new LumoLabel();
        activePModeSetLayout.add(uploadedAtHeader);
        activePModeSetLayout.add(uploadedAt);
        activePModeSetLayout.add(this.downloadPModesAnchor);

        activePModeSetLayout.add(description);

        activePModeSetLayout.add(createServicesDiv());
        activePModeSetLayout.add(createActionsDiv());
        activePModeSetLayout.add(createPartiesDiv());
//		activePModeSetLayout.add(createConnectorstoreDiv());

    }

    private Anchor createDownloadPModesAnchor(DC5PmodeService.DomibusConnectorPModeSet pModeSet) {
        LumoLabel downloadPModesButton = new LumoLabel("download");
        Anchor downloadPModesAnchor = new Anchor();

        if (pModeSet.getPModes() != null) {
            final StreamResource resource = new StreamResource("pModes.xml",
                    () -> new BufferedInputStream(new ByteArrayInputStream(pModeSet.getPModes())));

            downloadPModesAnchor.setHref(resource);
            downloadPModesAnchor.setEnabled(true);
        } else {
            downloadPModesAnchor.setEnabled(false);
        }
        downloadPModesAnchor.getElement().setAttribute("download", true);
        downloadPModesAnchor.setTarget("_blank");
        downloadPModesAnchor.setTitle("Download PModes");
        downloadPModesAnchor.add(downloadPModesButton);

        return downloadPModesAnchor;
    }

    //TODO: make connectorstore downloadable again oder so
//	private VerticalLayout createConnectorstoreDiv() {
//		VerticalLayout connectorstore = new VerticalLayout();
//
//		LumoLabel connectorstoreLabel = createGridTitleText("Connectorstore contents:");
//		connectorstore.add(connectorstoreLabel);
//
//		connectorstoreInformationGrid = util.createKeystoreInformationGrid();
//		connectorstoreInformationGrid.setVisible(false);
//
//		connectorstore.add(connectorstoreInformationGrid);
//
//		connectorstore.add(connectorstorePassword);
//
//		connectorstore.add(connectorstoreResultLabel);
//
//		return connectorstore;
//	}

    private Div createServicesDiv() {
        Div services = new Div();

        LumoLabel servicesLabel = createGridTitleText("Services within active PMode-Set:");
        services.add(servicesLabel);

        serviceGrid = new Grid<>();

        serviceGrid.addColumn(DC5PmodeService.PModeService::getService).setHeader("Service").setWidth("500px").setSortable(true).setResizable(true);
        serviceGrid.addColumn(DC5PmodeService.PModeService::getServiceType).setHeader("Service Type").setWidth("500px").setSortable(true).setResizable(true);
        serviceGrid.setWidth("1020px");
        serviceGrid.setHeight("320px");
        serviceGrid.setMultiSort(true);
        serviceGrid.setVisible(true);

        services.add(serviceGrid);
        return services;
    }

    private Div createActionsDiv() {
        Div actions = new Div();

        LumoLabel actionsLabel = createGridTitleText("Actions within active PMode-Set:");
        actions.add(actionsLabel);

        actionGrid = new Grid<DC5PmodeService.PModeAction>();


        actionGrid.addColumn(DC5PmodeService.PModeAction::getAction).setHeader("Action").setWidth("600px").setSortable(true).setResizable(true);
        actionGrid.setWidth("620px");
        actionGrid.setHeight("320px");
        actionGrid.setMultiSort(true);
        actionGrid.setVisible(true);

        actions.add(actionGrid);
        return actions;
    }

    private Div createPartiesDiv() {
        Div parties = new Div();

        LumoLabel partiesLabel = createGridTitleText("Parties within active PMode-Set:");
        parties.add(partiesLabel);

        partyGrid = new Grid<>();


        partyGrid.addColumn(DC5PmodeService.PModeParty::getPartyId).setHeader("Party ID").setWidth("250px").setSortable(true).setResizable(true);
        partyGrid.addColumn(DC5PmodeService.PModeParty::getPartyIdType).setHeader("Party ID Type").setWidth("500px").setSortable(true).setResizable(true);
        partyGrid.setWidth("1760px");
        partyGrid.setHeight("320px");
        partyGrid.setMultiSort(true);
        partyGrid.setVisible(true);

        parties.add(partyGrid);
        return parties;
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

    private void refreshUI() {
        activePModeSet = pmodeService.getCurrentPModeSet(domainSelect.getValue()).orElse(null);
        areaNoActivePModeSetDiv.removeAll();
        if (activePModeSet != null) {
            partyGrid.setItems(activePModeSet.getParties());
            actionGrid.setItems(activePModeSet.getActions());
            serviceGrid.setItems(activePModeSet.getServices());
            // TODO: fix broken download
            this.downloadPModesAnchor = createDownloadPModesAnchor(activePModeSet);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent arg0) {
        refreshUI();
    }
}

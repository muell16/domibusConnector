package eu.domibus.connector.web.viewAreas.configuration.link;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


public abstract class LinkConfiguration extends VerticalLayout {

    @Autowired
    DCActiveLinkManagerService linkManager;

    @Autowired
    DCLinkPersistenceService dcLinkPersistenceService;

    @Autowired
    ApplicationContext applicationContext;

    private Grid<DomibusConnectorLinkPartner> linkGrid = new Grid<>();

    private Button addLinkButton = new Button("Add Link");

    public LinkConfiguration() {
    }

    protected abstract LinkType getLinkType();

    @PostConstruct
    private void initUI() {
        this.setSizeFull();

        addAndExpand(addLinkButton);
        addLinkButton.addClickListener(this::addLinkButtonClicked);

        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
            @Override
            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
                Button b = new Button(new Icon(VaadinIcon.EDIT));
                b.addClickListener(event -> editClicked(event, domibusConnectorLinkPartner));
                return b;
            }
        }).setHeader("Edit");

        linkGrid.addColumn(DomibusConnectorLinkPartner::getLinkPartnerName).setHeader("Link Partner Name");
        linkGrid.addColumn(DomibusConnectorLinkPartner::isEnabled).setHeader("run on startup");
        linkGrid.addColumn((ValueProvider) o -> {
            DomibusConnectorLinkPartner d = (DomibusConnectorLinkPartner) o;
            return d.getLinkConfiguration().getConfigName();
        }).setHeader("Config Name");
        linkGrid.addColumn((ValueProvider) o -> {
            DomibusConnectorLinkPartner d = (DomibusConnectorLinkPartner) o;
            return linkManager.getActiveLinkPartner(d.getLinkPartnerName()).isPresent() ? "running" : "stopped";
        }).setHeader("Current Link State");

        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
            @Override
            public Component apply(DomibusConnectorLinkPartner linkPartner) {
                HorizontalLayout hl = new HorizontalLayout();

                Button startLinkButton = new Button(new Icon(VaadinIcon.PLAY));
                startLinkButton.addClickListener(event -> startLinkButtonClicked(event, linkPartner));
                startLinkButton.setEnabled(!linkManager.getActiveLinkPartner(linkPartner.getLinkPartnerName()).isPresent());
                hl.add(startLinkButton);

                Button stopLinkButton = new Button(new Icon(VaadinIcon.STOP));
                stopLinkButton.addClickListener(event -> stopLinkButtonClicked(event, linkPartner));
                stopLinkButton.setEnabled(linkManager.getActiveLinkPartner(linkPartner.getLinkPartnerName()).isPresent());
                hl.add(stopLinkButton);

                return hl;
            }
        });

        addAndExpand(linkGrid);
        linkGrid.setSizeFull();

    }

    private void stopLinkButtonClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner linkPartner) {
        try {
            linkManager.shutdownLinkPartner(linkPartner.getLinkPartnerName());
            Notification.show("Link " + linkPartner.getLinkPartnerName() +" stopped");
        } finally {
            refreshList();
        }
    }

    private void startLinkButtonClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner linkPartner) {
        Optional<ActiveLinkPartner> activeLinkPartner = this.linkManager.activateLinkPartner(linkPartner);
        if (activeLinkPartner.isPresent()) {
            Notification.show("Link " + linkPartner.getLinkPartnerName() + " started");
        } else {
            Notification.show("Link " + linkPartner.getLinkPartnerName() + " start failed!");
        }
        refreshList();
    }

    private void editClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
        //TODO: show edit for LinkPartner
    }

    private void addLinkButtonClicked(ClickEvent<Button> buttonClickEvent) {
        final Dialog dialog = new Dialog();
        CreateLinkPanel createLinkWizard = applicationContext.getBean(CreateLinkPanel.class);
        createLinkWizard.setParentDialog(dialog);
        createLinkWizard.getWizard().addCancelListener((a,c) -> dialog.close());
        createLinkWizard.getWizard().addFinishListener((a,c) -> {
            dialog.close();
            refreshList();
        });

        //TODO: workaround for https://github.com/vaadin/vaadin-dialog-flow/issues/35#issuecomment-381064459
        //dialog.setWidth("80%");
        dialog.setWidth("1000px");

        dialog.add(createLinkWizard);
        dialog.setCloseOnEsc(true);
        dialog.open();
    }

    protected void onAttach(AttachEvent attachEvent) {
        refreshList();
    }

    private void refreshList() {
        List<DomibusConnectorLinkPartner> gwLinks = dcLinkPersistenceService.getAllLinksOfType(getLinkType());
        ListDataProvider<DomibusConnectorLinkPartner> linkPartners = new ListDataProvider<DomibusConnectorLinkPartner>(gwLinks);
        linkGrid.setDataProvider(linkPartners);
        linkPartners.refreshAll();
    }


}

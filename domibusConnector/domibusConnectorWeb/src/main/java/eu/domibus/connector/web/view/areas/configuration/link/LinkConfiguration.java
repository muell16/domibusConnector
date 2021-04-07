package eu.domibus.connector.web.view.areas.configuration.link;


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
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

public abstract class LinkConfiguration extends VerticalLayout {


    private final DCLinkFacade dcLinkFacade;
    private final ApplicationContext applicationContext;
    private final LinkType linkType;

    private Grid<DomibusConnectorLinkPartner> linkGrid = new Grid<>();
    private Button addLinkButton = new Button("Add Link");

    protected LinkConfiguration(DCLinkFacade dcLinkFacade, ApplicationContext applicationContext, LinkType linkType) {
        this.dcLinkFacade = dcLinkFacade;
        this.applicationContext = applicationContext;
        this.linkType = linkType;
    }

    protected LinkType getLinkType() {
        return linkType;
    }

    @PostConstruct
    private void initUI() {
        this.setSizeFull();

        addAndExpand(addLinkButton);
        addLinkButton.addClickListener(this::addLinkButtonClicked);
//        addLinkButton.setEnabled(false);

        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
            @Override
            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
                Button b = new Button(new Icon(VaadinIcon.SEARCH));
                b.addClickListener(event -> viewClicked(event, domibusConnectorLinkPartner));
                return b;
            }
        }).setHeader("View");

        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
            @Override
            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
                Button b = new Button(new Icon(VaadinIcon.EDIT));
                b.addClickListener(event -> editClicked(event, domibusConnectorLinkPartner));
                return b;
            }
        }).setHeader("Edit");

        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
            @Override
            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
                Button b = new Button(new Icon(VaadinIcon.DEL));
                b.addClickListener(event -> deleteClicked(event, domibusConnectorLinkPartner));
                return b;
            }
        }).setHeader("Remove");

        linkGrid.addColumn(DomibusConnectorLinkPartner::getLinkPartnerName).setHeader("Link Partner Name");
        linkGrid.addColumn(DomibusConnectorLinkPartner::isEnabled).setHeader("run on startup");
        linkGrid.addColumn(DomibusConnectorLinkPartner::getConfigurationSource).setHeader("Configured via");
        linkGrid.addColumn((ValueProvider) o -> {
            DomibusConnectorLinkPartner d = (DomibusConnectorLinkPartner) o;
            return d.getLinkConfiguration().getConfigName();
        }).setHeader("Config Name");
        linkGrid.addColumn((ValueProvider) o -> {
            DomibusConnectorLinkPartner d = (DomibusConnectorLinkPartner) o;
            return dcLinkFacade.isActive(d) ? "running" : "stopped";
        }).setHeader("Current Link State");

        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
            @Override
            public Component apply(DomibusConnectorLinkPartner linkPartner) {
                HorizontalLayout hl = new HorizontalLayout();
                if (linkPartner == null) {
                    return hl;
                }

                Button startLinkButton = new Button(new Icon(VaadinIcon.PLAY));
                startLinkButton.addClickListener(event -> startLinkButtonClicked(event, linkPartner));
//                Optional<ActiveLinkPartnerManager> activeLinkPartner = linkManager.getActiveLinkPartner(linkPartner.getLinkPartnerName());
//                boolean linkPartnerPresent = activeLinkPartner.isPresent();
//                startLinkButton.setEnabled(!linkPartnerPresent);
                hl.add(startLinkButton);

                Button stopLinkButton = new Button(new Icon(VaadinIcon.STOP));
                stopLinkButton.addClickListener(event -> stopLinkButtonClicked(event, linkPartner));
//                stopLinkButton.setEnabled(linkPartnerPresent);
                hl.add(stopLinkButton);

                return hl;
            }
        });

        addAndExpand(linkGrid);
        linkGrid.setSizeFull();

    }

    private void deleteClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner linkPartner) {
        try {
            //TODO: if not deleteable....
            dcLinkFacade.deleteLinkPartner(linkPartner);
            Notification.show("Link " + linkPartner.getLinkPartnerName() +" removed from config");
        } finally {
            refreshList();
        }
    }

    private void stopLinkButtonClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner linkPartner) {
        try {
            dcLinkFacade.shutdownLinkPartner(linkPartner);
            Notification.show("Link " + linkPartner.getLinkPartnerName() +" stopped");
        } finally {
            refreshList();
        }
    }

    private void startLinkButtonClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner linkPartner) {
        try {
            dcLinkFacade.startLinkPartner(linkPartner);
            Notification.show("Link " + linkPartner.getLinkPartnerName() + " started");
        } catch (LinkPluginException e) {
            Notification.show("Link " + linkPartner.getLinkPartnerName() + " start failed!\n" + e.getMessage());
        }
        refreshList();
    }

    private void editClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
        //TODO: show edit for LinkPartner
    }

    private void viewClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
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

        dialog.setWidth("100%");
        dialog.addDialogCloseActionListener((e) -> refreshList());
        dialog.add(createLinkWizard);
        dialog.setCloseOnEsc(true);
        dialog.open();

    }

    protected void onAttach(AttachEvent attachEvent) {
        refreshList();
    }

    private void refreshList() {
        List<DomibusConnectorLinkPartner> gwLinks = dcLinkFacade.getAllLinksOfType(getLinkType());
        ListDataProvider<DomibusConnectorLinkPartner> linkPartners = new ListDataProvider<DomibusConnectorLinkPartner>(gwLinks);
        linkGrid.setDataProvider(linkPartners);
        linkPartners.refreshAll();
    }


}

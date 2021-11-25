package eu.domibus.connector.ui.view.areas.configuration.link;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.link.service.DCLinkFacade;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class LinkConfiguration extends VerticalLayout implements AfterNavigationObserver {


    private final DCLinkFacade dcLinkFacade;
    private final ApplicationContext applicationContext;
    private final LinkType linkType;
    private final WebLinkItemHierachicalDataProvider webLinkItemHierachicalDataProvider;

    private TreeGrid<WebLinkItem> treeGrid = new TreeGrid<>();

    private Grid<DomibusConnectorLinkPartner> linkGrid = new Grid<>();
    protected Button addLinkButton = new Button("Add Link");
    protected HorizontalLayout buttonBar = new HorizontalLayout();

    protected LinkConfiguration(DCLinkFacade dcLinkFacade,
                                ApplicationContext applicationContext,
                                LinkType linkType) {
        this.webLinkItemHierachicalDataProvider = new WebLinkItemHierachicalDataProvider(dcLinkFacade, linkType);
        this.dcLinkFacade = dcLinkFacade;
        this.applicationContext = applicationContext;
        this.linkType = linkType;
    }


    @PostConstruct
    private void initUI() {
        this.setSizeFull();

        addAndExpand(buttonBar);
        buttonBar.add(addLinkButton);
        addLinkButton.addClickListener(this::addLinkButtonClicked);
//        addLinkButton.setEnabled(false);

//        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
//            @Override
//            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
//                Button b = new Button(new Icon(VaadinIcon.EDIT));
//                b.addClickListener(event -> editWebLinkItem(event, domibusConnectorLinkPartner));
//                return b;
//            }
//        }).setHeader("Edit Link Config");

//        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
//            @Override
//            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
//                Button b = new Button(new Icon(VaadinIcon.EDIT));
//                b.addClickListener(event -> editLinkPartner(event, domibusConnectorLinkPartner));
//                return b;
//            }
//        }).setHeader("Edit Link Partner");

//        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
//            @Override
//            public Component apply(DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
//                Button b = new Button(new Icon(VaadinIcon.DEL));
//                b.addClickListener(event -> deleteClicked(event, domibusConnectorLinkPartner));
//                return b;
//            }
//        }).setHeader("Remove");

//        linkGrid.addColumn(DomibusConnectorLinkPartner::getLinkPartnerName).setHeader("Link Partner Name");
//        linkGrid.addColumn(DomibusConnectorLinkPartner::isEnabled).setHeader("init on startup");
//        linkGrid.addColumn(DomibusConnectorLinkPartner::getConfigurationSource).setHeader("Configured via");
//        linkGrid.addColumn((ValueProvider) o -> {
//            DomibusConnectorLinkPartner d = (DomibusConnectorLinkPartner) o;
//            return d.getLinkConfiguration().getConfigName();
//        }).setHeader("Config Name");
//        linkGrid.addColumn((ValueProvider) o -> {
//            DomibusConnectorLinkPartner d = (DomibusConnectorLinkPartner) o;
//            return dcLinkFacade.isActive(d) ? "running" : "stopped";
//        }).setHeader("Current Link State");

//        linkGrid.addComponentColumn(new ValueProvider<DomibusConnectorLinkPartner, Component>() {
//            @Override
//            public Component apply(DomibusConnectorLinkPartner linkPartner) {
//                HorizontalLayout hl = new HorizontalLayout();
//                if (linkPartner == null) {
//                    return hl;
//                }
//
//                Button startLinkButton = new Button(new Icon(VaadinIcon.PLAY));
//                startLinkButton.addClickListener(event -> startLinkButtonClicked(event, linkPartner));
//                hl.add(startLinkButton);
//
//                Button stopLinkButton = new Button(new Icon(VaadinIcon.STOP));
//                stopLinkButton.addClickListener(event -> stopLinkButtonClicked(event, linkPartner));
//                hl.add(stopLinkButton);
//
//                return hl;
//            }
//        });


        treeGrid.setDataProvider(webLinkItemHierachicalDataProvider);
//        treeGrid.setHierarchyColumn("name");
        treeGrid.addHierarchyColumn(WebLinkItem::getName).setHeader("Name");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, Component>) webLinkItem -> {
            Button b = new Button(new Icon(VaadinIcon.EDIT));
            b.addClickListener( (event) -> editWebLinkItem(webLinkItem, event));
            b.setEnabled(webLinkItem.getConfigurationSource() == ConfigurationSource.DB);
            return b;
        }).setWidth("4em").setHeader("Edit");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, Component>) webLinkItem -> {
            Button b = new Button(new Icon(VaadinIcon.TRASH));
            b.addClickListener( (event) -> deleteWebLinkItem(webLinkItem, event));
            b.setEnabled(webLinkItem.getConfigurationSource() == ConfigurationSource.DB);
            return b;
        }).setWidth("4em")
                .setHeader("Delete");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, ? extends Component>) webLinkItem -> {
            if (webLinkItem instanceof WebLinkItem.WebLinkConfigurationItem) {
                Button b = new Button(new Icon(VaadinIcon.PLUS));
                b.addClickListener((event) -> addLinkPartner((WebLinkItem.WebLinkConfigurationItem) webLinkItem, event));
                b.setEnabled(webLinkItem.getConfigurationSource() == ConfigurationSource.DB);
                return b;
            } else {
                return new Div();
            }
        }).setWidth("4em").setHeader("Add Link Partner");

        treeGrid.addColumn(WebLinkItem::getConfigurationSource).setWidth("4em").setHeader("Configured via");
        treeGrid.addColumn(WebLinkItem::isEnabled).setWidth("4em").setHeader("Start on startup");
//        treeGrid.addColumn(WebLinkItem::getCurrentState).setHeader("Current Link State");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, ? extends Component>) webLinkItem -> {
            if (webLinkItem instanceof WebLinkItem.WebLinkConfigurationItem) {
                DomibusConnectorLinkConfiguration linkConfiguration = webLinkItem.getLinkConfiguration();

            } else if (webLinkItem instanceof WebLinkItem.WebLinkPartnerItem) {
                DomibusConnectorLinkPartner d = webLinkItem.getLinkPartner();
                return dcLinkFacade.isActive(d) ? new Span("running") : new Span("stopped");
            }
            return new Div();
        }).setHeader("Current Link State");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, ? extends Component>) webLinkItem -> {
            HorizontalLayout hl = new HorizontalLayout();
            if (webLinkItem instanceof WebLinkItem.WebLinkPartnerItem) {
                DomibusConnectorLinkPartner linkPartner = webLinkItem.getLinkPartner();
                Optional<LinkPlugin> linkPlugin = webLinkItem.getLinkPlugin();

                Button startLinkButton = new Button(new Icon(VaadinIcon.PLAY));
                startLinkButton.addClickListener(event -> startLinkButtonClicked(event, linkPartner));
                startLinkButton.setEnabled(!dcLinkFacade.isActive(linkPartner));
                hl.add(startLinkButton);

                Button stopLinkButton = new Button(new Icon(VaadinIcon.STOP));
                stopLinkButton.addClickListener(event -> stopLinkButtonClicked(event, linkPartner));
                boolean stopButtonEnabled = linkPlugin.map(l -> l.getFeatures().contains(PluginFeature.SUPPORTS_LINK_PARTNER_SHUTDOWN)).orElse(false);
                stopButtonEnabled = stopButtonEnabled && dcLinkFacade.isActive(linkPartner);
                stopLinkButton.setEnabled(stopButtonEnabled);
                hl.add(stopLinkButton);

            }
            return hl;
        });


        addAndExpand(treeGrid);
        treeGrid.setSizeFull();


//        addAndExpand(linkGrid);
//        linkGrid.setSizeFull();

    }

    private void editWebLinkItem(WebLinkItem webLinkItem, ClickEvent<Button> buttonClickEvent) {
        //TODO: open edit panel...
        if (webLinkItem instanceof WebLinkItem.WebLinkPartnerItem) {
            DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = webLinkItem.getLinkPartner().getLinkPartnerName();
            String lp = linkPartnerName.getLinkName();
            getUI().ifPresent(ui -> ui.navigate(DCLinkPartnerView.class, lp));
        } else if (webLinkItem instanceof WebLinkItem.WebLinkConfigurationItem) {
            DomibusConnectorLinkConfiguration linkConfiguration = webLinkItem.getLinkConfiguration();
            String configName = linkConfiguration.getConfigName().toString();
            getUI().ifPresent(ui -> ui.navigate(DCLinkConfigurationView.class, configName));
        }

    }

    private void deleteWebLinkItem(WebLinkItem webLinkItem, ClickEvent<Button> buttonClickEvent) {
        //TODO: open delete dialog...

    }

    private void addLinkPartner(WebLinkItem.WebLinkConfigurationItem webLinkItem, ClickEvent<Button> buttonClickEvent) {
        //TODO: open add link partner dialog for configuration

    }

//    private void deleteClicked(ClickEvent<Button> event, DomibusConnectorLinkPartner linkPartner) {
//        try {
//            //TODO: if not deleteable....
//            dcLinkFacade.deleteLinkPartner(linkPartner);
//            Notification.show("Link " + linkPartner.getLinkPartnerName() +" removed from config");
//        } finally {
//            refreshList();
//        }
//    }

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

//    private void editLinkPartner(ClickEvent<Button> event, DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
//        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = domibusConnectorLinkPartner.getLinkPartnerName();
//        String lp = linkPartnerName.getLinkName();
//        getUI().ifPresent(ui -> ui.navigate(DCLinkPartnerView.class, lp));
//
//    }

//    private void editWebLinkItem(ClickEvent<Button> event, DomibusConnectorLinkPartner domibusConnectorLinkPartner) {
//        DomibusConnectorLinkConfiguration linkConfiguration = domibusConnectorLinkPartner.getLinkConfiguration();
//        String configName = linkConfiguration.getConfigName().toString();
//        getUI().ifPresent(ui -> ui.navigate(DCLinkConfigurationView.class, configName));
//    }

    private Dialog createDialog(Component bean) {
        final Dialog dialog = new Dialog();
        dialog.setWidth("100%");
        dialog.addDialogCloseActionListener((e) -> refreshList());
        dialog.add(bean);
        dialog.setCloseOnEsc(true);
        return dialog;
    }



    private void addLinkButtonClicked(ClickEvent<Button> buttonClickEvent) {
        CreateLinkPanel createLinkWizard = applicationContext.getBean(CreateLinkPanel.class);

        final Dialog dialog = createDialog(createLinkWizard);

        createLinkWizard.setLinkType(linkType);
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

    public void afterNavigation(AfterNavigationEvent event) {
        refreshList();
    }

    private void refreshList() {
//        List<DomibusConnectorLinkPartner> gwLinks = dcLinkFacade.getAllLinksOfType(getLinkType());
//        ListDataProvider<DomibusConnectorLinkPartner> linkPartners = new ListDataProvider<DomibusConnectorLinkPartner>(gwLinks);
//        linkGrid.setDataProvider(linkPartners);
//        linkPartners.refreshAll();

        webLinkItemHierachicalDataProvider.refreshAll();
        treeGrid.expand(webLinkItemHierachicalDataProvider
                .fetchChildren(new HierarchicalQuery<>(new WebLinkItemFilter(), null))
                .collect(Collectors.toSet())); //expand root items..
    }


}

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
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.ui.dialogs.EditBeanDialogBuilder;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
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
        addLinkButton.addClickListener(this::addLinkConfigurationButtonClicked);

        treeGrid.setDataProvider(webLinkItemHierachicalDataProvider);

        treeGrid.addHierarchyColumn(WebLinkItem::getName)
                .setResizable(true)
                .setHeader("Name");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, Component>) webLinkItem -> {
            Button b = new Button(new Icon(VaadinIcon.EDIT));
            b.addClickListener( (event) -> editWebLinkItem(webLinkItem, event));
            return b;
        }).setWidth("4em")
                .setResizable(true)
                .setHeader("Edit");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, Component>) webLinkItem -> {
            Button b = new Button(new Icon(VaadinIcon.TRASH));
            b.addClickListener( (event) -> deleteWebLinkItem(webLinkItem, event));
            b.setEnabled(webLinkItem.getConfigurationSource() == ConfigurationSource.DB);
            return b;
        }).setWidth("4em")
                .setResizable(true)
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
        }).setWidth("4em")
                .setResizable(true)
                .setHeader("Add Link Partner");

        treeGrid.addColumn(WebLinkItem::getConfigurationSource)
                .setWidth("4em")
                .setResizable(true)
                .setHeader("Configured via");
        treeGrid.addColumn(WebLinkItem::isEnabled)
                .setWidth("4em")
                .setResizable(true)
                .setHeader("Start on startup");
//        treeGrid.addColumn(WebLinkItem::getCurrentState).setHeader("Current Link State");

        treeGrid.addComponentColumn((ValueProvider<WebLinkItem, ? extends Component>) webLinkItem -> {
                    if (webLinkItem instanceof WebLinkItem.WebLinkConfigurationItem) {
                        DomibusConnectorLinkConfiguration linkConfiguration = webLinkItem.getLinkConfiguration();

                    } else if (webLinkItem instanceof WebLinkItem.WebLinkPartnerItem) {
                        DomibusConnectorLinkPartner d = webLinkItem.getLinkPartner();
                        return dcLinkFacade.isActive(d) ? new Span("running") : new Span("stopped");
                    }
                    return new Div();
                })
                .setResizable(true)
                .setHeader("Current Link State");

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
                })
                .setResizable(true);

        addAndExpand(treeGrid);
        treeGrid.setSizeFull();


    }

    private void editWebLinkItem(WebLinkItem webLinkItem, ClickEvent<Button> buttonClickEvent) {
        Map<String, String> s = new HashMap<>();
        if (webLinkItem.getConfigurationSource() == ConfigurationSource.DB) {
            s.put(DCLinkConfigurationView.EDIT_MODE_TYPE_QUERY_PARAM, EditMode.EDIT.name());
        } else {
            s.put(DCLinkConfigurationView.EDIT_MODE_TYPE_QUERY_PARAM, EditMode.VIEW.name());
        }
        //navigate to edit view
        if (webLinkItem instanceof WebLinkItem.WebLinkPartnerItem) {
            DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = webLinkItem.getLinkPartner().getLinkPartnerName();
            String lp = linkPartnerName.getLinkName();
            String url = RouteConfiguration.forSessionScope()
                    .getUrl(DCLinkPartnerView.class, lp);

            QueryParameters queryParameters = QueryParameters.simple(s);

            getUI().ifPresent(ui -> ui.navigate(url, queryParameters));
        } else if (webLinkItem instanceof WebLinkItem.WebLinkConfigurationItem) {
            DomibusConnectorLinkConfiguration linkConfiguration = webLinkItem.getLinkConfiguration();
            String configName = linkConfiguration.getConfigName().toString();

            String url = RouteConfiguration.forSessionScope()
                    .getUrl(DCLinkConfigurationView.class, configName);

            s.put(DCLinkConfigurationView.LINK_TYPE_QUERY_PARAM, linkType.name());

            QueryParameters queryParameters = QueryParameters.simple(s);
            getUI().ifPresent(ui -> ui.navigate(url, queryParameters));
        }

    }

    private void deleteWebLinkItem(WebLinkItem webLinkItem, ClickEvent<Button> buttonClickEvent) {
        //TODO: open delete dialog...

    }

    private void addLinkPartner(WebLinkItem.WebLinkConfigurationItem webLinkItem, ClickEvent<Button> buttonClickEvent) {
        //TODO: open add link partner dialog for configuration
        DomibusConnectorLinkConfiguration linkConfiguration = webLinkItem.getLinkConfiguration();

        DCLinkPartnerField linkPartnerField = applicationContext.getBean(DCLinkPartnerField.class);

        DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
        linkPartner.setLinkConfiguration(linkConfiguration);
        linkPartnerField.setValue(linkPartner);

        EditBeanDialogBuilder<DomibusConnectorLinkPartner> dialogBuilder = EditBeanDialogBuilder.getBuilder();
        dialogBuilder.setOnValueCallback(value -> {
            //save value / validation?
            dcLinkFacade.createNewLinkPartner(value);
            refreshList();
            return true; //close dialog
        });
        dialogBuilder.setCloseOnEscape(true);
        dialogBuilder.setField(linkPartnerField);
        dialogBuilder.show();

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
    

    private Dialog createDialog(Component bean) {
        final Dialog dialog = new Dialog();
        dialog.setWidth("100%");
        dialog.addDialogCloseActionListener((e) -> refreshList());
        dialog.add(bean);
        dialog.setCloseOnEsc(true);
        return dialog;
    }



    private void addLinkConfigurationButtonClicked(ClickEvent<Button> buttonClickEvent) {

        String url = RouteConfiguration.forSessionScope()
                .getUrl(DCLinkConfigurationView.class
                );
        Map<String, String> s = new HashMap<>();
        s.put(DCLinkConfigurationView.LINK_TYPE_QUERY_PARAM, linkType.name());
        s.put(DCLinkConfigurationView.EDIT_MODE_TYPE_QUERY_PARAM, EditMode.CREATE.name());
        QueryParameters queryParameters = QueryParameters.simple(s);
        getUI().ifPresent(ui -> ui.navigate(url, queryParameters));

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

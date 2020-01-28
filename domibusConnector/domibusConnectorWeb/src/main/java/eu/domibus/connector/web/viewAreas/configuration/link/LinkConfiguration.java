package eu.domibus.connector.web.viewAreas.configuration.link;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


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

        add(addLinkButton);
        addLinkButton.addClickListener(this::addLinkButtonClicked);

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

        add(linkGrid);

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

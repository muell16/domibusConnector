package eu.domibus.connector.web.view.areas.configuration.link;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationOverviewView;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@UIScope
@Route(value = DCLinkPartnerView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class DCLinkPartnerView extends VerticalLayout implements HasUrlParameter<String> {

    public static final String ROUTE = "linkPartner";

    public static final String TITLE_LABEL_TEXT = "Edit LinkPartner";

    private final DCLinkFacade dcLinkFacade;
    private final DCLinkPartnerPanel dcLinkPartnerPanel;

    private Label titleLabel = new Label("Edit LinkPartner");
    private Button discardButton;
    private Button saveButton;

    private LinkType linkType;
    private DomibusConnectorLinkPartner linkPartner;

    public DCLinkPartnerView(DCLinkFacade dcLinkFacade, DCLinkPartnerPanel dcLinkPartnerPanel) {
        this.dcLinkFacade = dcLinkFacade;
        this.dcLinkPartnerPanel = dcLinkPartnerPanel;

        initUI();
    }

    private void initUI() {
        discardButton = new Button("Back");
        saveButton = new Button("Save");

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.add(discardButton, saveButton);

        discardButton.addClickListener(this::discardButtonClicked);
        saveButton.addClickListener(this::saveButtonClicked);

        this.add(titleLabel);
        this.add(buttonBar);
        this.add(dcLinkPartnerPanel);
    }

    private void saveButtonClicked(ClickEvent<Button> buttonClickEvent) {
        BinderValidationStatus<DomibusConnectorLinkPartner> validate = this.dcLinkPartnerPanel.validate();
        if (validate.isOk()) {
            try {
                dcLinkPartnerPanel.writeBean(linkPartner);
            } catch (ValidationException e) {
                //TODO: show user...
            }
            dcLinkFacade.updateLinkPartner(linkPartner);
            //TODO: print success Notification
            navgiateBack();
        }
     }

    private void discardButtonClicked(ClickEvent<Button> buttonClickEvent) {
        navgiateBack();
    }

    private void navgiateBack() {
        getUI().ifPresent(ui -> {
            if (linkType == LinkType.GATEWAY) {
                ui.navigate(GatewayLinkConfiguration.class);
            } else if (linkType == LinkType.BACKEND) {
                ui.navigate(BackendLinkConfiguration.class);
            } else {
                ui.navigate(ConfigurationOverviewView.class);
            }
        });
    }


    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        DomibusConnectorLinkPartner.LinkPartnerName lp = new DomibusConnectorLinkPartner.LinkPartnerName(parameter);
        Optional<DomibusConnectorLinkPartner> optionalLinkPartner = dcLinkFacade.loadLinkPartner(lp);
        if (optionalLinkPartner.isPresent()) {
            linkPartner = optionalLinkPartner.get();
            dcLinkPartnerPanel.setValue(linkPartner);
            linkType = linkPartner.getLinkType();
            dcLinkPartnerPanel.setVisible(true);
            titleLabel.setText(TITLE_LABEL_TEXT + " " + parameter);
            saveButton.setEnabled(linkPartner.getConfigurationSource() == ConfigurationSource.DB);
        } else {
            titleLabel.setText(TITLE_LABEL_TEXT + " [None]");
            dcLinkPartnerPanel.setVisible(false);
        }

    }

}

package eu.domibus.connector.ui.view.areas.configuration.link;


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
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationOverview;

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
    private final DCLinkPartnerField dcLinkPartnerField;

    private Label titleLabel = new Label("Edit LinkPartner");
    private Button discardButton;
    private Button saveButton;

    private LinkType linkType;
    private DomibusConnectorLinkPartner linkPartner;

    public DCLinkPartnerView(DCLinkFacade dcLinkFacade, DCLinkPartnerField dcLinkPartnerField) {
        this.dcLinkFacade = dcLinkFacade;
        this.dcLinkPartnerField = dcLinkPartnerField;

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
        this.add(dcLinkPartnerField);
    }

    private void saveButtonClicked(ClickEvent<Button> buttonClickEvent) {
//        BinderValidationStatus<DomibusConnectorLinkPartner> validate = this.dcLinkPartnerField.validate();
//        if (validate.isOk()) {
//            try {
//                dcLinkPartnerField.writeBean(linkPartner);
//            } catch (ValidationException e) {
//                //TODO: show user...
//            }
//            dcLinkFacade.updateLinkPartner(linkPartner);
            //TODO: print success Notification
//            navgiateBack();
//        }
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
                ui.navigate(ConfigurationOverview.class);
            }
        });
    }


    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        DomibusConnectorLinkPartner.LinkPartnerName lp = new DomibusConnectorLinkPartner.LinkPartnerName(parameter);
        Optional<DomibusConnectorLinkPartner> optionalLinkPartner = dcLinkFacade.loadLinkPartner(lp);
        if (optionalLinkPartner.isPresent()) {
            linkPartner = optionalLinkPartner.get();
            dcLinkPartnerField.setValue(linkPartner);
            linkType = linkPartner.getLinkType();
            dcLinkPartnerField.setVisible(true);
            titleLabel.setText(TITLE_LABEL_TEXT + " " + parameter);
            saveButton.setEnabled(linkPartner.getConfigurationSource() == ConfigurationSource.DB);
        } else {
            titleLabel.setText(TITLE_LABEL_TEXT + " [None]");
            dcLinkPartnerField.setVisible(false);
        }

    }

}

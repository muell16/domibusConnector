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
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationOverview;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@UIScope
@Route(value = DCLinkConfigView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class DCLinkConfigView extends VerticalLayout implements HasUrlParameter<String> {


    public static final String ROUTE = "linkConfig";

    public static final String TITLE_LABEL_TEXT = "Edit LinkConfiguration";

    private final DCLinkFacade dcLinkFacade;
    private final DCLinkConfigPanel linkConfigPanel;

    private final Label titleLabel = new Label();
    private final Button discardButton = new Button("Discard");
    private final Button saveButton = new Button("Save");

    private LinkType linkType;
    private DomibusConnectorLinkConfiguration linkConfig;


    public DCLinkConfigView(DCLinkFacade dcLinkFacade, DCLinkConfigPanel linkConfigPanel) {
        this.dcLinkFacade = dcLinkFacade;
        this.linkConfigPanel = linkConfigPanel;

        this.initUI();
    }

    private void initUI() {

        discardButton.addClickListener(this::discardButtonClicked);
        saveButton.addClickListener(this::saveButtonClicked);

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.add(discardButton, saveButton);

        this.add(titleLabel, buttonBar, linkConfigPanel);
    }

    private void saveButtonClicked(ClickEvent<Button> buttonClickEvent) {
        BinderValidationStatus validate = linkConfigPanel.validate();
        if (validate.isOk()) {
            try {
                linkConfigPanel.writeBean(linkConfig);
            } catch (ValidationException e) {
                //TODO: show user...
            }
            dcLinkFacade.updateLinkConfig(linkConfig);
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
                ui.navigate(ConfigurationOverview.class);
            }
        });
    }


    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        DomibusConnectorLinkConfiguration.LinkConfigName configName = new DomibusConnectorLinkConfiguration.LinkConfigName((parameter));
        Optional<DomibusConnectorLinkConfiguration> optionalConfig = dcLinkFacade.loadLinkConfig(configName);
        if (optionalConfig.isPresent()) {
            linkConfig = optionalConfig.get();
            linkConfigPanel.setLinkConfiguration(linkConfig);
            linkConfigPanel.setImplChangeAble(false);
            linkConfigPanel.setVisible(true);
            titleLabel.setText(TITLE_LABEL_TEXT + " " + parameter);
            saveButton.setEnabled(linkConfig.getConfigurationSource() == ConfigurationSource.DB);
        } else {
            titleLabel.setText(TITLE_LABEL_TEXT + " [None]");
            linkConfigPanel.setVisible(false);
        }
    }
}

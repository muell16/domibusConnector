package eu.domibus.connector.ui.view.areas.configuration.link;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationOverview;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@UIScope
@Route(value = DCLinkConfigurationView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
public class DCLinkConfigurationView extends VerticalLayout implements HasUrlParameter<String> {


    public static final String ROUTE = "linkConfig";
    public static final String LINK_TYPE_QUERY_PARAM = "linkType";
    public static final String EDIT_MODE_TYPE_QUERY_PARAM = "modeType";

    public static final String TITLE_LABEL_TEXT = "Edit LinkConfiguration";

    private final DCLinkFacade dcLinkFacade;
    private final DCLinkConfigurationField linkConfigPanel;

    private final Label titleLabel = new Label();
    private final Button discardButton = new Button("Back");
    private final Button saveButton = new Button("Save");

    private LinkType linkType;
    private EditMode editMode;
    private DomibusConnectorLinkConfiguration linkConfig;


    public DCLinkConfigurationView(DCLinkFacade dcLinkFacade, DCLinkConfigurationField linkConfigPanel) {
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
//        BinderValidationStatus validate = linkConfigPanel.validate();
//        if (validate.isOk()) {
//            try {
//                linkConfigPanel.writeBean(linkConfig);
//            } catch (ValidationException e) {
//                //TODO: show user...
//            }
//            dcLinkFacade.updateLinkConfig(linkConfig);
//            //TODO: print success Notification
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
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        Map<String, List<String>> parameters = location.getQueryParameters().getParameters();
        this.linkType = parameters.getOrDefault(LINK_TYPE_QUERY_PARAM, Collections.emptyList())
                .stream().findFirst().map(LinkType::valueOf).orElse(null);
        this.editMode = parameters.getOrDefault(EDIT_MODE_TYPE_QUERY_PARAM, Collections.emptyList())
                .stream().findFirst().map(EditMode::valueOf).orElse(EditMode.VIEW);



        DomibusConnectorLinkConfiguration.LinkConfigName configName = new DomibusConnectorLinkConfiguration.LinkConfigName((parameter));
        Optional<DomibusConnectorLinkConfiguration> optionalConfig = dcLinkFacade.loadLinkConfig(configName);
        if (optionalConfig.isPresent()) {
            linkConfig = optionalConfig.get();
            linkConfigPanel.setValue(linkConfig);
//            linkConfigPanel.setImplAndConfigNameReadOnly(true);
            linkConfigPanel.setVisible(true);
            titleLabel.setText(TITLE_LABEL_TEXT + " " + parameter);
        } else if (editMode == EditMode.CREATE) {
            linkConfig = new DomibusConnectorLinkConfiguration();
            linkConfig.setConfigurationSource(ConfigurationSource.DB);
            linkConfig.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName("New Link Config"));
            linkConfigPanel.setValue(linkConfig);
//            linkConfigPanel.setImplAndConfigNameReadOnly(false);
            linkConfigPanel.setVisible(true);
            titleLabel.setText(TITLE_LABEL_TEXT + " new config");
        } else {
            titleLabel.setText(TITLE_LABEL_TEXT + " [None]");
            linkConfigPanel.setVisible(false);
        }
        linkConfigPanel.setEditMode(editMode);
        updateUI();

    }

    private void updateUI() {
        if (editMode == EditMode.VIEW) {
//            linkConfigPanel.setReadOnly(true);
            saveButton.setEnabled(false);
        } else if (editMode == EditMode.EDIT) {
            linkConfigPanel.setReadOnly(false);
//            linkConfigPanel.setImplAndConfigNameReadOnly(false);
            saveButton.setEnabled(linkConfig.getConfigurationSource() == ConfigurationSource.DB);
        } else if (editMode == EditMode.CREATE) {
            linkConfigPanel.setReadOnly(false);
//            linkConfigPanel.setImplAndConfigNameReadOnly(false);
            saveButton.setEnabled(linkConfig.getConfigurationSource() == ConfigurationSource.DB);
        }
    }

}

package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.persistence.service.DCLinkPersistenceService;
import eu.domibus.connector.link.service.DCLinkPluginConfiguration;
import eu.domibus.connector.web.component.WizardComponent;
import eu.domibus.connector.web.component.WizardStep;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@Route("createlink")
@Profile(DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME)
public class CreateLinkPanel extends VerticalLayout {

    private static final Logger LOGGER = LogManager.getLogger(CreateLinkPanel.class);

    private final DCActiveLinkManagerService linkManagerService;
    private final DCLinkPersistenceService dcLinkPersistenceService;
    private final ConfigurationPropertyCollector configurationPropertyCollector;
    private final ApplicationContext applicationContext;

    public CreateLinkPanel(ApplicationContext applicationContext,
                           ConfigurationPropertyCollector configurationPropertyCollector,
                           DCLinkPersistenceService dcLinkPersistenceService,
                           DCActiveLinkManagerService linkManagerService
                           ) {
        this.linkManagerService = linkManagerService;
        this.configurationPropertyCollector = configurationPropertyCollector;
        this.dcLinkPersistenceService = dcLinkPersistenceService;
        this.applicationContext = applicationContext;
        init();
    }

    private WizardComponent wizard;
    private Dialog parentDialog;

    private DomibusConnectorLinkConfiguration linkConfiguration;
    private DomibusConnectorLinkPartner linkPartner;

    private void init() {
        linkConfiguration = new DomibusConnectorLinkConfiguration();
        linkPartner = new DomibusConnectorLinkPartner();
        linkPartner.setLinkType(getLinkType());
        linkPartner.setEnabled(true);

        initUI();
    }

    private LinkType getLinkType() {
        return LinkType.GATEWAY;
    }

    private void initUI() {
        wizard = WizardComponent.getBuilder()
                .addStep(new ChooseOrCreateLinkConfigurationStep())
                .addStep(new CreateLinkPartnerStep())
                .addFinishedListener(this::wizardFinished)
                .build();
        add(wizard);
    }

    public void setParentDialog(Dialog parentDialog) {
        this.parentDialog = parentDialog;
    }

    private void wizardFinished(WizardComponent wizardComponent, WizardStep wizardStep) {
        linkPartner.setLinkConfiguration(linkConfiguration);
        try {
            dcLinkPersistenceService.addLinkPartner(linkPartner);
        } catch (RuntimeException e) {
            LOGGER.error("Exception occured while creating a new Link Configuration", e);
            Notification.show("No Link Configuration was created due: " + e.getMessage(), (int) Duration.ofSeconds(5).toMillis(), Notification.Position.TOP_CENTER);
        }
        Notification.show("New LinkPartner configuration successfully created", (int) Duration.ofSeconds(5).toMillis(), Notification.Position.TOP_CENTER);
        if (parentDialog != null) {
            parentDialog.close();
        }
    }

    public WizardComponent getWizard() {
        return this.wizard;
    }


    public class CreateLinkPartnerStep extends VerticalLayout implements WizardStep {

        private DCLinkPartnerPanel dcLinkPartnerPanel;

        public CreateLinkPartnerStep() {
            dcLinkPartnerPanel = applicationContext.getBean(DCLinkPartnerPanel.class);
            dcLinkPartnerPanel.setValue(linkPartner);
            add(dcLinkPartnerPanel);
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public void onForward(Command success) {
            try {
            BinderValidationStatus<DomibusConnectorLinkPartner> validate = dcLinkPartnerPanel.validate();
                if (validate.isOk()) {
                    dcLinkPartnerPanel.writeBean(linkPartner);
                    success.execute();
                } else {
                    String errorMessages = validate.getValidationErrors()
                            .stream()
                            .map(ValidationResult::getErrorMessage)
                            .collect(Collectors.joining(","));
                    ConfirmDialogBuilder.getBuilder()
                            .setMessage(errorMessages)
                            .setOnConfirmCallback(() -> {
                                dcLinkPartnerPanel.writeBeanAsDraft(linkPartner);
                                success.execute();
                            })
                            .setOnCancelCallback(() -> {
                                LOGGER.debug("Saving with errors has been canceled!");
                            })
                            .show();
                }
            } catch (ValidationException e) {
                LOGGER.error("Validation exception", e);
            }
        }

        @Override
        public String getStepTitle() {
            return "Create Link Partner Configuration";
        }

    }

    public class ChooseOrCreateLinkConfigurationStep extends VerticalLayout implements WizardStep {

        private final RadioButtonGroup<String> newLinkConfiguration = new RadioButtonGroup<>();
        private static final String EXISTING_LINK_CONFIG = "Use existing Link Configuration";
        private static final String NEW_LINK_CONFIG = "Create new Link Configuration";

        private ComboBox<DomibusConnectorLinkConfiguration> linkConfigurationChooser = new ComboBox<>();

        private DCLinkConfigPanel linkConfigPanel;

        public ChooseOrCreateLinkConfigurationStep() {
            initUI();

//            linkConfigPanel.setNewConfiguration(true);
        }



        private void initUI() {



            //the radio button group to switch mode between new or existing link config
            newLinkConfiguration.setItems(EXISTING_LINK_CONFIG, NEW_LINK_CONFIG);
            newLinkConfiguration.setValue(NEW_LINK_CONFIG);
            newLinkConfiguration.addValueChangeListener(this::newExistingLinkConfigChanged);

            //choose an existing link configuration
            linkConfigurationChooser.addValueChangeListener(this::linkConfigChanged);
            linkConfigurationChooser.setAllowCustomValue(false);
            linkConfigurationChooser.setItems(dcLinkPersistenceService.getAllLinkConfigurations());
            linkConfigurationChooser.setRequired(true);
            linkConfigurationChooser.setItemLabelGenerator(item -> {
                if (item != null) {
                    return item.getConfigName() + " with impl " + item.getLinkImpl();
                }
                return "No Configuration set";
            });

            //the link configuration panel
            linkConfigPanel = applicationContext.getBean(DCLinkConfigPanel.class);
            linkConfigPanel.setLinkConfiguration(linkConfiguration);

            add(newLinkConfiguration);
            add(linkConfigurationChooser);
            add(linkConfigPanel);

            newLinkConfiguration.setValue(EXISTING_LINK_CONFIG);
        }

        private void newExistingLinkConfigChanged(HasValue.ValueChangeEvent<String> valueChangeEvent) {
            if (NEW_LINK_CONFIG.equals(valueChangeEvent.getValue())) {
                linkConfigPanel.setReadOnly(false);
                linkConfigurationChooser.setReadOnly(true);
                DomibusConnectorLinkConfiguration newLinkConfig = new DomibusConnectorLinkConfiguration();
                newLinkConfig.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName("changeme"));
                linkConfiguration = newLinkConfig;
                linkConfigPanel.setValue(linkConfiguration);

            } else if (EXISTING_LINK_CONFIG.equals(valueChangeEvent.getValue())) {
                linkConfigPanel.setReadOnly(true);
                linkConfigurationChooser.setReadOnly(false);
            }
        }

        private void linkConfigChanged(AbstractField.ComponentValueChangeEvent<ComboBox<DomibusConnectorLinkConfiguration>, DomibusConnectorLinkConfiguration> changeEvent) {
            DomibusConnectorLinkConfiguration value = changeEvent.getValue();
            if (value != null) {
                linkConfiguration = value;
                linkConfigPanel.setValue(linkConfiguration);
                linkConfigPanel.setVisible(true);
            } else {
                linkConfigPanel.setVisible(false);
            }
        }

        @Override
        public Component getComponent() {
            return this;
        }



        @Override
        public void onForward(Command success) {
            BinderValidationStatus<DomibusConnectorLinkConfiguration> validate;
            validate = linkConfigPanel.validate();

            if (validate.hasErrors()) {
                String errorMessages = validate.getValidationErrors()
                        .stream()
                        .map(ValidationResult::getErrorMessage)
                        .collect(Collectors.joining(","));
                ConfirmDialogBuilder.getBuilder()
                        .setMessage(errorMessages)
                        .setOnConfirmCallback(() -> {
                            linkConfigPanel.writeBeanAsDraft(linkConfiguration);
                            success.execute();
                        })
                        .setOnCancelCallback(() -> {
                            LOGGER.debug("Saving with errors has been canceled!");
                        })
                        .show();
            } else {
                try {
                    linkConfigPanel.writeBean(linkConfiguration);
                    success.execute();

                } catch (ValidationException e) {
                    LOGGER.error("Validation Exception", e);

                }
            }
        }

        @Override
        public String getStepTitle() {
            return "Configure Link Implementation";
        }
    }



}

package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import eu.domibus.connector.link.service.DCLinkPluginConfiguration;
import eu.domibus.connector.web.component.WizardComponent;
import eu.domibus.connector.web.component.WizardStep;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@Route("createlink")
@Profile(DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME)
public class CreateLinkPanel extends VerticalLayout {

    private static final Logger LOGGER = LogManager.getLogger(CreateLinkPanel.class);

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    DCLinkPersistenceService dcLinkPersistenceService;

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    @Autowired
    ApplicationContext applicationContext;

    private WizardComponent wizard;
    private Dialog parentDialog;

    private DomibusConnectorLinkConfiguration linkConfiguration;
    private DomibusConnectorLinkPartner linkPartner;




//    ListConfigurationPropertiesComponent listConfigurationPropertiesComponentLinkImpl;




    @PostConstruct
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
                .addStep(new ChooseImplStep())
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


        private final ListConfigurationPropertiesComponent configPropsList;
        private TextField linkPartnerName = new TextField();
        private TextField description = new TextField();
        private Checkbox enabled = new Checkbox();
        private Binder<DomibusConnectorLinkPartner> linkPartnerBinder = new Binder<>();

        public CreateLinkPartnerStep() {
            linkPartnerName.setLabel("Link Partner Name");
            linkPartnerBinder.forField(linkPartnerName)
                    .withNullRepresentation("")
                    .withValidator(new Validator<String>() {
                        @Override
                        public ValidationResult apply(String value, ValueContext context) {
                            if (StringUtils.isEmpty(value)) {
                                return ValidationResult.error("Must not empty!");
                            }
                            return ValidationResult.ok();
                        }
                    })
                    .bind(
                            (ValueProvider<DomibusConnectorLinkPartner, String>) linkPartner -> linkPartner.getLinkPartnerName() == null ? null : linkPartner.getLinkPartnerName().getLinkName(),
                            (Setter<DomibusConnectorLinkPartner, String>) (linkPartner, s) -> linkPartner.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(s)));
            add(linkPartnerName);
            description.setLabel("Description");
            linkPartnerBinder.forField(description)
                    .withNullRepresentation("")
                    .bind(DomibusConnectorLinkPartner::getDescription, DomibusConnectorLinkPartner::setDescription);
            add(description);
            enabled.setLabel("Enabled");
            linkPartnerBinder.forField(enabled)
                    .bind(DomibusConnectorLinkPartner::isEnabled, DomibusConnectorLinkPartner::setEnabled);
            add(enabled);

            configPropsList = applicationContext.getBean(ListConfigurationPropertiesComponent.class);
//            linkConfiguration.getLinkImpl()

            Optional<LinkPlugin> linkPluginByName = linkManagerService.getLinkPluginByName(linkConfiguration.getLinkImpl());
            if (linkPluginByName.isPresent()) {
                List<Class> partnerConfigurationPropertyClasses = linkPluginByName.get().getPartnerConfigurationProperties();
                Collection<ConfigurationProperty> configProperties = partnerConfigurationPropertyClasses.stream()
                        .map(clz -> configurationPropertyCollector.getConfigurationPropertyFromClazz(clz).stream())
                        .flatMap(Function.identity())
                        .collect(Collectors.toList());
                configPropsList.setConfigurationProperties(configProperties);
            }
            linkPartnerBinder.setBean(linkPartner);
            add(configPropsList);
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public boolean onForward() {
            BinderValidationStatus<DomibusConnectorLinkPartner> validate = this.linkPartnerBinder.validate();
            BinderValidationStatus<Properties> configPropsList = this.configPropsList.getBinder().validate();
            if (validate.isOk() && configPropsList.isOk()) {
                linkPartner = linkPartnerBinder.getBean();
                Properties bean = configPropsList.getBinder().getBean();
                linkPartner.setProperties(bean);
                return true;
            }
            return false;

        }

        @Override
        public String getStepTitle() {
            return "Create Link Partner Configuration";
        }

    }

    public class ChooseImplStep extends VerticalLayout implements WizardStep {


//        private ComboBox<LinkPlugin> implChooser = new ComboBox<>();
//        private ListConfigurationPropertiesComponent configPropsList;
        private ComboBox<DomibusConnectorLinkConfiguration> linkConfigurationChooser = new ComboBox<>();
//        private TextField linkConfigName = new TextField();
//        private Binder<DomibusConnectorLinkConfiguration> linkConfigurationBinder = new Binder<>();
//
//        private DomibusConnectorLinkConfiguration newLinkConfiguration = new DomibusConnectorLinkConfiguration();
//        private boolean newConfig = true;

        private DCLinkConfigPanel linkConfigPanel;

        public ChooseImplStep() {
            initUI();

//            linkConfigPanel.setNewConfiguration(true);
        }



        private void initUI() {
            linkConfigPanel = applicationContext.getBean(DCLinkConfigPanel.class);
            linkConfigPanel.setLinkConfiguration(linkConfiguration);

            add(linkConfigurationChooser);

            linkConfigurationChooser.addValueChangeListener(this::linkConfigChanged);
            linkConfigurationChooser.setItemLabelGenerator(item -> {
                if (item != null) {
                    return item.getConfigName() + " with impl " + item.getLinkImpl();
                }
                return "No Configuration set";
            });
        }
//
////            List<DomibusConnectorLinkConfiguration> allLinkConfigurations = dcLinkPersistenceService.getAllLinkConfigurations();
////            linkConfigurationChooser.setItems(allLinkConfigurations);
////            linkConfigurationChooser.setPlaceholder("New Config");
////            linkConfigurationChooser.setClearButtonVisible(true);
////            linkConfigurationChooser.setLabel("Link Configuration Name");
////            linkConfigurationChooser.setAllowCustomValue(false);
////            linkConfigurationChooser.addCustomValueSetListener(this::customValueAdded);
////            linkConfigurationChooser.setMinWidth("10em");
//
//
//
//            this.configPropsList = applicationContext.getBean(ListConfigurationPropertiesComponent.class);
////            configPropsList.setSizeFull();
//
//            implChooser.setItems(linkManagerService.getAvailableLinkPlugins());
//
//            implChooser.setLabel("Link Implementation");
//            implChooser.setItemLabelGenerator((ItemLabelGenerator<LinkPlugin>) LinkPlugin::getPluginName);
//            implChooser.addValueChangeListener(this::choosenLinkImplChanged);
//            implChooser.setMinWidth("10em");
//            update();
//
//            linkConfigurationBinder
//                    .forField(implChooser)
//                    .bind(
//                            (ValueProvider<DomibusConnectorLinkConfiguration, LinkPlugin>) linkConfiguration -> {
//                                Optional<LinkPlugin> linkPlugin = linkManagerService.getLinkPluginByName(linkConfiguration.getLinkImpl());
//                                if (linkPlugin.isPresent()) {
//                                    return linkPlugin.get();
//                                } else {
//                                    LOGGER.warn("No Implemntation found for [{}]", linkConfiguration.getLinkImpl());
//                                    return null;
//                                }
//                            },
//                            (Setter<DomibusConnectorLinkConfiguration, LinkPlugin>) (linkConfiguration, linkPlugin) -> linkConfiguration.setLinkImpl(linkPlugin == null ? null : linkPlugin.getPluginName())
//                    );
//
//
//
//            add(implChooser);
////            add(linkConfigName);
////            linkConfigName.setLabel("Link Configuration Name");
////            linkConfigurationBinder
////                    .forField(linkConfigName)
////                    .asRequired()
////                    .withNullRepresentation("")
////                    .bind(
////                            c -> c.getConfigName() == null ? null : c.getConfigName().getConfigName(),
////                            (c, v) -> c.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(v)));
//
//
//            add(configPropsList);
//
//
////            listConfigurationPropertiesComponentLinkImpl.setSizeFull();
////            listConfigurationPropertiesComponentLinkImpl.setVisible(true);
////            ChooseImplStep.this.setSizeFull();
//
//        }
//
//        public void setLinkConfiguration(DomibusConnectorLinkConfiguration linkConfig) {
//            newLinkConfiguration = linkConfig;
//            linkConfigurationBinder.setBean(linkConfig);
//            newConfig = false;
//            update();
//        }
//
//        private void update() {
//            implChooser.setEnabled(!newConfig);
//            configPropsList.setReadOnly(!newConfig);
//        }
//
////        private void customValueAdded(GeneratedVaadinComboBox.CustomValueSetEvent<ComboBox<DomibusConnectorLinkConfiguration>> comboBoxCustomValueSetEvent) {
////            String newConfigName = comboBoxCustomValueSetEvent.getDetail();
////
////            newLinkConfiguration.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(newConfigName));
////            newLinkConfiguration.setLinkImpl("");
////            newLinkConfiguration.setProperties(new Properties());
////
////            List<DomibusConnectorLinkConfiguration> allLinkConfigurations = dcLinkPersistenceService.getAllLinkConfigurations();
////            allLinkConfigurations.add(newLinkConfiguration);
////            linkConfigurationChooser.setItems(allLinkConfigurations);
////
////            newConfig = true;
////            linkConfigurationChooser.setValue(newLinkConfiguration);
////
////        }
//


        private void linkConfigChanged(AbstractField.ComponentValueChangeEvent<ComboBox<DomibusConnectorLinkConfiguration>, DomibusConnectorLinkConfiguration> changeEvent) {
//            DomibusConnectorLinkConfiguration value = changeEvent.getValue();
//
//            if (newConfig) {
//                implChooser.setReadOnly(false);
//                configPropsList.setReadOnly(false);
//                linkConfiguration = newLinkConfiguration;
//            } else {
//                implChooser.setReadOnly(true);
//                configPropsList.setReadOnly(false);
//                linkConfiguration = value;
//            }
//            linkConfigurationBinder.setBean(linkConfiguration);
//            newConfig = false;
        }
//
//        private void choosenLinkImplChanged(HasValue.ValueChangeEvent<LinkPlugin> valueChangeEvent) {
//            LinkPlugin value = valueChangeEvent.getValue();
//            List<Class> configurationClasses = new ArrayList<>();
//            if (value != null) {
//                configurationClasses = value.getPluginConfigurationProperties();
//            }
//
//            List<ConfigurationProperty> configurationProperties = configurationClasses.stream()
//                    .map(clz -> configurationPropertyCollector.getConfigurationPropertyFromClazz(clz).stream())
//                    .flatMap(Function.identity()).collect(Collectors.toList());
//
//            configPropsList.setConfigurationProperties(configurationProperties);
////
////            Binder<Properties> binder = new Binder();
////            binder.setBean(domibusConnectorLinkConfiguration.getProperties());
////
////





        @Override
        public Component getComponent() {
            return linkConfigPanel;
        }

        @Override
        public boolean onForward() {
//            List<ValidationResult> validate = configPropsList.validate();
//            Properties bean = configPropsList.getBinder().getBean();
//
//            BinderValidationStatus<DomibusConnectorLinkConfiguration> linkConfigurationValidation = linkConfigurationBinder.validate();

            List<ValidationResult> validate =  linkConfigPanel.validate();


            if(validate.isEmpty()) {
//                linkConfiguration.setProperties(bean);
                return true;
            }
            LOGGER.debug("onForward# Validation failed, returning false");
            return false;
        }

        @Override
        public String getStepTitle() {
            return "Configure Link Implementation";
        }
    }



}

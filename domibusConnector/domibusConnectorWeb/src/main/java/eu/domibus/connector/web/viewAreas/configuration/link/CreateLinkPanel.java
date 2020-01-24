package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import eu.domibus.connector.web.component.WizardComponent;
import eu.domibus.connector.web.component.WizardStep;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@Route("createlink")
public class CreateLinkPanel extends VerticalLayout {


    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    DCLinkPersistenceService dcLinkPersistenceService;

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    @Autowired
    ApplicationContext applicationContext;

    private WizardComponent wizard;

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
                .build();
        add(wizard);
    }


    public class CreateLinkPartnerStep extends VerticalLayout implements WizardStep {


        private final ListConfigurationPropertiesComponent configPropsList;
        private TextField linkPartnerName = new TextField();
        private TextField description = new TextField();
        private Checkbox enabled = new Checkbox();

        public CreateLinkPartnerStep() {
            linkPartnerName.setLabel("Link Partner Name");
            add(linkPartnerName);
            description.setLabel("Description");
            add(description);
            enabled.setLabel("Enabled");
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

            add(configPropsList);
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public boolean onForward() {


            return false;

        }

        @Override
        public String getStepTitle() {
            return "Create Link Partner Configuration";
        }

    }

    public class ChooseImplStep extends VerticalLayout implements WizardStep {


        private ComboBox<LinkPlugin> implChooser = new ComboBox<>();
        private ListConfigurationPropertiesComponent configPropsList;
        private ComboBox<DomibusConnectorLinkConfiguration> linkConfigurationChooser = new ComboBox<>();
        private TextField linkConfigName = new TextField();
        private Binder<DomibusConnectorLinkConfiguration> linkConfigurationBinder = new Binder<>();

        public ChooseImplStep() {
            initUI();
        }



        private void initUI() {

            add(linkConfigurationChooser);

            linkConfigurationChooser.addValueChangeListener(this::linkConfigChanged);
            linkConfigurationChooser.setItemLabelGenerator((ItemLabelGenerator<DomibusConnectorLinkConfiguration>) item -> item.getConfigName() + " with impl " + item.getLinkImpl());

            List<DomibusConnectorLinkConfiguration> allLinkConfigurations = dcLinkPersistenceService.getAllLinkConfigurations();
            linkConfigurationChooser.setItems(allLinkConfigurations);
            linkConfigurationChooser.setPlaceholder("New Config");


            this.configPropsList = applicationContext.getBean(ListConfigurationPropertiesComponent.class);
//            configPropsList.setSizeFull();

            implChooser.setItems(linkManagerService.getAvailableLinkPlugins());
            implChooser.setItemLabelGenerator((ItemLabelGenerator<LinkPlugin>) LinkPlugin::getPluginName);
            implChooser.addValueChangeListener(this::choosenLinkImplChanged);
            linkConfigurationBinder
                    .forField(implChooser)
                    .bind(
                            (ValueProvider<DomibusConnectorLinkConfiguration, LinkPlugin>) linkConfiguration -> linkManagerService.getLinkPluginByName(linkConfiguration.getLinkImpl()).get(),
                            (Setter<DomibusConnectorLinkConfiguration, LinkPlugin>) (linkConfiguration, linkPlugin) -> linkConfiguration.setLinkImpl(linkPlugin.getPluginName())
                    );



            add(implChooser);
            add(linkConfigName);
            linkConfigurationBinder
                    .forField(linkConfigName)
                    .asRequired()
                    .withNullRepresentation("")
                    .bind(c -> c.getConfigName().getConfigName(), (c, v) -> c.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(v)));


            add(configPropsList);


//            listConfigurationPropertiesComponentLinkImpl.setSizeFull();
//            listConfigurationPropertiesComponentLinkImpl.setVisible(true);
//            ChooseImplStep.this.setSizeFull();

        }

        private void linkConfigChanged(AbstractField.ComponentValueChangeEvent<ComboBox<DomibusConnectorLinkConfiguration>, DomibusConnectorLinkConfiguration> changeEvent) {
            DomibusConnectorLinkConfiguration value = changeEvent.getValue();

            if (value == null) {
                implChooser.setVisible(true);
                configPropsList.setVisible(true);
                linkConfiguration = new DomibusConnectorLinkConfiguration();
            } else {
                implChooser.setVisible(false);
                configPropsList.setVisible(false);
                linkConfiguration = value;
            }
            linkConfigurationBinder.setBean(linkConfiguration);


        }

        private void choosenLinkImplChanged(HasValue.ValueChangeEvent<LinkPlugin> valueChangeEvent) {
            LinkPlugin value = valueChangeEvent.getValue();
            List<Class> configurationClasses = value.getPluginConfigurationProperties();

            List<ConfigurationProperty> configurationProperties = configurationClasses.stream()
                    .map(clz -> configurationPropertyCollector.getConfigurationPropertyFromClazz(clz).stream())
                    .flatMap(Function.identity()).collect(Collectors.toList());

            configPropsList.setConfigurationProperties(configurationProperties);
//
//            Binder<Properties> binder = new Binder();
//            binder.setBean(domibusConnectorLinkConfiguration.getProperties());
//
//
        }


        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public boolean onForward() {
            List<ValidationResult> validate = configPropsList.validate();
            Properties bean = configPropsList.getBinder().getBean();

            BinderValidationStatus<DomibusConnectorLinkConfiguration> validate1 = linkConfigurationBinder.validate();

            if(validate.isEmpty() && validate1.isOk()) {
                linkConfiguration.setProperties(bean);
                return true;
            }
            return false;
        }

        @Override
        public String getStepTitle() {
            return "Configure Link Implementation";
        }
    }



}

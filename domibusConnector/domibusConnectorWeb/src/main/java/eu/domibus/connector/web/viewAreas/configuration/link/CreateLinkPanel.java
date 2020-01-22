package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@Route("createlink")
public class CreateLinkPanel extends VerticalLayout {

    private ProgressBar progressBar = new ProgressBar(10, 100, 10);
    private Button nextButton = new Button("Next");
    private Button backButton = new Button("Back");
    private Text header = new Text ("Create Link");
    private Div content = new Div();

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    DCLinkPersistenceService dcLinkPersistenceService;

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    @Autowired
    ApplicationContext applicationContext;
//    ListConfigurationPropertiesComponent listConfigurationPropertiesComponentLinkImpl;


//    @Autowired
//    ConfigurationFormFactory configurationFormFactory;

    List<WizardStep> steps = new ArrayList<>();

    DomibusConnectorLinkConfiguration linkConfiguration;
    private WizardStep activeStep;


    @PostConstruct
    private void init() {
        linkConfiguration = new DomibusConnectorLinkConfiguration();

        initUI();
        initSteps();
    }

    private void initUI() {

        initSteps();

        add(header);
        add(progressBar);
        addAndExpand(new HorizontalLayout(backButton, nextButton));
        addAndExpand(content);
        nextButton.addClickListener(this::forwardButtonClicked);

        //set first step...
        this.content.setSizeFull();


        this.setActiveStep(this.steps.get(0));

        this.progressBar.setMin(0);
        this.progressBar.setMax((this.steps.size()));
        this.progressBar.setValue(1);

    }

    private void setActiveStep(WizardStep wizardStep) {
        this.content.removeAll();
        this.content.add(wizardStep.getComponent());
        this.activeStep = wizardStep;
    }

    private void forwardButtonClicked(ClickEvent<Button> buttonClickEvent) {
        boolean forward = this.activeStep.forward();
        if (forward) {
            steps.indexOf(activeStep);
            
        }
    }

    private void initSteps() {

        this.steps.add(new ChooseImplStep());
    }

    public interface WizardStep {
        Component getComponent();
        boolean forward();
    }

    public class CreateLinkPartnerStep extends VerticalLayout implements WizardStep {

        @Override
        public Component getComponent() {
            return null;
        }

        @Override
        public boolean forward() {
            return false;
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
        public boolean forward() {
            List<ValidationResult> validate = configPropsList.validate();
            Properties bean = configPropsList.getBinder().getBean();

            if(validate.isEmpty()) {
                linkConfiguration.setProperties(bean);
                return true;
            }
            return false;
        }
    }



}

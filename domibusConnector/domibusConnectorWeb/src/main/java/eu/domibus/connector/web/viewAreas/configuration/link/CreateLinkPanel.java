package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component
@Route("createlink")
public class CreateLinkPanel extends VerticalLayout {

    private ProgressBar progressBar = new ProgressBar(10, 100, 10);
    private Button forwardButton = new Button("Next");
    private Button backButton = new Button("Back");
    private Text header = new Text ("Create Link");
    private Div content = new Div();

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    @Autowired
    ApplicationContext applicationContext;
//    ListConfigurationPropertiesComponent listConfigurationPropertiesComponentLinkImpl;


//    @Autowired
//    ConfigurationFormFactory configurationFormFactory;

    List<Component> steps = new ArrayList<>();

    private DomibusConnectorLinkConfiguration domibusConnectorLinkConfiguration;


    @PostConstruct
    private void init() {
        domibusConnectorLinkConfiguration = new DomibusConnectorLinkConfiguration();

        initUI();
        initSteps();
    }

    private void initUI() {

        initSteps();

        add(header);
        add(progressBar);
        addAndExpand(content);
        addAndExpand(new HorizontalLayout(backButton, forwardButton));
        forwardButton.addClickListener(this::forwardButtonClicked);

        //set first step...
        this.content.setSizeFull();
        this.content.add(this.steps.get(0));



    }

    private void forwardButtonClicked(ClickEvent<Button> buttonClickEvent) {

    }

    private void initSteps() {

        this.steps.add(new ChooseImplStep());
    }

    public interface WizardStep {
        boolean forward();
    }


    public class ChooseImplStep extends VerticalLayout implements WizardStep {

        private ComboBox<LinkPlugin> implChooser = new ComboBox<>();
        private ListConfigurationPropertiesComponent configPropsList;

        public ChooseImplStep() {
            initUI();
        }

        private void initUI() {

            this.configPropsList = applicationContext.getBean(ListConfigurationPropertiesComponent.class);
            configPropsList.setSizeFull();

            implChooser.setItems(linkManagerService.getAvailableLinkPlugins());
            implChooser.setItemLabelGenerator((ItemLabelGenerator<LinkPlugin>) LinkPlugin::getPluginName);
            implChooser.addValueChangeListener(this::choosenLinkImplChanged);

            this.addAndExpand(implChooser, configPropsList);


//            listConfigurationPropertiesComponentLinkImpl.setSizeFull();
//            listConfigurationPropertiesComponentLinkImpl.setVisible(true);
//            ChooseImplStep.this.setSizeFull();

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
        public boolean forward() {
            configPropsList.validate();

            return false;
        }
    }



}

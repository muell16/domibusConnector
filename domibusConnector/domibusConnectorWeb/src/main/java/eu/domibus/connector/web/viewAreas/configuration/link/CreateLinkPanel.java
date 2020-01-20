package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.ConfigurationFormFactory;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
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
    private Button forwardButton = new Button("Next");
    private Button backButton = new Button("Back");
    private Text header = new Text ("Create Link");
    private Div content = new Div();

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    @Autowired
    ConfigurationFormFactory configurationFormFactory;

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
        add(content);
        add(new HorizontalLayout(backButton, forwardButton));

        this.content.add(this.steps.get(0));



    }

    private void initSteps() {
        this.steps.add(new ChooseImplStep());
    }

    public class ChooseImplStep extends VerticalLayout {
        private ComboBox<LinkPlugin> implChooser = new ComboBox<>();

        public ChooseImplStep() {
            initUI();
        }

        private void initUI() {

            implChooser.setItems(linkManagerService.getAvailableLinkPlugins());
            implChooser.setItemLabelGenerator((ItemLabelGenerator<LinkPlugin>) LinkPlugin::getPluginName);
            add(implChooser);
            implChooser.addValueChangeListener(this::valueChanged);

        }


        private void valueChanged(HasValue.ValueChangeEvent<LinkPlugin> valueChangeEvent) {
            LinkPlugin value = valueChangeEvent.getValue();
            List<Class> configurationClasses = value.getPluginConfigurationProperties();

            List<ConfigurationProperty> configurationProperties = configurationClasses.stream()
                    .map(clz -> configurationPropertyCollector.getConfigurationPropertyFromClazz(clz).stream())
                    .flatMap(Function.identity()).collect(Collectors.toList());

//            domibusConnectorLinkConfiguration.getProperties();
//
//            Binder<Properties> binder = new Binder();
//            binder.setBean(domibusConnectorLinkConfiguration.getProperties());
//
//

        }


    }



}

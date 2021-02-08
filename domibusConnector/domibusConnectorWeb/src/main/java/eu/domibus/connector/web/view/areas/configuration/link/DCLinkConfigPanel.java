package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DCLinkConfigPanel extends VerticalLayout
        implements HasValue<HasValue.ValueChangeEvent<DomibusConnectorLinkConfiguration>, DomibusConnectorLinkConfiguration>,
        HasValidator<DomibusConnectorLinkConfiguration>
{

    private static final Logger LOGGER = LogManager.getLogger(DCLinkConfigPanel.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    private ComboBox<LinkPlugin> implChooser = new ComboBox<>();
    private DCConfigurationPropertiesListField configPropsList;
    private TextField linkConfigName = new TextField();
    private Binder<DomibusConnectorLinkConfiguration> linkConfigurationBinder = new Binder<>();
    private DomibusConnectorLinkConfiguration newLinkConfiguration = new DomibusConnectorLinkConfiguration();
    private boolean readOnly = false;

    public DCLinkConfigPanel() {
    }

    @PostConstruct
    private void initUI() {

        this.configPropsList = applicationContext.getBean(DCConfigurationPropertiesListField.class);

        implChooser.setItems(linkManagerService.getAvailableLinkPlugins());
        implChooser.setLabel("Link Implementation");
        implChooser.setItemLabelGenerator((ItemLabelGenerator<LinkPlugin>) LinkPlugin::getPluginName);
        implChooser.addValueChangeListener(this::choosenLinkImplChanged);
        implChooser.setMinWidth("10em");
        update();

        linkConfigurationBinder
                .forField(linkConfigName)
                .bind(
                        (ValueProvider<DomibusConnectorLinkConfiguration, String>) linkConfiguration -> linkConfiguration.getConfigName() == null ? "" : linkConfiguration.getConfigName().toString(),
                        (Setter<DomibusConnectorLinkConfiguration, String>) (linkConfiguration, configName) -> linkConfiguration.setConfigName(configName == null ? new DomibusConnectorLinkConfiguration.LinkConfigName("") : new DomibusConnectorLinkConfiguration.LinkConfigName(configName))
                );

        linkConfigurationBinder
                .forField(implChooser)
                .bind(
                        (ValueProvider<DomibusConnectorLinkConfiguration, LinkPlugin>) linkConfiguration -> {
                            Optional<LinkPlugin> linkPlugin = linkManagerService.getLinkPluginByName(linkConfiguration.getLinkImpl());
                            if (linkPlugin.isPresent()) {
                                return linkPlugin.get();
                            } else {
                                LOGGER.warn("No Implementation found for [{}]", linkConfiguration.getLinkImpl());
                                return null;
                            }
                        },
                        (Setter<DomibusConnectorLinkConfiguration, LinkPlugin>) (linkConfiguration, linkPlugin) -> linkConfiguration.setLinkImpl(linkPlugin == null ? null : linkPlugin.getPluginName())
                );

//        linkConfigurationBinder
//                .forField(configPropsList)
//                .bind(
//                        (ValueProvider<DomibusConnectorLinkConfiguration, Properties>) linkConfiguration -> linkConfiguration.getProperties(),
//                        (Setter<DomibusConnectorLinkConfiguration, Properties>) (linkConfiguration, linkProps) -> linkConfiguration.setProperties(linkProps)
//                );

        add(linkConfigName);
        add(implChooser);
        add(configPropsList);


    }

    public DomibusConnectorLinkConfiguration getLinkConfiguration() {
        return newLinkConfiguration;
    }

    public void setLinkConfiguration(DomibusConnectorLinkConfiguration linkConfig) {
        newLinkConfiguration = linkConfig;
        linkConfigurationBinder.setBean(linkConfig);
        update();
    }

    private void update() {
        implChooser.setReadOnly(readOnly);
//        configPropsList.setReadOnly(readOnly);
    }

    private void choosenLinkImplChanged(HasValue.ValueChangeEvent<LinkPlugin> valueChangeEvent) {
        LinkPlugin value = valueChangeEvent.getValue();
        List<Class> configurationClasses = new ArrayList<>();
        if (value != null) {
            configurationClasses = value.getPluginConfigurationProperties();
        }

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

    public List<ValidationResult> validate() {
        List<ValidationResult> validate = configPropsList.validate();


        BinderValidationStatus<DomibusConnectorLinkConfiguration> linkConfigurationValidation = linkConfigurationBinder.validate();

        validate.addAll(linkConfigurationValidation.getBeanValidationErrors());

        return validate;

    }

    public void writeBean() {
        List<ValidationResult> validate = validate();
        if (validate.isEmpty()) {
            Properties properties = configPropsList.getBinder().getBean();
//            newLinkConfiguration.setProperties(properties);
        }

    }



    @Override
    public void setValue(DomibusConnectorLinkConfiguration value) {
        this.setLinkConfiguration(value);
    }

    @Override
    public DomibusConnectorLinkConfiguration getValue() {
        return this.linkConfigurationBinder.getBean();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<DomibusConnectorLinkConfiguration>> listener) {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        update();
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        //no indicator support
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }
}
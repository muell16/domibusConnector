package eu.domibus.connector.ui.view.areas.configuration.link;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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


    private final ApplicationContext applicationContext;
    private final DCActiveLinkManagerService linkManagerService;
    private final ConfigurationPropertyCollector configurationPropertyCollector;

    private ComboBox<LinkPlugin> implChooser = new ComboBox<>();
    private DCConfigurationPropertiesListField configPropsList;
    private TextField linkConfigName;
    private Binder<DomibusConnectorLinkConfiguration> linkConfigurationBinder = new Binder<>();
    private DomibusConnectorLinkConfiguration linkConfiguration = new DomibusConnectorLinkConfiguration();
    private boolean readOnly = false;
    private boolean implChangeAble = true;

    public DCLinkConfigPanel(ApplicationContext applicationContext, DCActiveLinkManagerService linkManagerService, ConfigurationPropertyCollector configurationPropertyCollector) {
        this.applicationContext = applicationContext;
        this.linkManagerService = linkManagerService;
        this.configurationPropertyCollector = configurationPropertyCollector;
        initUI();
    }

    private void initUI() {



        linkConfigName = new TextField("Link Configuration Name");

        implChooser.setItems(linkManagerService.getAvailableLinkPlugins());
        implChooser.setLabel("Link Implementation");
        implChooser.setItemLabelGenerator((ItemLabelGenerator<LinkPlugin>) LinkPlugin::getPluginName);
        implChooser.addValueChangeListener(this::choosenLinkImplChanged);
        implChooser.setMinWidth("10em");


        linkConfigurationBinder
                .forField(linkConfigName)
                .withValidator((Validator<String>) (value, context) -> {
                    if (StringUtils.isEmpty(value)) {
                        return ValidationResult.error("Must not be emtpy!");
                    }
                    return ValidationResult.ok();
                })
                .bind(
                        (ValueProvider<DomibusConnectorLinkConfiguration, String>) linkConfiguration -> linkConfiguration.getConfigName() == null ? "" : linkConfiguration.getConfigName().toString(),
                        (Setter<DomibusConnectorLinkConfiguration, String>) (linkConfiguration, configName) -> linkConfiguration.setConfigName(configName == null ? new DomibusConnectorLinkConfiguration.LinkConfigName("") : new DomibusConnectorLinkConfiguration.LinkConfigName(configName))
                );

        linkConfigurationBinder
                .forField(implChooser)
                .withValidator((Validator<? super LinkPlugin>) (value, context) -> {
                    if (value == null) {
                        return ValidationResult.error("Must be set!");
                    }
                    return ValidationResult.ok();
                })
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

        configPropsList = applicationContext.getBean(DCConfigurationPropertiesListField.class);
        configPropsList.setLabel("Link Configuration Properties");
        configPropsList.setSizeFull();
        linkConfigurationBinder
            .forField(configPropsList)
//            .withValidator((Validator<? super Map<String, String>>) (value, context) -> {
//                List<ValidationResult> validate = configPropsList.validate();
//                if (validate.isEmpty()) {
//                    return ValidationResult.ok();
//                } else {
//                    return ValidationResult.error("Invalid Properties!");
//                }
//            })
            .bind(DomibusConnectorLinkConfiguration::getProperties, DomibusConnectorLinkConfiguration::setProperties);

        add(linkConfigName, implChooser, configPropsList);

        updateUI();

    }

    public void setLinkConfiguration(DomibusConnectorLinkConfiguration linkConfig) {
        linkConfiguration = linkConfig;
//        linkConfigurationBinder.readBean(linkConfig);
        linkManagerService.getLinkPluginByName(linkConfig.getLinkImpl()).ifPresent(this::updateConfigurationProperties);
        linkConfigurationBinder.setBean(linkConfig);
        updateUI();
    }

    private void updateUI() {
        implChooser.setReadOnly(readOnly && !implChangeAble);
        linkConfigName.setReadOnly(readOnly);
        configPropsList.setReadOnly(readOnly);
    }

    public void setImplChangeAble(boolean changeAble) {
        this.implChangeAble = changeAble;
        implChooser.setReadOnly(readOnly && !implChangeAble);
    }

    private void choosenLinkImplChanged(HasValue.ValueChangeEvent<LinkPlugin> valueChangeEvent) {
        LinkPlugin value = valueChangeEvent.getValue();
        updateConfigurationProperties(value);
        updateUI();

    }

    private void updateConfigurationProperties(LinkPlugin value) {
        List<Class> configurationClasses = new ArrayList<>();
        if (value != null) {
            configurationClasses = value.getPluginConfigurationProperties();
        }

        List<ConfigurationProperty> configurationProperties = configurationClasses.stream()
                .map(clz -> configurationPropertyCollector.getConfigurationPropertyFromClazz(clz).stream())
                .flatMap(Function.identity()).collect(Collectors.toList());

        configPropsList.setConfigurationProperties(configurationProperties);
    }

    public void writeBean(DomibusConnectorLinkConfiguration lnkConfig) throws ValidationException {
        this.linkConfigurationBinder.writeBean(lnkConfig);
    }

    public void writeBeanAsDraft(DomibusConnectorLinkConfiguration lnkConfig) {
        this.linkConfigurationBinder.writeBeanAsDraft(lnkConfig);
    }

    @Override
    public void setValue(DomibusConnectorLinkConfiguration value) {
        this.setLinkConfiguration(value);
    }

    @Override
    public DomibusConnectorLinkConfiguration getValue() {
        return this.linkConfiguration;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<DomibusConnectorLinkConfiguration>> listener) {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        linkConfigurationBinder.setReadOnly(readOnly);
        this.readOnly = readOnly;
        updateUI();
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

    public BinderValidationStatus<DomibusConnectorLinkConfiguration> validate() {
        return linkConfigurationBinder.validate();
    }

}

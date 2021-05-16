package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.shared.Registration;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DCLinkPartnerPanel extends VerticalLayout
        implements HasValue<HasValue.ValueChangeEvent<DomibusConnectorLinkPartner>, DomibusConnectorLinkPartner>,
        HasValidator<DomibusConnectorLinkPartner>
{

    private static final Logger LOGGER = LogManager.getLogger(DCLinkPartnerPanel.class);

    private final ApplicationContext applicationContext;
    private final DCActiveLinkManagerService linkManagerService;
    private final ConfigurationPropertyCollector configurationPropertyCollector;

    private DomibusConnectorLinkPartner linkPartner;
    private boolean readOnly;

    private Binder<DomibusConnectorLinkPartner> binder = new Binder<>();

    private TextField linkPartnerNameTextField;
    private TextField descriptionTextField;
    private ComboBox<LinkMode> rcvLinkModeComboBox;
    private ComboBox<LinkMode> sendLinkModeComboBox;
    private DCConfigurationPropertiesListField configPropsList;

    public DCLinkPartnerPanel(ApplicationContext applicationContext,
                              DCActiveLinkManagerService linkManagerService,
                              ConfigurationPropertyCollector configurationPropertyCollector) {
        this.applicationContext = applicationContext;
        this.linkManagerService = linkManagerService;
        this.configurationPropertyCollector = configurationPropertyCollector;

        initUI();
    }

    private void initUI() {


        linkPartnerNameTextField = new TextField("Link Partner Name");
        binder.forField(linkPartnerNameTextField)
                .asRequired()
                .withValidator((Validator<String>) (value, context) -> {
                    if (value.isEmpty()) {
                        return ValidationResult.error("Is empty!");
                    }
                    return ValidationResult.ok();
                })
                .bind(p -> p.getLinkPartnerName() == null ? null : p.getLinkPartnerName().getLinkName(),
                        (DomibusConnectorLinkPartner p, String s) -> {
                            p.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(s));
                });
        add(linkPartnerNameTextField);

        descriptionTextField = new TextField("Description");
        binder.bind(descriptionTextField, DomibusConnectorLinkPartner::getDescription, DomibusConnectorLinkPartner::setDescription);
        add(descriptionTextField);


        sendLinkModeComboBox = new ComboBox<>("Sender Mode");
        sendLinkModeComboBox.setItems(LinkMode.values());
        binder.forField(sendLinkModeComboBox)
                .asRequired()
                .withValidator(this::validateSendLinkMode)
                .bind(DomibusConnectorLinkPartner::getSendLinkMode, DomibusConnectorLinkPartner::setSendLinkMode);
        add(sendLinkModeComboBox);

        rcvLinkModeComboBox = new ComboBox<>("Receiver Mode");
        rcvLinkModeComboBox.setItems(LinkMode.values());
        binder.forField(rcvLinkModeComboBox)
                .asRequired()
                .withValidator(this::validateRcvLinkMode)
                .bind(DomibusConnectorLinkPartner::getRcvLinkMode, DomibusConnectorLinkPartner::setRcvLinkMode);
        add(rcvLinkModeComboBox);

        configPropsList = applicationContext.getBean(DCConfigurationPropertiesListField.class);
        binder.forField(configPropsList)
                .bind(DomibusConnectorLinkPartner::getProperties, DomibusConnectorLinkPartner::setProperties);
        add(configPropsList);
        configPropsList.setSizeFull();

        updateUI();
    }

    private ValidationResult validateSendLinkMode(LinkMode linkMode, ValueContext valueContext) {
        Optional<LinkPlugin> linkPluginByName = linkManagerService.getLinkPluginByName(linkPartner.getLinkConfiguration().getLinkImpl());
        if (linkPluginByName.isPresent()) {
            List<LinkMode> rcvItems = linkPluginByName.get().getFeatures()
                    .stream()
                    .map(feature -> {
                        if (PluginFeature.RCV_PULL_MODE == feature) {
                            return LinkMode.PULL;
                        } else if (PluginFeature.RCV_PASSIVE_MODE == feature) {
                            return LinkMode.PASSIVE;
                        } else {
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            if (rcvItems.contains(linkMode)) {
                return ValidationResult.ok();
            } else {
                return ValidationResult.error(String.format("Only [%s] LinkModes  are supported", rcvItems.stream().map(LinkMode::toString).collect(Collectors.joining(","))));
            }
        };
        return ValidationResult.ok();
    }

    private ValidationResult validateRcvLinkMode(LinkMode linkMode, ValueContext valueContext) {
        Optional<LinkPlugin> linkPluginByName = linkManagerService.getLinkPluginByName(linkPartner.getLinkConfiguration().getLinkImpl());
        if (linkPluginByName.isPresent()) {
            List<LinkMode> rcvItems = linkPluginByName.get().getFeatures()
                    .stream()
                    .map(feature -> {
                        if (PluginFeature.SEND_PUSH_MODE == feature) {
                            return LinkMode.PUSH;
                        } else if (PluginFeature.SEND_PASSIVE_MODE == feature) {
                            return LinkMode.PASSIVE;
                        } else {
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            if (rcvItems.contains(linkMode)) {
                return ValidationResult.ok();
            } else {
                return ValidationResult.error(String.format("Only [%s] LinkModes  are supported", rcvItems.stream().map(LinkMode::toString).collect(Collectors.joining(","))));
            }
        };
        return ValidationResult.ok();
    }

    private void updateUI() {
        updatePropertyTable();
    }

    private void updatePropertyTable() {
        if (linkPartner != null
                && linkPartner.getLinkConfiguration() != null
                && linkPartner.getLinkConfiguration().getLinkImpl() != null) {
            String linkImplName = linkPartner.getLinkConfiguration().getLinkImpl();
            Optional<LinkPlugin> linkPluginByName = linkManagerService.getLinkPluginByName(linkImplName);

            if (linkPluginByName.isPresent()) {
                List<Class> configurationClasses = linkPluginByName.get().getPartnerConfigurationProperties();
                List<ConfigurationProperty> configurationProperties = configurationClasses.stream()
                        .map(clz -> configurationPropertyCollector.getConfigurationPropertyFromClazz(clz).stream())
                        .flatMap(Function.identity()).collect(Collectors.toList());
                configPropsList.setConfigurationProperties(configurationProperties);
            } else {
                LOGGER.warn("Did not find a linkimpl for [{}]", linkImplName);
            }
        }

    }


    @Override
    public void setValue(DomibusConnectorLinkPartner value) {
        this.linkPartner = value;
        updateUI();
        binder.setBean(value);
    }

    @Override
    public DomibusConnectorLinkPartner getValue() {
        return this.linkPartner;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<DomibusConnectorLinkPartner>> listener) {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        //no support yet
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    public BinderValidationStatus<DomibusConnectorLinkPartner> validate() {
        return this.binder.validate();
    }

    public void writeBean(DomibusConnectorLinkPartner linkPartner) throws ValidationException {
        this.binder.writeBean(linkPartner);
    }

    public void writeBeanAsDraft(DomibusConnectorLinkPartner linkPartner) {
        this.binder.writeBeanAsDraft(linkPartner);
    }
}

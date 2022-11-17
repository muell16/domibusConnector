package eu.domibus.connector.ui.view.areas.configuration;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.ui.component.DomainSelect;
import eu.domibus.connector.ui.utils.binder.SpringBeanValidationBinderFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConfigurationPanelFactory {

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final SpringBeanValidationBinderFactory springBeanValidationBinderFactory;


    public ConfigurationPanelFactory(ConfigurationPropertyManagerService configurationPropertyManagerService,
                                     SpringBeanValidationBinderFactory springBeanValidationBinderFactory) {
        this.configurationPropertyManagerService = configurationPropertyManagerService;
        this.springBeanValidationBinderFactory = springBeanValidationBinderFactory;
    }

    public <T> ConfigurationPanel<T> createConfigurationPanel(FormLayout form, DomainSelect domainSelect, Class<T> configurationClazz) {
        return new ConfigurationPanel<>(form, domainSelect, configurationClazz);
    }

    public interface DialogCloseCallback {
        public void dialogHasBeenClosed();
    }
    public Dialog showChangedPropertiesDialog(Object boundConfigValue, DomibusConnectorBusinessDomain.BusinessDomainId domainId) {
        return showChangedPropertiesDialog(boundConfigValue, domainId, null);
    }

    public Dialog showChangedPropertiesDialog(Object boundConfigValue, DomibusConnectorBusinessDomain.BusinessDomainId domainId, DialogCloseCallback dialogCloseCallback) {
        Map<String, String> updatedConfiguration = configurationPropertyManagerService.getUpdatedConfiguration(domainId,
                boundConfigValue);

        //use custom callback with overwriting setOpened because, addDialogCloseActionListener does not work
        final Dialog d = new Dialog() {
            public void setOpened(boolean opened) {
                super.setOpened(opened);
                if (!opened && dialogCloseCallback != null) {
                    dialogCloseCallback.dialogHasBeenClosed();
                }
            }
        };

        Grid<Map.Entry<String, String>> g = new Grid<>();
        g.setItems(updatedConfiguration.entrySet());
        g.addColumn(Map.Entry::getKey).setHeader("key");
        g.addColumn(Map.Entry::getValue).setHeader("value");

        Button saveButton = new Button(VaadinIcon.CHECK.create());
        saveButton.addClickListener(clickEvent -> saveUpdatedProperties(d, boundConfigValue.getClass(), updatedConfiguration, domainId));

        Button discardButton = new Button(VaadinIcon.CLOSE.create());
        discardButton.addClickListener(ev -> d.close());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(saveButton, discardButton);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(horizontalLayout);
        verticalLayout.add(g);

        d.add(verticalLayout);
        d.setCloseOnEsc(true);
        d.setSizeFull();
        d.open();
        return d;
    }

    //save changed properties and close dialog
    private void saveUpdatedProperties(Dialog d, Class<?> configurationClazz, Map<String, String> updatedConfiguration, DomibusConnectorBusinessDomain.BusinessDomainId domainId) {
        configurationPropertyManagerService.updateConfiguration(domainId,
                configurationClazz, updatedConfiguration);
        d.close();
    }

    public class ConfigurationPanel<T> extends VerticalLayout implements AfterNavigationObserver {

        private final Label errorField;
        private final FormLayout form;
        private final Class<T> configurationClazz;
        private Binder<T> binder;
        private T boundConfigValue;

        private DomainSelect domainSelect;

        private ConfigurationPanel(FormLayout ecxContainerConfigForm, DomainSelect domainSelect, Class<T> configurationClazz) {
            this.form = ecxContainerConfigForm;
            this.domainSelect = domainSelect;
            this.configurationClazz = configurationClazz;

            this.errorField = new Label("");

            initUI();
        }

        private void initUI() {

            domainSelect.addValueChangeListener(comboBoxBusinessDomainIdComponentValueChangeEvent -> this.refreshUI());

            VerticalLayout configDiv = new VerticalLayout();

            HorizontalLayout saveResetButtonLayout = new HorizontalLayout();
            Button saveChangesButton = new Button("Save Changes");
            saveChangesButton.addClickListener(this::saveChangesButtonClicked);
            saveResetButtonLayout.add(saveChangesButton);

            Button resetChangesButton = new Button("Reset Changes");
            resetChangesButton.addClickListener(this::resetButtonClicked);
            saveResetButtonLayout.add(resetChangesButton);

            configDiv.add(domainSelect);
            configDiv.add(saveResetButtonLayout);
            configDiv.add(errorField);

            binder = springBeanValidationBinderFactory.create(configurationClazz);
            binder.setStatusLabel(errorField);

            binder.bindInstanceFields(form);
            configDiv.add(form);

            add(configDiv);
        }

        private void resetButtonClicked(ClickEvent<Button> buttonClickEvent) {
            T currentConfig = readConfigFromPropertyService();
            binder.readBean(currentConfig); //reset config
        }

        private T readConfigFromPropertyService() {
            return configurationPropertyManagerService.loadConfiguration(
                    domainSelect.getValue(),
                    configurationClazz);
        }

        private void saveChangesButtonClicked(ClickEvent<Button> buttonClickEvent) {

            BinderValidationStatus<T> validate = binder.validate();
            if (validate.isOk()) {
                try {
                    binder.writeBean(this.boundConfigValue);
                } catch (ValidationException e) {
                    //should not occur since validate.isOk()
                    throw new RuntimeException(e);
                }

                showChangedPropertiesDialog(boundConfigValue, domainSelect.getValue());

            } else {
                Notification.show("Error, cannot save due:\n" + validate.getBeanValidationErrors()
                        .stream()
                        .map(vr -> vr.getErrorMessage())
                        .collect(Collectors.joining("\n"))
                );
            }

        }

        @Override
        public void afterNavigation(AfterNavigationEvent event) {
            refreshUI();
        }

        public void refreshUI() {
            this.boundConfigValue = readConfigFromPropertyService();
            binder.setBean(boundConfigValue); //bind bean
        }
    }

}

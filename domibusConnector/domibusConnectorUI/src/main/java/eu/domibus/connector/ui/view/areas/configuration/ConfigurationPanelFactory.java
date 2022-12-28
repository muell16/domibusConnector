package eu.domibus.connector.ui.view.areas.configuration;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.ui.component.DomainSelect;
import eu.domibus.connector.ui.utils.binder.SpringBeanValidationBinderFactory;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ConfigurationPanelFactory {

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final SpringBeanValidationBinderFactory springBeanValidationBinderFactory;

    private final ChangedPropertiesDialogFactory dialogFactory;

    public ConfigurationPanelFactory(ConfigurationPropertyManagerService configurationPropertyManagerService,
                                     SpringBeanValidationBinderFactory springBeanValidationBinderFactory, ChangedPropertiesDialogFactory dialogFactory) {
        this.configurationPropertyManagerService = configurationPropertyManagerService;
        this.springBeanValidationBinderFactory = springBeanValidationBinderFactory;
        this.dialogFactory = dialogFactory;
    }

    public <T> ConfigurationPanel<T> createConfigurationPanel(FormLayout form, DomainSelect domainSelect, Class<T> configurationClazz) {
        return new ConfigurationPanel<>(form, domainSelect, configurationClazz);
    }





    public class ConfigurationPanel<T> extends VerticalLayout implements AfterNavigationObserver {

        private final Label errorField;
        private final FormLayout form;
        private final Class<T> configurationClazz;
        private Binder<T> binder;
        private T boundConfigValue;

        private final DomainSelect domainSelect;

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

                final Dialog changedPropertiesDialog = dialogFactory.createChangedPropertiesDialog(boundConfigValue, domainSelect.getValue());
                changedPropertiesDialog.addOpenedChangeListener(e -> this.refreshUI());

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
            domainSelect.reloadItems(); // todo: really necessary? this worked before???
        }
    }

}

package eu.domibus.connector.ui.view.areas.configuration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ChangedPropertiesDialogFactory {

    private final DCBusinessDomainManager domainManager;
    private final ConfigurationPropertyManagerService configurationPropertyManagerService;

    public ChangedPropertiesDialogFactory(DCBusinessDomainManager domainManager, ConfigurationPropertyManagerService configurationPropertyManagerService) {
        this.domainManager = domainManager;
        this.configurationPropertyManagerService = configurationPropertyManagerService;
    }

    public Dialog createChangedPropertiesDialog(Object config, DomibusConnectorBusinessDomain.BusinessDomainId domainId) {
        // do not use Arrays.asList(config) because when you pass a single parameter of type Object it assumes it is an array.
        return createChangedPropertiesDialog(Collections.singletonList(config), domainId);

    }

    public Dialog createChangedPropertiesDialog(List<Object> configs, DomibusConnectorBusinessDomain.BusinessDomainId domainId) {
        Map<String, String> updatedConfiguration = configurationPropertyManagerService.getUpdatedConfiguration(domainId,
                configs);


        final Button domainValidBadge = new Button();
        domainValidBadge.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        VerticalLayout dialogLayout = new VerticalLayout();

        //use custom callback with overwriting setOpened because, addDialogCloseActionListener does not work
        final Dialog d = new Dialog();
        d.add(dialogLayout);
        d.setCloseOnEsc(true);
        d.setSizeFull();
        d.open();

        Grid<Map.Entry<String, String>> grid = new Grid<>();
        grid.setItems(updatedConfiguration.entrySet());
        grid.addColumn(Map.Entry::getKey).setHeader("key");
        grid.addColumn(Map.Entry::getValue).setHeader("value");

        Button saveButton = new Button(VaadinIcon.CHECK.create());
        saveButton.addClickListener(clickEvent -> {
            for (Object o : configs) {
                configurationPropertyManagerService.updateConfiguration(domainId,
                        o.getClass(), updatedConfiguration);
            }
            d.close();
        });

        final Grid<ValidationTuple> errorGrid = new Grid<>();
        // TODO: Improve display of error information. The columns are not appropriately sized and the errors are cut off.
        errorGrid.addColumn(validationTuple -> validationTuple.type).setHeader("Validation Type");
        errorGrid.addColumn(validationTuple -> validationTuple.error.toString()).setHeader("Validation Error");

        Button discardButton = new Button(VaadinIcon.CLOSE.create());
        discardButton.addClickListener(ev -> d.close());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(saveButton, discardButton);

        final HorizontalLayout validationLayout = new HorizontalLayout();
        validationLayout.add(domainValidBadge);

        dialogLayout.add(buttonLayout);
        dialogLayout.add(validationLayout);
        dialogLayout.add(grid);

        final Optional<DomibusConnectorBusinessDomain> businessDomain = domainManager.getBusinessDomain(domainId);
        if (businessDomain.isPresent()) {
            final DomibusConnectorBusinessDomain domibusConnectorBusinessDomain = businessDomain.get();
            domibusConnectorBusinessDomain.getProperties().putAll(updatedConfiguration); //overwrite props in current domain with updated props
            final DCBusinessDomainManager.DomainValidResult domainValidResult = domainManager.validateDomain(domibusConnectorBusinessDomain);

            if (domainValidResult.isValid()) {
                domainValidBadge.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                domainValidBadge.setText("Config will be valid!");
            } else {
                domainValidBadge.addThemeVariants(ButtonVariant.LUMO_ERROR);
                domainValidBadge.setText("Config won't be valid!");

                final List<ValidationTuple> validationResultByType = new ArrayList<>();

                for (Object error : domainValidResult.getErrors()) {
                    validationResultByType.add(new ValidationTuple("Error", error));
                }
                for (Object warning : domainValidResult.getWarnings()) {
                    validationResultByType.add(new ValidationTuple("Warning", warning));
                }
                errorGrid.setItems(validationResultByType);
                dialogLayout.add(errorGrid);
            }
        }

        return d;
    }

    static final class ValidationTuple {
        public final String type;
        public final Object error;

        public ValidationTuple(String type, Object error) {
            this.type = type;
            this.error = error;
        }
    }
}

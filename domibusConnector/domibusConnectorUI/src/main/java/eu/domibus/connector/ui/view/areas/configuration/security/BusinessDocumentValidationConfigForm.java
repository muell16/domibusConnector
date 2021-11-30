package eu.domibus.connector.ui.view.areas.configuration.security;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.ui.fields.SignatureValidationConfigurationField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BusinessDocumentValidationConfigForm extends FormLayout {

    private final TextField country = new TextField();
    private final TextField serviceProvider = new TextField();

    private final Select<AdvancedElectronicSystemType> defaultAdvancedSystemType = new Select<>();
    private final Checkbox allowSystemTypeOverrideByClient = new Checkbox();

    private final MultiSelectListBox<AdvancedElectronicSystemType> allowedAdvancedSystemTypes;

    @SuppressWarnings("FieldCanBeLocal")
    private final SignatureValidationConfigurationField signatureValidation;

    public BusinessDocumentValidationConfigForm(SignatureValidationConfigurationField signatureValidation) {
        allowedAdvancedSystemTypes = new MultiSelectListBox<>();
        allowedAdvancedSystemTypes.setItems(Stream.of(AdvancedElectronicSystemType.values()).collect(Collectors.toSet()));
        allowedAdvancedSystemTypes.setWidth("100%");

        defaultAdvancedSystemType.setItems(AdvancedElectronicSystemType.values());

        this.signatureValidation = signatureValidation;
        this.setResponsiveSteps(new ResponsiveStep("30cm", 1, ResponsiveStep.LabelsPosition.ASIDE));

        addFormItem(country, "Country");
        addFormItem(serviceProvider, "Service Provider");
        addFormItem(allowedAdvancedSystemTypes, "Allowed AdvancedSystemTypes");
        addFormItem(defaultAdvancedSystemType, "Default AdvancedSystemType");
        addFormItem(allowSystemTypeOverrideByClient, "Allow client to set AdvancedSystemType on message");
        addFormItem(signatureValidation, "Signature Validation Config");
    }

    public void bindInstanceFields(Binder b) {
        b.bindInstanceFields(this);
    }

}

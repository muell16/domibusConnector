package eu.domibus.connector.ui.view.areas.configuration.security;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import eu.domibus.connector.ui.fields.SignatureConfigurationField;
import eu.domibus.connector.ui.fields.SignatureValidationConfigurationField;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class EcxContainerConfigForm extends FormLayout {

    private final SignatureValidationConfigurationField signatureValidation;
    private final SignatureConfigurationField signature;

    public EcxContainerConfigForm(SignatureValidationConfigurationField signatureValidation, SignatureConfigurationField signature) {
        this.signatureValidation = signatureValidation;
        this.signature = signature;

        this.setResponsiveSteps(new ResponsiveStep("15cm", 1, ResponsiveStep.LabelsPosition.ASIDE));

        addFormItem(signatureValidation, "Signature Validation Config");
        addFormItem(signature, "Signature Configuration");

    }

    public void bindInstanceFields(Binder b) {
        b.bindInstanceFields(this);
    }

}

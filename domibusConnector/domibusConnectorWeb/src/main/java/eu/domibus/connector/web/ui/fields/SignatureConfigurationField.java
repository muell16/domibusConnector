package eu.domibus.connector.web.ui.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
import eu.domibus.connector.security.configuration.SignatureConfigurationProperties;
import eu.domibus.connector.web.ui.binder.SpringBeanValidationBinder;
import eu.domibus.connector.web.ui.binder.SpringBeanValidationBinderFactory;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SignatureConfigurationField extends CustomField<SignatureConfigurationProperties> {

    private final SpringBeanValidationBinderFactory validationBinderFactory;
    private final KeyConfigurationField privateKey;

    private Select<EncryptionAlgorithm> encryptionAlgorithm;
    private Select<DigestAlgorithm> digestAlgorithm;
    private final StoreConfigurationField keyStore;


    private SpringBeanValidationBinder<SignatureConfigurationProperties> binder;

    private Label statusLabel = new Label("");
    private FormLayout formLayout = new FormLayout();

    private SignatureConfigurationProperties value;

    public SignatureConfigurationField(SpringBeanValidationBinderFactory validationBinderFactory,
                                       KeyConfigurationField keyConfigurationField,
                                       StoreConfigurationField keyStore) {
        this.validationBinderFactory = validationBinderFactory;
        this.keyStore = keyStore;
        this.privateKey = keyConfigurationField;

        add(statusLabel);
        add(formLayout);

        encryptionAlgorithm = new Select<>(EncryptionAlgorithm.values());
        digestAlgorithm = new Select<>(DigestAlgorithm.values());

        formLayout.addFormItem(encryptionAlgorithm, "Encryption Algorithm");
        formLayout.addFormItem(digestAlgorithm, "Digest Algorithm");
        formLayout.addFormItem(keyStore, "Key Store Configuration");
        formLayout.addFormItem(privateKey, "Private Key Configuration");
        //TODO: set keystore on keyfield, so keyfield can be a chooser

        binder = validationBinderFactory.create(SignatureConfigurationProperties.class);
        binder.bindInstanceFields(this);
        binder.addValueChangeListener(this::valueChanged);

        binder.setStatusLabel(statusLabel);
    }

    private void valueChanged(ValueChangeEvent<?> valueChangeEvent) {
        SignatureConfigurationProperties changedValue = new SignatureConfigurationProperties();
        binder.writeBeanAsDraft(changedValue, true);
        setModelValue(changedValue, valueChangeEvent.isFromClient());
        value = changedValue;

    }


    @Override
    protected SignatureConfigurationProperties generateModelValue() {
        return value;
    }

    @Override
    protected void setPresentationValue(SignatureConfigurationProperties newPresentationValue) {
        binder.readBean(newPresentationValue);
        if (newPresentationValue == null) {
            formLayout.setVisible(false);
        } else {
            formLayout.setVisible(true);
        }
    }

}

package eu.domibus.connector.ui.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.ui.utils.binder.SpringBeanValidationBinderFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class StoreConfigurationField extends CustomField<StoreConfigurationProperties> {

    private final SpringBeanValidationBinderFactory validationBinderFactory;

    private Binder<StoreConfigurationProperties> binder;

    private Label statusLabel = new Label();
    private FormLayout formLayout = new FormLayout();

    private TextField path = new TextField();
    private PasswordField password = new PasswordField();
    private Select<String> type = new Select();

    public StoreConfigurationField(SpringBeanValidationBinderFactory validationBinderFactory) {
        this.validationBinderFactory = validationBinderFactory;


        this.add(statusLabel);
        this.add(formLayout);

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("5cm", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        //TODO: add button to show content of key/truststore
        formLayout.addFormItem(path, "Store Location");
        formLayout.addFormItem(password, "Store password");
        formLayout.addFormItem(type, "Store Type");
        type.setItems("JKS", "JCEKS", "PKCS");

        binder = validationBinderFactory.create(StoreConfigurationProperties.class);
        binder.bindInstanceFields(this);
        binder.addValueChangeListener(this::valueChanged);
        binder.setStatusLabel(statusLabel);

    }

    private void valueChanged(ValueChangeEvent<?> valueChangeEvent) {
        StoreConfigurationProperties newValue = new StoreConfigurationProperties();
        binder.writeBeanAsDraft(newValue, true);
        super.setModelValue(newValue, valueChangeEvent.isFromClient());
    }


    @Override
    protected StoreConfigurationProperties generateModelValue() {
        return binder.getBean();
    }

    @Override
    protected void setPresentationValue(StoreConfigurationProperties newPresentationValue) {
        binder.readBean(newPresentationValue);
        if (newPresentationValue == null) {
            formLayout.setVisible(false);
        } else {
            formLayout.setVisible(true);
        }
    }
}

package eu.domibus.connector.web.ui.fields;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.shared.Registration;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
import eu.domibus.connector.dss.service.DSSTrustedListsManager;
import eu.domibus.connector.web.ui.binder.SpringBeanValidationBinder;
import eu.domibus.connector.web.ui.binder.SpringBeanValidationBinderFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@Scope("prototype")
public class SignatureValidationConfigurationField extends CustomField<SignatureValidationConfigurationProperties>
implements AfterNavigationObserver {

    private final DSSTrustedListsManager trustedListsManager;
    private final SpringBeanValidationBinderFactory validationBinderFactory;

    private SpringBeanValidationBinder<SignatureValidationConfigurationProperties> binder;

    private TextField validationConstraintsXml = new TextField();
    private Checkbox trustStoreEnabled = new Checkbox();
    private Select<String> trustedListSource;
    private Checkbox ocspEnabled = new Checkbox();
    private Checkbox crlEnabled = new Checkbox();
    private StoreConfigurationField trustStore;
    private StoreConfigurationField ignoreStore;
    private Checkbox ignoreStoreEnabled = new Checkbox();

    private FormLayout formLayout = new FormLayout();
    private SignatureValidationConfigurationProperties value = new SignatureValidationConfigurationProperties();

    public SignatureValidationConfigurationField(DSSTrustedListsManager trustedListsManager,
                                                 StoreConfigurationField trustStore,
                                                 StoreConfigurationField ignoreStore,
                                                 SpringBeanValidationBinderFactory validationBinderFactory) {
        this.trustedListsManager = trustedListsManager;
        this.validationBinderFactory = validationBinderFactory;
        this.trustStore = trustStore;
        this.ignoreStore = ignoreStore;
        initUI();
    }

    private void initUI() {
        Label statusLabel = new Label();
        statusLabel.getStyle().set("color", "red");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("15cm", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        formLayout.addFormItem(validationConstraintsXml, "Location of EtsiValidationPolicyXml");

        //TODO: do not show store, if not enabled
        //trust store
        formLayout.addFormItem(trustStoreEnabled, "Use TrustStore");
        formLayout.addFormItem(trustStore, "Trust Store Configuration");

        //TODO: do not show store, if not enabled
        //ignore Store
        formLayout.addFormItem(ignoreStoreEnabled, "Use Ignore Store");
        formLayout.addFormItem(ignoreStore, "Ignore Store Configuration");

        //Trusted Lists Source
        trustedListSource = new Select<String>();
        trustedListSource.setEmptySelectionAllowed(true);
        trustedListSource.setDataProvider(new CallbackDataProvider<String, String>(this::fetchTrustedLists, this::countTrustedLists));
        formLayout.addFormItem(trustedListSource, "Set the trusted list source");

        formLayout.addFormItem(ocspEnabled, "Should OCSP be used on certificate verification");
        formLayout.addFormItem(crlEnabled, "Should CRL be used on certificate verification");

        this.add(statusLabel);
        this.add(formLayout);

        binder = validationBinderFactory.create(SignatureValidationConfigurationProperties.class);
        binder.bindInstanceFields(this);
        binder.addValueChangeListener(this::valueChanged);

        binder.setStatusLabel(statusLabel);
    }

    private int countTrustedLists(Query<String, String> query) {
        return trustedListsManager.getAllSourceNames().size();
    }

    private Stream<String> fetchTrustedLists(Query<String, String> query) {
        return trustedListsManager.getAllSourceNames().stream();
    }


    private void valueChanged(ValueChangeEvent<?> valueChangeEvent) {

        SignatureValidationConfigurationProperties changedValue = new SignatureValidationConfigurationProperties();
        binder.writeBeanAsDraft(changedValue, true);
        setModelValue(changedValue, valueChangeEvent.isFromClient());
        value = changedValue;

    }


    @Override
    protected void setPresentationValue(SignatureValidationConfigurationProperties newPresentationValue) {
        binder.readBean(newPresentationValue);
        if (newPresentationValue == null) {
            formLayout.setVisible(false);
        } else {
            formLayout.setVisible(true);
        }
    }


    @Override
    protected SignatureValidationConfigurationProperties generateModelValue() {
        return value;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        //init trusted list source items on enter event
//        this.trustedListSource.setItems(trustedListsManager.getAllSourceNames());
    }

}


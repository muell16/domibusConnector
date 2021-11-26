package eu.domibus.connector.ui.view.areas.configuration.link;

import com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import eu.domibus.connector.security.configuration.SignatureConfigurationProperties;
import eu.domibus.connector.ui.utils.binder.SpringBeanValidationBinderFactory;
import eu.domibus.connector.ui.utils.field.FindFieldService;
import eu.domibus.connector.utils.service.BeanToPropertyMapConverter;
import eu.domibus.connector.utils.service.PropertyMapToBeanConverter;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.validation.ConstraintViolation;
import java.util.*;

@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DCConfigurationPropertiesListField extends CustomField<Map<String, String>>
{

    private static final Logger LOGGER = LogManager.getLogger(eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent.class);

    private final BeanToPropertyMapConverter beanToPropertyMapConverter;
    private final PropertyMapToBeanConverter propertyMapToBeanConverter;
    private final FindFieldService findFieldService;
    private final javax.validation.Validator jsrValidator;
//    private final SpringBeanValidationBinderFactory springBeanValidationBinderFactory;

    private VerticalLayout layout = new VerticalLayout();

    private List<Class<?>> configurationClasses = new ArrayList<>();
    private Binder<Map<String, String>> binder = new Binder<>();
    private Map<String, String> value;
    private Map<Class<?>, Component> fields = new HashMap<>();

    public DCConfigurationPropertiesListField(
            BeanToPropertyMapConverter beanToPropertyMapConverter,
            PropertyMapToBeanConverter propertyMapToBeanConverter,
            javax.validation.Validator jsrValidator,
//            SpringBeanValidationBinderFactory springBeanValidationBinderFactory,
            FindFieldService findFieldService) {
        this.jsrValidator = jsrValidator;
//        this.springBeanValidationBinderFactory = springBeanValidationBinderFactory;
        this.beanToPropertyMapConverter = beanToPropertyMapConverter;
        this.propertyMapToBeanConverter = propertyMapToBeanConverter;
        this.findFieldService = findFieldService;

        initUI();
    }

    public void initUI() {
        this.add(layout);
        updateUI();
    }

    public Map<String, String> getEmptyValue() {
        return new HashMap<>();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ComponentValueChangeEvent<CustomField<Map<String, String>>, Map<String, String>>> listener) {
        return super.addValueChangeListener(listener);
    }

    public Collection<ConfigurationProperty> getConfigurationProperties() {
        return new ArrayList<>();
    }

    public void setConfigurationClasses(List<Class<?>> classes) {
        this.configurationClasses = classes;
        updateUI();
    }

    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        updateUI();
    }


    private void updateUI() {
        layout.removeAll();
        fields.clear();
        binder = new Binder<>();
        binder.setReadOnly(this.isReadOnly());
        binder.addValueChangeListener(this::valueChanged);
        //generate fields
        configurationClasses.forEach(this::processConfigCls);

    }

    private <T> void processConfigCls(Class<T> cls) {
        CustomField<T> field = findFieldService.findField(cls);
        Label statusLabel = new Label();
        fields.put(cls, field);
        layout.add(statusLabel);
        layout.add(field);
        field.setReadOnly(this.isReadOnly());
        binder.forField(field)
                .withValidator((Validator<T>) (t, valueContext) -> {
                    Set<ConstraintViolation<T>> validate = jsrValidator.validate(t);
                    if (validate.isEmpty()) {
                        return ValidationResult.ok();
                    }
                    StringBuilder errorText = new StringBuilder();
                    errorText.append("Validation errors found");
                    for (ConstraintViolation<T> v : validate) {
                        errorText.append("\n\tValidation problem: " + v.getMessage());
                    }
                    return ValidationResult.error(errorText.toString());
                })
                .withStatusLabel(statusLabel)
                .bind(
                    (ValueProvider<Map<String, String>, T>) stringStringMap -> propertyMapToBeanConverter.loadConfigurationOnlyFromMap(stringStringMap, cls, ""),
                    (Setter<Map<String, String>, T>) (o, o2) -> {
                        Map<String, String> stringStringMap = beanToPropertyMapConverter.readBeanPropertiesToMap(o2, "");
                        o.putAll(stringStringMap);
                    });

//                .withConverter(
//                        b -> beanToPropertyMapConverter.readBeanPropertiesToMap(b, ""),
//                        m -> propertyMapToBeanConverter.loadConfigurationOnlyFromMap(m, cls, ""),
//                        "Error converting failed"
//                )
//                .withStatusLabel(statusLabel)
//                .bind(m -> m, Map::putAll);

//                .withValidator(new Validator<T>() {
//                    @Override
//                    public ValidationResult apply(T t, ValueContext valueContext) {
//
//                        return null;
//                    }
//                })
//        .bind(
//                (ValueProvider<Map<String, String>, T>) stringStringMap -> propertyMapToBeanConverter.loadConfigurationOnlyFromMap(stringStringMap, cls, ""),
//                (Setter<Map<String, String>, T>) (o, o2) -> {
//                    Map<String, String> stringStringMap = beanToPropertyMapConverter.readBeanPropertiesToMap(o2, "");
//                    o.putAll(stringStringMap);
//                });

    }


    private void valueChanged(ValueChangeEvent<?> valueChangeEvent) {
        Map<String, String> changedValue = new HashMap<>();
        binder.writeBeanAsDraft(changedValue, true);
        setModelValue(changedValue, valueChangeEvent.isFromClient());
        value = changedValue;

    }

    @Override
    protected Map<String, String> generateModelValue() {
        return value;
    }

    @Override
    protected void setPresentationValue(Map<String, String> value) {
        binder.readBean(value);
    }

}



package eu.domibus.connector.ui.view.areas.configuration.link;

import com.vaadin.flow.component.AbstractField;
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

import java.util.*;

@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DCConfigurationPropertiesListField extends CustomField<Map<String, String>>
        // implements HasValue<HasValue.ValueChangeEvent<Map<String, String>>, Map<String, String>>, HasValidator<Map<String, String>>
{

    private static final Logger LOGGER = LogManager.getLogger(eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent.class);

//    private final ConfigurationPropertyCollector configurationPropertyCollector;
//    private final ConfigurationPropertyChecker configurationPropertyChecker;
//    private final ConfigurationFormsFactory configurationFormFactory;

//    private final Grid<ConfigurationProperty> grid = new Grid<>(ConfigurationProperty.class, false);
//    private final Label statusLabel = new Label();


    private final BeanToPropertyMapConverter beanToPropertyMapConverter;
    private final PropertyMapToBeanConverter propertyMapToBeanConverter;
    private final FindFieldService findFieldService;


    private List<Class<?>> configurationClasses = new ArrayList<>();


    private VerticalLayout layout = new VerticalLayout();


    Binder<Map<String, String>> binder = new Binder<>();
    private Map<String, String> value;

    public DCConfigurationPropertiesListField(
//            ConfigurationPropertyCollector configurationPropertyCollector,
//                                              ConfigurationPropertyChecker configurationPropertyChecker,
//                                              ConfigurationFormsFactory configurationFormFactory
            BeanToPropertyMapConverter beanToPropertyMapConverter,
            PropertyMapToBeanConverter propertyMapToBeanConverter,
            SpringBeanValidationBinderFactory springBeanValidationBinderFactory,
            FindFieldService findFieldService) {
//        this.springBeanValidationBinderFactory = springBeanValidationBinderFactory;
        this.beanToPropertyMapConverter = beanToPropertyMapConverter;
        this.propertyMapToBeanConverter = propertyMapToBeanConverter;
        this.findFieldService = findFieldService;

//        this.configurationPropertyCollector = configurationPropertyCollector;
//        this.configurationPropertyChecker = configurationPropertyChecker;
//        this.configurationFormFactory = configurationFormFactory;

        initUI();
    }

    public void initUI() {
        this.add(layout);
        binder.addValueChangeListener(this::valueChanged);

//        propertiesBinder.setBean(properties);
//
//        grid.addColumn("propertyName").setHeader("Property Path");
//        grid.addColumn("label").setHeader("Label");
//        grid.addColumn("type").setHeader("Type");
//        grid.addComponentColumn(new ValueProvider<ConfigurationProperty, Component>() {
//            @Override
//            public Component apply(ConfigurationProperty configurationProperty) {
//                AbstractField field = configurationFormFactory.createField(configurationProperty, propertiesBinder);
//                propertyFields.add(field);
//                return field;
//            }
//        });
//        grid.setDetailsVisibleOnClick(true);
//        grid.setItemDetailsRenderer(new ComponentRenderer<>(configProp -> {
//            VerticalLayout vl = new VerticalLayout();
//            vl.add(new Label("Description"));
//            vl.add(new Label(configProp.getDescription()));
//            return vl;
//        }));
//
//        grid.setItems(configurationProperties);
//
//        VerticalLayout verticalLayout = new VerticalLayout();
//        verticalLayout.add(grid, statusLabel);
//        add(verticalLayout);

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

    public void setConfigurationProperties(Collection<ConfigurationProperty> configurationProperties) {

//        propertiesBinder = new Binder<>();
//        propertiesBinder.setBean(properties);
//
//        this.configurationProperties = configurationProperties;
//        this.grid.setItems(configurationProperties);
//
//        List<Class> configClasses = configurationProperties
//                .stream()
//                .map(prop -> prop.getParentClass())
//                .distinct()
//                .collect(Collectors.toList());
//
//        propertiesBinder.withValidator(new Validator<Map<String, String>>() {
//            @Override
//            public ValidationResult apply(Map<String, String> value, ValueContext context) {
//                ConfigurationPropertySource configSource = new MapConfigurationPropertySource(value);
//                List<ValidationErrors> validationErrors = configurationPropertyChecker.validateConfiguration(configSource, configClasses);
//                if (validationErrors.isEmpty()) {
//                    return ValidationResult.ok();
//                }
//                //TODO: improve error representation
//                String errString = validationErrors.stream().map(err -> err.getAllErrors().stream())
//                        .flatMap(Function.identity())
//                        .map(objectError -> {
//                            if (objectError instanceof FieldError) {
//                                FieldError fieldError = (FieldError) objectError;
//                                return fieldError.getObjectName() + "." + fieldError.getField() + ": " + fieldError.getDefaultMessage();
//                            }
//                            return objectError.getObjectName() + ": " + objectError.getDefaultMessage();
//                        })
//                        .collect(Collectors.joining("; "));
//                return ValidationResult.error(errString);
//            }
//        });



    }

//    public Validator<Map<String, String>> getDefaultValidator() {
//
//    }

//    public List<ValidationResult> validate() {

//        BinderValidationStatus<Map<String, String>> validate = this.propertiesBinder.validate();
//        List<ValidationResult> beanValidationErrors = validate.getValidationErrors();
//        LOGGER.trace("BeanValidationErrors: [{}]", beanValidationErrors);
//        String collect = beanValidationErrors.stream()
//                .map(error -> error.getErrorMessage())
//                .collect(Collectors.joining("\n\n"));
//        this.statusLabel.setText(collect);
//        return beanValidationErrors;
//    }


    private void updateUI() {
        layout.removeAll();
        //generate fields
        configurationClasses.forEach(this::processConfigCls);

    }

    private <T> void processConfigCls(Class<T> cls) {
        CustomField<T> field = findFieldService.findField(cls);
        layout.add(field);

        binder.forField(field)
        .bind(
                (ValueProvider<Map<String, String>, T>) stringStringMap -> propertyMapToBeanConverter.loadConfigurationOnlyFromMap(stringStringMap, cls, ""),
                (Setter<Map<String, String>, T>) (o, o2) -> {
                    Map<String, String> stringStringMap = beanToPropertyMapConverter.readBeanPropertiesToMap(o2, "");
                    o.putAll(stringStringMap);
                });

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



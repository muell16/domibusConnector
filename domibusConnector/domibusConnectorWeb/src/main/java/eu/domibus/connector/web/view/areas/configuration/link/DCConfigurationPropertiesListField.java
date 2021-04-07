package eu.domibus.connector.web.view.areas.configuration.link;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyChecker;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import eu.ecodex.utils.configuration.ui.vaadin.tools.ConfigurationFormsFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DCConfigurationPropertiesListField extends VerticalLayout
        implements HasValue<HasValue.ValueChangeEvent<Map<String, String>>, Map<String, String>> {

    private static final Logger LOGGER = LogManager.getLogger(eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent.class);

    private final ConfigurationPropertyCollector configurationPropertyCollector;
    private final ConfigurationPropertyChecker configurationPropertyChecker;
    private final ConfigurationFormsFactory configurationFormFactory;

    private Grid<ConfigurationProperty> grid = new Grid<>(ConfigurationProperty.class, false);
    private Map<String, String> properties = new HashMap<>();
    private Label statusLabel = new Label();
    private Binder<Map<String, String>> propertiesBinder = new Binder();

    private Collection<ConfigurationProperty> configurationProperties = new ArrayList<>();
    private Collection<AbstractField> propertyFields = new ArrayList<>();
    private boolean readOnly = false;

    public DCConfigurationPropertiesListField(ConfigurationPropertyCollector configurationPropertyCollector, ConfigurationPropertyChecker configurationPropertyChecker, ConfigurationFormsFactory configurationFormFactory) {
        this.configurationPropertyCollector = configurationPropertyCollector;
        this.configurationPropertyChecker = configurationPropertyChecker;
        this.configurationFormFactory = configurationFormFactory;

        initUI();
    }

    public void initUI() {

        propertiesBinder.setBean(properties);

        grid.addColumn("propertyName").setHeader("Property Path");
        grid.addColumn("label").setHeader("Label");
        grid.addColumn("type").setHeader("Type");
        grid.addComponentColumn(new ValueProvider<ConfigurationProperty, Component>() {
            @Override
            public Component apply(ConfigurationProperty configurationProperty) {
                AbstractField field = configurationFormFactory.createField(configurationProperty, propertiesBinder);
                propertyFields.add(field);
                return field;
            }
        });
        grid.setDetailsVisibleOnClick(true);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(configProp -> {
            VerticalLayout vl = new VerticalLayout();
            vl.add(new Label("Description"));
            vl.add(new Label(configProp.getDescription()));
            return vl;
        }));


        grid.setItems(configurationProperties);


        //TODO: add validation error field before ListView

        this.add(this.grid);
        this.add(this.statusLabel);
    }

    public Collection<ConfigurationProperty> getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Collection<ConfigurationProperty> configurationProperties) {
        this.configurationProperties = configurationProperties;
        this.grid.setItems(configurationProperties);

        List<Class> configClasses = configurationProperties
                .stream()
                .map(prop -> prop.getParentClass())
                .distinct()
                .collect(Collectors.toList());

        propertiesBinder.withValidator(new Validator<Map<String, String>>() {
            @Override
            public ValidationResult apply(Map<String, String> value, ValueContext context) {
                ConfigurationPropertySource configSource = new MapConfigurationPropertySource(value);
                List<ValidationErrors> validationErrors = configurationPropertyChecker.validateConfiguration(configSource, configClasses);
                if (validationErrors.isEmpty()) {
                    return ValidationResult.ok();
                }
                //TODO: improve error representation
                String errString = validationErrors.stream().map(err -> err.getAllErrors().stream())
                        .flatMap(Function.identity())
                        .map(objectError -> {
                            if (objectError instanceof FieldError) {
                                FieldError fieldError = (FieldError) objectError;
                                return fieldError.getObjectName() + "." + fieldError.getField() + ": " + fieldError.getDefaultMessage();
                            }
                            return objectError.getObjectName() + ": " + objectError.getDefaultMessage();
                        })
                        .collect(Collectors.joining("; "));
                return ValidationResult.error(errString);
            }
        });
//        binder.setStatusLabel(this.statusLabel);

    }

    public List<ValidationResult> validate() {

        BinderValidationStatus<Map<String, String>> validate = this.propertiesBinder.validate();
        List<ValidationResult> beanValidationErrors = validate.getValidationErrors();
        LOGGER.trace("BeanValidationErrors: [{}]", beanValidationErrors);
//        String collect = beanValidationErrors.stream()
//                .map(error -> error.getErrorMessage())
//                .collect(Collectors.joining("\n\n"));
//        this.statusLabel.setText(collect);
        return beanValidationErrors;
    }

    @Override
    public void setValue(Map<String, String> value) {
        //this.binder.setBean(value);
        this.properties = value;
        this.propertiesBinder.readBean(value);
    }

//    public void writeBean() throws ValidationException {
//        this.propertiesBinder.writeBean(this.properties);
//    }

    @Override
    public Map<String, String> getValue() {
        return propertiesBinder.getBean();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Map<String, String>>> listener) {
        Registration registration = this.propertiesBinder
                .addValueChangeListener( event -> listener.valueChanged((ValueChangeEvent<Map<String, String>>) event));
        return registration;
    }


    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateUI(readOnly);
    }

    private void updateUI(boolean readOnly) {
        propertyFields.stream().forEach(f -> f.setReadOnly(readOnly));
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        //no support yet...
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

}



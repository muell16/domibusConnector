package eu.domibus.connector.ui.ui.binder;

import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.internal.BeanUtil;

import javax.validation.Validator;
import java.util.Locale;

public class SpringBeanValidator extends BeanValidator {

    private final Validator javaxValidator;

    /**
     * Creates a new JSR-303 {@code BeanValidator} that validates values of the
     * specified property. Localizes validation messages using the
     * {@linkplain Locale#getDefault() default locale}.
     *
     * @param beanType     the bean type declaring the property, not null
     * @param propertyName the property to validate, not null
     * @throws IllegalStateException if {@link BeanUtil#checkBeanValidationAvailable()} returns
     *                               false
     */
    public SpringBeanValidator(Validator javaxValidator, Class<?> beanType, String propertyName) {
        super(beanType, propertyName);
        this.javaxValidator = javaxValidator;
    }

    public javax.validation.Validator getJavaxBeanValidator() {
        return this.javaxValidator;
    }

}

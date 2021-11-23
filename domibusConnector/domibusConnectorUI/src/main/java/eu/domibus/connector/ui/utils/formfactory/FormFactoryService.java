package eu.domibus.connector.ui.utils.formfactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.ApplicationContext;

@SpringComponent
public class FormFactoryService {

    private final ApplicationContext applicationContext;

    public FormFactoryService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Component getForm(Class<?> clazz) {
        //TODO: lookup correct component
        return null;
    }

}

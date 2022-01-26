package eu.domibus.connector.ui.view.areas.configuration.processing;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.ui.fields.AuthenticationValidationConfigurationField;
import eu.domibus.connector.ui.fields.SignatureValidationConfigurationField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConnectorMessageProcessingConfigForm extends FormLayout {

    private final Checkbox sendGeneratedEvidencesToBackend = new Checkbox();
    private final Checkbox ebmsIdGeneratorEnabled = new Checkbox();
    private final TextField ebmsIdSuffix = new TextField();

    public ConnectorMessageProcessingConfigForm() {

        this.setResponsiveSteps(new FormLayout.ResponsiveStep("30cm", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        addFormItem(sendGeneratedEvidencesToBackend, "Send Generated Evidences Back to Backend");
        addFormItem(ebmsIdGeneratorEnabled, "Should connector create EBMS id");
        addFormItem(ebmsIdSuffix, "EBMS id suffix: UUID@<ebmsIdSuffix>");

    }


}

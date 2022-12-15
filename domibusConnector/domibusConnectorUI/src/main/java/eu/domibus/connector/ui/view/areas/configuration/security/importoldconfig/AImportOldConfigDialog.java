package eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.ui.component.DomainSelect;
import eu.domibus.connector.ui.view.areas.configuration.ChangedPropertiesDialogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


public abstract class AImportOldConfigDialog extends Dialog {

    private static final Logger LOGGER = LogManager.getLogger(AImportOldConfigDialog.class);
    private final ChangedPropertiesDialogFactory dialogFactory;
    private VerticalLayout layout = new VerticalLayout();
    //upload result area
    private VerticalLayout resultArea = new VerticalLayout();
    //Upload
    private MemoryBuffer buffer = new MemoryBuffer();
    private Upload upload = new Upload(buffer);

    private DomainSelect domainSelect;

    public AImportOldConfigDialog(ChangedPropertiesDialogFactory dialogFactory, DomainSelect domainSelect) {
        this.dialogFactory = dialogFactory;
        this.domainSelect = domainSelect;
        this.setWidth("80%");
        this.setHeightFull();

        add(layout);

        upload.addSucceededListener(this::uploadSucceeded);

        layout.add(upload, domainSelect, resultArea);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);
        this.addOpenedChangeListener(event -> this.close());
    }

    private void uploadSucceeded(SucceededEvent succeededEvent) {
        try {
            InputStream inputStream = buffer.getInputStream();

            Properties properties = new Properties();
            properties.load(inputStream);
            Map<String, String> p = properties.entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));

            //show imported config...
            Div div = new Div();
            Object configBean = showImportedConfig(div, p);

            //add save button...
            Button saveButton = new Button("Save Imported Config");
            saveButton.addClickListener(event -> {
                this.save(configBean, domainSelect.getValue());
            });
            resultArea.add(saveButton);
            resultArea.add(div);


        } catch (IOException e) {
            throw new RuntimeException("Unable to parse uploaded file", e);
        }

    }

    protected abstract Object showImportedConfig(Div div, Map<String, String> p);

    protected void save(Object configClass, DomibusConnectorBusinessDomain.BusinessDomainId domainId) {
        dialogFactory.createChangedPropertiesDialog(configClass, domainId);
    }

}

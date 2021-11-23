package eu.domibus.connector.ui.view.areas.configuration.link.importoldconfig;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.link.utils.Connector42LinkConfigTo43LinkConfigConverter;
import eu.domibus.connector.ui.view.areas.configuration.link.DCLinkConfigPanel;
import eu.domibus.connector.utils.service.BeanToPropertyMapConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


public abstract class ImportOldConfigDialog extends Dialog {

//    private final ApplicationContext context;
    protected final DCLinkConfigPanel linkConfigPanel;
    protected final BeanToPropertyMapConverter beanToPropertyMapConverter;
    protected final DCLinkFacade dcLinkFacade;


    TextField linkConfigName = new TextField();
//    RadioButtonGroup<ConversionSource> chooseGwOrBackend = new RadioButtonGroup<>();


    //Upload
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    //upload result area
    VerticalLayout resultArea = new VerticalLayout();

    public ImportOldConfigDialog(DCLinkConfigPanel linkConfigPanel,
                                 BeanToPropertyMapConverter beanToPropertyMapConverter,
                                 DCLinkFacade dcLinkFacade) {
        this.linkConfigPanel = linkConfigPanel;
        this.beanToPropertyMapConverter = beanToPropertyMapConverter;
        this.dcLinkFacade = dcLinkFacade;
        initUi();


    }

    private void initUi() {
        this.setWidth("80%");
        this.setHeightFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        add(verticalLayout);

        upload.addSucceededListener(this::uploadSecceeded);
        linkConfigName.setLabel("Link Configuration Name");


        verticalLayout.add(linkConfigName, upload, resultArea);
        linkConfigName.setValue("ImportedLinkConfig");
    }

    private void uploadSecceeded(SucceededEvent succeededEvent) {
        try {
            InputStream inputStream = buffer.getInputStream();

            Properties properties = new Properties();
            properties.load(inputStream);
            Connector42LinkConfigTo43LinkConfigConverter connector42LinkConfigTo43LinkConfigConverter =
                    new Connector42LinkConfigTo43LinkConfigConverter(properties);

            String configName = linkConfigName.getValue();

            DomibusConnectorLinkConfiguration linkConfiguration = new DomibusConnectorLinkConfiguration();
            linkConfiguration.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(configName));
            linkConfiguration.setProperties(getConfigurationProperties(connector42LinkConfigTo43LinkConfigConverter));
            linkConfiguration.setLinkImpl(getPluginName());

            linkConfigPanel.setReadOnly(true);
            linkConfigPanel.setValue(linkConfiguration);

            resultArea.add(linkConfigPanel);
            Button saveButton = new Button("Save Imported Config");
            saveButton.addClickListener(event -> {
                this.saveLinkConfiguration(linkConfiguration);
            });
            resultArea.add(saveButton);

            //TODO: add save button...



        } catch (IOException e) {
            throw new RuntimeException("Unable to parse uploaded file", e);
        }

    }

    protected abstract void saveLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration);


    protected abstract Map<String, String> getConfigurationProperties(Connector42LinkConfigTo43LinkConfigConverter connector42LinkConfigTo43LinkConfigConverter);

    protected abstract String getPluginName();


//    private enum ConversionSource {
//        BACKEND_42("4.2 Backend Configuration"),
//        GW_42("4.2 Gateway Configuration");
//
//        private final String visibleName;
//
//        ConversionSource(String humanName) {
//            this.visibleName = humanName;
//        }
//
//        @Override
//        public String toString() {
//            return visibleName;
//        }
//    }


}

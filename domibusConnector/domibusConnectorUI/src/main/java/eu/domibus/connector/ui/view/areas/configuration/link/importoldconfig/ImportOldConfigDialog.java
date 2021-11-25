package eu.domibus.connector.ui.view.areas.configuration.link.importoldconfig;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.link.utils.Connector42LinkConfigTo43LinkConfigConverter;
import eu.domibus.connector.ui.component.WizardComponent;
import eu.domibus.connector.ui.component.WizardStep;
import eu.domibus.connector.ui.view.areas.configuration.link.DCLinkConfigurationField;
import eu.domibus.connector.utils.service.BeanToPropertyMapConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


public abstract class ImportOldConfigDialog extends Dialog {

//    private final ApplicationContext context;
    protected final DCLinkConfigurationField linkConfigPanel;
    protected final BeanToPropertyMapConverter beanToPropertyMapConverter;
    protected final DCLinkFacade dcLinkFacade;


    private WizardComponent wizardComponent;

//    TextField linkConfigName = new TextField();
////    RadioButtonGroup<ConversionSource> chooseGwOrBackend = new RadioButtonGroup<>();
//
//
//    //Upload
//    MemoryBuffer buffer = new MemoryBuffer();
//    Upload upload = new Upload(buffer);
//    //upload result area
//    VerticalLayout resultArea = new VerticalLayout();

    public ImportOldConfigDialog(DCLinkConfigurationField linkConfigPanel,
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

        WizardComponent.WizardBuilder wizardBuilder = WizardComponent.getBuilder();
        wizardBuilder.addStep(new UploadConfigFileStep());

        wizardBuilder.addCancelListener((w, s)-> this.close()); //close dialog
        wizardBuilder.addFinishedListener((w, s) -> this.close()); //close dialog

        wizardComponent = wizardBuilder.build();

        this.add(wizardComponent);


    }

    protected abstract void saveLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration);

    protected abstract Map<String, String> getConfigurationProperties(Connector42LinkConfigTo43LinkConfigConverter connector42LinkConfigTo43LinkConfigConverter);

    protected abstract String getPluginName();


    public class UploadConfigFileStep extends VerticalLayout implements WizardStep {

        private TextField linkConfigName = new TextField();
        private MemoryBuffer buffer = new MemoryBuffer();
        private Upload upload = new Upload(buffer);
        private boolean nextEnabled = false;

        public UploadConfigFileStep() {
            initUI();
        }

        private void initUI() {
            upload.addSucceededListener(this::uploadSecceeded);
            linkConfigName.setLabel("Link Configuration Name");

            this.add(linkConfigName, upload);
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

                nextEnabled = true;
                wizardComponent.sendWizardStepChangeEvent(new WizardStepStateChangeEvent(this));

            } catch (IOException e) {
                nextEnabled = false;
                throw new RuntimeException("Unable to parse uploaded file", e);
            }

        }


        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public boolean isNextSupported() {
            return this.nextEnabled;
        }

        @Override
        public String getStepTitle() {
            return "Upload Config File";
        }

    }


}

package eu.domibus.connector.ui.view.areas.configuration.link.importoldconfig;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.service.DCLinkFacade;
import eu.domibus.connector.link.utils.Connector42LinkConfigTo43LinkConfigConverter;
import eu.domibus.connector.ui.view.areas.configuration.link.DCLinkConfigPanel;
import eu.domibus.connector.utils.service.BeanToPropertyMapConverter;
import eu.domibus.connectorplugins.link.gwwspushplugin.WsGatewayPlugin;
import eu.domibus.connectorplugins.link.wsbackendplugin.WsBackendPlugin;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
@Lazy
public class ImportOldBackendConfigDialog extends ImportOldConfigDialog {

    public ImportOldBackendConfigDialog(DCLinkConfigPanel linkConfigPanel, BeanToPropertyMapConverter beanToPropertyMapConverter, DCLinkFacade dcLinkFacade) {
        super(linkConfigPanel, beanToPropertyMapConverter, dcLinkFacade);
    }

    @Override
    protected void saveLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
//        linkConfiguration.setConfigurationSource(ConfigurationSource.DB);
//
//        DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
//        linkPartner.setConfigurationSource(ConfigurationSource.DB);
//        linkPartner.setLinkType(LinkType.BACKEND);
//        linkPartner.setConfigurationSource(ConfigurationSource.DB);
//        linkPartner.setSendLinkMode(LinkMode.PUSH);
//        linkPartner.setRcvLinkMode(LinkMode.PASSIVE);
//        linkPartner.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName("gateway"));
//        linkPartner.setEnabled(true);
//        linkPartner.setLinkConfiguration(linkConfiguration);
//        linkPartner.setDescription("Imported from old connector config");
//        //TODO: show link partner also in UI?
//        //check already configured gws?
//
//        dcLinkFacade.updateLinkConfig(linkConfiguration);
//        dcLinkFacade.updateLinkPartner(linkPartner);

        this.close();
    }

    @Override
    protected Map<String, String> getConfigurationProperties(Connector42LinkConfigTo43LinkConfigConverter configConverter) {
        return beanToPropertyMapConverter
                .readBeanPropertiesToMap(configConverter.convertBackendLinkProperties(), "");
    }

    @Override
    protected String getPluginName() {
        return WsBackendPlugin.IMPL_NAME;
    }
}

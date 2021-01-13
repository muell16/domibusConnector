package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLinkManager;
import eu.domibus.connector.link.api.ActiveLinkPartnerManager;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginLinkPartnerConfigurationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

public class DCWsActiveLinkPartner implements ActiveLinkPartnerManager {

    private static final Logger LOGGER = LogManager.getLogger(DCWsActiveLinkPartner.class);

    private final DomibusConnectorLinkPartner linkPartner;
    private final DCWsSubmitTo submitTo;
    private final DCWsActiveLink activeLink;
    private WsBackendPluginLinkPartnerConfigurationProperties configurationProperties;

    public DCWsActiveLinkPartner(DomibusConnectorLinkPartner linkPartner,
                                 DCWsSubmitTo submitTo,
                                 DCWsActiveLink activeLink
                                 ) {
        this.activeLink = activeLink;
        this.linkPartner = linkPartner;
        this.submitTo = submitTo;

        initConfigurationProperties();

    }

    private void initConfigurationProperties() {

        MapConfigurationPropertySource propertySource = new MapConfigurationPropertySource();
        propertySource.putAll(linkPartner.getProperties());

        Bindable<WsBackendPluginLinkPartnerConfigurationProperties> bindable = Bindable.of(WsBackendPluginLinkPartnerConfigurationProperties.class);

        Binder binder = new Binder(propertySource);
        BindResult<WsBackendPluginLinkPartnerConfigurationProperties> name = binder.bind("", bindable);
        WsBackendPluginLinkPartnerConfigurationProperties wsBackendPluginLinkPartnerConfigurationProperties = name.get();

        this.configurationProperties = wsBackendPluginLinkPartnerConfigurationProperties;
    }


    @Override
    public ActiveLinkManager getActiveLink() {
        return null;
    }

    @Override
    public DomibusConnectorLinkPartner getLinkPartner() {
        return linkPartner;
    }

    @Override
    public SubmitToLink getSubmitToLinkBean() {
        return submitTo;
    }


    public String getPushAddress() {
        return configurationProperties.getPushAddress();
    }

    public Object getEncryptionAlias() {
        return configurationProperties.getEncryptionAlias();
    }


}

package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.env.PropertiesPropertySource;

public class DCWsActiveLinkPartner implements ActiveLinkPartner {

    private final DomibusConnectorLinkPartner linkPartner;
    private final DCWsSubmitTo submitTo;
    private final DCWsActiveLink activeLink;
    private DCWsLinkPartnerConfigurationProperties configurationProperties;

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

        Bindable<DCWsLinkPartnerConfigurationProperties> bindable = Bindable.of(DCWsLinkPartnerConfigurationProperties.class);

        Binder binder = new Binder(propertySource);
        BindResult<DCWsLinkPartnerConfigurationProperties> name = binder.bind("name", bindable);
//        DCWsLinkPartnerConfigurationProperties dcWsLinkPartnerConfigurationProperties = name.get();

        //TODO: map partner properties!

        this.configurationProperties = new DCWsLinkPartnerConfigurationProperties();
    }


    @Override
    public ActiveLink getActiveLink() {
        return activeLink;
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

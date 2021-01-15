package eu.domibus.connector.link.impl.gwwspushplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.impl.gwwspullplugin.childctx.DCGatewayPullPluginConfiguration;
import eu.domibus.connector.link.impl.gwwspushplugin.childctx.WsGatewayPluginConfiguration;
import eu.domibus.connector.link.utils.LinkPluginUtils;
import org.apache.cxf.feature.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WsGatewayPlugin implements LinkPlugin {

    public static final String IMPL_NAME = "wsgatewayplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    private SubmitToLink submitToLink;

    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        ActiveLink activeLink = new ActiveLink();
        activeLink.setLinkConfiguration(linkConfiguration);
        activeLink.setLinkPlugin(this);
        return activeLink;
    }

    @Override
    public void shutdownConfiguration(ActiveLink activeLink) {
        throw new RuntimeException("not supported yet!");
    }

    @Override
    public synchronized ActiveLinkPartner enableLinkPartner(DomibusConnectorLinkPartner linkPartner, ActiveLink activeLink) {

        if (submitToLink != null) {
            throw new RuntimeException("Plugin already activated!");
        }

        ActiveLinkPartner activeLinkPartner = new ActiveLinkPartner();
        activeLinkPartner.setLinkPartner(linkPartner);
        activeLinkPartner.setParentLink(activeLink);

        ConfigurableApplicationContext childCtx = LinkPluginUtils.getChildContextBuilder(applicationContext)
                .withDomibusConnectorLinkConfiguration(activeLink.getLinkConfiguration())
                .withDomibusConnectorLinkPartner(linkPartner)
                .withSources(WsGatewayPluginConfiguration.class)
                .withProfiles(WsGatewayPluginConfiguration.WS_GATEWAY_PLUGIN)
                .run();

        activeLinkPartner.setChildContext(childCtx);

        submitToLink = childCtx.getBean(SubmitToLink.class);
        activeLinkPartner.setSubmitToLink(submitToLink);

        return activeLinkPartner;
    }

    @Override
    public void shutdownActiveLinkPartner(ActiveLinkPartner linkPartner) {
        throw new RuntimeException("not supported yet!");
    }

    @Override
    public SubmitToLink getSubmitToLink(ActiveLinkPartner linkPartner) {
        return submitToLink;
    }

    @Override
    public List<PluginFeature> getFeatures() {
        return Stream.of(PluginFeature.PUSH_MODE).collect(Collectors.toList());
    }

    @Override
    public List<Class> getPluginConfigurationProperties() {
        return null;
    }

    @Override
    public List<Class> getPartnerConfigurationProperties() {
        return null;
    }

}

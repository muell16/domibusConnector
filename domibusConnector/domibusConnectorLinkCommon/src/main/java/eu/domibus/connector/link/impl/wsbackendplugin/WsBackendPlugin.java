package eu.domibus.connector.link.impl.wsbackendplugin;

import eu.domibus.connector.link.service.SubmitToLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsActiveLinkPartnerManager;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginConfiguration;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginLinkPartnerConfigurationProperties;
import eu.domibus.connector.link.utils.LinkPluginUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.tools.logging.LoggingMarker.Log4jMarker.CONFIG;

//@Component
//@Profile(LINK_PLUGIN_PROFILE_NAME)
public class WsBackendPlugin implements LinkPlugin { //extends  AbstractDCWsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(WsBackendPlugin.class);
    public static final String IMPL_NAME = "wsbackendplugin";


    @Autowired
    ConfigurableApplicationContext applicationContext;


    @Override
    public String getPluginName() {
        return IMPL_NAME;
    }

    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {

        LOGGER.info("Starting Configuration for [{}]", linkConfiguration);
//        LOGGER.debug("Using Properties")

        ConfigurableApplicationContext childCtx = LinkPluginUtils.getChildContextBuilder(applicationContext)
                .withDomibusConnectorLinkConfiguration(linkConfiguration)
                .withSources(WsBackendPluginConfiguration.class)
                .withProfiles(WsBackendPluginConfiguration.WS_BACKEND_PLUGIN_PROFILE_NAME)
                .run();


        ActiveLink activeLink = new ActiveLink();
        activeLink.setLinkConfiguration(linkConfiguration);
        activeLink.setChildContext(childCtx);


        return activeLink;
    }

    @Override
    public void shutdownConfiguration(ActiveLink activeLink) {
        throw new RuntimeException("Not supported!");
    }

    @Autowired
    Validator validator;

    @Override
    public ActiveLinkPartner enableLinkPartner(DomibusConnectorLinkPartner linkPartner, ActiveLink activeLink) {
        LOGGER.debug("Enabling LinkPartner [{}]", linkPartner);
        Map<String, String> properties = linkPartner.getProperties();

        LOGGER.debug("Binding properties [{}] to linkPartnerConfig [{}]", properties, WsBackendPluginLinkPartnerConfigurationProperties.class);

        ValidationBindHandler validationBindHandler = new ValidationBindHandler(validator);


        Binder binder = new Binder(new MapConfigurationPropertySource(properties));
        BindResult<WsBackendPluginLinkPartnerConfigurationProperties> bindingResult = binder.bind("", Bindable.of(WsBackendPluginLinkPartnerConfigurationProperties.class), validationBindHandler);
        if (!bindingResult.isBound()) {
            String error = String.format("Binding properties [%s] to linkPartnerConfig [%s] failed", properties, WsBackendPluginLinkPartnerConfigurationProperties.class);
            throw new LinkPluginException(error);
        }

        WsBackendPluginLinkPartnerConfigurationProperties linkPartnerConfig = bindingResult.get();

        ConfigurableApplicationContext childContext = activeLink.getChildContext();
        if (childContext == null) {
            throw new LinkPluginException("Cannot start LinkPartner, because context of Link is null");
        }
        WsBackendPluginActiveLinkPartner activeLinkPartner = new WsBackendPluginActiveLinkPartner();
        activeLinkPartner.setLinkPartner(linkPartner);
        activeLinkPartner.setChildContext(childContext);
        activeLinkPartner.setParentLink(activeLink);
        activeLinkPartner.setConfig(linkPartnerConfig);
        activeLinkPartner.setSubmitToLink(childContext.getBean(SubmitToLinkPartner.class));

        //register certificate DN for authentication
        WsActiveLinkPartnerManager bean = childContext.getBean(WsActiveLinkPartnerManager.class);
        bean.registerDn(activeLinkPartner);

        LOGGER.info(CONFIG, "Successfully enabled LinkPartner [{}]", linkPartner);
        return activeLinkPartner;
    }

    @Override
    public void shutdownActiveLinkPartner(ActiveLinkPartner linkPartner) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public SubmitToLinkPartner getSubmitToLink(ActiveLinkPartner linkPartner) {
        return linkPartner.getChildContext().map( ctx -> ctx.getBean(SubmitToLinkPartner.class)).orElse(null);
    }

    @Override
    public List<PluginFeature> getFeatures() {
        return Stream
                .of(PluginFeature.RCV_PASSIVE_MODE,
                        PluginFeature.SEND_PUSH_MODE,
                        PluginFeature.SEND_PASSIVE_MODE,
                        PluginFeature.SUPPORTS_MULTIPLE_PARTNERS)
                .collect(Collectors.toList());
    }

    @Override
    public List<Class> getPluginConfigurationProperties() {
        //TODO: implement
        throw new RuntimeException("Not supported yet!");
    }

    @Override
    public List<Class> getPartnerConfigurationProperties() {
        //TODO: implement
        throw new RuntimeException("Not supported yet!");
    }


}

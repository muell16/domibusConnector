package eu.domibus.connector.link.impl.wsbackendplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.*;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.link.impl.gwwspullplugin.childctx.DCGatewayPullPluginConfiguration;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginConfiguration;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginLinkPartnerConfigurationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.DC_WS_BACKEND_PLUGIN_PROFILE_NAME;
import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME;
import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Component
@Profile(LINK_PLUGIN_PROFILE_NAME)
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

    }

    @Autowired
    Validator validator;

    @Override
    public ActiveLinkPartner enableLinkPartner(DomibusConnectorLinkPartner linkPartner, ActiveLink activeLink) {
        Properties properties = linkPartner.getProperties();

        LOGGER.debug("Binding properties [{}] to linkPartnerConfig [{}]", properties, WsBackendPluginLinkPartnerConfigurationProperties.class);

//        WsBackendPluginLinkPartnerConfigurationProperties linkPartnerConfig = new WsBackendPluginLinkPartnerConfigurationProperties();
//
//        DataBinder dataBinder = new DataBinder(linkPartnerConfig);
//        PropertyValues pv = new MutablePropertyValues(properties);
//        dataBinder.addValidators(validator);
//        dataBinder.bind(pv);
//        BindingResult bindingResult = dataBinder.getBindingResult();
//        dataBinder.
//        if (bindingResult.hasFieldErrors() || bindingResult.hasGlobalErrors()) {
//            LOGGER.error("Binding properties [{}] to linkPartnerConfig [{}] failed", properties, WsBackendPluginLinkPartnerConfigurationProperties.class);
//            LOGGER.error("Binding errors: [{}]", bindingResult.getAllErrors());
//            //TODO: print errors
//            return null;
//        }
        ValidationBindHandler validationBindHandler = new ValidationBindHandler(validator);


        Binder binder = new Binder(new MapConfigurationPropertySource(properties));
        BindResult<WsBackendPluginLinkPartnerConfigurationProperties> bindingResult = binder.bind("", Bindable.of(WsBackendPluginLinkPartnerConfigurationProperties.class), validationBindHandler);
        if (!bindingResult.isBound()) {
            String error = String.format("Binding properties [%s] to linkPartnerConfig [%s] failed", properties, WsBackendPluginLinkPartnerConfigurationProperties.class);
//            LOGGER.error("Binding properties [{}] to linkPartnerConfig [{}] failed", properties, WsBackendPluginLinkPartnerConfigurationProperties.class);

            throw new LinkPluginException(error);
        }

        WsBackendPluginLinkPartnerConfigurationProperties linkPartnerConfig = bindingResult.get();


        WsBackendPluginActiveLinkPartner activeLinkPartner = new WsBackendPluginActiveLinkPartner();
        activeLinkPartner.setLinkPartner(linkPartner);
        activeLinkPartner.setChildContext(activeLinkPartner.getChildContext());
        activeLinkPartner.setParentLink(activeLink);
        activeLinkPartner.setConfig(linkPartnerConfig);


        return activeLinkPartner;
    }

    @Override
    public void shutdownActiveLinkPartner(ActiveLinkPartner linkPartner) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public SubmitToLink getSubmitToLink(ActiveLinkPartner linkPartner) {
        return linkPartner.getChildContext().getBean(SubmitToLink.class);
    }

    @Override
    public List<PluginFeature> getFeatures() {
        return Stream
                .of(PluginFeature.PUSH_MODE, PluginFeature.SUPPORTS_MULTIPLE_PARTNERS)
                .collect(Collectors.toList());
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

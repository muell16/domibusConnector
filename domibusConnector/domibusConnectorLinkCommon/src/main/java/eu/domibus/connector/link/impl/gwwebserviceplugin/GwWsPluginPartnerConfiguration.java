package eu.domibus.connector.link.impl.gwwebserviceplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.plugin.webService.generated.BackendInterface;
import org.apache.cxf.jaxws.JaxWsClientFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(GwWsPlugin.DC_DOMIBUSGW_WS_PLUGIN_PROFILE_NAME)
@ComponentScan(basePackageClasses = GwWsPluginConfigurationProperties.class)
public class GwWsPluginPartnerConfiguration {

    @Autowired
    GwWsPluginPartnerConfigurationProperties partnerConfig;


    @Bean
    eu.domibus.plugin.webService.generated.BackendInterface backendInterface() {
        JaxWsProxyFactoryBean  clientFactoryBean = new JaxWsProxyFactoryBean();
        clientFactoryBean.setServiceClass(BackendInterface.class);
        clientFactoryBean.setAddress(partnerConfig.getBackendServiceUrl());

        if (partnerConfig.getUsername() != null) {
            clientFactoryBean.setUsername(partnerConfig.getUsername());
            clientFactoryBean.setPassword(partnerConfig.getPassword());
        }

        return (BackendInterface) clientFactoryBean.create();
    }

    @Bean
    public SubmitToLink submitToLink() {
        return new GwWsPluginSubmitToLink();
    }

}

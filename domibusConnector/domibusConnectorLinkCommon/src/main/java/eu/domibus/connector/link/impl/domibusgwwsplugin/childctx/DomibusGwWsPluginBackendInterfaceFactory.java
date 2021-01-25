package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;

import eu.domibus.plugin.webService.generated.BackendInterface;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class DomibusGwWsPluginBackendInterfaceFactory {


    @Autowired
    DomibusGwWsPluginConfigurationProperties configurationProperties;

    eu.domibus.plugin.webService.generated.BackendInterface getWebserviceProxyClient(DomibusGwWsPluginActiveLinkPartner activeLinkPartner) {
        JaxWsProxyFactoryBean clientFactoryBean = new JaxWsProxyFactoryBean();
        clientFactoryBean.setServiceClass(BackendInterface.class);
        clientFactoryBean.setAddress(configurationProperties.getGwAddress());

        String username = activeLinkPartner.getConfig().getUsername();
        String password = activeLinkPartner.getConfig().getPassword();

        if (!StringUtils.isEmpty(username)) {
            clientFactoryBean.setUsername(username);
            clientFactoryBean.setPassword(password);
        }

        return (BackendInterface) clientFactoryBean.create();
    }

}

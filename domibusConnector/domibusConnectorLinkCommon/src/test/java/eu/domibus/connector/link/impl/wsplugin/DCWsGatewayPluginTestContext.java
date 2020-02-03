package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.LinkTestContext;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCLinkPersistenceService;
import eu.domibus.connector.link.service.LinkPluginQualifier;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkPartner;
import org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({LinkTestContext.class, CxfAutoConfiguration.class})
public class DCWsGatewayPluginTestContext {

    @Autowired
    DCLinkPersistenceService linkPersistenceService;

    @Bean
    @ConditionalOnMissingBean
    @Profile("ws-test")
    public DomibusConnectorLinkPartnerDao domibusConnectorWsLinkPartnerDao() {
        DomibusConnectorLinkPartnerDao dao = Mockito.mock(DomibusConnectorLinkPartnerDao.class);
        Mockito.when(dao.findAllByEnabledIsTrue())
                .thenReturn(Stream.of(getWsGwLinkInfo()).collect(Collectors.toList()));
        Mockito.when(dao.findOneByLinkName("cn=gw"))
                .thenReturn(Optional.of(getWsGwLinkInfo()));

        return dao;
    }

    @Bean
//    @ConditionalOnMissingBean
    @Profile("ws-test")
    public DomibusConnectorLinkConfigurationDao domibusConnectorWsLinkConfigDao() {
        DomibusConnectorLinkConfigurationDao dao = Mockito.mock(DomibusConnectorLinkConfigurationDao.class);
        Mockito.when(dao.getOneByConfigName(eq("wsgateway")))
                .thenReturn(Optional.of(getWsGatewayLinkConfig()));

        return dao;
    }

    @Bean
    @Profile(DCWsPluginConfiguration.DC_WS_GATEWAY_PLUGIN_PROFILE_NAME)
    @LinkPluginQualifier
    public LinkPlugin linkPlugin() {
        return new DCWsGatewayPlugin();
    }

    @Bean
    @Profile("ws-test")
    public DomibusConnectorLinkConfiguration domibusConnectorLinkConfiguration() {
        return linkPersistenceService.getLinkConfiguration(new DomibusConnectorLinkConfiguration.LinkConfigName("wsgateway")).get();
    }


    public static PDomibusConnectorLinkConfiguration getWsGatewayLinkConfig() {
        PDomibusConnectorLinkConfiguration linkConfig = new PDomibusConnectorLinkConfiguration();
        linkConfig.setLinkImpl(DCWsBackendPlugin.IMPL_NAME);
        linkConfig.setConfigName("wsgateway");

        HashMap<String, String> props = new HashMap<>();

        props.put("link.wsgatewayplugin.soap.key-store.path", "./target/test-classes/keystores/alice.jks");
        props.put("link.wsgatewayplugin.soap.key-store.password", "12345");
        props.put("link.wsgatewayplugin.soap.private-key.alias", "alice");
        props.put("link.wsgatewayplugin.soap.private-key.password", "12345");

        props.put("link.wsgatewayplugin.soap.trust-store.path", "./target/test-classes/keystores/alice.jks");
        props.put("link.wsgatewayplugin.soap.trust-store.password", "12345");
        return linkConfig;
    }

    public static PDomibusConnectorLinkPartner getWsGwLinkInfo() {

        PDomibusConnectorLinkPartner linkPartner1 = new PDomibusConnectorLinkPartner();
        String linkName1 = "cn=gw";

        linkPartner1.setLinkType(LinkType.GATEWAY);
        linkPartner1.setLinkMode(LinkMode.PUSH);
        linkPartner1.setLinkConfiguration(getWsGatewayLinkConfig());
        linkPartner1.setEnabled(true);

        return linkPartner1;

    }


}

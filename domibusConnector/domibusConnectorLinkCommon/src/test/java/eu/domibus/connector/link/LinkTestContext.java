package eu.domibus.connector.link;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPlugin;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkPartner;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, })
public class LinkTestContext {

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorLinkPartnerDao domibusConnectorLinkInfoDao() {
        DomibusConnectorLinkPartnerDao dao = Mockito.mock(DomibusConnectorLinkPartnerDao.class);
        Mockito.when(dao.findAllByEnabledIsTrue())
                .thenReturn(Stream.of(getLinkInfo()).collect(Collectors.toList()));
        return dao;
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorLinkConfigurationDao domibusConnectorLinkConfigurationDao() {
        DomibusConnectorLinkConfigurationDao dao = Mockito.mock(DomibusConnectorLinkConfigurationDao.class);
//        Mockito.when(dao.findAllByEnabledIsTrue())
//                .thenReturn(Stream.of(getLinkInfo()).collect(Collectors.toList()));
        return dao;
    }

    private PDomibusConnectorLinkPartner getLinkInfo() {

        PDomibusConnectorLinkPartner linkPartner = new PDomibusConnectorLinkPartner();
        String linkName = "firstLINK";


        linkPartner.setLinkName(linkName);
        linkPartner.setDescription("A description for this link...");
        linkPartner.setEnabled(true);
        linkPartner.setLinkType(LinkType.GATEWAY);

        PDomibusConnectorLinkConfiguration linkConfig = new PDomibusConnectorLinkConfiguration();
        linkConfig.setLinkImpl(GwJmsPlugin.LINK_IMPL_NAME);
        linkConfig.setConfigName("config2");

        HashMap<String, String> props = new HashMap();
        props.put("link.gwjmsplugin.put-attachment-in-queue", "true");
        props.put("link.gwjmsplugin.put-attachment-in-queue", "true");

        props.put("link.gwjmsplugin.username", "username");
        props.put("link.gwjmsplugin.password", "password");
        props.put("link.gwjmsplugin.in-queue", "contogw");
        props.put("link.gwjmsplugin.reply-queue", "replyfromgwtocon");
        props.put("link.gwjmsplugin.out-queue", "gwtocon");
        props.put("link.gwjmsplugin.error-notify-producer-queue", "errornotifyproducer");
        props.put("link.gwjmsplugin.error-notify-conusmer-queue", "errornotifyconsumer");

        linkConfig.setProperties(props);
        linkPartner.setLinkConfiguration(linkConfig);

        return linkPartner;
    }
}

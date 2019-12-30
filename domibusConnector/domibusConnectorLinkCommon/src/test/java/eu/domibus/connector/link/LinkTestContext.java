package eu.domibus.connector.link;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPluginFactory;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkInfoDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkInfo;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, })
public class LinkTestContext {

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorLinkInfoDao domibusConnectorLinkInfoDao() {
        DomibusConnectorLinkInfoDao dao = Mockito.mock(DomibusConnectorLinkInfoDao.class);
        Mockito.when(dao.findAllByEnabledIsTrue())
                .thenReturn(Stream.of(getLinkInfo()).collect(Collectors.toList()));
        return dao;
    }

    private PDomibusConnectorLinkInfo getLinkInfo() {

        PDomibusConnectorLinkInfo linkInfo = new PDomibusConnectorLinkInfo();
        String linkName = "firstLINK";


        linkInfo.setLinkName(linkName);
        linkInfo.setDescription("A description for this link...");
        linkInfo.setEnabled(true);
        linkInfo.setLinkType(LinkType.GATEWAY);

        PDomibusConnectorLinkConfiguration linkConfig = new PDomibusConnectorLinkConfiguration();
        linkConfig.setLinkImpl(GwJmsPluginFactory.LINK_IMPL_NAME);

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
        linkInfo.setLinkConfiguration(linkConfig);

        return linkInfo;
    }
}

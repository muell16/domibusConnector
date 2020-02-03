package eu.domibus.connector.link;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPlugin;
import eu.domibus.connector.link.impl.wsplugin.DCWsBackendPlugin;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, })
public class LinkTestContext {

    private static final Logger LOGGER = LogManager.getLogger(LinkTestContext.class);

    @Bean
    @ConditionalOnMissingBean
    @Profile("jms-test")
    public DomibusConnectorLinkPartnerDao domibusConnectorJmsLinkInfoDao() {
        DomibusConnectorLinkPartnerDao dao = Mockito.mock(DomibusConnectorLinkPartnerDao.class);
        Mockito.when(dao.findAllByEnabledIsTrue())
                .thenReturn(Stream.of(getJmsLinkInfo()).collect(Collectors.toList()));
        return dao;
    }


    @Bean
    @ConditionalOnMissingBean
    public TransportStatusService transportStatusService() {
        TransportStatusService mock = Mockito.mock(TransportStatusService.class);

        return mock;
    }

//    @Bean
//    @ConditionalOnMissingBean(value = DomibusConnectorLinkConfigurationDao.class, search = SearchStrategy.ALL)
//    public DomibusConnectorLinkConfigurationDao domibusConnectorLinkConfigurationDao() {
//        DomibusConnectorLinkConfigurationDao dao = Mockito.mock(DomibusConnectorLinkConfigurationDao.class);
//        return dao;
//    }

    @Bean
    @ConditionalOnMissingBean
    public SubmitToConnector submitToConnector() {
        return (message, linkPartner) -> {
            BlockingQueue<DomibusConnectorMessage> q = toConnectorSubmittedMessages();
            LOGGER.info("Adding message [{}] to submitToConnector [{}] Queue", message, q);
            try {
                q.put(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static final String SUBMIT_TO_CONNECTOR_QUEUE = "submitToConnector";

    @Bean
    @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
    public BlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages() {
        return new LinkedBlockingDeque<>(90);
    }


    private PDomibusConnectorLinkPartner getJmsLinkInfo() {

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
        props.put("link.gwjmsplugin.toDomibusGateway", "q1");
        props.put("link.gwjmsplugin.toConnector", "q2");

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

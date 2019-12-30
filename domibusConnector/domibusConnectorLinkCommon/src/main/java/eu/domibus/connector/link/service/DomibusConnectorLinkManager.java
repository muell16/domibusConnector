package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import eu.domibus.connector.link.api.LinkPluginFactory;
import eu.domibus.connector.link.impl.gwjmsplugin.ReceiveFromJmsQueueConfiguration;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkInfoDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class manages the lifecycle of the connector links
 */
@Service
public class DomibusConnectorLinkManager {

    private final static Logger LOGGER = LogManager.getLogger(DomibusConnectorLinkManager.class);

    @Autowired(required = false)
    List<LinkPluginFactory> linkPluginFactories = new ArrayList<>();

    @Autowired
    ConfigurableApplicationContext applicationContext;

//    private List<DomibusConnectorLinkInfo> domibusConnectorLinkInfoList;
    private Map<String, ActiveLink> activeLinkMap = new ConcurrentHashMap<>();



    SubmitToLink getLink(String linkName) {
        ActiveLink activeLink = activeLinkMap.get(linkName);
        return activeLink.getSubmitToLink();
    }


    public void addLink(DomibusConnectorLinkInfo linkInfo) {
        DomibusConnectorLinkConfiguration linkConfiguration = linkInfo.getLinkConfiguration();
        ActiveLink activeLink = activeLinkMap.get(linkInfo.getLinkName());
        if (activeLink != null) {
            String error = String.format("Link [{}] already active! Destroy old link first!", linkInfo.getLinkName());
            throw new RuntimeException(error);
        } else {
            activeLink = new ActiveLink();
        }

        String linkImpl = linkConfiguration.getLinkImpl();
        if (StringUtils.isEmpty(linkImpl)) {
            LOGGER.warn("link impl of [{}] is empty! No link will be created!", linkInfo);
            return;
        }
        Optional<LinkPluginFactory> first = linkPluginFactories.stream().filter(l -> l.canHandle(linkImpl)).findFirst();
        if (!first.isPresent()) {
            LOGGER.warn("No link factory for linkImpl [{}] found! No link will be created!", linkImpl);
            return;
        }
        LinkPluginFactory linkPluginFactory = first.get();

        activeLink = linkPluginFactory.createLink(linkInfo);


        LOGGER.info("Enabled link [{}]", linkInfo.getLinkName());
        this.activeLinkMap.put(linkInfo.getLinkName(), activeLink);

    }

    private void destroyLink(ActiveLink activeLink) {
        activeLinkMap.remove(activeLink.getLinkName(), activeLink);
        if (activeLink.getCloseCallback() == null) {
            LOGGER.error("Cannot close link [{}]! Because CloseCallback is null!", activeLink);
        }
        activeLink.getCloseCallback().close();
    }

}

package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.persistence.service.DCLinkPersistenceService;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@ConditionalOnBean(DCLinkPluginConfiguration.class)
public class DCLinkFacade {

    public static final String CONFIG_SOURCE_DB = "DB"; //database
    public static final String CONFIG_SOURCE_ENV = "ENV"; //property environment

    private final DCActiveLinkManagerService linkManager;
    private final DCLinkPersistenceService dcLinkPersistenceService;
    private final DCLinkPluginConfigurationProperties lnkConfig;

    public DCLinkFacade(DCActiveLinkManagerService linkManager,
                        DCLinkPersistenceService dcLinkPersistenceService,
                        DCLinkPluginConfigurationProperties props) {
        this.linkManager = linkManager;
        this.dcLinkPersistenceService = dcLinkPersistenceService;
        this.lnkConfig = props;
    }


    public boolean isActive(DomibusConnectorLinkPartner d) {
        if (d.getLinkPartnerName() == null) {
            throw new IllegalArgumentException("LinkPartner name is not allowed to be null!");
        }
        return linkManager.getActiveLinkPartnerByName(d.getLinkPartnerName()).isPresent();
    }

    public void shutdownLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        linkManager.shutdownLinkPartner(linkPartner.getLinkPartnerName());
    }

    public List<DomibusConnectorLinkPartner> getAllLinksOfType(LinkType linkType) {
        List<DomibusConnectorLinkPartner> allLinksOfType = dcLinkPersistenceService.getAllLinksOfType(linkType);
        allLinksOfType.forEach(l -> l.setConfigurationSource("DB"));

        Stream<DomibusConnectorLinkPartner> domibusConnectorLinkPartners;
        if (linkType == LinkType.BACKEND) {
            domibusConnectorLinkPartners = lnkConfig.getBackend().stream().flatMap(b -> mapCnfg(b, LinkType.BACKEND));
        } else if (linkType == LinkType.GATEWAY) {
            domibusConnectorLinkPartners = mapCnfg(lnkConfig.getGateway(), LinkType.GATEWAY);
        } else {
            domibusConnectorLinkPartners = Stream.empty();
        }

        return Stream.of(allLinksOfType.stream(), domibusConnectorLinkPartners)
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    private Stream<DomibusConnectorLinkPartner> mapCnfg(DCLinkPluginConfigurationProperties.DCLnkPropertyConfig b, LinkType linkType) {
        DomibusConnectorLinkConfiguration linkConfig = new DomibusConnectorLinkConfiguration();
        BeanUtils.copyProperties(b.getLinkConfig(), linkConfig);
        linkConfig.setConfigurationSource(CONFIG_SOURCE_ENV);
        return b.getLinkPartners().stream().map(p -> {
            DomibusConnectorLinkPartner p1 = new DomibusConnectorLinkPartner();
            BeanUtils.copyProperties(p, p1);
            p1.setConfigurationSource(CONFIG_SOURCE_ENV);
            p1.setLinkConfiguration(linkConfig);
            p1.setLinkType(linkType);
            return p1;
        });
    }

    public void startLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        Optional<ActiveLinkPartner> activeLinkPartner = this.linkManager.activateLinkPartner(linkPartner);
        if (!activeLinkPartner.isPresent()) {
            throw new LinkPluginException("Start failed!");
        }
    }

    public void deleteLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        try {
            linkManager.shutdownLinkPartner(linkPartner.getLinkPartnerName());
        } catch (LinkPluginException exception) {
            //handle
        }
        if (CONFIG_SOURCE_DB.equals(linkPartner.getConfigurationSource())) {
            dcLinkPersistenceService.deleteLinkPartner(linkPartner);
        }

    }

}

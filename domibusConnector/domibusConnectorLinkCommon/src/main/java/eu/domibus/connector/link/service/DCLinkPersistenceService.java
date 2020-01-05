package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class DCLinkPersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DCLinkPersistenceService.class);

    @Autowired
    DomibusConnectorLinkPartnerDao linkPartnerDao;

    public List<DomibusConnectorLinkPartner> getAllEnabledLinks() {
        return linkPartnerDao.findAllByEnabledIsTrue().stream().map(this::mapToLinkPartner).collect(Collectors.toList());
    }

    private DomibusConnectorLinkPartner mapToLinkPartner(PDomibusConnectorLinkPartner dbLinkInfo) {
        DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
        BeanUtils.copyProperties(dbLinkInfo, linkPartner);
        PDomibusConnectorLinkConfiguration linkConfiguration = dbLinkInfo.getLinkConfiguration();

        linkPartner.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(dbLinkInfo.getLinkName()));
        linkPartner.setLinkConfiguration(mapToLinkConfiguration(linkConfiguration));
        return linkPartner;
    }

    private DomibusConnectorLinkConfiguration mapToLinkConfiguration(PDomibusConnectorLinkConfiguration dbLinkConfig) {
        DomibusConnectorLinkConfiguration configuration = new DomibusConnectorLinkConfiguration();

        Properties p = new Properties();
        p.putAll(dbLinkConfig.getProperties());
        configuration.setProperties(p);
        configuration.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(dbLinkConfig.getConfigName()));
        configuration.setLinkImpl(dbLinkConfig.getLinkImpl());
        return configuration;
    }


    public List<DomibusConnectorLinkPartner> getAllLinksOfType(LinkType linkType) {
        PDomibusConnectorLinkPartner linkPartner = new PDomibusConnectorLinkPartner();
        linkPartner.setLinkType(linkType);
        Example<PDomibusConnectorLinkPartner> example = Example.of(linkPartner);
        return linkPartnerDao.findAll(example)
                .stream()
                .map(this::mapToLinkPartner)
                .collect(Collectors.toList());
    }

}

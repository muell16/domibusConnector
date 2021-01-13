package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

//@Service
public class DCLinkPersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DCLinkPersistenceService.class);

    @Autowired
    DomibusConnectorLinkPartnerDao linkPartnerDao;

    @Autowired
    DomibusConnectorLinkConfigurationDao linkConfigurationDao;

    public List<DomibusConnectorLinkPartner> getAllEnabledLinks() {
        return linkPartnerDao
                .findAllByEnabledIsTrue()
                .stream()
                .map(this::mapToLinkPartner)
                .collect(Collectors.toList());
    }

    public List<DomibusConnectorLinkConfiguration> getAllLinkConfigurations() {
        return linkConfigurationDao.
                findAll()
                .stream()
                .map(this::mapToLinkConfiguration)
                .collect(Collectors.toList());
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
        if (dbLinkConfig == null) {
            return null;
        }
        DomibusConnectorLinkConfiguration configuration = new DomibusConnectorLinkConfiguration();

        Properties p = new Properties();
        p.putAll(dbLinkConfig.getProperties());
//        configuration.setProperties(p);
        configuration.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(dbLinkConfig.getConfigName()));
        configuration.setLinkImpl(dbLinkConfig.getLinkImpl());
        return configuration;
    }


    public List<DomibusConnectorLinkPartner> getAllLinksOfType(LinkType linkType) {
        PDomibusConnectorLinkPartner linkPartner = new PDomibusConnectorLinkPartner();
        linkPartner.setLinkType(linkType);
        linkPartner.setEnabled(null);
        Example<PDomibusConnectorLinkPartner> example = Example.of(linkPartner);
        return linkPartnerDao.findAll(example)
                .stream()
                .map(this::mapToLinkPartner)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        if (linkPartner.getLinkConfiguration() == null) {
            throw new IllegalArgumentException("Cannot add a LinkPartner without an LinkConfiguration!");
        }
        PDomibusConnectorLinkPartner dbLinkPartner = mapToDbLinkPartner(linkPartner);

        PDomibusConnectorLinkConfiguration dbLinkConfiguration = mapToDbLinkConfiguration(linkPartner.getLinkConfiguration());
        dbLinkConfiguration = linkConfigurationDao.save(dbLinkConfiguration);
        dbLinkPartner.setLinkConfiguration(dbLinkConfiguration);

        linkPartnerDao.save(dbLinkPartner);
        LOGGER.debug("Saving [{}] to database", dbLinkPartner);

        //check for only one gw link config...
        PDomibusConnectorLinkPartner gatewayExample = new PDomibusConnectorLinkPartner();
        gatewayExample.setLinkType(LinkType.GATEWAY);
        gatewayExample.setEnabled(true);

        List<PDomibusConnectorLinkPartner> all = linkPartnerDao.findAll(Example.of(gatewayExample));
        if (all.size() > 1) {
            LOGGER.warn("Only one active gateway configuration at once is allowed - new link will be inactive!");
            dbLinkPartner.setEnabled(false);
            linkPartnerDao.save(dbLinkPartner);
        }

        LOGGER.debug("Successfully saved [{}] to database", dbLinkPartner);

    }

    private PDomibusConnectorLinkPartner mapToDbLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        if (linkPartner == null) {
            return null;
        }
        String linkName = linkPartner.getLinkPartnerName() == null ? null : linkPartner.getLinkPartnerName().getLinkName();

        Optional<PDomibusConnectorLinkPartner> oneByLinkName = linkPartnerDao.findOneByLinkName(linkName);
        PDomibusConnectorLinkPartner dbLinkPartner = oneByLinkName.orElse(new PDomibusConnectorLinkPartner());

        BeanUtils.copyProperties(linkPartner, dbLinkPartner);

        dbLinkPartner.setLinkType(linkPartner.getLinkType());
        dbLinkPartner.setDescription(linkPartner.getDescription());
        dbLinkPartner.setLinkName(linkName);
        dbLinkPartner.setEnabled(linkPartner.isEnabled());
        dbLinkPartner.setLinkConfiguration(this.mapToDbLinkConfiguration(linkPartner.getLinkConfiguration()));
        dbLinkPartner.setProperties(this.mapProperties(linkPartner.getProperties()));

        return dbLinkPartner;
    }

    private PDomibusConnectorLinkConfiguration mapToDbLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        if (linkConfiguration == null) {
            return null;
        }
        String configName = linkConfiguration.getConfigName() == null ? null : linkConfiguration.getConfigName().getConfigName();

        Optional<PDomibusConnectorLinkConfiguration> oneByConfigName = linkConfigurationDao.getOneByConfigName(configName);

        PDomibusConnectorLinkConfiguration dbLinkConfig = oneByConfigName.orElse(new PDomibusConnectorLinkConfiguration());
        dbLinkConfig.setConfigName(configName);
        dbLinkConfig.setLinkImpl(linkConfiguration.getLinkImpl());
        dbLinkConfig.setProperties(linkConfiguration.getProperties());
        return dbLinkConfig;
    }

    private Map<String, String> mapProperties(Properties properties) {
        Map<String, String> map = properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
        return map;
    }


    public Optional<DomibusConnectorLinkConfiguration> getLinkConfiguration(DomibusConnectorLinkConfiguration.LinkConfigName configName) {
        Optional<PDomibusConnectorLinkConfiguration> oneByConfigName = linkConfigurationDao.getOneByConfigName(configName.getConfigName());
        return Optional.ofNullable(this.mapToLinkConfiguration(oneByConfigName.orElse(null)));
    }

    public Optional<DomibusConnectorLinkPartner> getLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        Optional<PDomibusConnectorLinkPartner> linkPartner = linkPartnerDao.findOneByLinkName(linkPartnerName.getLinkName());
        return Optional.ofNullable(this.mapToLinkPartner(linkPartner.orElse(null)));
    }
}

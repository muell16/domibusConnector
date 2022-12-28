package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.model.DC5LinkConfigJpaEntity;
import eu.domibus.connector.persistence.model.DC5LinkPartnerJpaEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class DCLinkPersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DCLinkPersistenceService.class);

    private static final String CONFIG_PROPERTY_PREFIX = "prop.";
    private static final String PULL_INTERVAL_PROPERTY = "pull-interval";
    private static final String SEND_LINK_MODE_PROPERTY = "send-link-mode";
    private static final String RCV_LINK_MODE_PROPERTY = "rcv-link-mode";

    private final DomibusConnectorLinkPartnerDao linkPartnerDao;
    private final DomibusConnectorLinkConfigurationDao linkConfigurationDao;

    public DCLinkPersistenceService(DomibusConnectorLinkPartnerDao linkPartnerDao,
                                    DomibusConnectorLinkConfigurationDao linkConfigurationDao) {
        this.linkPartnerDao = linkPartnerDao;
        this.linkConfigurationDao = linkConfigurationDao;
    }


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

    private DomibusConnectorLinkPartner mapToLinkPartner(DC5LinkPartnerJpaEntity dbLinkInfo) {
        DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
        BeanUtils.copyProperties(dbLinkInfo, linkPartner);
        DC5LinkConfigJpaEntity linkConfiguration = dbLinkInfo.getLinkConfiguration();

        linkPartner.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(dbLinkInfo.getLinkName()));
        linkPartner.setLinkConfiguration(mapToLinkConfiguration(linkConfiguration));

        Map<String, String> dbProperties = dbLinkInfo.getProperties();
        String pullInterval = dbProperties.get(PULL_INTERVAL_PROPERTY);
        if (!StringUtils.isEmpty(pullInterval)) {
            linkPartner.setPullInterval(Duration.parse(pullInterval));
        }
        linkPartner.setSendLinkMode(mapOrDefault(dbProperties.get(SEND_LINK_MODE_PROPERTY)));
        linkPartner.setRcvLinkMode(mapOrDefault(dbProperties.get(RCV_LINK_MODE_PROPERTY)));
        linkPartner.setProperties(mapToLinkPartnerProperties(dbProperties));
        linkPartner.setConfigurationSource(ConfigurationSource.DB);

        return linkPartner;
    }

    private LinkMode mapOrDefault(String s) {
        return LinkMode.ofDbName(s).orElse(null);
    }


    private DomibusConnectorLinkConfiguration mapToLinkConfiguration(DC5LinkConfigJpaEntity dbLinkConfig) {
        if (dbLinkConfig == null) {
            return null;
        }
        DomibusConnectorLinkConfiguration configuration = new DomibusConnectorLinkConfiguration();

        HashMap<String, String> p = new HashMap<>();
        p.putAll(dbLinkConfig.getProperties());
        configuration.setProperties(p);
        configuration.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName(dbLinkConfig.getConfigName()));
        configuration.setLinkImpl(dbLinkConfig.getLinkImpl());
        configuration.setConfigurationSource(ConfigurationSource.DB);
        return configuration;
    }




//    @Transactional
    public void addLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        if (linkPartner.getLinkConfiguration() == null) {
            throw new IllegalArgumentException("Cannot add a LinkPartner without an LinkConfiguration!");
        }
        DC5LinkPartnerJpaEntity dbLinkPartner = mapToDbLinkPartner(linkPartner);

        DC5LinkConfigJpaEntity dbLinkConfiguration = mapToDbLinkConfiguration(linkPartner.getLinkConfiguration());
        dbLinkConfiguration = linkConfigurationDao.save(dbLinkConfiguration);
        dbLinkPartner.setLinkConfiguration(dbLinkConfiguration);

        linkPartnerDao.save(dbLinkPartner);
        LOGGER.debug("Saving [{}] to database", dbLinkPartner);

        //check for only one gw link config...
        DC5LinkPartnerJpaEntity gatewayExample = new DC5LinkPartnerJpaEntity();
        gatewayExample.setLinkType(LinkType.GATEWAY);
        gatewayExample.setEnabled(true);

        List<DC5LinkPartnerJpaEntity> all = linkPartnerDao.findAll(Example.of(gatewayExample));
        if (all.size() > 1) {
            LOGGER.warn("Only one active gateway configuration at once is allowed - new link will be inactive!");
            dbLinkPartner.setEnabled(false);
            linkPartnerDao.save(dbLinkPartner);
        }

        LOGGER.debug("Successfully saved [{}] to database", dbLinkPartner);

    }

    private DC5LinkPartnerJpaEntity mapToDbLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        if (linkPartner == null) {
            return null;
        }
        String linkName = linkPartner.getLinkPartnerName() == null ? null : linkPartner.getLinkPartnerName().getLinkName();

        Optional<DC5LinkPartnerJpaEntity> oneByLinkName = linkPartnerDao.findOneByLinkName(linkName);
        DC5LinkPartnerJpaEntity dbLinkPartner = oneByLinkName.orElse(new DC5LinkPartnerJpaEntity());

        BeanUtils.copyProperties(linkPartner, dbLinkPartner);

        dbLinkPartner.setLinkType(linkPartner.getLinkType());
        dbLinkPartner.setDescription(linkPartner.getDescription());
        dbLinkPartner.setLinkName(linkName);
        dbLinkPartner.setEnabled(linkPartner.isEnabled());
        dbLinkPartner.setLinkConfiguration(this.mapToDbLinkConfiguration(linkPartner.getLinkConfiguration()));

        Map<String, String> dbProperties = mapToDbProperties(linkPartner.getProperties());
        dbProperties.put(PULL_INTERVAL_PROPERTY, linkPartner.getPullInterval().toString());
        dbProperties.put(SEND_LINK_MODE_PROPERTY, linkPartner.getSendLinkMode().getDbName());
        dbProperties.put(RCV_LINK_MODE_PROPERTY, linkPartner.getRcvLinkMode().getDbName());
        dbLinkPartner.setProperties(dbProperties);

        return dbLinkPartner;
    }

    private DC5LinkConfigJpaEntity mapToDbLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        if (linkConfiguration == null) {
            return null;
        }
        String configName = linkConfiguration.getConfigName() == null ? null : linkConfiguration.getConfigName().getConfigName();

        Optional<DC5LinkConfigJpaEntity> oneByConfigName = linkConfigurationDao.getOneByConfigName(configName);

        DC5LinkConfigJpaEntity dbLinkConfig = oneByConfigName.orElse(new DC5LinkConfigJpaEntity());
        dbLinkConfig.setConfigName(configName);
        dbLinkConfig.setLinkImpl(linkConfiguration.getLinkImpl());
    
        Map<String, String> collect = linkConfiguration.getProperties().entrySet()
                .stream().filter(e -> StringUtils.hasText(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        dbLinkConfig.setProperties(collect);

        return dbLinkConfig;
    }

    private Map<String, String> mapToDbProperties(Map<String, String> properties) {
        if (properties == null) {
            return new HashMap<>();
        }
        Map<String, String> map = properties.entrySet().stream()
                .filter(e -> StringUtils.hasText(e.getValue()))
                .collect(Collectors.toMap(e -> CONFIG_PROPERTY_PREFIX + e.getKey(), Map.Entry::getValue));
        return map;
    }

    private Map<String, String> mapToLinkPartnerProperties(Map<String, String> properties) {
        Map<String, String> map = properties.entrySet().stream()
                .filter(e -> e.getKey().startsWith(CONFIG_PROPERTY_PREFIX))
                .collect(Collectors.toMap(e -> e.getKey().substring(CONFIG_PROPERTY_PREFIX.length()), Map.Entry::getValue));
        return map;
    }

    public Optional<DomibusConnectorLinkConfiguration> getLinkConfiguration(DomibusConnectorLinkConfiguration.LinkConfigName configName) {
        Optional<DC5LinkConfigJpaEntity> oneByConfigName = linkConfigurationDao.getOneByConfigName(configName.getConfigName());
        return Optional.ofNullable(this.mapToLinkConfiguration(oneByConfigName.orElse(null)));
    }

    public Optional<DomibusConnectorLinkPartner> getLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        Optional<DC5LinkPartnerJpaEntity> linkPartner = linkPartnerDao.findOneByLinkName(linkPartnerName.getLinkName());
        return Optional.ofNullable(this.mapToLinkPartner(linkPartner.orElse(null)));
    }

    public void deleteLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = linkPartner.getLinkPartnerName();
        Optional<DC5LinkPartnerJpaEntity> dbEntity = linkPartnerDao.findOneByLinkName(linkPartnerName.getLinkName());
        dbEntity.ifPresent(pDomibusConnectorLinkPartner -> linkPartnerDao.delete(pDomibusConnectorLinkPartner));
    }

    public List<DomibusConnectorLinkPartner> getAllLinks() {
        return linkPartnerDao.findAll()
                .stream()
                .map(this::mapToLinkPartner)
                .collect(Collectors.toList());
    }

    public void updateLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        DC5LinkPartnerJpaEntity dbLinkPartner = mapToDbLinkPartner(linkPartner);
        linkPartnerDao.save(dbLinkPartner);
    }

    @Transactional
    public void updateLinkConfig(DomibusConnectorLinkConfiguration linkConfig) {
    	DC5LinkConfigJpaEntity dbLinkConfig = mapToDbLinkConfiguration(linkConfig);
        linkConfigurationDao.save(dbLinkConfig);
    }

    public void addLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        DC5LinkConfigJpaEntity dbLinkConfiguration = mapToDbLinkConfiguration(linkConfiguration);
        linkConfigurationDao.save(dbLinkConfiguration);
    }

    public void deleteLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        getAllLinks()
                .stream()
                .filter(l -> Objects.equals(l.getLinkConfiguration(), linkConfiguration))
                .forEach(this::deleteLinkPartner);
        DC5LinkConfigJpaEntity DC5LinkConfigJpaEntity = mapToDbLinkConfiguration(linkConfiguration);
        linkConfigurationDao.delete(DC5LinkConfigJpaEntity);
    }
}

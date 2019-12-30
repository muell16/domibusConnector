package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkInfoDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkConfiguration;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class LinkInfoPersistenceService {

    @Autowired
    DomibusConnectorLinkInfoDao linkInfoDao;

    public List<DomibusConnectorLinkInfo> getAllEnabledLinks() {
        return linkInfoDao.findAllByEnabledIsTrue().stream().map(this::mapToLinkInfo).collect(Collectors.toList());
    }

    private DomibusConnectorLinkInfo mapToLinkInfo(PDomibusConnectorLinkInfo dbLinkInfo) {
        DomibusConnectorLinkInfo linkInfo = new DomibusConnectorLinkInfo();
        BeanUtils.copyProperties(dbLinkInfo, linkInfo);
        PDomibusConnectorLinkConfiguration linkConfiguration = dbLinkInfo.getLinkConfiguration();
        DomibusConnectorLinkConfiguration configuration = new DomibusConnectorLinkConfiguration();

        Properties p = new Properties();
        p.putAll(linkConfiguration.getProperties());
        configuration.setProperties(p);

        linkInfo.setLinkConfiguration(configuration);
        return linkInfo;
    }




}

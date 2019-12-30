package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Spring configuration class which reads
 * on startup all configured links from database via
 * {@code LinkInfoPersistenceService}
 * and triggers the {@code DomibusConnectorLinkManager}
 * to create the configured links
 */
@Configuration
public class DomibusConnectorLinkCreatorConfigurationService {

    @Autowired
    LinkInfoPersistenceService linkInfoPersistenceService;

    @Autowired
    DomibusConnectorLinkManager linkManager;

    @PostConstruct
    public void init() {
        linkInfoPersistenceService.getAllEnabledLinks().stream().forEach(this::activateLink);
    }

    private void activateLink(DomibusConnectorLinkInfo linkInfo) {
        linkManager.addLink(linkInfo);
    }


}

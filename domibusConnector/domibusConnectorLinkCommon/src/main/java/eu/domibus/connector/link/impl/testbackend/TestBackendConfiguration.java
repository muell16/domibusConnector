package eu.domibus.connector.link.impl.testbackend;

import eu.domibus.connector.common.DomibusConnectorDefaults;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

/**
 * Initializes and registers a test backend
 *
 */
//@Configuration
//@Profile(LINK_PLUGIN_PROFILE_NAME)
public class TestBackendConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(TestBackendConfiguration.class);

    private final DCActiveLinkManagerService dcActiveLinkManagerService;
    private final ConnectorTestConfigurationProperties testConfigurationProperties;

    public TestBackendConfiguration(DCActiveLinkManagerService dcActiveLinkManagerService,
                                    ConnectorTestConfigurationProperties testConfigurationProperties) {
        this.dcActiveLinkManagerService = dcActiveLinkManagerService;
        this.testConfigurationProperties = testConfigurationProperties;
    }


    @PostConstruct
    public void init() {
        if (testConfigurationProperties.isEnabled()) {

            DomibusConnectorLinkConfiguration linkConfiguration = new DomibusConnectorLinkConfiguration();
            linkConfiguration.setConfigurationSource(ConfigurationSource.IMPL);
            linkConfiguration.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName("TestBackendConfig"));
            linkConfiguration.setLinkImpl(TestbackendPlugin.IMPL_NAME);

            DomibusConnectorLinkPartner domibusConnectorLinkPartner = new DomibusConnectorLinkPartner();
            domibusConnectorLinkPartner.setEnabled(true);
            domibusConnectorLinkPartner.setDescription("Default Test Backend Link Partner");
            domibusConnectorLinkPartner.setLinkType(LinkType.BACKEND);
            domibusConnectorLinkPartner.setConfigurationSource(ConfigurationSource.IMPL);
            domibusConnectorLinkPartner.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(DomibusConnectorDefaults.DEFAULT_TEST_BACKEND));
            domibusConnectorLinkPartner.setLinkConfiguration(linkConfiguration);

            LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Connector2Connector tests are enabled: property {}={}. Connector Test Backend will be activated!", ConnectorTestConfigurationProperties.PREFIX, testConfigurationProperties.isEnabled());
            dcActiveLinkManagerService.activateLinkPartner(domibusConnectorLinkPartner);
        } else {
            LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Connector2Connector tests are not enabled: property {}={}. No Connector Test Backend will be activated!", ConnectorTestConfigurationProperties.PREFIX, testConfigurationProperties.isEnabled());
        }
    }

}


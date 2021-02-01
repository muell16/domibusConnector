package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "debug=true",
        "connector.confirmation-messages.retrieval.service.service-type=serviceType",
        "connector.confirmation-messages.retrieval.service.name=aService",
        "connector.confirmation-messages.retrieval.action=retrievalAction",
})
class ConfigurationPropertyLoaderServiceImplTest {

    @Autowired
    ConfigurationPropertyLoaderServiceImpl propertyLoaderService;

    @SpringBootApplication(scanBasePackages = "eu.domibus.connector.common")
    public static class TestContext {

    }

    @Test
    void loadConfiguration() {
        EvidenceActionServiceConfigurationProperties evidenceActionServiceConfigurationProperties = propertyLoaderService.loadConfiguration(DomibusConnectorMessageLane.getDefaultMessageLaneId(), EvidenceActionServiceConfigurationProperties.class);

        assertThat(evidenceActionServiceConfigurationProperties).isNotNull();

        EvidenceActionServiceConfigurationProperties.AS4Action action = evidenceActionServiceConfigurationProperties.getRetrieval().getAction();
        assertThat(action.getAction()).isEqualTo("retrievalAction");

        EvidenceActionServiceConfigurationProperties.AS4Service service = evidenceActionServiceConfigurationProperties.getRetrieval().getService();
        assertThat(service).isNotNull();
        assertThat(service.getName()).as("service is aService").isEqualTo("aService");
        assertThat(service.getConnectorService().getService()).as("Connector Service must be").isEqualTo("aService");
        assertThat(service.getConnectorService().getServiceType()).as("Connector serviceType must be").isEqualTo("serviceType");

    }
}
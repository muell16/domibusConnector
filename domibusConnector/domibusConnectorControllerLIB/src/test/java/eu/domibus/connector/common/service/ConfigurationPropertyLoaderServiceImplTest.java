package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "debug=true",
        "connector.confirmation-messages.retrieval.service.service-type=serviceType",
        "connector.confirmation-messages.retrieval.service.name=aService",
        "connector.confirmation-messages.retrieval.action=retrievalAction",
})
@EnableConfigurationProperties(ConnectorConfigurationProperties.class)
public class ConfigurationPropertyLoaderServiceImplTest {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationPropertyLoaderServiceImplTest.class);

    @Autowired
    ConfigurationPropertyLoaderServiceImpl propertyLoaderService;

    @MockBean
    DCBusinessDomainManagerImpl dcBusinessDomainManagerImpl;

    @SpringBootApplication(scanBasePackages = "eu.domibus.connector.common")
    public static class TestContext {

    }

    @Test
    void loadConfiguration() {
        EvidenceActionServiceConfigurationProperties evidenceActionServiceConfigurationProperties = propertyLoaderService.loadConfiguration(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), EvidenceActionServiceConfigurationProperties.class);

        assertThat(evidenceActionServiceConfigurationProperties).isNotNull();

        EvidenceActionServiceConfigurationProperties.AS4Action action = evidenceActionServiceConfigurationProperties.getRetrieval().getAction();
        assertThat(action.getAction()).isEqualTo("retrievalAction");

        EvidenceActionServiceConfigurationProperties.AS4Service service = evidenceActionServiceConfigurationProperties.getRetrieval().getService();
        assertThat(service).isNotNull();
        assertThat(service.getName()).as("service is aService").isEqualTo("aService");
        assertThat(service.getConnectorService().getService()).as("Connector Service must be").isEqualTo("aService");
        assertThat(service.getConnectorService().getServiceType()).as("Connector serviceType must be").isEqualTo("serviceType");

    }


    @Test
    public void testGetPropertyMap() {
        MyTestProperties myTestProperties = new MyTestProperties();
        myTestProperties.setProp1("prop1");
        myTestProperties.setProp2(23);
        myTestProperties.getNested().setAbc("abc");
        myTestProperties.getNested().setDuration(Duration.ofDays(23));
        myTestProperties.getNested().setaVeryLongPropertyName("propLong");

        Map<String, String> propertyMap = propertyLoaderService.createPropertyMap(DomibusConnectorBusinessDomain.getDefaultMessageLaneId(), myTestProperties);

//        assertThat(propertyMap).hasSize(4);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("test.example.prop1", "prop1");
        expectedMap.put("test.example.prop2", "23");
        expectedMap.put("test.example.nested.abc", "abc");
        expectedMap.put("test.example.nested.duration", "PT552H");
        expectedMap.put("test.example.nested.a-very-long-property-name", "propLong");

        assertThat(propertyMap).containsExactlyEntriesOf(expectedMap);

        LOGGER.info("Mapped properties are: [{}]", propertyMap);
    }
}
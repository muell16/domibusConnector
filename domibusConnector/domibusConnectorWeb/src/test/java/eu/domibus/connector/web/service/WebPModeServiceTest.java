package eu.domibus.connector.web.service;

import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPartyPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;
import eu.domibus.connector.persistence.spring.PersistenceProfiles;
import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StreamUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
    properties = { "spring.liquibase.change-log=classpath:/db/changelog/install.xml" }
)
@ActiveProfiles({PersistenceProfiles.STORAGE_DB_PROFILE_NAME, "test"})
public class WebPModeServiceTest {

    @SpringBootApplication(
            scanBasePackages = {"eu.domibus.connector.persistence"}
    )
    public static class TestContext {
        @Bean
        public WebPModeService webPModeService() {
            return new WebPModeService();
        }
    }


    @Autowired
    WebPModeService webPModeService;


    @Autowired
    private DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;


    @Test
    void importPModes() throws IOException {
        assertThat(webPModeService).isNotNull();

        Resource resource = new ClassPathResource("pmodes/example-pmodes-1.xml");
        byte[] pMode = StreamUtils.copyToByteArray(resource.getInputStream());

        webPModeService.importPModes(pMode, Mockito.mock(ConfigurationUtil.class));

        assertThat(webPModeService.getPartyList())
                .as("example pmodes contains 12 parties")
                .hasSize(12);

    }


    @Test
    void importPModesTwice() throws IOException {
        assertThat(webPModeService).isNotNull();

        Resource resource = new ClassPathResource("pmodes/example-pmodes-1.xml");
        byte[] pMode = StreamUtils.copyToByteArray(resource.getInputStream());

        webPModeService.importPModes(pMode, Mockito.mock(ConfigurationUtil.class));

        webPModeService.importPModes(pMode, Mockito.mock(ConfigurationUtil.class));

        assertThat(webPModeService.getPartyList())
                .as("example pmodes contains 12 parties")
                .hasSize(12);

    }
}
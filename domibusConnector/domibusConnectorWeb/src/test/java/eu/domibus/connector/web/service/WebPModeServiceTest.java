package eu.domibus.connector.web.service;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.evidences.spring.HomePartyConfigurationProperties;
import eu.domibus.connector.persistence.spring.PersistenceProfiles;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(
    properties = {
            "spring.liquibase.change-log=classpath:/db/changelog/install.xml",
            "spring.liquibase.enabled=true",
            "spring.jta.atomikos.properties.default-jta-timeout=30m",
            "spring.jta.atomikos.properties.max-timeout=30m"
    }
)
@ActiveProfiles({PersistenceProfiles.STORAGE_DB_PROFILE_NAME, "test"})
public class WebPModeServiceTest {

    @SpringBootApplication(
            scanBasePackages = {"eu.domibus.connector"}
    )
    public static class TestContext {
    }

    @MockBean
    SubmitToLinkService submitToLinkService;

    @Autowired
    HomePartyConfigurationProperties homePartyConfigurationProperties;

    @Autowired
    SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

    @Autowired
    WebPModeService webPModeService;



    @Test
    void importPModes() throws IOException {
        assertThat(webPModeService).isNotNull();

        Resource resource = new ClassPathResource("pmodes/example-pmodes-1.xml");
        byte[] pMode = StreamUtils.copyToByteArray(resource.getInputStream());

        byte[] keyStoreBytes = "Hello World".getBytes(StandardCharsets.UTF_8);

        DomibusConnectorKeystore keystore = webPModeService.importConnectorstore(keyStoreBytes, "pw", DomibusConnectorKeystore.KeystoreType.JKS);
        webPModeService.importPModes(pMode, "description", keystore);

        assertThat(webPModeService.getPartyList())
                .as("example pmodes contains 24 parties")
                .hasSize(24);

        CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());

        assertThat(homePartyConfigurationProperties.getName())
                .isEqualTo("service_ctp");
        assertThat(homePartyConfigurationProperties.getEndpointAddress())
                .isEqualTo("https://ctpo.example.com/domibus/services/msh");

        assertThat(securityToolkitConfigurationProperties.getTruststore().getPassword())
                .isEqualTo("pw");
        assertThat(StreamUtils.copyToByteArray(securityToolkitConfigurationProperties.getTruststore().getPathAsResource().getInputStream())).isEqualTo(keyStoreBytes);
//        assertThat(securityToolkitConfigurationProperties.getTruststore().get)

        //TODO: check key store config...

    }

    @Test
    void importPModesSet2() throws IOException {
        assertThat(webPModeService).isNotNull();

        Resource resource = new ClassPathResource("pmodes/example-pmodes-2.xml");
        byte[] pMode = StreamUtils.copyToByteArray(resource.getInputStream());

        DomibusConnectorKeystore keystore = webPModeService.importConnectorstore(new byte[0], "pw", DomibusConnectorKeystore.KeystoreType.JKS);
        webPModeService.importPModes(pMode, "description", keystore);


        assertThat(webPModeService.getPartyList())
                .as("example pmodes contains 88 parties")
                .hasSize(88);

        //TODO: also check party attributes within DB!

    }

    @Test
    void importPModesTwice() throws IOException {
        assertThat(webPModeService).isNotNull();

        Resource resource = new ClassPathResource("pmodes/example-pmodes-1.xml");
        byte[] pMode = StreamUtils.copyToByteArray(resource.getInputStream());

        DomibusConnectorKeystore keystore = webPModeService.importConnectorstore(new byte[0], "pw", DomibusConnectorKeystore.KeystoreType.JKS);
        webPModeService.importPModes(pMode, "description", keystore);


        webPModeService.importPModes(pMode, "description", keystore);


        assertThat(webPModeService.getPartyList())
                .as("example pmodes contains 24 parties")
                .hasSize(24);

    }
}
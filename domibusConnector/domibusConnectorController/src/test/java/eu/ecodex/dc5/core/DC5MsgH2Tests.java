package eu.ecodex.dc5.core;

import eu.ecodex.dc5.message.MessageJpaConfiguration;
import eu.ecodex.dc5.message.repo.DC5EbmsRepo;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
//        "spring.jpa.hibernate.ddl-auto=create", // apparently not needed
        "spring.jpa.show-sql=true",
        "spring.liquibase.enabled=false"
})
class DC5MsgH2Tests {

    @SpringBootApplication
    @Import({MessageJpaConfiguration.class})
    public static class TestConfiguration {}

    @Autowired
    private DC5MessageRepo msgRepo;

//    @Autowired
//    private DC5PayloadRepo payloadRepo;
    @Autowired
    private DC5EbmsRepo ebmsRepo;

    // Tests use the same H2 instance!

    @Test
    public void contextLoads() {
        assertThat(msgRepo).isNotNull();
    }

    // TODO: test query performance
    // TODO: test persistence behaviour

    @Test
    public void can_persist_message() {
        // Arrange
        final DC5Message dc5BusinessDocumentMessage = new DC5Message();
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5BusinessDocumentMessage.setEbmsData(dc5Ebms);

        // Act
        final DC5Message save = msgRepo.save(dc5BusinessDocumentMessage);

        // Assert
        assertThat(save.getId()).isNotNull();
    }

    @Test
    public void persisting_a_message_also_persits_ebms_segment() {
        // Arrange
        final DC5Message dc5BusinessDocumentMessage = new DC5Message();
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5Ebms.setEbmsMessageId(EbmsMessageId.ofString("foo"));
        dc5BusinessDocumentMessage.setEbmsData(dc5Ebms);

        // Act
        final DC5Message save = msgRepo.save(dc5BusinessDocumentMessage);

        // Assert
//        Assertions.assertThat(Optional.empty()).isPresent(); // see it fail
        assertThat(ebmsRepo.findByEbmsMessageId(EbmsMessageId.ofString("foo"))).isPresent();
    }

    @Test
    public void can_persist_ebms_entity() {
        // Arrange
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5Ebms.setBackendAddress(new DC5EcxAddress("ecxAddrSend", new DC5Party("ID_SENDER", "FOO")));
        dc5Ebms.setGatewayAddress(new DC5EcxAddress("ecxAddrRec", new DC5Party("ID_RECEIVER", "BAZ")));

        // Act
        final Long id = ebmsRepo.save(dc5Ebms).getId();
        final DC5Ebms save = ebmsRepo.findById(id).get();

        // Assert
        assertThat(save.getGatewayAddress().getEcxAddress()).isEqualTo("ecxAddrRec");
        assertThat(save.getGatewayAddress().getParty().getPartyId()).isEqualTo("ID_RECEIVER");

        assertThat(save.getBackendAddress().getEcxAddress()).isEqualTo("ecxAddrSend");
        assertThat(save.getBackendAddress().getParty().getPartyId()).isEqualTo("ID_SENDER");
    }
}
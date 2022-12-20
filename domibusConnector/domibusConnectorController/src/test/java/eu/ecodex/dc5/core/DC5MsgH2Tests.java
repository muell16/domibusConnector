package eu.ecodex.dc5.core;

import eu.ecodex.dc5.message.repo.DC5EbmsRepo;
import eu.ecodex.dc5.core.repository.DC5PayloadRepo;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
//        "spring.jpa.hibernate.ddl-auto=create", // apparently not needed
        "spring.jpa.show-sql=true",
        "spring.liquibase.enabled=false"
})

class DC5MsgH2Tests {

    @SpringBootApplication
    public static class TestConfiguration {}

    @Autowired
    private DC5MessageRepo msgRepo;

    @Autowired
    private DC5PayloadRepo payloadRepo;
    @Autowired
    private DC5EbmsRepo ebmsRepo;

    // Tests use the same H2 instance!

    @Test
    public void contextLoads() {
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
        Assertions.assertThat(save.getId()).isGreaterThan(1000L);
    }

    @Test
    public void persisting_a_message_also_persits_ebms_segment() {
        // Arrange
        final DC5Message dc5BusinessDocumentMessage = new DC5Message();
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5Ebms.setEbmsMessageId(EbmsMessageId.ofRandom());
        dc5BusinessDocumentMessage.setEbmsData(dc5Ebms);

        // Act
        final DC5Message save = msgRepo.save(dc5BusinessDocumentMessage);

        // Assert
//        Assertions.assertThat(Optional.empty()).isPresent(); // see it fail
        Assertions.assertThat(ebmsRepo.findByEbmsMessageId("foo")).isPresent();
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
        Assertions.assertThat(save.getGatewayAddress().getEcxAddress()).isEqualTo("ecxAddrRec");
        Assertions.assertThat(save.getGatewayAddress().getParty().getPartyId()).isEqualTo("ID_RECEIVER");
//        Assertions.assertThat(save.getGatewayAddress().getRole().getRoleType()).isEqualTo(DC5RoleType.RESPONDER);

        Assertions.assertThat(save.getBackendAddress().getEcxAddress()).isEqualTo("ecxAddrSend");
        Assertions.assertThat(save.getBackendAddress().getParty().getPartyId()).isEqualTo("ID_SENDER");
//        Assertions.assertThat(save.getBackendAddress().getRole().getRoleType()).isEqualTo(DC5RoleType.INITIATOR);
    }
}
package eu.ecodex.dc5.core;

import eu.ecodex.dc5.core.model.*;
import eu.ecodex.dc5.core.repository.DC5EbmsRepo;
import eu.ecodex.dc5.core.repository.DC5MessageRepo;
import eu.ecodex.dc5.core.repository.DC5PayloadRepo;
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
        "spring.jpa.show-sql=true"
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
        final DC5Msg dc5BusinessDocumentMessage = new DC5Msg();
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5BusinessDocumentMessage.setEbmsSegment(dc5Ebms);

        // Act
        final DC5Msg save = msgRepo.save(dc5BusinessDocumentMessage);

        // Assert
        Assertions.assertThat(save.getId()).isGreaterThan(1000L);
    }

    @Test
    public void persisting_a_message_also_persits_ebms_segment() {
        // Arrange
        final DC5Msg dc5BusinessDocumentMessage = new DC5Msg();
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5Ebms.setEbmsMessageId("foo");
        dc5BusinessDocumentMessage.setEbmsSegment(dc5Ebms);

        // Act
        final DC5Msg save = msgRepo.save(dc5BusinessDocumentMessage);

        // Assert
//        Assertions.assertThat(Optional.empty()).isPresent(); // see it fail
        Assertions.assertThat(ebmsRepo.findByEbmsMessageId("foo")).isPresent();
    }

    @Test
    public void can_persist_ebms_entity() {
        // Arrange
        final DC5Ebms dc5Ebms = new DC5Ebms();
        dc5Ebms.setSender(new DC5EcxAddress("ecxAddrSend", new DC5Party("ID_SENDER", "FOO"), new DC5Role("SENDER", DC5RoleType.INITIATOR)));
        dc5Ebms.setReceiver(new DC5EcxAddress("ecxAddrRec", new DC5Party("ID_RECEIVER", "BAZ"), new DC5Role("RECEIVER", DC5RoleType.RESPONDER)));

        // Act
        final Long id = ebmsRepo.save(dc5Ebms).getId();
        final DC5Ebms save = ebmsRepo.findById(id).get();

        // Assert
        Assertions.assertThat(save.getReceiver().getEcxAddress()).isEqualTo("ecxAddrRec");
        Assertions.assertThat(save.getReceiver().getParty().getPartyId()).isEqualTo("ID_RECEIVER");
        Assertions.assertThat(save.getReceiver().getRole().getRoleType()).isEqualTo(DC5RoleType.RESPONDER);

        Assertions.assertThat(save.getSender().getEcxAddress()).isEqualTo("ecxAddrSend");
        Assertions.assertThat(save.getSender().getParty().getPartyId()).isEqualTo("ID_SENDER");
        Assertions.assertThat(save.getSender().getRole().getRoleType()).isEqualTo(DC5RoleType.INITIATOR);
    }
}
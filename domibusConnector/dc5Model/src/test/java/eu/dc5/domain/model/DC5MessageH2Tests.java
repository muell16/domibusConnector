package eu.dc5.domain.model;

import eu.dc5.domain.repository.DC5MessageRepo;
import eu.dc5.domain.repository.DC5PayloadRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class DC5MessageH2Tests {

    @Autowired
    private DC5MessageRepo msgRepo;

    @Autowired
    private DC5PayloadRepo payloadRepo;

    // Tests use the same H2 instance!

    @Test
    public void contextLoads() {

    }

    @Test
    public void canStoreMessage() {
        final DC5BusinessDocumentMessage dc5BusinessDocumentMessage = new DC5BusinessDocumentMessage();
        new DC5Payload();
        final DC5BusinessDocumentMessage save = msgRepo.save(dc5BusinessDocumentMessage);

        Assertions.assertThat(save.getId()).isGreaterThan(1000L);
    }

    @Test
    public void storing_a_message_with_payload_also_persists_the_payload() {
        // Arrange
        final DC5BusinessDocumentMessage dc5BusinessDocumentMessage = new DC5BusinessDocumentMessage();
        final DC5Payload payload = new DC5Payload(); // bidirectional mapping requires linking from both ends
        payload.setMessage(dc5BusinessDocumentMessage);
        dc5BusinessDocumentMessage.getPayload().add(payload);

        // Act
        final DC5BusinessDocumentMessage save = msgRepo.saveAndFlush(dc5BusinessDocumentMessage);

        // Assert
        final DC5Payload persistedPayload = payloadRepo.findAll().get(0);
        Assertions.assertThat(persistedPayload.getMessage().getId()).isEqualTo(save.getId());
    }
}
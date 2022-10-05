package eu.dc5.domain.model;

import eu.dc5.domain.repository.DC5MessageRepo;
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
        "spring.jpa.hibernate.ddl-auto=create"
})
class DC5MessageH2Tests {

    @Autowired
    private DC5MessageRepo msgRepo;

//    @Example
    @Test
    public void contextLoads() {

    }

    @Test
    public void canStoreMessage() {
        final DC5BusinessDocumentMessage dc5BusinessDocumentMessage = new DC5BusinessDocumentMessage();
        final DC5BusinessDocumentMessage save = msgRepo.save(dc5BusinessDocumentMessage);

        Assertions.assertThat(save.getId()).isEqualTo(1001L);
    }
}
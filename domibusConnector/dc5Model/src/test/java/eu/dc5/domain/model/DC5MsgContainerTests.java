package eu.dc5.domain.model;

import eu.dc5.domain.repository.DC5MessageRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//@JqwikSpringSupport
@SpringBootTest
@Testcontainers
//@ContextConfiguration(classes = TestApplication.class)
//@BootstrapWith(SpringBootTestContextBootstrapper.class)
//@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Disabled // does not work on CI, must be disabled before check in!
class DC5MsgContainerTests {

    // TODO: WIP

    @Autowired
    private DC5MessageRepo msgRepo;

    @Container
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:latest");

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }

//    @Example
    @Test
    public void contextLoads() {

    }

//    @Example
    @Test
    public void canStoreMessage() {
        final DC5MsgBusinessDocument dc5BusinessDocumentMessage = new DC5MsgBusinessDocument();
        msgRepo.save(dc5BusinessDocumentMessage);
    }
}
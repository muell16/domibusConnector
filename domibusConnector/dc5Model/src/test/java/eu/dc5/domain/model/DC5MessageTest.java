package eu.dc5.domain.model;

import net.jqwik.api.Example;
import net.jqwik.spring.JqwikSpringSupport;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@JqwikSpringSupport
@Testcontainers
class DC5MessageTest {
    @Container
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:latest");

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }

    @Example
    public void contextLoads() {

    }
}
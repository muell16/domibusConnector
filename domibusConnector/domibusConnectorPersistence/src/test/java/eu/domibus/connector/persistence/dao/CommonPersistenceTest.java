package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.DBRiderTestExecutionListener;
import com.github.database.rider.spring.api.DBRider;
import eu.domibus.connector.persistence.testutil.RecreateDbByLiquibaseTestExecutionListener;
import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = SetupPersistenceContext.class)
@TestPropertySource(properties = {
        "connector.persistence.big-data-impl-class=eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl",
        "spring.liquibase.change-log=db/changelog/test/testdata.xml",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.active.profiles=connector,db-storage"
})
@TestExecutionListeners(
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {RecreateDbByLiquibaseTestExecutionListener.class, //drop and create db by liquibase after each TestClass
                DBRiderTestExecutionListener.class, //activate @DBRider
        })
@ActiveProfiles({"test", "db_h2", "storage-db"})
@DBUnit(allowEmptyFields = true)
@Inherited
public @interface CommonPersistenceTest {
}

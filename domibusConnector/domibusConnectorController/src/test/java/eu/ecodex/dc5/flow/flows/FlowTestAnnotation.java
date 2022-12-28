package eu.ecodex.dc5.flow.flows;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@AutoConfigureTestDatabase
@SpringBootTest(classes = eu.ecodex.dc5.DC5FlowModule.class)
@ActiveProfiles({"small", "flow-test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS, hierarchyMode = DirtiesContext.HierarchyMode.EXHAUSTIVE)
@EnableTransactionManagement
public @interface FlowTestAnnotation {
}

package eu.domibus.connector.controller.test.util;


import org.junit.runner.RunWith;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Inherited;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ITCaseTestContext.class})
@TestPropertySource("classpath:application-test.properties")
@Commit
@ActiveProfiles({"ITCaseTestContext", "storage-db"})
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface ITCaseTestAnnotation {
}

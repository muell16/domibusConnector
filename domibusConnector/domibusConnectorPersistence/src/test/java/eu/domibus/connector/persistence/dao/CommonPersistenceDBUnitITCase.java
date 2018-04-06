
package eu.domibus.connector.persistence.dao;

import javax.sql.DataSource;

import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public abstract class CommonPersistenceDBUnitITCase {

    protected static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @BeforeClass
    public static void beforeClass() {
        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext();
    }

    @AfterClass
    public static void afterClass() {
        APPLICATION_CONTEXT.close();
    }

    protected DataSource ds;


    protected ConfigurableApplicationContext applicationContext;
        
    @Before
    public void setUp() throws Exception {        
        this.applicationContext = APPLICATION_CONTEXT;
        //lookup type
        this.ds = APPLICATION_CONTEXT.getBean(DataSource.class);
        //lookup name
//        this.persistenceService = APPLICATION_CONTEXT.getBean("persistenceService", DomibusConnectorPersistenceService.class);
    }

}

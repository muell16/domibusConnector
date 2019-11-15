
package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.time.Duration;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorMsgContent.xml", strategy = CLEAN_INSERT)
public class DomibusConnectorMsgContDaoDBUnit extends CommonPersistenceDBUnitITCase {

    @Autowired
    private DomibusConnectorMsgContDao msgContDao;

    @Autowired
    private DomibusConnectorMessageDao messageDao;

    @Autowired
    private DatabaseDataSourceConnection ddsc;

//    @BeforeEach
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        this.msgContDao = applicationContext.getBean(DomibusConnectorMsgContDao.class);
//        this.messageDao = applicationContext.getBean(DomibusConnectorMessageDao.class);
//
//        //Load testdata
//        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorMsgContent.xml").getInputStream()));
//
//        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
//        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
//
//    }

    @Test
    public void testDeleteByMessage() throws SQLException, AmbiguousTableNameException, DataSetException {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            PDomibusConnectorMessage message = messageDao.findOneByConnectorMessageId("conn1");
            msgContDao.deleteByMessage(message);

            //check result in DB
            DatabaseDataSourceConnection conn = ddsc;
            QueryDataSet dataSet = new QueryDataSet(conn);
            dataSet.addTable("DOMIBUS_CONNECTOR_MSG_CONT", "SELECT * FROM DOMIBUS_CONNECTOR_MSG_CONT");

            ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_MSG_CONT");

            int rows = domibusConnectorTable.getRowCount();

            assertThat(rows).isEqualTo(1);
        });
    }
    
    
    
    
    
}

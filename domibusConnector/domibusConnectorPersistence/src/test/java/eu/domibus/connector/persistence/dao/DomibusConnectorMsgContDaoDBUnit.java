
package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.service.CommonPersistenceDBUnitITCase;
import java.sql.SQLException;
import java.util.Date;
import static org.assertj.core.api.Assertions.*;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorMsgContDaoDBUnit extends CommonPersistenceDBUnitITCase {

    private DomibusConnectorMsgContDao msgContDao;
       
    private DomibusConnectorMessageDao messageDao;
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();        
        this.msgContDao = applicationContext.getBean(DomibusConnectorMsgContDao.class);
        this.messageDao = applicationContext.getBean(DomibusConnectorMessageDao.class);

        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorMsgContent.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
    }
    
    @Test
    public void testDeleteByMessage() throws SQLException, AmbiguousTableNameException, DataSetException {
        PDomibusConnectorMessage message = messageDao.findOneByConnectorMessageId("conn1");
        msgContDao.deleteByMessage(message);
        
        //check result in DB        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("DOMIBUS_CONNECTOR_MSG_CONT", "SELECT * FROM DOMIBUS_CONNECTOR_MSG_CONT");
       
        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_MSG_CONT");
        
        int rows = domibusConnectorTable.getRowCount();
        
        assertThat(rows).isEqualTo(1);
    }
    
    
    
    
    
}

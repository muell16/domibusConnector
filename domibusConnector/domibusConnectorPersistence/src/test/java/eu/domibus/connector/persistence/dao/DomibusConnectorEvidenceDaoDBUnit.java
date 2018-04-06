
package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import eu.domibus.connector.persistence.model.enums.EvidenceType;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorEvidenceDaoDBUnit extends CommonPersistenceDBUnitITCase {

    private DomibusConnectorEvidenceDao evidenceDao;
    
    private TransactionTemplate transactionTemplate;
    
    private DomibusConnectorMessageDao messageDao;

    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.evidenceDao = applicationContext.getBean(DomibusConnectorEvidenceDao.class);
        this.messageDao = applicationContext.getBean(DomibusConnectorMessageDao.class);
        
        this.transactionTemplate = new TransactionTemplate(applicationContext.getBean(PlatformTransactionManager.class));
        
        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorEvidence.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
    }

    @Test(timeout=20000)
    public void testFindEvidencesForMessage() {
        
        List<PDomibusConnectorEvidence> evidences = evidenceDao.findByMessage_Id(73L);
        
        assertThat(evidences).hasSize(3);
    }

    @Test(timeout=20000)
    public void testSetDeliveredToGateway() throws SQLException, AmbiguousTableNameException, DataSetException {                
        int result = evidenceDao.setDeliveredToGateway(82L);        
        assertThat(result).isEqualTo(1); //check on row updated
        
         //check result in DB
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("DOMIBUS_CONNECTOR_EVIDENCE", "SELECT * FROM DOMIBUS_CONNECTOR_EVIDENCE WHERE ID=82");
       
        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_EVIDENCE");
        Date value = (Date) domibusConnectorTable.getValue(0, "DELIVERED_GW");
        assertThat(value).isCloseTo(new Date(), 2000);        
    }

    @Test(timeout=20000)
    public void testSetDeliveredToGateway_updateNonExistant_shouldReturnZero() {
        int result = evidenceDao.setDeliveredToGateway(882L);        
        assertThat(result).isEqualTo(0); //check on row updated
    }

    @Test(timeout=20000)
    public void testSetDeliveredToGateway_ByMessageIdAndType() throws SQLException, DataSetException {
        PDomibusConnectorMessage dbMessage = new PDomibusConnectorMessage();
        dbMessage.setId(73L);
        int result = evidenceDao.setDeliveredToGateway(dbMessage, EvidenceType.SUBMISSION_REJECTION);

        assertThat(result).isEqualTo(1);
        //check result in DB
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("DOMIBUS_CONNECTOR_EVIDENCE", "SELECT * FROM DOMIBUS_CONNECTOR_EVIDENCE WHERE ID=82");

        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_EVIDENCE");
        Date value = (Date) domibusConnectorTable.getValue(0, "DELIVERED_GW");
        assertThat(value).isNotNull();
        assertThat(value).isCloseTo(new Date(), 2000);
    }


    @Test(timeout=20000)
    public void testSetDeliveredToBackend() throws SQLException, AmbiguousTableNameException, DataSetException {
        int result = evidenceDao.setDeliveredToBackend(83L);        
        assertThat(result).isEqualTo(1); //check one row updated
        
         //check result in DB
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("DOMIBUS_CONNECTOR_EVIDENCE", "SELECT * FROM DOMIBUS_CONNECTOR_EVIDENCE WHERE ID=83");
       
        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_EVIDENCE");
        Date value = (Date) domibusConnectorTable.getValue(0, "DELIVERED_NAT");
        assertThat(value).isCloseTo(new Date(), 2000);
    }

    @Test(timeout=20000)
    public void testSetDeliveredToBackend_ByMessageIdAndType() throws SQLException, DataSetException {
        PDomibusConnectorMessage dbMessage = new PDomibusConnectorMessage();
        dbMessage.setId(74L);
        int result = evidenceDao.setDeliveredToBackend(dbMessage, EvidenceType.SUBMISSION_ACCEPTANCE);

        assertThat(result).isEqualTo(1);
        //check result in DB
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("DOMIBUS_CONNECTOR_EVIDENCE", "SELECT * FROM DOMIBUS_CONNECTOR_EVIDENCE WHERE ID=85");

        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_EVIDENCE");
        Date value = (Date) domibusConnectorTable.getValue(0, "DELIVERED_NAT");
        assertThat(value).isNotNull();
        assertThat(value).isCloseTo(new Date(), 2000);
    }


    @Test(timeout=20000)
    public void testSetDeliveredToBackend_updateNoneExistant_shouldReturnZero() {
        int result = evidenceDao.setDeliveredToBackend(83231L);        
        assertThat(result).isEqualTo(0); //check one row updated
    }

    @Test(timeout=20000)
    public void testSaveEvidence() {
        PDomibusConnectorEvidence dbEvidence = new PDomibusConnectorEvidence();
        
        PDomibusConnectorMessage dbMessage = messageDao.findOne(75L);  
        assertThat(dbMessage).isNotNull();
        
        byte[] evidence = "Hallo Welt".getBytes();

        dbEvidence.setMessage(dbMessage);
        dbEvidence.setEvidence(new String(evidence));
        dbEvidence.setType(eu.domibus.connector.persistence.model.enums.EvidenceType.DELIVERY);
        dbEvidence.setDeliveredToGateway(null);
        dbEvidence.setDeliveredToNationalSystem(null);
        
        evidenceDao.save(dbEvidence);
    }


}

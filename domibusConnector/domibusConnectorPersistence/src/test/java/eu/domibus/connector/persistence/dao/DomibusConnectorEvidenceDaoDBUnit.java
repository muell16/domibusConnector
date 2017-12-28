/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.CommonPersistenceDBUnitITCase;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
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
public class DomibusConnectorEvidenceDaoDBUnit extends CommonPersistenceDBUnitITCase {

    private DomibusConnectorEvidenceDao evidenceDao;

    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.evidenceDao = applicationContext.getBean(DomibusConnectorEvidenceDao.class);
        
        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorEvidence.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
    }
    
    @Test
    public void testFindEvidencesForMessage() {
        fail("not finished yet!");
    }
    
    @Test
    public void testSetDeliveredToGateway() {
        fail("not finished yet!");
    }
  
    
    @Test
    public void testSetDeliveredToBackend() {
        fail("not finished yet!");
    }
}

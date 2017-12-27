/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.CommonPersistenceDBUnitITCase;
import java.io.FileInputStream;
import static org.assertj.core.api.Assertions.*;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorMessageDaoDBUnit extends CommonPersistenceDBUnitITCase {

    private DomibusConnectorMessageDao messageDao;

    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.messageDao = applicationContext.getBean(DomibusConnectorMessageDao.class);
        
//        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorMessage.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
    }
    
    
    @Test
    public void testFindOneMessage() {
        DomibusConnectorMessage msg = messageDao.findOne(73L);
        assertThat(msg).isNotNull();
    }
    
}

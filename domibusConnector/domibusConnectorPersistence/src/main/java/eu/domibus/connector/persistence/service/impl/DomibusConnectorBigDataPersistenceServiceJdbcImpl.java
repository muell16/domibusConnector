/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * this service uses jdbc to create an input / output stream without writing it 
 * to an byte array (jpa/hibernate requires this)
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorBigDataPersistenceServiceJdbcImpl implements DomibusConnectorBigDataPersistenceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceJdbcImpl.class);
    
    private static String TABLE_NAME = "DOMIBUS_CONNECTOR_BIGDATA";
    private static String COL_ID = "ID";
    private static String COL_CHECKSUM = "CHECKSUM";
    private static String COL_CREATED = "CREATED";
    private static String COL_MESSAGE_ID = "MESSAGE_ID";
    private static String COL_LAST_ACCESS = "LAST_ACCESS";
    private static String COL_NAME = "NAME";
    private static String COL_MIMETYPE = "MIMETYPE";
    private static String COL_CONTENT = "CONTENT";
    
    private static String INSERT_INTO = "INSERT INTO DOMIBUS_CONNECTOR_BIGDATA(CHECKSUM, CREATED, MESSAGE_ID, LAST_ACCESS, NAME, MIMETYPE) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
        
    @Autowired
    private DataSource dataSource;
    
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    
    // BEGIN SETTER
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }    
    // END SETTER
    
    @PostConstruct
    public void postConstruct() {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    
    @Override
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        DomibusConnectorBigDataReference reference = new JdbcBackedDomibusConnectorBigDataReference();
        
        //jdbcTemplate.update(COL_ID, paramSource, generatedKeyHolder, keyColumnNames)
        return reference;
    }
    
    @Override
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

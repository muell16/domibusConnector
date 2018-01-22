package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


/**
 * this service uses jdbc to create an input / output stream with writing it 
 * to an byte array (ByteArrayInputStream / ByteArrayOutputStream)
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
@Transactional
public class DomibusConnectorBigDataPersistenceServiceJdbcImpl implements DomibusConnectorBigDataPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceJdbcImpl.class);
    
    private static final String TABLE_NAME = "DOMIBUS_CONNECTOR_BIGDATA";
    private static final String COL_NAME = "NAME";
    private static final String COL_MIMETYPE = "mimetype";
    private static final String COL_CONTENT = "CONTENT";
    private static final String COL_LAST_ACCESS = "LAST_ACCESS";
    
    private static final String INSERT_INTO = "INSERT INTO DOMIBUS_CONNECTOR_BIGDATA(ID, CREATED, MESSAGE_ID, LAST_ACCESS) "
                    + "VALUES (?, ?, ?, ?)";
    
    private static final String QUERY_BY_ID = "SELECT ID, CREATED, MESSAGE_ID, LAST_ACCESS, NAME, MIMETYPE, CONTENT FROM " + TABLE_NAME + " WHERE ID = ?";
    
    private static final String DELETE_ALL_BY_MESSAGE_ID = "DELETE FROM " + TABLE_NAME +  " WHERE MESSAGE_ID = ?";
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private DomibusConnectorMessageDao messageDao;
    
    @Autowired 
    private PlatformTransactionManager transactionManager;
    
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
    @Transactional(readOnly = false)
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
        if (bigDataReference.getStorageIdReference() == null) {
            throw new IllegalArgumentException("storageIdReference must be not null!\n The reference must exist in database!");
        }
        String storageReference = bigDataReference.getStorageIdReference();
        
        JdbcBackedDomibusConnectorBigDataReference reference = new JdbcBackedDomibusConnectorBigDataReference(this);
        
         try (  Connection conn = this.dataSource.getConnection(); 
                PreparedStatement queryForBigDataStm = conn.prepareStatement(QUERY_BY_ID, ResultSet.FETCH_FORWARD, ResultSet.CONCUR_UPDATABLE)) {
            queryForBigDataStm.setString(1, storageReference);
            try (ResultSet resultSet = queryForBigDataStm.executeQuery()) {
                resultSet.next();
                byte[] bytes = resultSet.getBytes(COL_CONTENT);
                InputStream is = new ByteArrayInputStream(bytes);
                String mimetype = resultSet.getString(COL_MIMETYPE);
                String name = resultSet.getString(COL_NAME);
                
                reference.setInputStream(is);
                reference.setReadable(true);
                reference.setStorageIdReference(storageReference);
                reference.setName(name);
                reference.setMimetype(mimetype);
                
                //TODO: update last access field!
                resultSet.updateDate(COL_LAST_ACCESS, new java.sql.Date((new Date()).getTime()));
            }
            
        } catch (SQLException sqle) {
            throw new PersistenceException("SQLException occured in getReadableDataSource during loading bigData from database", sqle);
        }
         return reference;
        

    }

    @Override
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        LOGGER.trace("createDomibusConnectorBigDataReference: called with message {}", message);
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        JdbcBackedDomibusConnectorBigDataReference reference = new JdbcBackedDomibusConnectorBigDataReference(this);
        reference.setStorageIdReference(UUID.randomUUID().toString());


        try (   Connection connection = this.dataSource.getConnection(); 
                PreparedStatement stm = connection.prepareStatement(INSERT_INTO)) {

            reference.setOutputStream(new JdbcBackedOutputStream(reference.getStorageIdReference()));

            stm.setString(1, reference.getStorageIdReference());
            stm.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stm.setLong(3, dbMessage.getId());
            stm.setDate(4, java.sql.Date.valueOf(LocalDate.now()));

            stm.executeUpdate();


        } catch (SQLException sqlException) {
            throw new PersistenceException("SQLException occured during creating BigData Entry in Database", sqlException);
        }
                
        return reference;

    }
    
    @Override
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        LOGGER.trace("deleteDomibusConnectorBigDataReference: called to delete all data related to message {}", message);
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        
        try (   Connection connection = dataSource.getConnection(); 
                PreparedStatement stm = connection.prepareStatement(DELETE_ALL_BY_MESSAGE_ID)) {

            stm.setLong(1, dbMessage.getId());
            int deletedRows = stm.executeUpdate();
            LOGGER.debug("Deleted {} rows of big data content", deletedRows);
            
        } catch (SQLException sqle) {
            throw new PersistenceException(
                    String.format("SQLException occured during deleting all data related to message with db id %s", dbMessage.getId()),
                    sqle);
        }
        
    }

    
    private void writeOutputStreamToDatabase(final JdbcBackedOutputStream outputStream) throws IOException {    
        LOGGER.trace("writeOutputStreamToDatabase: called with outputStream");
        String storageReference = outputStream.getStorageReference();
        
            
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stm = connection.prepareStatement("UPDATE DOMIBUS_CONNECTOR_BIGDATA SET CONTENT=? WHERE ID=?")) {

            stm.setString(2, storageReference);
            stm.setBytes(1, outputStream.toByteArray());    //TODO: avoid byte array Output Stream
            stm.executeUpdate();


        } catch (SQLException sqle) {
            throw new IOException("SQLException occured during writing stream into database!", sqle);
        }                   
    }
    
    
    private class JdbcBackedOutputStream extends ByteArrayOutputStream {
        
        private final String storageReference;
        
        public JdbcBackedOutputStream(String storageReference) {
            this.storageReference = storageReference;          
        }

        public String getStorageReference() {
            return storageReference;
        }
        
        @Override
        public void close() throws IOException {
            writeOutputStreamToDatabase(this);
        }

        
    }

}

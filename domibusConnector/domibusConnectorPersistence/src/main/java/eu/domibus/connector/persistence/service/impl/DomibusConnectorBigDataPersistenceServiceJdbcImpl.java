package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StreamUtils;


/**
 * this service uses jdbc to create an input / output stream without writing it 
 * to an byte array (jpa/hibernate requires this)
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
@Transactional
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
    
    private static String INSERT_INTO = "INSERT INTO DOMIBUS_CONNECTOR_BIGDATA(ID, CREATED, MESSAGE_ID, LAST_ACCESS, CONTENT) "
            + "VALUES (:ID, :CREATED, :MESSAGE_ID, :LAST_ACCESS, :CONTENT)";
        
    private static String UPDATE_BLOB = "UPDATE DOMIBUS_CONNECTOR_BIGDATA SET CONTENT = ? WHERE ID = ?";
    
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
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(DomibusConnectorMessage message) {

        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        JdbcBackedDomibusConnectorBigDataReference reference = new JdbcBackedDomibusConnectorBigDataReference(this);
        reference.setStorageIdReference(UUID.randomUUID().toString());

//        //            Connection conn = dataSource.getConnection();
//        Map<String, Object> params = new HashMap<>();
//        params.put("ID", reference.getStorageIdReference());
//        params.put("CREATED", new Date());
//        params.put("MESSAGE_ID", dbMessage.getId());
//        params.put("LAST_ACCESS", new Date());
//        params.put("CONTENT", " ".getBytes()); //make sure blob is not null!
//
//        jdbcTemplate.update(INSERT_INTO, params);

        try {
        
            Connection connection = this.dataSource.getConnection();
            
            PreparedStatement stm = connection.prepareStatement("INSERT INTO DOMIBUS_CONNECTOR_BIGDATA(ID, CREATED, MESSAGE_ID, LAST_ACCESS) "
                    + "VALUES (?, ?, ?, ?)");
            
//            Blob blob = connection.createBlob();
//            OutputStream blobOutputStream = blob.setBinaryStream(1);
//            reference.setOutputStream(blobOutputStream);
            reference.setOutputStream(new JdbcBackedOutputStream(reference.getStorageIdReference()));
            
            stm.setString(1, reference.getStorageIdReference());
            stm.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stm.setLong(3, dbMessage.getId());
            stm.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
//            stm.setBlob(5, blob);
            
            
            stm.executeUpdate();
            stm.close();
            
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
        
        
        return reference;

    }
    
    @Override
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    InputStream getInputStream(JdbcBackedDomibusConnectorBigDataReference bigDataRef)  {
//        return null;
//    }
//
//    OutputStream getOutputStream(JdbcBackedDomibusConnectorBigDataReference bigDataRef) {
//        
//        try {
//            Connection connection = this.dataSource.getConnection();
////            JdbcBackedOutputStream outputStream = new JdbcBackedOutputStream(bigDataRef.getStorageIdReference());
////            String ref = bigDataRef.getStorageIdReference();
////
////            
////            Blob blob = connection.createBlob();
////            blob.setBinaryStream(0);
////            connection.prepareStatement(INSERT_BLOB_INTO);
////            
////            
////            
//////            Map<String, Object> params = new HashMap<>();
//////            params.put("ID", ref);
//////            params.put("CONTENT", "HalloWelt".getBytes());
//////            OutputStream outputStream = blob.setBinaryStream(1);
//////            StreamUtils.copy("HalloWelt".getBytes(), outputStream);
//////            jdbcTemplate.update(INSERT_BLOB_INTO, params);            
////            
////            //maybe i have to wait before inserting to finish writing to output stream?
//            return outputStream;
//        } catch (SQLException sqlException) {
//            throw new PersistenceException("SQL Exception occured, during open stream to database", sqlException);
//        }
////        } catch (IOException ex) {
////            //remove
////            throw new RuntimeException(ex);
////        }
//    }
    
    private void writeOutputStreamToDatabase(final JdbcBackedOutputStream outputStream) {
//        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
//        transactionTemplate.execute((TransactionStatus status) -> {       
            try  {
                String storageReference = outputStream.getStorageReference();
                Connection connection = dataSource.getConnection();
//                connection.setAutoCommit(false);
                
                PreparedStatement stm = connection.prepareStatement("UPDATE DOMIBUS_CONNECTOR_BIGDATA SET CONTENT=? WHERE ID=?");
                stm.setString(2, storageReference);
                stm.setBytes(1, outputStream.toByteArray());    //TODO: avoid byte array Output Stream
                stm.executeUpdate();   
                stm.close();
//                connection.commit();
                connection.close();
                
//                return null;
            } catch(SQLException sqle) {
                throw new RuntimeException(sqle);
            }            
//        });
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
        public void close() {
            writeOutputStreamToDatabase(this);
        }

        
    }

}

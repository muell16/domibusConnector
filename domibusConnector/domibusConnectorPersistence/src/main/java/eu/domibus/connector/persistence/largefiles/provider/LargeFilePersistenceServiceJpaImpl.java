package eu.domibus.connector.persistence.largefiles.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;

import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.dao.DomibusConnectorBigDataDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorBigData;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;


/**
 * this service uses jdbc to create an input / output stream with writing it 
 * to an byte array (ByteArrayInputStream / ByteArrayOutputStream)
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//initialized by DomibusConnectorPersistenceContext.class
@Transactional
public class LargeFilePersistenceServiceJpaImpl implements LargeFilePersistenceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(LargeFilePersistenceServiceJpaImpl.class);

    public static final String PROVIDER_NAME = "JPA";
    
    @Autowired
    private DomibusConnectorMessageDao messageDao;

    @Autowired
    private DomibusConnectorBigDataDao bigDataDao;

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    @Transactional(readOnly = false)
    public LargeFileReference getReadableDataSource(LargeFileReference bigDataReference) {
        if (bigDataReference.getStorageIdReference() == null) {
            throw new IllegalArgumentException("storageIdReference must be not null!\n The reference must exist in database!");
        }
        String storageReference = bigDataReference.getStorageIdReference();

        try {
            long storageRef = Long.parseLong(storageReference);
            PDomibusConnectorBigData bigData = null;
            LOGGER.debug("Loading big data with storage ref [{}] from database", storageRef);
            Optional<PDomibusConnectorBigData> bigDataOptional = bigDataDao.findById(storageRef);
            
            if(bigDataOptional.isPresent()) {
            	bigData = bigDataOptional.get();
            } else {
                throw new RuntimeException(String.format("No data found in DB for JPA based LargeFileReference with id [%s]\n" +
                        "Check Table %s", storageRef, PDomibusConnectorBigData.TABLE_NAME));
            }

            JpaBasedLargeFileReference jpaBasedDomibusConnectorBigDataReference = new JpaBasedLargeFileReference();
            jpaBasedDomibusConnectorBigDataReference.setStorageProviderName(this.getProviderName());

            //TODO: use stream from db!
            Blob content = bigData.getContent();
            if (content != null) {
                InputStream dbStream = content.getBinaryStream();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(StreamUtils.copyToByteArray(dbStream));
                jpaBasedDomibusConnectorBigDataReference.setInputStream(byteArrayInputStream);
            } else {
                String error = String.format("Blob Content of bigDataStorage with reference [%d] is null!", storageRef);
                throw new IllegalStateException(error);
            }


            jpaBasedDomibusConnectorBigDataReference.setStorageIdReference(storageReference);
            jpaBasedDomibusConnectorBigDataReference.setReadable(true);
            jpaBasedDomibusConnectorBigDataReference.setWriteable(false);
            jpaBasedDomibusConnectorBigDataReference.setMimetype(bigData.getMimeType());
            jpaBasedDomibusConnectorBigDataReference.setName(bigData.getName());

            return jpaBasedDomibusConnectorBigDataReference;


        } catch (NumberFormatException nfe) {
            String error = String.format("Cannot load big data with storage reference [%s]\nThe actual implementation expects a Long as storage ref key!", storageReference);
            throw new RuntimeException(error, nfe);
        } catch (SQLException e) {
            String error = String.format("Error while loading from database");
            throw new RuntimeException(error, e);
        } catch (IOException e) {
            String error = String.format("Error while reading stream from database");
            throw new RuntimeException(error, e);
        }

    }


    @Override
    @Transactional
    public LargeFileReference createDomibusConnectorBigDataReference(String connectorMessageId, String documentName, String documentContentType) {
        LOGGER.trace("#createDomibusConnectorBigDataReference: called for message {} and document {}", connectorMessageId, documentName);
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);

        JpaBasedLargeFileReference reference = new JpaBasedLargeFileReference();
        reference.setStorageProviderName(this.getProviderName());
        reference.setReadable(false);
        reference.setWriteable(true);

        PDomibusConnectorBigData bigData = new PDomibusConnectorBigData();
        bigData.setMessage(dbMessage.getId());

        bigData.setName(documentName);
        bigData.setMimeType(documentContentType);
        bigData.setLastAccess(new Date());

        DbBackedOutputStream outputStream = new DbBackedOutputStream(bigData);
//        try {
//            StreamUtils.copy(in, outputStream);
//        } catch (IOException e1) {
//            LOGGER.error("Exception copy streams for big data to database!", e1);
//        }
        byte[] toByteArray = outputStream.toByteArray();

        Session hibernateSession = entityManager.unwrap(Session.class);
        Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob(toByteArray);
        bigData.setContent(blob);
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(toByteArray);
        bigData.setChecksum(md5DigestAsHex);

        try {
            bigData = bigDataDao.save(bigData);
        }catch (Exception e) {
            LOGGER.error("Exception saving big data to database!", e);
        }

        reference.setOutputStream(outputStream);


        reference.setStorageIdReference(Long.toString(bigData.getId()));
        return reference;
    }

    @Override
    @Transactional(readOnly = false)
    public LargeFileReference createDomibusConnectorBigDataReference(InputStream in, String connectorMessageId, String documentName, String documentContentType) {

            LOGGER.trace("#createDomibusConnectorBigDataReference: called for message {} and document {}", connectorMessageId, documentName);
            PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);

            JpaBasedLargeFileReference reference = new JpaBasedLargeFileReference();
            reference.setReadable(false);
            reference.setWriteable(false);

            PDomibusConnectorBigData bigData = new PDomibusConnectorBigData();
            bigData.setMessage(dbMessage.getId());
            
            bigData.setName(documentName);
            bigData.setMimeType(documentContentType);
            bigData.setLastAccess(new Date());
            
            DbBackedOutputStream outputStream = new DbBackedOutputStream(bigData);
            try {
				StreamUtils.copy(in, outputStream);
			} catch (IOException e1) {
				LOGGER.error("Exception copy streams for big data to database!", e1);
			}
            byte[] toByteArray = outputStream.toByteArray();
            
            Session hibernateSession = entityManager.unwrap(Session.class);
            Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob(toByteArray);
            bigData.setContent(blob);
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(toByteArray);
            bigData.setChecksum(md5DigestAsHex);

            try {
            bigData = bigDataDao.save(bigData);
            }catch (Exception e) {
            	LOGGER.error("Exception saving big data to database!", e);
            }

//            reference.setOutputStream(outputStream);

            reference.setStorageIdReference(Long.toString(bigData.getId()));
            return reference;


    }



    @Override
    @Transactional(readOnly = false)
    public void deleteDomibusConnectorBigDataReference(LargeFileReference bigDataReference) {
        LOGGER.trace("deleteDomibusConnectorBigDataReference: called to delete all data {}", bigDataReference);
//        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        JpaBasedLargeFileReference ref = (JpaBasedLargeFileReference) bigDataReference;
        long dataId = convertStorageIdReferenceToDbId(ref.getStorageIdReference());

        LOGGER.debug("Deleting big data entry with db id: [{}]", dataId);
        bigDataDao.deleteById(dataId);

//        if (dbMessage != null) {
//            List<PDomibusConnectorBigData> dataByMsg = bigDataDao.findAllByMessage(dbMessage.getId());
//            bigDataDao.deleteAll(dataByMsg);
//        } else {
//            LOGGER.warn(String.format("Did not delete big data of message with connector id [%s], because there was no entry in database", message));
//        }

    }

    @Override
    public Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<LargeFileReference>> getAllAvailableReferences() {

        Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<LargeFileReference>> map = new HashMap<>();

        Iterable<PDomibusConnectorBigData> all = bigDataDao.findAll();
        all.forEach(bigData -> {
            Long messageId = bigData.getMessage();
            Optional<PDomibusConnectorMessage> byId = messageDao.findById(messageId);

            if (byId.isPresent()) {
                DomibusConnectorMessage.DomibusConnectorMessageId connectorMessageId =
                        new DomibusConnectorMessage.DomibusConnectorMessageId(byId.get().getConnectorMessageId());

                if (!map.containsKey(connectorMessageId)) {
                    map.put(connectorMessageId, new ArrayList<>());
                }

                List<LargeFileReference> dataRefList = map.get(connectorMessageId);
                JpaBasedLargeFileReference reference = new JpaBasedLargeFileReference();
                reference.setStorageProviderName(this.getProviderName());
                reference.setReadable(false);
                reference.setWriteable(false);
                reference.setStorageIdReference(Long.toString(bigData.getId()));
                dataRefList.add(reference);

            } else {
                LOGGER.error(String.format("Found data reference [%s] which is not linked to a message with dbId [%s]! Possibly corrupted data!", bigData, messageId));
            }
        });
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void saveOnClose(DbBackedOutputStream dbBackedOutputStream) {
        PDomibusConnectorBigData bigData = bigDataDao.findById(dbBackedOutputStream.storageReference.getId()).get();

        byte[] toByteArray = dbBackedOutputStream.toByteArray();

        Session hibernateSession = entityManager.unwrap(Session.class);
        Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob(toByteArray);
        bigData.setContent(blob);
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(toByteArray);
        bigData.setChecksum(md5DigestAsHex);

        bigData.setLastAccess(new Date());

        bigDataDao.save(bigData);

    }

    private long convertStorageIdReferenceToDbId(String storageRef) {
        //TODO: error handling!!!
        long l = Long.parseLong(storageRef);
        return l;
    }

    private class DbBackedOutputStream extends ByteArrayOutputStream {

        private final PDomibusConnectorBigData storageReference;

        public DbBackedOutputStream(PDomibusConnectorBigData bigData) {
            this.storageReference = bigData;
        }
        
        @Override
        public void flush() throws IOException {
        	LOGGER.debug("called flush on DbBackedOutputStream [{}] - flush is not implemented doing nothing!", this);
            super.flush();
        }

        @Override
        public void close() throws IOException {
            LOGGER.debug("called close on DbBackedOutputStream [{}]", this);
            super.close();
            LargeFilePersistenceServiceJpaImpl.this.saveOnClose(this);

        }

        public String toString() {
            long byteCount = this.toByteArray() == null ? -1 : this.toByteArray().length;
            return String.format("DbBackedOutputStream with bytes [%d] and with storageRef: [%s]", byteCount, storageReference);
        }
    }



    private static class JpaBasedLargeFileReference extends LargeFileReference {

        /**
		 * 
		 */
		private static final long serialVersionUID = -4587251476768140113L;

		transient InputStream inputStream;

        transient OutputStream outputStream;

        boolean readable;

        boolean writeable;

        @Override
        public String getStorageProviderName() {
            return PROVIDER_NAME;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return this.inputStream;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return this.outputStream;
        }

        @Override
        public boolean isReadable() {
            return readable;
        }

        @Override
        public boolean isWriteable() {
            return writeable;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void setOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public void setReadable(boolean readable) {
            this.readable = readable;
        }

        public void setWriteable(boolean writeable) {
            this.writeable = writeable;
        }

    }

}
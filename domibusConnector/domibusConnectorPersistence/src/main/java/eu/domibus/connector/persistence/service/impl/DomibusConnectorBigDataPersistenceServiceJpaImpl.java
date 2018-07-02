package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.dao.DomibusConnectorBigDataDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorBigData;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StreamUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * this service uses jdbc to create an input / output stream with writing it
 * to an byte array (ByteArrayInputStream / ByteArrayOutputStream)
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
@Transactional
public class DomibusConnectorBigDataPersistenceServiceJpaImpl implements DomibusConnectorBigDataPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceJpaImpl.class);


    @Autowired
    private DomibusConnectorMessageDao messageDao;

    @Autowired
    private DomibusConnectorBigDataDao bigDataDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional(readOnly = false)
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
        if (bigDataReference.getStorageIdReference() == null) {
            throw new IllegalArgumentException("storageIdReference must be not null!\n The reference must exist in database!");
        }
        String storageReference = bigDataReference.getStorageIdReference();

        try {
            long storageRef = Long.parseLong(storageReference);
            LOGGER.debug("Loading big data with storage ref [{}] from database", storageRef);
            PDomibusConnectorBigData bigData = bigDataDao.findOne(storageRef);

            JpaBasedDomibusConnectorBigDataReference jpaBasedDomibusConnectorBigDataReference = new JpaBasedDomibusConnectorBigDataReference(this);

            InputStream inputStream = createInputStream(bigData);

            jpaBasedDomibusConnectorBigDataReference.setInputStream(inputStream);
            jpaBasedDomibusConnectorBigDataReference.setStorageIdReference(storageReference);
            jpaBasedDomibusConnectorBigDataReference.setReadable(true);
            jpaBasedDomibusConnectorBigDataReference.setWriteable(false);
            jpaBasedDomibusConnectorBigDataReference.setMimetype(bigData.getMimeType());
            jpaBasedDomibusConnectorBigDataReference.setName(bigData.getName());

            return jpaBasedDomibusConnectorBigDataReference;

        } catch (NumberFormatException nfe) {
            String error = String.format("Cannot load big data with storage reference [%s]%nThe actual implementation accepts only a Long number as storage ref key!", storageReference);
            throw new RuntimeException(error, nfe);
        }
    }

    private InputStream createInputStream(PDomibusConnectorBigData bigData) {
        try {
            //TODO: use stream from db!
            Blob content = bigData.getContent();
            if (content != null) {
                InputStream dbStream = content.getBinaryStream();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(StreamUtils.copyToByteArray(dbStream));
                return byteArrayInputStream;
            } else {
                String error = String.format("Blob Content of bigDataStorage with reference [%d] is null!", bigData.getId());
                throw new IllegalStateException(error);
            }
        } catch (SQLException e) {
            String error = String.format("Error while loading big data with ref [%s] from database", bigData.getId());
            throw new RuntimeException(error, e);
        } catch (IOException e) {
            String error = String.format("Error while reading stream of big data with ref [%s] from database", bigData.getId());
            throw new UncheckedIOException(error, e);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(DomibusConnectorMessage message) {

        LOGGER.trace("#createDomibusConnectorBigDataReference: called with message {}", message);
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());

        JpaBasedDomibusConnectorBigDataReference reference = new JpaBasedDomibusConnectorBigDataReference(this);
        reference.setReadable(false);
        reference.setWriteable(true);

        PDomibusConnectorBigData bigData = new PDomibusConnectorBigData();
        bigData.setName(reference.getName());
        bigData.setMimeType(reference.getContentType());
        bigData.setLastAccess(new Date());
        if (dbMessage != null) {
            bigData.setMessage(dbMessage.getId());
        }

        bigData = bigDataDao.save(bigData);

        OutputStream outputStream = new DbBackedOutputStream(bigData);
        reference.setOutputStream(outputStream);
        reference.setStorageIdReference(Long.toString(bigData.getId()));
        return reference;


    }

    @Override
    @Transactional(readOnly = false)
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        LOGGER.trace("deleteDomibusConnectorBigDataReference: called to delete all data related to message {}", message);
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());

        if (dbMessage != null) {
            List<PDomibusConnectorBigData> dataByMsg = bigDataDao.findAllByMessage(dbMessage.getId());
            bigDataDao.delete(dataByMsg);
        } else {
            LOGGER.warn(String.format("Did not delete big data of message with connector id [%s], because there was no entry in database", message));
        }

    }

    private void writeOutputStreamToDatabase(final DbBackedOutputStream dbBackedOutputStream) {
        LOGGER.trace("#writeOutputStreamToDatabase: outputStream: [{}]", dbBackedOutputStream);
        PDomibusConnectorBigData data = dbBackedOutputStream.getStorageReference();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            Session hibernateSession = entityManager.unwrap(Session.class);
            Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob(dbBackedOutputStream.toByteArray());
            data.setContent(blob);
            LOGGER.trace("#writeOutputStreamToDatabase: calling save");
            bigDataDao.save(data);
            return null;
        });


    }


    private class DbBackedOutputStream extends ByteArrayOutputStream {

        private final PDomibusConnectorBigData storageReference;

        DbBackedOutputStream(PDomibusConnectorBigData bigData) {
            this.storageReference = bigData;
        }

        public PDomibusConnectorBigData getStorageReference() {
            return storageReference;
        }

        @Override
        public void close() throws IOException {
            LOGGER.debug("called close on DbBackedOutputStream [{}]", this);
            writeOutputStreamToDatabase(this);
        }

        @Override
        @SuppressWarnings("findbugs:RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
        public String toString() {
            long byteCount = this.toByteArray() == null ? -1 : this.toByteArray().length;
            return String.format("DbBackedOutputStream with bytes [%d] and with storageRef: [%s]", byteCount, storageReference);
        }
    }

    private static class JpaBasedDomibusConnectorBigDataReference extends DomibusConnectorBigDataReference {

        private static final long serialVersionUID = 1L;

        private transient InputStream inputStream;

        private transient OutputStream outputStream;

        private transient final DomibusConnectorBigDataPersistenceServiceJpaImpl bigDataPersistenceService;

        private boolean readable;

        private boolean writeable;


        JpaBasedDomibusConnectorBigDataReference(DomibusConnectorBigDataPersistenceServiceJpaImpl persistenceService) {
            this.bigDataPersistenceService = persistenceService;
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

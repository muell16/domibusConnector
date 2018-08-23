package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.persistence.spring.DomibusConnectorFilesystemPersistenceProperties;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@ConditionalOnProperty("eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl")
public class DomibusConnectorBigDataPersistenceServiceFilesystemImpl implements DomibusConnectorBigDataPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceFilesystemImpl.class);

    @Autowired
    DomibusConnectorFilesystemPersistenceProperties filesystemPersistenceProperties;

    //setter
    public void setFilesystemPersistenceProperties(DomibusConnectorFilesystemPersistenceProperties filesystemPersistenceProperties) {
        this.filesystemPersistenceProperties = filesystemPersistenceProperties;

    }

    @Override
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference ref) {
        if (!(ref instanceof FileBasedDomibusConnectorBigDataReference)) {
            throw new PersistenceException(String.format("Can only getReadableDataSource for a [%s] BigDataReference but the provided reference was of type [%s]",
                    FileBasedDomibusConnectorBigDataReference.class, ref.getClass()));
        }
        FileBasedDomibusConnectorBigDataReference fileBasedReference = (FileBasedDomibusConnectorBigDataReference)ref;


        String storageIdReference = fileBasedReference.getStorageIdReference();
        String filePath = getStoragePath() + File.separator + storageIdReference;

        try {
            //TODO: create decrypted inputStream!
            FileInputStream fis = new FileInputStream(filePath);
            fileBasedReference.setInputStream(fis);
        } catch (FileNotFoundException e) {
            throw new PersistenceException(String.format("Could not found the required file [{}]!", filePath), e);
        }


        return fileBasedReference;
    }

    @Override
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(InputStream input, String connectorMessageId, String documentName, String documentContentType) {
        FileBasedDomibusConnectorBigDataReference bigDataReference = new FileBasedDomibusConnectorBigDataReference();
        bigDataReference.setName(documentName);
        bigDataReference.setMimetype(documentContentType);


        //Path f = getStoragePath();

        String folder = connectorMessageId;
        Path messageFolder = getStoragePath().resolve(folder); // //new File(f.getAbsolutePath() + File.separator + folder);
        try {
            Files.createDirectory(messageFolder);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot create directory [%s]", messageFolder), e);
        }

        String storageFileName = UUID.randomUUID().toString();
        Path storageFileRelativePath = Paths.get(folder + File.separator + storageFileName);
        bigDataReference.setStorageIdReference(storageFileRelativePath.toString());
        Path storageFile = messageFolder.resolve(storageFileName);
        LOGGER.debug("Storage file path is [{}]", storageFile.toAbsolutePath());

        try {
            Files.createFile(storageFile);
        } catch (IOException e) {
            throw new PersistenceException(String.format("Error while creating file [%s]", storageFile), e);
        }

        try (FileOutputStream fos = new FileOutputStream(storageFile.toFile())) {
            //TODO: replace with encrypting outputstream...and set encrpytion key
            //bigDataReference.setEncryptionKey(UUID.randomUUID().toString());
            IOUtils.copy(input, fos);
        } catch (FileNotFoundException e) {
            throw new PersistenceException(String.format("Error while creating FileOutpuStream for file [%s]", storageFile), e);
        } catch (IOException e) {
            throw new PersistenceException(String.format("Error while writing to file [%s]", storageFile), e);
        }

        return bigDataReference;
    }

//    private File createNewFileOrThrow(Path storageFile) {
//        try {
//            if (Files.createFile()) {
//
//            }
//            LOGGER.debug("Created new file [{}]", storageFile.getName());
//        } catch (IOException e) {
//            throw new PersistenceException(String.format("Error while creating file [%s]", storageFile), e);
//        }
//        return storageFile;
//    }

    @Override
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        String connectorMessageId = message.getConnectorMessageId();
        String storageFolder = getStoragePath() + File.separator + connectorMessageId;

        Path file = getStoragePath().resolve(connectorMessageId);


        File folder = new File(storageFolder);

        boolean successfullyDeleted = FileSystemUtils.deleteRecursively(folder);

        if (!successfullyDeleted) {
            //TODO: throw exception OR just log WARNING?
        }
        
    }

    private Path getStoragePath() {
        return filesystemPersistenceProperties.getStoragePath();
    }

    @PostConstruct
    public void init() {
//        File storagePath = getStoragePath();
        //TODO: check if i can write...
    }

    /**
     *
     *
     *
     * BigDataPersistenceServiceFilesystemImpl implementation of BigDataReference
     *  this class is internal api do not use this class outside the BigDataPersistenceServiceFilesystemImpl
     */
    static final class FileBasedDomibusConnectorBigDataReference extends DomibusConnectorBigDataReference {

        /**
         *
         */
        private static final long serialVersionUID = 1;

        transient InputStream inputStream;

        transient OutputStream outputStream;

        boolean readable;

        boolean writeable;

        private String encryptionKey;


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

        public String getEncryptionKey() {
            return encryptionKey;
        }

        public void setEncryptionKey(String encryptionKey) {
            this.encryptionKey = encryptionKey;
        }
    }
}

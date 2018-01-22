package eu.domibus.connector.domain.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.activation.DataSource;
import javax.annotation.Nonnull;

/**
 * Represents a reference to a storage system for big files
 */
public class DomibusConnectorBigDataReference implements DataSource, Serializable {

    private String storageIdReference;

    private String mimetype;
    
    private String name;

    public DomibusConnectorBigDataReference() {}

    public DomibusConnectorBigDataReference(@Nonnull String storageIdReference) {
        if (storageIdReference == null) {
            throw new IllegalArgumentException("StorageIdReference cannot be null!");
        }
        this.storageIdReference = storageIdReference;
    }

    public @Nullable String getStorageIdReference() {
        return storageIdReference;
    }

    public void setStorageIdReference(String storageIdReference) {
        this.storageIdReference = storageIdReference;
    }
  
    @Override
    public InputStream getInputStream() throws IOException {
        throw new IOException("not initialized yet!");
    }
    
    
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("not initialized yet!");
    }

    /**
     * Is readable if a valid input stream can be returned
     * @return - if it's readable
     */
    public boolean isReadable() { return false; }
    
    /**
     * @return is true if a writeable output stream can be returned!
     */
    public boolean isWriteable() { return false; }
    
    @Override
    public String getContentType() {
        return this.mimetype;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setMimetype(String contentType) {
        this.mimetype = contentType;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
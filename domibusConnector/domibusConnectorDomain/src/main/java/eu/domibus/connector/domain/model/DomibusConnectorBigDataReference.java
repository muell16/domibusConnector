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
public abstract class DomibusConnectorBigDataReference implements DataSource, Serializable {

    private String storageIdReference;

    private String contentType;
    
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
    public abstract InputStream getInputStream() throws IOException;
    
    @Override
    public abstract OutputStream getOutputStream() throws IOException;

    /**
     * Is readable if a valid input stream can be returned
     * @return - if it's readable
     */
    public abstract boolean isReadable();
    
    /**
     * @return is true if a writeable output stream can be returned!
     */
    public abstract boolean isWriteable();
    
    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
}

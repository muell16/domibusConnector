package eu.domibus.connector.domain.model;

import java.io.*;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.activation.DataSource;
import javax.annotation.Nonnull;
import org.springframework.core.style.ToStringCreator;

/**
 * Represents a reference to a storage system for big files
 *
 * If you extends this class make SURE that all additional fields are serializable
 * or transient!
 */
public class DomibusConnectorBigDataReference implements DataSource, Serializable {

    private String storageIdReference = "";

    private String mimetype = "";
    
    private String name = "";

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
    
    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("storageReference", this.getStorageIdReference());
        return builder.toString();        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.storageIdReference);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomibusConnectorBigDataReference other = (DomibusConnectorBigDataReference) obj;
        if (!Objects.equals(this.storageIdReference, other.storageIdReference)) {
            return false;
        }
        return true;
    }



}


package eu.domibus.connector.domain.transformer.util;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorBigDataReferenceDataHandlerBacked extends DomibusConnectorBigDataReference {

    
    private transient DataHandler dataHandler;
    
    public DomibusConnectorBigDataReferenceDataHandlerBacked() {}
    
    public DomibusConnectorBigDataReferenceDataHandlerBacked(DataHandler dh) {
        this.dataHandler = dh;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        if (dataHandler == null) {
            throw new IOException("DataHandler Backend is missing");
        }
        return dataHandler.getInputStream();
        
//        Object content = dataHandler.getContent();
//        if (content instanceof InputStream) {
//            return (InputStream) content;
//        }        
//        throw new IOException("No input stream available");
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Read only!");
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    public boolean isReadable() {
        return this.dataHandler != null;
    }

    @Override
    public boolean isWriteable() {
        return false;
    }
     
}


package eu.domibus.connector.controller.test.util;

import eu.domibus.connector.domain.model.LargeFileReference;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class LargeFileReferenceInMemory extends LargeFileReference {

    InputStream inputStream;
    
    OutputStream outputStream;

    boolean readable;
    
    boolean writeable;
    
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    @Override
    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }
    
    
    
    
    
}

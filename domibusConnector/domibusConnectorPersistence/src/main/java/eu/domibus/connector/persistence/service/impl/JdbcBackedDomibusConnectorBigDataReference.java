/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * 
 * 
 * Jdbc based implementation of DomibusConnectorBigDataReference
 * which is only usable during an active transaction!
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class JdbcBackedDomibusConnectorBigDataReference extends DomibusConnectorBigDataReference {

    InputStream inputStream;
    
    OutputStream outputStream;
    
    boolean readable;
    
    boolean writeable;
    
    
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.transformer.util;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorBigDataReferenceMemoryBacked extends DomibusConnectorBigDataReference {

    private transient byte[] bytes;
    private boolean read = false;
    private boolean write = false;

    public DomibusConnectorBigDataReferenceMemoryBacked(byte[] bytes) {
        this.read = true;
        this.bytes = bytes;
    }
    
    public DomibusConnectorBigDataReferenceMemoryBacked() {
        this.write = true;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }

    @Override
    public boolean isReadable() {
        return this.read;
    }

    @Override
    public boolean isWriteable() {
        return this.write;
    }

}

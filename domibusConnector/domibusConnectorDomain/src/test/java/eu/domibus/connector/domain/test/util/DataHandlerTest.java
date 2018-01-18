/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.test.util;

import eu.domibus.connector.domain.transformer.util.InputStreamDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import org.junit.Test;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DataHandlerTest {

    
    @Test
    public void testDataHandlerFromDataSource() throws IOException {
//        DataSource ds = new DataSource() {
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return new ByteArrayInputStream(input.getBytes());
//            }
//
//            @Override
//            public OutputStream getOutputStream() throws IOException {
//                throw new IOException("this DataSource is read only!");
//            }
//
//            @Override
//            public String getContentType() {
//                return "application/octet-stream";
//            }
//
//            @Override
//            public String getName() {
//                return "Stringbasedds";
//            }
//        };
        
        ByteArrayInputStream bin = new ByteArrayInputStream("test".getBytes());        
        InputStreamDataSource ds = new InputStreamDataSource(bin);        
        DataHandler dh = new DataHandler(ds);
        
        dh.getInputStream();
        
        
    }
}

package eu.domibus.connector.domain.test.util;

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;

public class DataHandlerCreator {

    public static DataHandler createDataHandlerFromString(@Nonnull  String input) {
        DataSource ds = new DataSource() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(input.getBytes());
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                throw new IOException("this DataSource is read only!");
            }

            @Override
            public String getContentType() {
                return "application/octet-stream";
            }

            @Override
            public String getName() {
                return "Stringbasedds";
            }
        };
        DataHandler dh =  new DataHandler(ds);
        
        return dh;
        
    }
}

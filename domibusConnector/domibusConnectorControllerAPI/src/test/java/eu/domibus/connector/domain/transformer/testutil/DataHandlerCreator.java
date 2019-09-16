package eu.domibus.connector.domain.transformer.testutil;

import eu.domibus.connector.domain.transformer.util.InputStreamDataSource;
import eu.domibus.connector.domain.transformer.util.InputStreamDataSource;
import javax.activation.DataSource;
import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;

public class DataHandlerCreator {

    public static DataHandler createDataHandlerFromString(@Nonnull String input) {
        InputStreamDataSource ds = InputStreamDataSource.InputStreamDataSourceFromByteArray(input.getBytes());
        DataHandler dh = new DataHandler(ds);
        return dh;
    }
}

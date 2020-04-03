
package eu.domibus.connector.persistence.service.testutil;

import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.LargeFileReferenceGetSetBased;
import eu.domibus.connector.persistence.largefiles.provider.LargeFilePersistenceProvider;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class LargeFilePersistenceServiceMemoryImpl implements LargeFilePersistenceProvider, LargeFilePersistenceService {

    public static final String PROVIDER_NAME = "MEMORY";

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
	public LargeFileReference getReadableDataSource(LargeFileReference bigDataReference) {
		LargeFileReferenceGetSetBased bigDataReference2 = (LargeFileReferenceGetSetBased) bigDataReference;
		try {
			if(bigDataReference2.getInputStream()==null) {				
				bigDataReference2.setInputStream(new ByteArrayInputStream(bigDataReference2.getBytes()));
                bigDataReference2.setReadable(true);
				return bigDataReference2;
			}
			return bigDataReference2;
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public LargeFileReference createDomibusConnectorBigDataReference(InputStream input, String connectorMessageId, String documentName,
                                                                     String documentContentType) {
		return createDomibusConnectorBigDataReference(connectorMessageId, documentName, documentContentType);
	}

    @Override
    public LargeFileReference createDomibusConnectorBigDataReference(String connectorMessageId, String documentName, String documentContentType) {
        LargeFileReferenceGetSetBased dataRef = new LargeFileReferenceGetSetBased();
        dataRef.setOutputStream(new MyOutputStream(dataRef));
        dataRef.setWriteable(true);
        dataRef.setReadable(false);
        return dataRef;
    }

    @Override
    public void deleteDomibusConnectorBigDataReference(LargeFileReference ref) {
        //Just do nothing!
    }

    @Override
    public Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<LargeFileReference>> getAllAvailableReferences() {
	    //just return empty map
        return new HashMap<>();
    }

    @Override
    public boolean isAvailable(LargeFileReference toCopy) {
        return true;
    }

    @Override
    public LargeFilePersistenceProvider getDefaultProvider() {
        return this;
    }

    public static class MyOutputStream extends ByteArrayOutputStream {

        private final LargeFileReferenceGetSetBased reference;
        
        public MyOutputStream(LargeFileReferenceGetSetBased reference) {
            this.reference = reference;
        }
        
        @Override
        public void close() throws IOException {
            flush();
            reference.setWriteable(false);
            reference.setBytes(this.toByteArray());
            reference.setOutputStream(null);
            super.close();
        }
                
    }
    
    
}

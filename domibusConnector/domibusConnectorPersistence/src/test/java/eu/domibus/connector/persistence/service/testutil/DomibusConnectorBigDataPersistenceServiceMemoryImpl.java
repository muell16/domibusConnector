
package eu.domibus.connector.persistence.service.testutil;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomibusConnectorBigDataReferenceGetSetBased;
import eu.domibus.connector.domain.transformer.util.DomibusConnectorBigDataReferenceMemoryBacked;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
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
public class DomibusConnectorBigDataPersistenceServiceMemoryImpl implements DomibusConnectorBigDataPersistenceService {

	@Override
	public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
		DomibusConnectorBigDataReferenceGetSetBased bigDataReference2 = (DomibusConnectorBigDataReferenceGetSetBased) bigDataReference;
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
	public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(InputStream input, String connectorMessageId, String documentName,
    		String documentContentType) {
		return createDomibusConnectorBigDataReference(connectorMessageId, documentName, documentContentType);
	}

    @Override
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(String connectorMessageId, String documentName, String documentContentType) {
        DomibusConnectorBigDataReferenceGetSetBased dataRef = new DomibusConnectorBigDataReferenceGetSetBased();
        dataRef.setOutputStream(new MyOutputStream(dataRef));
        dataRef.setWriteable(true);
        dataRef.setReadable(false);
        return dataRef;
    }

    @Override
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorBigDataReference ref) {
        //Just do nothing!
    }

    @Override
    public Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<DomibusConnectorBigDataReference>> getAllAvailableReferences() {
	    //just return empty map
        return new HashMap<>();
    }

    public static class MyOutputStream extends ByteArrayOutputStream {

        private final DomibusConnectorBigDataReferenceGetSetBased reference;
        
        public MyOutputStream(DomibusConnectorBigDataReferenceGetSetBased reference) {
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

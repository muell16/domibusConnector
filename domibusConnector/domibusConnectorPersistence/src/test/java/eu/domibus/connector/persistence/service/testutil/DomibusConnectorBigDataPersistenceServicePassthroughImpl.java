
package eu.domibus.connector.persistence.service.testutil;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomibusConnectorBigDataReferenceGetSetBased;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorBigDataPersistenceServicePassthroughImpl implements DomibusConnectorBigDataPersistenceService {

	@Override
	public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
		DomibusConnectorBigDataReferenceGetSetBased bigDataReference2 = (DomibusConnectorBigDataReferenceGetSetBased) bigDataReference;
		try {
			if(bigDataReference2.getInputStream()==null) {
				ByteArrayOutputStream out = (ByteArrayOutputStream) bigDataReference2.getOutputStream();
				bigDataReference2.setInputStream(new ByteArrayInputStream(out.toByteArray()));
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
		DomibusConnectorBigDataReferenceGetSetBased dataRef = new DomibusConnectorBigDataReferenceGetSetBased();
		dataRef.setOutputStream(new ByteArrayOutputStream());
		return dataRef;
	}

	@Override
	public void deleteDomibusConnectorBigDataReference(DomibusConnectorBigDataReference ref) {
		//just do nothing
	}

}

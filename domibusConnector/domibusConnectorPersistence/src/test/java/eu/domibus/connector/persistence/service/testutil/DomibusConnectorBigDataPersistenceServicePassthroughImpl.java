
package eu.domibus.connector.persistence.service.testutil;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomibusConnectorBigDataReferenceGetSetBased;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorBigDataPersistenceServicePassthroughImpl implements DomibusConnectorBigDataPersistenceService {

    @Override
    public DomibusConnectorBigDataReference getReadableDataSource(DomibusConnectorBigDataReference bigDataReference) {
        return bigDataReference;
    }

    @Override
    public DomibusConnectorBigDataReference createDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        DomibusConnectorBigDataReferenceGetSetBased dataRef = new DomibusConnectorBigDataReferenceGetSetBased();
        dataRef.setOutputStream(new ByteArrayOutputStream());
        return dataRef;
    }

    @Override
    public void deleteDomibusConnectorBigDataReference(DomibusConnectorMessage message) {
        //just do nothing
    }

}

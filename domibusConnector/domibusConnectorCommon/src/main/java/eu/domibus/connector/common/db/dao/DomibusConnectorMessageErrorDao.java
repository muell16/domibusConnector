package eu.domibus.connector.common.db.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorMessage;
import eu.domibus.connector.common.db.model.DomibusConnectorMessageError;

public interface DomibusConnectorMessageErrorDao {

    void persistMessageError(DomibusConnectorMessageError messageError);

    void mergeMessageError(DomibusConnectorMessageError messageError);

    List<DomibusConnectorMessageError> getErrorsForMessage(DomibusConnectorMessage message);

}

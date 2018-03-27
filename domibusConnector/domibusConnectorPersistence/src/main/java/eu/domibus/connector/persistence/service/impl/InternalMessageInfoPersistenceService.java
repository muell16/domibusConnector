package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.service.PersistenceException;

public interface InternalMessageInfoPersistenceService {



    void persistMessageInfo(DomibusConnectorMessage message, PDomibusConnectorMessage dbMessage) throws PersistenceException;

    PDomibusConnectorMessageInfo validatePartyServiceActionOfMessageInfo(PDomibusConnectorMessageInfo messageInfo) throws PersistenceException;

    DomibusConnectorMessageDetails mapMessageInfoIntoMessageDetails(PDomibusConnectorMessage dbMessage, DomibusConnectorMessageDetails details);

    void mapMessageDetailsToDbMessageInfoPersistence(DomibusConnectorMessageDetails messageDetails, PDomibusConnectorMessageInfo dbMessageInfo);

    void mergeMessageInfo(DomibusConnectorMessage message, PDomibusConnectorMessage dbMessage) throws PersistenceException;
}

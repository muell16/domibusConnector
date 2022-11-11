package eu.ecodex.dc5.flow.api;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.core.model.DC5Msg;

public interface DC5TransformToDomain<T> {
    DomibusConnectorMessage transform(T msg, DC5MsgProcess msgProcess) throws TransformMessageException;
}

package eu.ecodex.dc5.flow.api;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;
import eu.ecodex.dc5.core.model.DC5MsgProcess;

public interface DC5TransformToDomain<T> {
    DomibusConnectorMessage transform(T msg, DC5MsgProcess msgProcess) throws TransformMessageException;
}

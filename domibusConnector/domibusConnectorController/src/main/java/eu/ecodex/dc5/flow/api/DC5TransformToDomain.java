package eu.ecodex.dc5.flow.api;

import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.core.model.DC5Msg;

public interface DC5TransformToDomain<T> {
    DC5Msg transform(T msg, DC5MsgProcess msgProcess) throws TransformMessageException;
}

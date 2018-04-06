package eu.domibus.connector.controller.service.queue;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

import javax.annotation.Nonnull;

/**
 * A Simple service to put a DomibusConnectorMessage on a Queue
 *
 *
 */
public interface PutMessageOnQueue {

    public static String BACKEND_TO_CONTROLLER_QUEUE = "backendToControllerQueue";

    public static String GATEWAY_TO_CONTROLLER_QUEUE = "gatewayToControllerQueue";

    public void putMessageOnMessageQueue(@Nonnull DomibusConnectorMessage message);

}

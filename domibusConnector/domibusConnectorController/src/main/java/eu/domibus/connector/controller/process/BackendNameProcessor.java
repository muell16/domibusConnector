package eu.domibus.connector.controller.process;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.stereotype.Component;

/**
 * Processes a DomibusMessage and checks which
 * is the correct backend name
 *
 */
@Component
public class BackendNameProcessor implements DomibusConnectorMessageProcessor {

    public static final String BACKEND_NAME_PROCESSOR = "backendNameProcessor";

    @Override
    public void processMessage(DomibusConnectorMessage message) {
        //TODO: process routing rules...
    }


}

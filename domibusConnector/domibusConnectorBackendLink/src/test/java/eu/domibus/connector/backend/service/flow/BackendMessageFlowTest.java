package eu.domibus.connector.backend.service.flow;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import org.junit.Test;

public class BackendMessageFlowTest {


    @Test
    public void simpleTest() {
        DomibusConnectorBackendMessage msg = new DomibusConnectorBackendMessage();

        BackendMessageFlow.create()
                .process( (m) -> m)
                .process( (m) -> m)
                .processMessage(msg);


    }


}
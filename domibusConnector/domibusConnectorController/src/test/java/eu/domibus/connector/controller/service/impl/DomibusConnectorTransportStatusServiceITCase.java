package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.controller.transport.DomibusConnectorTransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = {ITCaseTestContext.class})
//@ActiveProfiles({"test","ittest", "storage-db"})
public class DomibusConnectorTransportStatusServiceITCase {

    @Autowired
    DomibusConnectorTransportStateService transportStatusService;

//    @Test
    public void setTransportStatusForTransportToGateway() {
//        assertThat(transportStatusService).isNotNull();
        //transportStatusService.updateTransportToGatewayStatus();

        TransportStateService.DomibusConnectorTransportState state = new TransportStateService.DomibusConnectorTransportState();
        state.setStatus(TransportState.ACCEPTED);
        state.setConnectorTransportId(new TransportStateService.TransportId("CONNID1"));
        state.setRemoteMessageId("REMOTE1");

        transportStatusService.updateTransportToGatewayStatus(new TransportStateService.TransportId("id"), state);

    }


}
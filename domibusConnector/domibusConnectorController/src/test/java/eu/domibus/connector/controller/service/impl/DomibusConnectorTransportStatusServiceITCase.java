package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = {ITCaseTestContext.class})
//@ActiveProfiles({"test","ittest", "storage-db"})
public class DomibusConnectorTransportStatusServiceITCase {

    @Autowired
    DomibusConnectorTransportStatusService transportStatusService;

//    @Test
    public void setTransportStatusForTransportToGateway() {
//        assertThat(transportStatusService).isNotNull();
        //transportStatusService.updateTransportToGatewayStatus();

        TransportStatusService.DomibusConnectorTransportState state = new TransportStatusService.DomibusConnectorTransportState();
        state.setStatus(TransportStatusService.TransportState.ACCEPTED);
        state.setConnectorTransportId("CONNID1");
        state.setRemoteTransportId("REMOTE1");

        transportStatusService.updateTransportToGatewayStatus(state);

    }


}
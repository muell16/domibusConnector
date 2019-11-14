package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
//import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorReceiveResponseService;
import eu.domibus.connector.testdata.TransitionCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

//import static org.junit.Assert.*;

@Disabled
public class HandleFromGwToConnectorDeliveredMessageTest {


    HandleFromGwToConnectorDeliveredMessage handleFromGwToConnectorDeliveredMessage;

    @Mock
    DomibusConnectorGatewayDeliveryService deliveryService;

//    @Mock
//    DomibusConnectorAsyncDeliverToConnectorReceiveResponseService sendResponseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        handleFromGwToConnectorDeliveredMessage = new HandleFromGwToConnectorDeliveredMessage();

        handleFromGwToConnectorDeliveredMessage.setControllerService(deliveryService);
//        handleFromGwToConnectorDeliveredMessage.setSendResponseService(sendResponseService);

    }



    @Test
    public void deliverMessage() throws Exception {

        DomibusConnectorMessageType message = TransitionCreator.createMessage();
        handleFromGwToConnectorDeliveredMessage.deliverMessage(message);

        //assertThat(deliveryService.deliverMessageFromGatewayToController())
        Mockito.verify(deliveryService, Mockito.times(1)).deliverMessageFromGatewayToController(Mockito.any());
//        Mockito.verify(sendResponseService, Mockito.times(1)).deliverResponse(Mockito.any());
    }

}
package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.process.DomibusGatewayLoopbackReceiveProcessor;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.queue.PutMessageOnQueue;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class DomibusConnectorGatewayDeliveryServiceImplTest {

    @Mock
    private PutMessageOnQueue putMessageOnQueue;

    @Mock
    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Mock
    private DomibusConnectorPersistAllBigDataOfMessageService bigDataOfMessagePersistenceService;

    @Mock
    private DomibusConnectorMessageIdGenerator messageIdGenerator;

    DomibusConnectorGatewayDeliveryServiceImpl deliveryService;

    private List<DomibusConnectorMessage> putOnQueue;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        putOnQueue = new ArrayList<>();
        deliveryService = new DomibusConnectorGatewayDeliveryServiceImpl();
        deliveryService.setMessageIdGenerator(messageIdGenerator);
        deliveryService.setMessagePersistenceService(messagePersistenceService);
        deliveryService.setBigDataOfMessagePersistenceService(bigDataOfMessagePersistenceService);
        deliveryService.setPutMessageOnQueue(putMessageOnQueue);
        deliveryService.setDomibusGatewayLoopbackReceiveProcessor(new DomibusGatewayLoopbackReceiveProcessor());

        Mockito.when(messageIdGenerator.generateDomibusConnectorMessageId()).thenReturn("id1");
        Mockito.when(messagePersistenceService.persistMessageIntoDatabase(any(DomibusConnectorMessage.class), any(DomibusConnectorMessageDirection.class)))
                .then((invoc) -> invoc.getArgument(0));

        Mockito.when(bigDataOfMessagePersistenceService.persistAllBigFilesFromMessage(any(DomibusConnectorMessage.class)))
                .then((invoc) -> invoc.getArgument(0));


        Mockito.when(messagePersistenceService.mergeMessageWithDatabase(any(DomibusConnectorMessage.class)))
                .then((invoc) -> invoc.getArgument(0));

        Mockito.doAnswer((invoc) -> putOnQueue.add(invoc.getArgument(0)))
                .when(putMessageOnQueue).putMessageOnMessageQueue(any(DomibusConnectorMessage.class));

    }


    @Test
    public void testDeliverMessageFromGateway_message() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessageFormAFromGwdomibusRed();

        deliveryService.deliverMessageFromGatewayToController(epoMessage);

        assertThat(putOnQueue).hasSize(1);
        assertThat(putOnQueue.get(0)).isEqualToComparingFieldByField(epoMessage);

    }

    @Test
    public void testDeliverMessageFromGateway_evidenceMessage() {
        DomibusConnectorMessage nonDeliveryMessage = DomainEntityCreator.createEvidenceNonDeliveryMessage();

        deliveryService.deliverMessageFromGatewayToController(nonDeliveryMessage);

        assertThat(putOnQueue).hasSize(1);
        assertThat(putOnQueue.get(0)).isEqualToComparingFieldByField(nonDeliveryMessage);

    }

    @Test
    public void testDeliverMessageFromGateway_null_shouldThrowException() {
        Assertions.assertThrows(DomibusConnectorControllerException.class, () -> {
            deliveryService.deliverMessageFromGatewayToController(null);
        });
    }

    @Test
    public void testDeliveryMessageFromGateway_illegalMessage_shouldThrowException() {
        Assertions.assertThrows(DomibusConnectorControllerException.class, () -> {
            DomibusConnectorMessage message = DomibusConnectorMessageBuilder.createBuilder()
                    .setMessageDetails(DomainEntityCreator.createDomibusConnectorMessageDetails())
                    .addConfirmation(DomainEntityCreator.createMessageDeliveryConfirmation())
                    .build();
            message.getMessageConfirmations().clear();

            deliveryService.deliverMessageFromGatewayToController(message);
        });
    }

}
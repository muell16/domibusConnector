package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.test.util.MockedCreateConfirmationMessageBuilderFactoryImplProvider;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@Disabled
public class BackendToGatewayConfirmationProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayConfirmationProcessorTest.class);

//    @Mock
//    private CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;
    MockedCreateConfirmationMessageBuilderFactoryImplProvider mockingProvider;

    @Mock
    private DCMessagePersistenceService messagePersistenceService;

    @Mock
    private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    @Mock
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    private BackendToGatewayConfirmationProcessor backendToGatewayConfirmationProcessor;

    private List<DomibusConnectorMessage> toGwSubmittedMessages;

    @BeforeEach
    public void setUp() throws DomibusConnectorGatewaySubmissionException, DomibusConnectorEvidencesToolkitException {

        mockingProvider = new MockedCreateConfirmationMessageBuilderFactoryImplProvider();

        MockitoAnnotations.initMocks(this);
        toGwSubmittedMessages = new ArrayList<>();
        backendToGatewayConfirmationProcessor = new BackendToGatewayConfirmationProcessor();

        backendToGatewayConfirmationProcessor.setMessagePersistenceService(messagePersistenceService);
        backendToGatewayConfirmationProcessor.setConfirmationMessageService(mockingProvider.getCreateConfirmationMessageBuilderFactory());

        backendToGatewayConfirmationProcessor.setGwSubmissionService(gwSubmissionService);
        backendToGatewayConfirmationProcessor.setBackendDeliveryService(backendDeliveryService);

        Mockito.doAnswer( (invoc) -> toGwSubmittedMessages.add(invoc.getArgument(0)))
                .when(gwSubmissionService).submitToGateway(any(DomibusConnectorMessage.class));

//        Mockito.when(evidencesToolkit.createEvidence(any(DomibusConnectorEvidenceType.class),
//                    any(DomibusConnectorMessage.class),
//                    any(DomibusConnectorRejectionReason.class),
//                    any(String.class)))
//                .thenAnswer( invoc -> { //just return a random confirmation
//                    DomibusConnectorMessage msg = invoc.getArgumentAt(1, DomibusConnectorMessage.class);
//                    msg.addConfirmation(DomainEntityCreator.createMessageDeliveryConfirmation());
//                    return msg.getMessageConfirmations().get(0);
//                });

    }

    @Test
    public void testProcessMessage_gwSubmissionFails_shouldThrowException() throws DomibusConnectorEvidencesToolkitException, DomibusConnectorGatewaySubmissionException {
        Assertions.assertThrows(DomibusConnectorMessageException.class, () -> {
            String connectorMessageId = "msg123456";
            String ebmsId = "ebms1234";
            DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
            epoMessage.setConnectorMessageId(connectorMessageId);
            epoMessage.getMessageDetails().setEbmsMessageId(ebmsId);
            //incoming epo message
            epoMessage.getMessageDetails().setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);

            LOGGER.debug("LOG MESSAGE: [{}]", epoMessage);
            Mockito.when(messagePersistenceService.findMessageByEbmsIdAndDirection(eq(ebmsId), eq(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND)))
                    .thenReturn(Optional.of(epoMessage));

            //outgoing confirmation message
            DomibusConnectorMessage confirmationMessage = DomainEntityCreator.createDeliveryEvidenceForMessage(epoMessage);
            confirmationMessage.getMessageDetails().setRefToMessageId(ebmsId);
            confirmationMessage.getMessageDetails().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);

            Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("something failed..."))
                    .when(gwSubmissionService).submitToGateway(any(DomibusConnectorMessage.class));

            backendToGatewayConfirmationProcessor.processMessage(confirmationMessage);
        });

    }


    @Test
    public void testProcessMessage() throws DomibusConnectorEvidencesToolkitException {
        String connectorMessageId = "msg123456";
        String ebmsId = "ebms1234";
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        epoMessage.setConnectorMessageId(connectorMessageId);
        epoMessage.getMessageDetails().setEbmsMessageId(ebmsId);

        Mockito.when(messagePersistenceService.findMessageByEbmsIdAndDirection(eq(ebmsId), eq(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND)))
                .thenReturn(Optional.of(epoMessage));

        DomibusConnectorMessage confirmationMessage = DomainEntityCreator.createDeliveryEvidenceForMessage(epoMessage);
        confirmationMessage.getMessageDetails().setRefToMessageId(ebmsId);

        backendToGatewayConfirmationProcessor.processMessage(confirmationMessage);

        assertThat(toGwSubmittedMessages).hasSize(1);

//        Mockito.verify(evidencesToolkit, times(1))
//                .createEvidence(eq(DomibusConnectorEvidenceType.DELIVERY),
//                        eq(epoMessage),
//                        any(DomibusConnectorRejectionReason.class),
//                        any(String.class));
//
//        Mockito.verify(evidencePersistenceService, times(1))
//                .persistEvidenceForMessageIntoDatabase(
//                        eq(epoMessage),
//                        Mockito.any(),
//                        any(DomibusConnectorEvidenceType.class));

//        Mockito.verify(messagePersistenceService, times(1)).checkMessageConfirmed(any(DomibusConnectorMessage.class));

    }



    @Test
    public void testProcessMessage_noEvidenceMessage_shouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();

            backendToGatewayConfirmationProcessor.processMessage(epoMessage);
        });
    }



}
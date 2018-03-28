package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

public class BackendToGatewayConfirmationProcessorTest {

    @Mock
    private CreateConfirmationMessageService createConfirmationMessageService;

    @Mock
    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Mock
    private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    private BackendToGatewayConfirmationProcessor backendToGatewayConfirmationProcessor;

    private List<DomibusConnectorMessage> toGwSubmittedMessages;

    @Before
    public void setUp() throws DomibusConnectorGatewaySubmissionException, DomibusConnectorEvidencesToolkitException {
        MockitoAnnotations.initMocks(this);
        toGwSubmittedMessages = new ArrayList<>();
        backendToGatewayConfirmationProcessor = new BackendToGatewayConfirmationProcessor();

        backendToGatewayConfirmationProcessor.setMessagePersistenceService(messagePersistenceService);
        backendToGatewayConfirmationProcessor.setConfirmationMessageService(createConfirmationMessageService);

        backendToGatewayConfirmationProcessor.setGwSubmissionService(gwSubmissionService);

        Mockito.doAnswer( (invoc) -> toGwSubmittedMessages.add(invoc.getArgumentAt(0, DomibusConnectorMessage.class)))
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

    @Test(expected = DomibusConnectorMessageException.class )
    public void testProcessMessage_gwSubmissionFails_shouldThrowException() throws DomibusConnectorEvidencesToolkitException, DomibusConnectorGatewaySubmissionException {
        String connectorMessageId = "msg123456";
        String ebmsId = "ebms1234";
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        epoMessage.setConnectorMessageId(connectorMessageId);
        epoMessage.getMessageDetails().setEbmsMessageId(ebmsId);

        Mockito.when(messagePersistenceService.findMessageByEbmsId(eq(ebmsId))).thenReturn(epoMessage);

        DomibusConnectorMessage confirmationMessage = DomainEntityCreator.createDeliveryEvidenceForMessage(epoMessage);
        confirmationMessage.getMessageDetails().setRefToMessageId(ebmsId);

        Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("something failed..."))
                .when(gwSubmissionService).submitToGateway(any(DomibusConnectorMessage.class));

        backendToGatewayConfirmationProcessor.processMessage(confirmationMessage);

    }


    @Test
    @Ignore
    public void testProcessMessage() throws DomibusConnectorEvidencesToolkitException {
        String connectorMessageId = "msg123456";
        String ebmsId = "ebms1234";
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        epoMessage.setConnectorMessageId(connectorMessageId);
        epoMessage.getMessageDetails().setEbmsMessageId(ebmsId);

        Mockito.when(messagePersistenceService.findMessageByEbmsId(eq(ebmsId))).thenReturn(epoMessage);

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

        Mockito.verify(messagePersistenceService, times(1)).checkMessageConfirmed(any(DomibusConnectorMessage.class));

    }



    @Test(expected = IllegalArgumentException.class)
    public void testProcessMessage_noEvidenceMessage_shouldThrowException() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();

        backendToGatewayConfirmationProcessor.processMessage(epoMessage);
    }

//    @Test
    public void testProcessMessage_RETRIEVAL_evidenceMessage() {
        String connectorMessageId = "msg123456";
        String ebmsId = "ebms1234";

        //prepare original message
        DomibusConnectorMessage originalMessage = DomainEntityCreator.createEpoMessage();
        originalMessage.addConfirmation(DomainEntityCreator.createMessageDeliveryConfirmation());



        //prepare test message
        DomibusConnectorMessageConfirmation retrievalConfirmation = new DomibusConnectorMessageConfirmation();
        retrievalConfirmation.setEvidenceType(DomibusConnectorEvidenceType.RETRIEVAL);

        DomibusConnectorMessage retrievalMessage = DomibusConnectorMessageBuilder.createBuilder()
                .setMessageDetails(originalMessage.getMessageDetails())
                .addConfirmation(retrievalConfirmation)
                .build();

        backendToGatewayConfirmationProcessor.processMessage(retrievalMessage);


    }


}
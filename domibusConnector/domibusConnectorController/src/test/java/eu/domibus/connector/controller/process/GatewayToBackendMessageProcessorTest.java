package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorActionBuilder;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

public class GatewayToBackendMessageProcessorTest {


    @Mock
    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Mock
    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;

    @Mock
    private DomibusConnectorActionPersistenceService actionPersistenceService;

    @Mock
    private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    @Mock
    private DomibusConnectorEvidencesToolkit evidencesToolkit;

    @Mock
    private DomibusConnectorSecurityToolkit securityToolkit;

    @Mock
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    @Mock
    private DomibusConnectorMessageErrorPersistenceService messageErrorPersistenceService;

    GatewayToBackendMessageProcessor gatewayToBackendMessageProcessor;

    private List<DomibusConnectorMessage> toGwDeliveredMessages;
    private List<DomibusConnectorMessage> toBackendDeliveredMessages;
    private List<DomibusConnectorMessageError> savedMessageErrors;

    private static final String CONNECTOR_TEST_ACTION_STRING = "connector-test";
    private static final String CONNECTOR_TEST_SERVICE_STRING = "connector-test";

    @Before
    public void setUp() throws DomibusConnectorGatewaySubmissionException {
        MockitoAnnotations.initMocks(this);
        toGwDeliveredMessages = new ArrayList<>();
        toBackendDeliveredMessages = new ArrayList<>();
        savedMessageErrors = new ArrayList<>();

        gatewayToBackendMessageProcessor = new GatewayToBackendMessageProcessor();
        gatewayToBackendMessageProcessor.setActionPersistenceService(actionPersistenceService);
        gatewayToBackendMessageProcessor.setEvidencePersistenceService(evidencePersistenceService);
        gatewayToBackendMessageProcessor.setEvidencesToolkit(evidencesToolkit);
        gatewayToBackendMessageProcessor.setGwSubmissionService(gwSubmissionService);
        gatewayToBackendMessageProcessor.setMessagePersistenceService(messagePersistenceService);
        gatewayToBackendMessageProcessor.setBackendDeliveryService(backendDeliveryService);
        gatewayToBackendMessageProcessor.setSecurityToolkit(securityToolkit);
        gatewayToBackendMessageProcessor.setMessageIdGenerator(() -> UUID.randomUUID().toString());
        gatewayToBackendMessageProcessor.setMessageErrorPersistenceService(messageErrorPersistenceService);

        gatewayToBackendMessageProcessor.setConnectorTestAction(CONNECTOR_TEST_ACTION_STRING);
        gatewayToBackendMessageProcessor.setConnectorTestService(CONNECTOR_TEST_SERVICE_STRING);

        Mockito.doAnswer(invoc -> invoc.getArgumentAt(0, DomibusConnectorMessage.class))
                .when(messagePersistenceService).mergeMessageWithDatabase(any(DomibusConnectorMessage.class));
        
        Mockito.doAnswer(invoc -> toGwDeliveredMessages.add(invoc.getArgumentAt(0, DomibusConnectorMessage.class)))
                .when(gwSubmissionService).submitToGateway(any(DomibusConnectorMessage.class));

        Mockito.doAnswer( invoc -> toBackendDeliveredMessages.add(invoc.getArgumentAt(0, DomibusConnectorMessage.class)))
                .when(backendDeliveryService).deliverMessageToBackend(any(DomibusConnectorMessage.class));

        Mockito.doAnswer(invoc -> invoc.getArgumentAt(0, DomibusConnectorMessage.class))
                .when(securityToolkit).validateContainer(any(DomibusConnectorMessage.class));

        Mockito.doAnswer(invoc -> savedMessageErrors.add(invoc.getArgumentAt(1, DomibusConnectorMessageError.class)))
                .when(messageErrorPersistenceService).persistMessageError(any(), any());

        Mockito.doAnswer(invoc -> {
            DomibusConnectorEvidenceType type = invoc.getArgumentAt(0, DomibusConnectorEvidenceType.class);
            DomibusConnectorMessageConfirmation confirmation = DomainEntityCreator.createMessageSubmissionAcceptanceConfirmation();
            confirmation.setEvidenceType(type);
            return confirmation;
                })
                .when(evidencesToolkit)
                .createEvidence(any(), any(), any(), any());

    }


    @Test
    public void processMessage() throws DomibusConnectorEvidencesToolkitException {
        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.getMessageDetails().setEbmsMessageId("ebms1");
        message.getMessageDetails().setConnectorBackendClientName(null);
        message.getMessageDetails().setBackendMessageId(null);

        message.getMessageConfirmations().clear();
        message.addConfirmation(DomainEntityCreator.createMessageSubmissionAcceptanceConfirmation());

        gatewayToBackendMessageProcessor.processMessage(message);

        //verify relay REMMDEvidences evidence is created
        Mockito.verify(evidencesToolkit, times(1))
                .createEvidence(eq(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE), any(), eq(null), eq(null));

        //TODO: decide to test sendEvidenceToBackToGateway here or in extra test method


        //confirm container of originalMessage is validated!
        Mockito.verify(securityToolkit, times(1)).validateContainer(eq(message));

    }

    @Test
    public void processMessage_connector2ConnectorTestMessage() {

        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.getMessageDetails().setEbmsMessageId("ebms1");
        message.getMessageDetails().setConnectorBackendClientName(null);
        message.getMessageDetails().setBackendMessageId(null);
        message.getMessageDetails().setAction(DomibusConnectorActionBuilder
                .createBuilder()
                .setAction(CONNECTOR_TEST_ACTION_STRING)
                .build());
        message.getMessageDetails().setService(new DomibusConnectorService(CONNECTOR_TEST_SERVICE_STRING, ""));

        message.getMessageConfirmations().clear();
        message.addConfirmation(DomainEntityCreator.createMessageSubmissionAcceptanceConfirmation());

        gatewayToBackendMessageProcessor.processMessage(message);

        //TODO: test that evidence is generated and sent back!

        //connector2connector Test originalMessage never reaches backend!
        Mockito.verifyZeroInteractions(backendDeliveryService);
    }

    /**
     * tests if the message is still processed even when sending generated evidence back
     * to gw failes!
     * @throws DomibusConnectorGatewaySubmissionException
     */
    @Test
    public void processMessage_sendingEvidenceToGwFailes() throws DomibusConnectorGatewaySubmissionException {
        Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("sent failed!"))
                .when(gwSubmissionService)
                .submitToGateway(any());

        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();

        gatewayToBackendMessageProcessor.processMessage(message);

        assertThat(toBackendDeliveredMessages).hasSize(1);
    }

    /**
     * tests if the message is still processed even when sending generated evidence back
     * to gw failes!
     * @throws DomibusConnectorGatewaySubmissionException
     */
    @Test
    public void processMessage_creatingEvidenceFailes() throws DomibusConnectorGatewaySubmissionException {

        Mockito.doThrow(new DomibusConnectorEvidencesToolkitException("ERROR!"))
                .when(evidencesToolkit)
                .createEvidence(any(), any(), any(), any());

        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();

        gatewayToBackendMessageProcessor.processMessage(message);

        assertThat(toBackendDeliveredMessages).hasSize(1);
    }


    /**
     * Test if security toolkit failes to resolve container,
     * RELAY_REMMD_ACCEPTANCE and then NON_DELIVERY is sent back to gw
     *
     */
    @Test
    public void processMessage_securityToolkitFailes() throws Exception {
        boolean exceptionThrown = false;
        Mockito.doThrow(new DomibusConnectorSecurityException("ERROR!"))
                .when(securityToolkit)
                .validateContainer(any());

        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();

        try {
            gatewayToBackendMessageProcessor.processMessage(message);
        } catch (DomibusConnectorSecurityException e) {
            exceptionThrown = true;
        }

        assertThat(exceptionThrown).isTrue();
        assertThat(toBackendDeliveredMessages).hasSize(0);
        assertThat(toGwDeliveredMessages).hasSize(2);
        assertThat(toGwDeliveredMessages.get(1).getMessageConfirmations().get(0).getEvidenceType())
                .isEqualTo(DomibusConnectorEvidenceType.NON_DELIVERY);


    }


}
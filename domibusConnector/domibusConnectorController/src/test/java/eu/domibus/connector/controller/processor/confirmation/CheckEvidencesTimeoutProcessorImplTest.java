package eu.domibus.connector.controller.processor.confirmation;

import eu.domibus.connector.controller.process.util.ConfirmationMessageBuilderFactory;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.lib.spring.DomibusConnectorDuration;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.testutil.logger.ListAppender;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class CheckEvidencesTimeoutProcessorImplTest {

    CheckEvidencesTimeoutProcessorImpl checkEvidencesTimeoutProcessor;


    EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties;

    @Mock
    private DomibusConnectorMessagePersistenceService persistenceService;
    @Mock
    private DomibusConnectorActionPersistenceService actionPersistenceService;
    @Mock
    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    @Mock
    private DomibusConnectorEvidencesToolkit evidencesToolkit;
    @Mock
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    private ConfirmationMessageBuilderFactory confirmationMessageBuilderFactory;

    public DomibusConnectorMessage createTestMsg1() {
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        return message;
    }

    static ListAppender listAppender;

    @BeforeAll
    public static void beforeAll() {
        LoggerContext context = LoggerContext.getContext(false);
        Logger logger = context.getLogger(CheckEvidencesTimeoutProcessorImpl.class.getName());
        listAppender = new ListAppender("List");
        listAppender.start();
//
        logger.setAdditive(true);
        ((org.apache.logging.log4j.core.Logger) logger).addAppender(listAppender);


    }


    @BeforeEach
    public void setUp() {
        listAppender.clear();
        MockitoAnnotations.initMocks(this);

        evidencesTimeoutConfigurationProperties = new EvidencesTimeoutConfigurationProperties();
        evidencesTimeoutConfigurationProperties.setCheckTimeout(DomibusConnectorDuration.valueOf("5m"));

        evidencesTimeoutConfigurationProperties.setDeliveryTimeout(DomibusConnectorDuration.valueOf("30m"));
        evidencesTimeoutConfigurationProperties.setDeliveryWarnTimeout(DomibusConnectorDuration.valueOf("20m"));

        evidencesTimeoutConfigurationProperties.setRelayREMMDTimeout(DomibusConnectorDuration.valueOf("15m"));
        evidencesTimeoutConfigurationProperties.setRelayREMMDWarnTimeout(DomibusConnectorDuration.valueOf("5m"));

//        evidencesTimeoutConfigurationProperties.setDeliveryWarnTimeout(DomibusConnectorDuration.valueOf("5m"));

        CreateConfirmationMessageBuilderFactoryImpl confirmationMessageFactory = new CreateConfirmationMessageBuilderFactoryImpl();
        confirmationMessageFactory.setActionPersistenceService(actionPersistenceService);
        confirmationMessageFactory.setEvidencePersistenceService(evidencePersistenceService);
        confirmationMessageFactory.setEvidencesToolkit(evidencesToolkit);
        confirmationMessageFactory.setMessageIdGenerator( () -> UUID.randomUUID().toString());
        confirmationMessageBuilderFactory = confirmationMessageFactory;

        checkEvidencesTimeoutProcessor = new CheckEvidencesTimeoutProcessorImpl();
        checkEvidencesTimeoutProcessor.setPersistenceService(persistenceService);
        checkEvidencesTimeoutProcessor.setBackendDeliveryService(backendDeliveryService);
        checkEvidencesTimeoutProcessor.setEvidencesTimeoutConfigurationProperties(this.evidencesTimeoutConfigurationProperties);
        checkEvidencesTimeoutProcessor.setConfirmationMessageBuilderFactory(confirmationMessageBuilderFactory);


        Mockito.when(persistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD())
                .thenReturn(Stream.of(createTestMsg1()).collect(Collectors.toList()));

        Mockito.when(persistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery())
                .thenReturn(Stream.of(createTestMsg1()).collect(Collectors.toList()));

        Mockito.when(evidencesToolkit.createEvidence(any(), any(), any(), any())).thenReturn(DomainEntityCreator.createMessageNonDeliveryConfirmation());

    }


    @Test
    public void testRelayREMMDTimeout() {

        checkEvidencesTimeoutProcessor.checkNotRejectedNorConfirmedWithoutRelayREMMD();
        Mockito.verify(backendDeliveryService, Mockito.times(1)).deliverMessageToBackend(any());

        String logText = "A RelayREMMDFailure evidence has been generated and sent";
        long count = listAppender.getEvents().stream().filter(event ->
                event.getMessage().toString().contains(logText) && LoggingMarker.BUSINESS_LOG.contains(event.getMarker().getName())
        ).count();
        assertThat(count).as("There should be one business log message which contains text '%s' ", logText).isEqualTo(1);
    }

    @Test
    public void testRelayREMMDWarmTimeout() {
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();

        Date currentTimePlus5Min = new Date(Instant.now().minus(Duration.ofMinutes(6)).toEpochMilli());

        message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
        message.getMessageDetails().setDeliveredToGateway(currentTimePlus5Min);

        Mockito.when(persistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD())
                .thenReturn(Stream.of(message).collect(Collectors.toList()));


        checkEvidencesTimeoutProcessor.checkNotRejectedNorConfirmedWithoutRelayREMMD();
        Mockito.verify(backendDeliveryService, Mockito.times(0)).deliverMessageToBackend(any());

        String logText = "reached warning limit for relayREMMD confirmation timeout. No RelayREMMD evidence for this message has been received yet!";
        long count = listAppender.getEvents().stream().filter(event ->
                event.getMessage().toString().contains(logText) && LoggingMarker.BUSINESS_LOG.contains(event.getMarker().getName())
        ).count();
        assertThat(count).as("There should be one business log message which contains text '%s' ", logText).isEqualTo(1);
    }


    @Test
    public void testCheckNotRejectedWithoutDelivery() {
        checkEvidencesTimeoutProcessor.checkNotRejectedWithoutDelivery();
        Mockito.verify(backendDeliveryService, Mockito.times(1)).deliverMessageToBackend(any());


        String logText = "reached Delivery confirmation timeout. A NonDelivery evidence has been generated and sent";
        long count = listAppender.getEvents().stream().filter(event ->
                event.getMessage().toString().contains(logText) && LoggingMarker.BUSINESS_LOG.contains(event.getMarker().getName())
        ).count();
        assertThat(count).as("There should be one business log message which contains text '%s' ", logText).isEqualTo(1);
    }

    @Test
    public void testCheckNotRejectedWithoutDeliveryWarnTimeout() {
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();

        Date currentTimePlus5Min = new Date(Instant.now().minus(Duration.ofMinutes(21)).toEpochMilli());

        message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
        message.getMessageDetails().setDeliveredToGateway(currentTimePlus5Min);

        Mockito.when(persistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery())
                .thenReturn(Stream.of(message).collect(Collectors.toList()));


        checkEvidencesTimeoutProcessor.checkNotRejectedWithoutDelivery();
        Mockito.verify(backendDeliveryService, Mockito.times(0)).deliverMessageToBackend(any());

        String logText = "reached warning limit for delivery confirmation timeout. No Delivery evidence for this message has been received yet!";
        long count = listAppender.getEvents().stream().filter(event ->
                event.getMessage().toString().contains(logText) && LoggingMarker.BUSINESS_LOG.contains(event.getMarker().getName())
        ).count();
        assertThat(count).as("There should be one business log message which contains text '%s' ", logText).isEqualTo(1);
    }


}
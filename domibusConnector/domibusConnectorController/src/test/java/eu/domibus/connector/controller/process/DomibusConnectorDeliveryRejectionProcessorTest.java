package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

public class DomibusConnectorDeliveryRejectionProcessorTest {


    DomibusConnectorDeliveryRejectionProcessor rejectionProcessor;

    private ArrayList<DomibusConnectorMessage> toGwDeliveredMessages;

    @Mock
    private CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;
    @Mock
    private DCMessagePersistenceService messagePersistenceService;
    @Mock
    private DomibusConnectorGatewaySubmissionService gatewaySubmissionService;

    @BeforeEach
    public void setupTest() {
        MockitoAnnotations.initMocks(this);
        toGwDeliveredMessages = new ArrayList<>();

        rejectionProcessor = new DomibusConnectorDeliveryRejectionProcessor();

        rejectionProcessor.setCreateConfirmationMessageBuilderFactoryImpl(createConfirmationMessageBuilderFactoryImpl);
        rejectionProcessor.setGwSubmissionService(gatewaySubmissionService);
        rejectionProcessor.setMessagePersistenceService(messagePersistenceService);


    }

    @Test
    void processMessage() {
        

    }
}
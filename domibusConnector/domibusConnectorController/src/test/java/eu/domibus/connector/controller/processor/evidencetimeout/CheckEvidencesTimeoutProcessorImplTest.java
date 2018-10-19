package eu.domibus.connector.controller.processor.evidencetimeout;

import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.lib.spring.Duration;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CheckEvidencesTimeoutProcessorImplTest {

    CheckEvidencesTimeoutProcessor checkEvidencesTimeoutProcessor;


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


    @BeforeEach
    public void setUp() {
        evidencesTimeoutConfigurationProperties = new EvidencesTimeoutConfigurationProperties();
        evidencesTimeoutConfigurationProperties.setCheckTimeout(Duration.valueOf("5m"));
        evidencesTimeoutConfigurationProperties.setDeliveryTimeout(Duration.valueOf("5m"));
//        evidencesTimeoutConfigurationProperties.setDeliveryWarnTimeout(Duration.valueOf("5m"));


        checkEvidencesTimeoutProcessor = new CheckEvidencesTimeoutProcessorImpl();

    }

}
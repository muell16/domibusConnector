package eu.domibus.connector.controller.test.util;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.process.util.ConfirmationMessageBuilderFactory;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorAction;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorActionPersistenceServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class MockedCreateConfirmationMessageBuilderFactoryImplProvider {

    private final CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactory;

    @Mock
    private DomibusConnectorEvidencesToolkit evidencesToolkit;
    @Mock
    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    @Mock
    private DomibusConnectorActionPersistenceService actionPersistenceService;
    @Mock
    private DomibusConnectorMessageIdGenerator messageIdGenerator;

    public DomibusConnectorEvidencesToolkit getMockedEvidencesToolkit() {
        return evidencesToolkit;
    }

    public DomibusConnectorEvidencePersistenceService getMockedEvidencePersistenceService() {
        return evidencePersistenceService;
    }

    public DomibusConnectorActionPersistenceService getMockedActionPersistenceService() {
        return actionPersistenceService;
    }

    public DomibusConnectorMessageIdGenerator getMockedMessageIdGenerator() {
        return messageIdGenerator;
    }

    public MockedCreateConfirmationMessageBuilderFactoryImplProvider() {

        MockitoAnnotations.initMocks(this);

        this.actionPersistenceService = new DomibusConnectorActionPersistenceServiceImpl();

        DomibusConnectorActionDao actionDao = Mockito.mock(DomibusConnectorActionDao.class);
        Mockito.when(actionDao.findById(any(String.class))).thenAnswer(
                (Answer<Optional<PDomibusConnectorAction>>) invocation -> {
                    PDomibusConnectorAction a = new PDomibusConnectorAction();
                    a.setAction(invocation.getArgument(0));
                    a.setDocumentRequired(false);
                    return Optional.of(a);
                }
        );
        ((DomibusConnectorActionPersistenceServiceImpl) this.actionPersistenceService).setActionDao(actionDao);


        Mockito.when(evidencesToolkit.createEvidence(any(), any(), any(), any())).thenReturn(DomainEntityCreator.createMessageDeliveryConfirmation());

        this.createConfirmationMessageBuilderFactory = new CreateConfirmationMessageBuilderFactoryImpl();
        createConfirmationMessageBuilderFactory.setActionPersistenceService(this.actionPersistenceService);
        createConfirmationMessageBuilderFactory.setEvidencePersistenceService(this.evidencePersistenceService);
        createConfirmationMessageBuilderFactory.setEvidencesToolkit(this.evidencesToolkit);
        createConfirmationMessageBuilderFactory.setMessageIdGenerator(this.messageIdGenerator);


        Mockito.when(messageIdGenerator.generateDomibusConnectorMessageId()).thenReturn(UUID.randomUUID().toString() + "@mockedid");

    }

    public CreateConfirmationMessageBuilderFactoryImpl getCreateConfirmationMessageBuilderFactory() {
        return createConfirmationMessageBuilderFactory;
    }
}

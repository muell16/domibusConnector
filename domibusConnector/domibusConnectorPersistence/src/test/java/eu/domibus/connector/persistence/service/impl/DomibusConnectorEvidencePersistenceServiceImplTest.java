package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createDeliveryEvidence;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createNonDeliveryEvidence;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class DomibusConnectorEvidencePersistenceServiceImplTest {

    @Mock
    DomibusConnectorEvidenceDao domibusConnectorEvidenceDao;

    @Mock
    DomibusConnectorMessageDao domibusConnectorMessageDao;

    DomibusConnectorEvidencePersistenceService evidencePersistenceService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorEvidencePersistenceServiceImpl impl = new DomibusConnectorEvidencePersistenceServiceImpl();
        impl.setEvidenceDao(domibusConnectorEvidenceDao);
        impl.setMessageDao(domibusConnectorMessageDao);
        evidencePersistenceService = impl;
    }

    @Test
    public void testPersistEvidenceForMessageIntoDatabase_evidenceBytesAreNull() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();


        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();

        byte[] evidence = null;

        Mockito.when(this.domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);

        Mockito.when(this.domibusConnectorEvidenceDao.save(any(PDomibusConnectorEvidence.class)))
                .thenAnswer(new Answer<PDomibusConnectorEvidence>() {
                    @Override
                    public PDomibusConnectorEvidence answer(InvocationOnMock invocation) throws Throwable {
                        PDomibusConnectorEvidence evidence = invocation.getArgument(0);
                        assertThat(evidence.getDeliveredToGateway()).isNull();
                        assertThat(evidence.getDeliveredToNationalSystem()).isNull();
                        assertThat(evidence.getType()).isEqualTo(eu.domibus.connector.persistence.model.enums.EvidenceType.DELIVERY);
                        assertThat(evidence.getMessage()).isNotNull();
                        assertThat(evidence.getEvidence()).isNull();
                        return evidence;
                    }
                });

        evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message, evidence, DomibusConnectorEvidenceType.DELIVERY, new DomibusConnectorMessage.DomibusConnectorMessageId("msgid"));

        Mockito.verify(this.domibusConnectorEvidenceDao, Mockito.times(1)).save(any(PDomibusConnectorEvidence.class));
    }


    @Test
    public void testPersistEvidenceForMessageIntoDatabase() {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();


        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();

        byte[] evidence = "EVIDENCE1".getBytes();

        Mockito.when(this.domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);

        Mockito.when(this.domibusConnectorEvidenceDao.save(any(PDomibusConnectorEvidence.class)))
                .thenAnswer(new Answer<PDomibusConnectorEvidence>() {
                    @Override
                    public PDomibusConnectorEvidence answer(InvocationOnMock invocation) throws Throwable {
                        PDomibusConnectorEvidence evidence = invocation.getArgument(0);
                        assertThat(evidence.getDeliveredToGateway()).isNull();
                        assertThat(evidence.getDeliveredToNationalSystem()).isNull();
                        assertThat(evidence.getType()).isEqualTo(eu.domibus.connector.persistence.model.enums.EvidenceType.DELIVERY);
                        assertThat(evidence.getMessage()).isNotNull();
                        assertThat(evidence.getEvidence()).isEqualTo("EVIDENCE1");
                        return evidence;
                    }
                });

        evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message, evidence, DomibusConnectorEvidenceType.DELIVERY, new DomibusConnectorMessage.DomibusConnectorMessageId("msgid"));

        Mockito.verify(this.domibusConnectorEvidenceDao, Mockito.times(1)).save(any(PDomibusConnectorEvidence.class));
    }

}
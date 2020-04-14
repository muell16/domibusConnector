package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

@Disabled
public class DomibusConnectorActionPersistenceServiceImplTest {

    @Mock
    DomibusConnectorActionDao actionDao;

    DomibusConnectorActionPersistenceService actionPersistenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorActionPersistenceServiceImpl impl = new DomibusConnectorActionPersistenceServiceImpl();
        impl.setActionDao(actionDao);
        actionPersistenceService = impl;
    }
//
//    /**
//     * Action
//     */
//    @Test
//    public void testGetAction() {
//
//        Mockito.when(this.actionDao.findById(eq("action1")))
//                .thenReturn(Optional.ofNullable(PersistenceEntityCreator.createAction()));
//
//        eu.domibus.connector.domain.model.DomibusConnectorAction action = actionPersistenceService.getAction("action1");
//
//        assertThat(action).isNotNull();
//        assertThat(action.getAction()).isEqualTo("action1");
//    }
//
//    @Test
//    public void testGetRelayREMMDAcceptanceRejectionAction() {
//        Mockito.when(this.actionDao.findById(eq("RelayREMMDAcceptanceRejection")))
//                .thenReturn(Optional.ofNullable(PersistenceEntityCreator.createRelayREMMDAcceptanceRejectionAction()));
//
//        eu.domibus.connector.domain.model.DomibusConnectorAction action = actionPersistenceService.getRelayREMMDAcceptanceRejectionAction();
//
//        assertThat(action).isNotNull();
//        assertThat(action.getAction()).isEqualTo("RelayREMMDAcceptanceRejection");
//    }
//
//    @Test
//    public void testGetDeliveryNonDeliveryToRecipientAction() {
//        Mockito.when(this.actionDao.findById(eq("DeliveryNonDeliveryToRecipient")))
//                .thenReturn(Optional.ofNullable(PersistenceEntityCreator.createDeliveryNonDeliveryToRecipientAction()));
//
//        eu.domibus.connector.domain.model.DomibusConnectorAction action = actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();
//
//        assertThat(action).isNotNull();
//        assertThat(action.getAction()).isEqualTo("DeliveryNonDeliveryToRecipient");
//    }
//
//    @Test
//    public void testGetRetrievalNonRetrievalToRecipientAction() {
//
//        Mockito.when(this.actionDao.findById(eq("RetrievalNonRetrievalToRecipient")))
//                .thenReturn(Optional.ofNullable(PersistenceEntityCreator.createRetrievalNonRetrievalToRecipientAction()));
//
//        eu.domibus.connector.domain.model.DomibusConnectorAction action = actionPersistenceService.getRetrievalNonRetrievalToRecipientAction();
//
//        assertThat(action).isNotNull();
//        assertThat(action.getAction()).isEqualTo("RetrievalNonRetrievalToRecipient");
//    }
//
//    @Test
//    public void testGetRelayREMMDFailure() {
//        Mockito.when(this.actionDao.findById(eq("RelayREMMDFailure")))
//                .thenReturn(Optional.ofNullable(PersistenceEntityCreator.createRelayREMMDFailureAction()));
//
//        eu.domibus.connector.domain.model.DomibusConnectorAction action = actionPersistenceService.getRelayREMMDFailure();
//
//        assertThat(action).isNotNull();
//        assertThat(action.getAction()).isEqualTo("RelayREMMDFailure");
//    }

}
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.*;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import eu.domibus.connector.persistence.service.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class InternalMessageInfoPersistenceServiceImplTest {

    @Mock
    private DomibusConnectorMessageInfoDao messageInfoDao;
    @Mock
    private DomibusConnectorPartyDao partyDao;
    @Mock
    private DomibusConnectorServiceDao serviceDao;
    @Mock
    private DomibusConnectorActionDao actionDao;

    private InternalMessageInfoPersistenceService internalMessageInfoPersistenceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        InternalMessageInfoPersistenceServiceImpl impl = new InternalMessageInfoPersistenceServiceImpl();
        impl.setActionDao(actionDao);
        impl.setMessageInfoDao(messageInfoDao);
        impl.setPartyDao(partyDao);
        impl.setServiceDao(serviceDao);

        PDomibusConnectorParty partyAT = PersistenceEntityCreator.createPartyAT();
        Mockito.when(partyDao.findOne(eq(new PDomibusConnectorPartyPK(partyAT))))
                .thenReturn(partyAT);

        PDomibusConnectorParty domibusBlue = PersistenceEntityCreator.createPartyDomibusBLUE();
        Mockito.when(partyDao.findOne(eq(new PDomibusConnectorPartyPK(domibusBlue))))
                .thenReturn(domibusBlue);

        PDomibusConnectorService epoService = PersistenceEntityCreator.createServiceEPO();
        Mockito.when(serviceDao.findOne(eq(epoService.getService())))
                .thenReturn(epoService);

        PDomibusConnectorAction relayREMMDAcceptanceRejectionAction = PersistenceEntityCreator.createRelayREMMDAcceptanceRejectionAction();
        Mockito.when(actionDao.findOne(eq(relayREMMDAcceptanceRejectionAction.getAction())))
                .thenReturn(relayREMMDAcceptanceRejectionAction);


        this.internalMessageInfoPersistenceService = impl;

    }



    @Test
    public void testValidatePartyServiceActionOfMessageInfo() {
        PDomibusConnectorMessageInfo messageInfo = PersistenceEntityCreator.createSimpleMessageInfo();

        internalMessageInfoPersistenceService.validatePartyServiceActionOfMessageInfo(messageInfo);
    }

    @Test(expected = PersistenceException.class)
    public void testValidatePartyServiceActionOfMessageInfo_partyNotConfigured() {
        PDomibusConnectorMessageInfo messageInfo = PersistenceEntityCreator.createSimpleMessageInfo();
        PDomibusConnectorParty unknownParty = new PDomibusConnectorParty();
        unknownParty.setRole("GW");
        unknownParty.setPartyId("id2");
        messageInfo.setFrom(unknownParty);

        internalMessageInfoPersistenceService.validatePartyServiceActionOfMessageInfo(messageInfo);
    }

}

package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.backend.persistence.model.testutil.BackendPersistenceEntityCreator;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 *
 *
 */
public class BackendClientInfoPersistenceServiceImplTest {


    BackendClientDao backendClientDao;
    
    BackendClientInfoPersistenceServiceImpl backendInfoPersistenceService;
    
    @Before
    public void setUp() {
        backendClientDao = Mockito.mock(BackendClientDao.class);
        
        backendInfoPersistenceService = new BackendClientInfoPersistenceServiceImpl();
        backendInfoPersistenceService.setBackendClientDao(backendClientDao);
    }

    @Test
    public void testMapDbEntityToDomainEntity() {
        BackendClientInfo bob = createBackendClientInfoBob();
        
        DomibusConnectorBackendClientInfo domainBackendInfo = backendInfoPersistenceService.mapDbEntityToDomainEntity(bob);
     
        assertThat(domainBackendInfo.getBackendDescription()).isEqualTo("description");
        assertThat(domainBackendInfo.getBackendKeyAlias()).isEqualTo("keyalias");
        assertThat(domainBackendInfo.getBackendKeyPass()).isEqualTo("keypass");
        assertThat(domainBackendInfo.getBackendName()).isEqualTo("backendname");
        assertThat(domainBackendInfo.getBackendPushAddress()).isEqualTo("backendpushaddress");
        assertThat(domainBackendInfo.isDefaultBackend()).isTrue();
    }
    
    @Test
    public void testMapDbEntityToDomainEntity_entityIsNull_shouldReturnNull() {
        DomibusConnectorBackendClientInfo domainBackendInfo = backendInfoPersistenceService.mapDbEntityToDomainEntity(null);
        assertThat(domainBackendInfo).isNull();
    }

    //TODO: test mapping from domainToDbEntity
    
    @Test
    public void testGetBackendClientInfoByName() {
        BackendClientInfo bob = createBackendClientInfoBob();
        Mockito.when(backendClientDao.findOneBackendByBackendNameAndEnabledIsTrue(eq("bob"))).thenReturn(bob);
        DomibusConnectorBackendClientInfo backendClientInfoByName = backendInfoPersistenceService.getEnabledBackendClientInfoByName("bob");
        
        assertThat(backendClientInfoByName).isNotNull();        
    }

    @Test
    public void testFindByService() {
        BackendClientInfo bob = createBackendClientInfoBob();

        Mockito.when(backendClientDao.findByServices_serviceAndEnabledIsTrue(eq("EPO-Service"))).thenReturn(Arrays.asList(new BackendClientInfo[] {bob}));

        DomibusConnectorService service = new DomibusConnectorService("EPO-Service", "");
        DomibusConnectorBackendClientInfo backendClientInfoByServiceName = backendInfoPersistenceService.getEnabledBackendClientInfoByService(service);

        assertThat(backendClientInfoByServiceName).isNotNull();
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void testFindByService_multipleBackends_shouldThrow() {
        BackendClientInfo bob = createBackendClientInfoBob();
        BackendClientInfo alice = createBackendClientInfoAlice();

        Mockito.when(backendClientDao.findByServices_serviceAndEnabledIsTrue(eq("EPO-Service"))).thenReturn(Arrays.asList(new BackendClientInfo[] {bob, alice}));

        DomibusConnectorService service = new DomibusConnectorService("EPO-Service", "");
        DomibusConnectorBackendClientInfo backendClientInfoByServiceName = backendInfoPersistenceService.getEnabledBackendClientInfoByService(service);
    }


    
    private BackendClientInfo createBackendClientInfoBob() {
        BackendClientInfo bob = BackendPersistenceEntityCreator.createBackendClientInfoBob();
        bob.setBackendDescription("description");
        bob.setBackendKeyAlias("keyalias");
        bob.setBackendKeyPass("keypass");
        bob.setBackendName("backendname");
        bob.setBackendPushAddress("backendpushaddress");
        bob.setDefaultBackend(true);
        bob.setId(20L);
        
        return bob;
    }

    private BackendClientInfo createBackendClientInfoAlice() {
        BackendClientInfo alice = BackendPersistenceEntityCreator.createBackendClientInfoBob();
        alice.setBackendName("alice");
        alice.setBackendDescription("description");
        alice.setBackendKeyAlias("keyalias");
        alice.setBackendKeyPass("keypass");
        alice.setBackendPushAddress("backendpushaddress");
        alice.setId(21L);

        return alice;
    }




}
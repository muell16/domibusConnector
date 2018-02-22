
package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.backend.persistence.model.testutil.BackendPersistenceEntityCreator;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class BackendClientInfoPersistenceServiceImplTest {


    BackendClientDao backendClientDao;
    
    BackendClientInfoPersistenceServiceImpl service;
    
    @Before
    public void setUp() {
        backendClientDao = Mockito.mock(BackendClientDao.class);
        
        service = new BackendClientInfoPersistenceServiceImpl();
        service.setBackendClientDao(backendClientDao);
    }

    @Test
    public void testMapDbEntityToDomainEntity() {
        BackendClientInfo bob = createBackendClientInfoBob();
        
        DomibusConnectorBackendClientInfo domainBackendInfo = service.mapDbEntityToDomainEntity(bob);
     
        assertThat(domainBackendInfo.getBackendDescription()).isEqualTo("description");
        assertThat(domainBackendInfo.getBackendKeyAlias()).isEqualTo("keyalias");
        assertThat(domainBackendInfo.getBackendKeyPass()).isEqualTo("keypass");
        assertThat(domainBackendInfo.getBackendName()).isEqualTo("backendname");
        assertThat(domainBackendInfo.getBackendPushAddress()).isEqualTo("backendpushaddress");                
    }
    
    @Test
    public void testMapDbEntityToDomainEntity_entityIsNull_shouldReturnNull() {
        DomibusConnectorBackendClientInfo domainBackendInfo = service.mapDbEntityToDomainEntity(null);
        assertThat(domainBackendInfo).isNull();
    }
    
    @Test
    public void test() {
        BackendClientInfo bob = createBackendClientInfoBob();
        Mockito.when(backendClientDao.findOneBackendByBackendName(eq("bob"))).thenReturn(bob);
        DomibusConnectorBackendClientInfo backendClientInfoByName = service.getBackendClientInfoByName("bob");
        
        assertThat(backendClientInfoByName).isNotNull();        
    }
    
    private BackendClientInfo createBackendClientInfoBob() {
        BackendClientInfo bob = BackendPersistenceEntityCreator.createBackendClientInfoBob();
        bob.setBackendDescription("description");
        bob.setBackendKeyAlias("keyalias");
        bob.setBackendKeyPass("keypass");
        bob.setBackendName("backendname");
        bob.setBackendPushAddress("backendpushaddress");
        bob.setId(20L);
        
        return bob;
    }

}
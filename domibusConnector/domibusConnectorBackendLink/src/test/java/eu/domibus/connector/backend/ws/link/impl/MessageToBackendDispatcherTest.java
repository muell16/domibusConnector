
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.ws.link.impl.MessageToBackendDispatcher;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class MessageToBackendDispatcherTest {

    private MessageToBackendDispatcher messageToBackendDispatcher;
    
    private BackendClientDao backendClientDao;
    
    @Before
    public void setUp() {
        backendClientDao = Mockito.mock(BackendClientDao.class);
        
        messageToBackendDispatcher = new MessageToBackendDispatcher();
        messageToBackendDispatcher.setBackendClientDao(backendClientDao);
    }

    
    @Test
    public void testSetBackendNameInMessageHandling() {
        BackendClientInfo backendClientBob = new BackendClientInfo();
        backendClientBob.setBackendName("bob");
        List<BackendClientInfo> backendClientsList = new ArrayList<>();
        backendClientsList.add(backendClientBob);
        Mockito.when(backendClientDao.findByServices_serviceAndEnabledIsTrue(eq("EPO"))).thenReturn(backendClientsList);
        
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();

        message = messageToBackendDispatcher.handleMessage(message);
        
        assertThat(message.getConnectorBackendClientName()).isEqualTo("bob");
    }
    
    @Test(expected=IllegalStateException.class)
    public void testSetBackendNameInMessageHandling_noBackends_shouldThrowIllegalStateException() {
        List<BackendClientInfo> backendClientsList = new ArrayList<>();
        Mockito.when(backendClientDao.findByServices_serviceAndEnabledIsTrue(eq("EPO"))).thenReturn(backendClientsList);
        
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();

        message = messageToBackendDispatcher.handleMessage(message);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testSetBackendNameInMessageHandling_moreThanOneBackend_shouldThrowIllegalStateException() {
        List<BackendClientInfo> backendClientsList = new ArrayList<>();
                
        BackendClientInfo backendClientBob = new BackendClientInfo();
        backendClientBob.setBackendName("bob");
        backendClientsList.add(backendClientBob);
                
        BackendClientInfo backendClientAlice = new BackendClientInfo();
        backendClientAlice.setBackendName("alice");
        backendClientsList.add(backendClientAlice);
        
        Mockito.when(backendClientDao.findByServices_serviceAndEnabledIsTrue(eq("EPO"))).thenReturn(backendClientsList);
        
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();

        message = messageToBackendDispatcher.handleMessage(message);
        
        assertThat(message.getConnectorBackendClientName()).isEqualTo("bob");
    }
    
    
}
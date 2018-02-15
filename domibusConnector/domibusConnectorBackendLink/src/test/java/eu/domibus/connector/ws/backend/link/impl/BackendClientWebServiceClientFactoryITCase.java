
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import static eu.domibus.connector.ws.backend.link.impl.AbstractBackendITTestHelper.backendApplicationContext;
import java.util.List;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assume.assumeTrue;
import org.springframework.context.ApplicationContext;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class BackendClientWebServiceClientFactoryITCase extends AbstractBackendITTestHelper {

    
    @Test
    public void testPushMessageToClientBob() {               
        DomibusConnectorBackendDeliveryWebService wsClient = createClient("bob", bobApplicationContext);
        
        DomibusConnectorMessageType message = TransitionCreator.createMessage();
        
        DomibsConnectorAcknowledgementType pushMessageToBackendClient = wsClient.deliverMessage(message);
        assertThat(pushMessageToBackendClient.isResult()).isTrue();
        
        //TODO: check if message is successfully rcv!
        List<DomibusConnectorMessageType> messages = bobApplicationContext.getBean("deliveredMessages", List.class);
        assertThat(messages).hasSize(1);        
    }
    
    
    @Test
    public void testPushMessageToClientAlice() {
        assumeTrue("alice must be started!", aliceApplicationContext != null);

        DomibusConnectorBackendDeliveryWebService wsClient = createClient("alice", aliceApplicationContext);
                
        DomibusConnectorMessageType message = TransitionCreator.createMessage();       
        assertThat(wsClient).isNotNull();
        
        //send message
        DomibsConnectorAcknowledgementType response = wsClient.deliverMessage(message);                
        assertThat(response.isResult()).isTrue();
        
        //check if message is successfully rcv!
        List<DomibusConnectorMessageType> messages = aliceApplicationContext.getBean("deliveredMessages", List.class);
        assertThat(messages).hasSize(1); 
        
    }
    
    @Test
    public void canCreateWebServiceClient() {
        assumeTrue("alice must be started!", aliceApplicationContext != null);

        BackendClientWebServiceClientFactory webServiceClientFactory = backendApplicationContext.getBean(BackendClientWebServiceClientFactory.class);

        DomibusConnectorBackendClientInfo alice = new DomibusConnectorBackendClientInfo();
        alice.setBackendName("alice");
        alice.setBackendKeyAlias("alice");
        alice.setBackendPushAddress(getBackendClientPushServiceAddress(aliceApplicationContext));
                
        DomibusConnectorMessageType message = TransitionCreator.createMessage();
                
        DomibusConnectorBackendDeliveryWebService wsClient = webServiceClientFactory.createWsClient(alice);
        assertThat(wsClient).isNotNull();
    }
    
    private DomibusConnectorBackendDeliveryWebService createClient(String name, ApplicationContext appContext) {
        assumeTrue(name + " must be started!", appContext != null);

        BackendClientWebServiceClientFactory webServiceClientFactory = backendApplicationContext.getBean(BackendClientWebServiceClientFactory.class);

        DomibusConnectorBackendClientInfo alice = new DomibusConnectorBackendClientInfo();
        alice.setBackendName(name);
        alice.setBackendKeyAlias(name);
        alice.setBackendPushAddress(getBackendClientPushServiceAddress(appContext));
                
        DomibusConnectorBackendDeliveryWebService wsClient = webServiceClientFactory.createWsClient(alice);
        assertThat(wsClient).isNotNull();
        return wsClient;
    }
    
    

}
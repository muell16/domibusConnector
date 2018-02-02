
package eu.domibus.connector.ws.backend.linktest.client;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.ws.backend.link.spring.ClientPasswordCallback;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.net.MalformedURLException;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendClientBob extends CommonBackendClient {

    public static void main(String[] args) throws MalformedURLException {
        ApplicationContext ctx = BackendClientBob.startSpringApplication("ws.username=bob", "ws.password=test");
        
        DomibusConnectorBackendWebService domibusConnectorBackendWebService = ctx.getBean("backendClient", DomibusConnectorBackendWebService.class);

        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType response = domibusConnectorBackendWebService.submitMessage(msg);
        System.out.println("RESPONSE result: " + response.isResult());
    }
    
    
    

//    @Override
//    protected ClientPasswordCallback getClientPasswordCallback() {
//        return new ClientPasswordCallback("bob", "test");
//    }
//
//    @Override
//    protected String getUser() {
//        return "bob";
//    }
    
}

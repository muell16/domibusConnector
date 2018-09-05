
package test.eu.domibus.connector.backend.ws.linktest.client;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.net.MalformedURLException;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendClientBob extends CommonBackendClient {

    public static void main(String[] args) throws MalformedURLException {
        String[] springProps = new String[] {"ws.backendclient.name=bob", "ws.backendclient.cn=bob", "ws.backendclient.password=test"};
        String[] springProfiles = new String[] {"ws-backendclient-client"};
        ApplicationContext ctx = BackendClientBob.startSpringApplication(springProfiles, springProps);
        
        DomibusConnectorBackendWebService domibusConnectorBackendWebService = ctx.getBean("backendClient", DomibusConnectorBackendWebService.class);

        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType response = domibusConnectorBackendWebService.submitMessage(msg);
        System.out.println("RESPONSE result: " + response.isResult());
    }
    

    
}

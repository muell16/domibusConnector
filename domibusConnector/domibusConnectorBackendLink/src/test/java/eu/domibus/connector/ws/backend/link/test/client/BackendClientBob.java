
package eu.domibus.connector.ws.backend.link.test.client;

import eu.domibus.connector.ws.backend.link.spring.ClientPasswordCallback;
import java.net.MalformedURLException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendClientBob extends AbstractClient {

    public static void main(String[] args) throws MalformedURLException {
        AbstractClient.startSpringApplication("ws.username=bob", "ws.password=test");
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

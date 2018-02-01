
package eu.domibus.connector.ws.backend.link.test.client;

import eu.domibus.connector.ws.backend.link.spring.ClientPasswordCallback;
import java.net.MalformedURLException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendClientBob extends AbstractClient {

    public static void main(String[] args) throws MalformedURLException {
        new BackendClientBob().initializeAndSendMessage();
    }
    

    @Override
    protected ClientPasswordCallback getClientPasswordCallback() {
        return new ClientPasswordCallback("bob", "test");
    }

    @Override
    protected String getUser() {
        return "bob";
    }
    
}

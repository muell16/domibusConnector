
package eu.domibus.connector.ws.backend.link.spring;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class ClientPasswordCallback implements CallbackHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientPasswordCallback.class);
    
    private final String password;

    public ClientPasswordCallback(String password) {
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        System.out.println("%n%n###############%nHANDLE CALLBACK!%n%n%n%");
        LOGGER.debug("#handle: callback: [{}]", callbacks);
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        // set the password for our message.
        pc.setPassword(password);
    }

}

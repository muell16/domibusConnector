
package eu.domibus.connector.backend.ws.link.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    

    private Map<String, String> passwords = 
	        new HashMap<String, String>();
    
    public ClientPasswordCallback(String username, String password) {
        this.passwords.put(username, password);
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        System.out.println("%n%n###############%nHANDLE CALLBACK!%n%n%n%");
        LOGGER.debug("#handle: callback: [{}]", callbacks);
//        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
//        // set the password for our message.
//        pc.setPassword(password);
        
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }        
    }
    
    public void setAliasPassword(String alias, String password) {
	        passwords.put(alias, password);
	}

}

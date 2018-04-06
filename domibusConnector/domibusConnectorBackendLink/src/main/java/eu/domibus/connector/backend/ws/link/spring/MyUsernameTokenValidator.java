
package eu.domibus.connector.backend.ws.link.spring;

import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
class MyUsernameTokenValidator extends UsernameTokenValidator {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyUsernameTokenValidator.class);
    
    @Override
    protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) {
        System.out.println("VERIFY PASSWORD....");
        LOGGER.debug("VERIFY USERNAME TOKEN usernameToken: [{}]", usernameToken);
        String username = null;
        String password = null;
        if (usernameToken != null) {
            username = usernameToken.getName();
            password = usernameToken.getPassword();
        }
        LOGGER.debug("USERNAME IS [{}] password IS [{}]", username, password);
        data.setUsername("username test");
    }

}

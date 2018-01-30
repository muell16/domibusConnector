
package eu.domibus.connector.ws.backend.link;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.ws.backend.link.spring.ClientPasswordCallback;
import eu.domibus.connector.ws.backend.link.spring.WSBackendLinkConfigurationProperties;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JStaxOutInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class BackendClientAlice extends AbstractClient {

    public static void main(String[] args) throws MalformedURLException {
        BackendClientAlice backendClientBob = new BackendClientAlice();
        backendClientBob.sendMessage();
    }
    
    
        
    
    @Override
    protected ClientPasswordCallback getClientPasswordCallback() {
        return new ClientPasswordCallback("test");
    }

    @Override
    protected String getUser() {
        return "alice";
    }
    
}

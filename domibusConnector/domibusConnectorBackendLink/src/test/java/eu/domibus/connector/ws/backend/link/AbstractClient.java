
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public abstract class AbstractClient {

    Properties loadProperties() {
        try {
            Properties props = new Properties();
            InputStream inputStream = BackendClientBob.class.getResourceAsStream("/application.properties");
            if (inputStream == null) {
                throw new RuntimeException("input stream is null!");
            }
            props.load(inputStream);
            return props;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    
    protected abstract ClientPasswordCallback getClientPasswordCallback();

    protected abstract String getUser();

    public void sendMessage() throws MalformedURLException {
        Properties props = loadProperties();
        String port = props.getProperty("server.port");
        String webservicesPath = props.getProperty("spring.webservices.path");
        WSBackendLinkConfigurationProperties backendLinkConfigurationProperties = new WSBackendLinkConfigurationProperties();
        String publish = backendLinkConfigurationProperties.getBackendPublishAddress();
        String url = "http://localhost:" + port + webservicesPath + publish;
        System.out.println("URL " + url);
        URL wsdlURL = new URL(url + "?wsdl");
        QName serviceName = DomibusConnectorBackendWSService.DomibusConnectorBackendWebService;
        MTOMFeature mtom = new MTOMFeature();
        Service service = Service.create(wsdlURL, serviceName, mtom);
        DomibusConnectorBackendWebService backendWebServiceClient = service.getPort(DomibusConnectorBackendWebService.class);
        Client client = ClientProxy.getClient(backendWebServiceClient);
        Endpoint cxfEndpoint = client.getEndpoint();
        //cxfEndpoint.getOutInterceptors().addAll(configureOutgoingInterceptors());
        cxfEndpoint.getOutInterceptors().add(configureOutgoingInterceptor());
        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType response = backendWebServiceClient.submitMessage(msg);
        System.out.println("RESPONSE result: " + response.isResult());
    }

    Interceptor<? extends Message> configureOutgoingInterceptor() {
        Map<String, Object> outProps = new HashMap<>();
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        // Specify our username
        outProps.put(WSHandlerConstants.USER, getUser());
        // Password type : plain text
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        // for hashed password use:
        //properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
        // Callback used to retrieve password for given user.
        outProps.put(WSHandlerConstants.PW_CALLBACK_REF, getClientPasswordCallback());
        outProps.put(WSHandlerConstants.EXPAND_XOP_INCLUDE_FOR_SIGNATURE, true);
        //WSS4JStaxOutInterceptor https://ws.apache.org/wss4j/streaming.html
        
        WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outProps);
        
        
        return outInterceptor;
    }
}

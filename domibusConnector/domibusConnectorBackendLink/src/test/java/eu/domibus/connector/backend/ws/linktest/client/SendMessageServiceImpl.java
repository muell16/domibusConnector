
package eu.domibus.connector.backend.ws.linktest.client;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.backend.ws.link.spring.WSBackendLinkConfigurationProperties;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.policy.PolicyBuilder;
import org.apache.cxf.ws.policy.PolicyBuilderImpl;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JStaxOutInterceptor;
import org.apache.neethi.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@Component
public class SendMessageServiceImpl implements SendMessageService {

    
    @Value("${ws.username}")
    String username;
    
    @Value("${ws.password}")
    String password;
    
    @Value("${spring.webservices.path}")
    String webservicesPath = "/services";
    
    @Value("${server.port}")
    String serverPort = "8092";
    
//    @Autowired
//    Bus springCxfBus;
    
    
    @Override
    public void sendMessage() {
        
            WSBackendLinkConfigurationProperties backendLinkConfigurationProperties = new WSBackendLinkConfigurationProperties();
            String publish = backendLinkConfigurationProperties.getBackendPublishAddress();
            String url = "http://localhost:" + serverPort + webservicesPath + publish;

            JaxWsProxyFactoryBean jaxWsProxyFactoryBean
                    = new JaxWsProxyFactoryBean();
            jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendWebService.class);
            
            MTOMFeature mtom = new MTOMFeature();
            
            
    
            jaxWsProxyFactoryBean.setAddress(url);
            jaxWsProxyFactoryBean.setWsdlURL(url+ "?wsdl");
            jaxWsProxyFactoryBean.setProperties(new HashMap<>());
            jaxWsProxyFactoryBean.getProperties().put("ws-security.username", "bob");
            jaxWsProxyFactoryBean.getProperties().put("ws-security.password", "test");




    //        requestContext
            DomibusConnectorBackendWebService domibusConnectorBackendWebService = (DomibusConnectorBackendWebService)jaxWsProxyFactoryBean.create();

            DomibusConnectorMessageType msg = TransitionCreator.createMessage();
            DomibsConnectorAcknowledgementType response = domibusConnectorBackendWebService.submitMessage(msg);
            System.out.println("RESPONSE result: " + response.isResult());

    }
        
//        
//        
//        
//        
////        Properties props = loadProperties();
////        String port = props.getProperty("server.port");
////        String webservicesPath = props.getProperty("spring.webservices.path");
//        
////        getCxfBus();
//                
//
//        System.out.println("URL " + url);
//        URL wsdlURL = new URL(url + "?wsdl");
//        //QName serviceName = DomibusConnectorBackendWSService.DomibusConnectorBackendWebService;
//        QName serviceName = new QName("http://connector.domibus.eu/ws/backend/webservice", "DomibusConnectorBackendWebServiceService");
//        
//        
//        MTOMFeature mtom = new MTOMFeature();
//        //WSPolicyFeature wsPolicyFeature = new WSPolicyFeature(this.createPolicy());
////        LoggingFeature loggingFeature = new LoggingFeature();        
//        
//                
//        Service service = Service.create(wsdlURL, serviceName, mtom);
//        
//        
//        
//        DomibusConnectorBackendWebService backendWebServiceClient = service.getPort(DomibusConnectorBackendWebService.class);
//        
//        BindingProvider bindingProvider = ((BindingProvider)backendWebServiceClient);
//        
//        
//        
//        Map<String, Object> requestContext = bindingProvider.getRequestContext();
//        requestContext.put("ws-security.username", "bob");
//        requestContext.put("ws-security.password", "test");
//        
//        
//        //requestContext.put("ws-security.callback-handler", getClientPasswordCallback());
//        
//        Client client = ClientProxy.getClient(backendWebServiceClient);        
//        Endpoint cxfEndpoint = client.getEndpoint();
////        cxfEndpoint.getActiveFeatures().add(wsPolicyFeature);
//
//        
//        
//        cxfEndpoint.getOutInterceptors().add(new PolicyBasedWSS4JStaxOutInterceptor());
//        
//        //cxfEndpoint.getOutInterceptors().add(configureOutgoingUsernameTokenInterceptor());
//        //cxfEndpoint.putAll(configureWsProperties());
////        WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(configureWsProperties());
////        cxfEndpoint.getOutInterceptors().add(configureOutgoingUsernameTokenInterceptor());
//
//        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
//        DomibsConnectorAcknowledgementType response = backendWebServiceClient.submitMessage(msg);
////        System.out.println("RESPONSE result: " + response.isResult());
//    }

    

        
    Map<String, Object> configureWsProperties() {
        Map<String, Object> outProps = new HashMap<>();
        
//        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
//        
//        
//        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
//        // Specify our username
        outProps.put("ws-security.username", username);
//        // Password type : plain text
//        outProps.put("ws-security.callback-handler", WSConstants.PW_TEXT);
//        // for hashed password use:
//        //properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
//        // Callback used to retrieve password for given user.
        outProps.put("ws-security.password", password);
//        outProps.put(WSHandlerConstants.EXPAND_XOP_INCLUDE_FOR_SIGNATURE, true);
//        //WSS4JStaxOutInterceptor https://ws.apache.org/wss4j/streaming.html
        
        return outProps;
    }
     
     
    Policy createPolicy() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/wsdl/backend.policy.xml");    
            if (inputStream == null) {
                throw new IOException("Input Stream is null!");
            }
            PolicyBuilder policyBuilder = new PolicyBuilderImpl();
            Policy policy = policyBuilder.getPolicy(inputStream);
        
            return policy;
        } catch (IOException | ParserConfigurationException | SAXException e ) {
            throw new RuntimeException("Loading Policy failed", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebservicesPath() {
        return webservicesPath;
    }

    public void setWebservicesPath(String webservicesPath) {
        this.webservicesPath = webservicesPath;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
    
    
    
    
    
}

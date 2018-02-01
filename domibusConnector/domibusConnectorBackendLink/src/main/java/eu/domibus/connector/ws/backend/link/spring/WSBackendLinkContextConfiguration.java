
package eu.domibus.connector.ws.backend.link.spring;

import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.policy.IgnorablePolicyInterceptorProvider;
import org.apache.cxf.ws.policy.PolicyBuilder;
import org.apache.cxf.ws.policy.PolicyBuilderImpl;
import org.apache.cxf.ws.policy.PolicyInterceptorProviderRegistry;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.cxf.ws.security.policy.WSSecurityPolicyLoader;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JStaxInInterceptor;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JStaxOutInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
import org.apache.neethi.Policy;
import org.apache.wss4j.common.WSS4JConstants;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.xml.sax.SAXException;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@ImportResource({"classpath:services/DomibusConnectorBackendWebServiceConfig.xml"})
public class WSBackendLinkContextConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSBackendLinkContextConfiguration.class);
    
    @Autowired
    private Bus bus;

    @Autowired
    private DomibusConnectorBackendWebService backendWebService;
    
    @Autowired
    WSBackendLinkConfigurationProperties wsBackendLinkConfigurationProperties;
    
    
//    @Bean    
    public Endpoint endpoint() {       
        
        MTOMFeature mtom = new MTOMFeature();
        WSPolicyFeature wsPolicyFeature = new WSPolicyFeature(this.createPolicy());
        
        //bus.getProperties().put("ws-security.ut.validator", new MUsernameTokenValidator());
//        bus.setProperties(configureWsProperties());

        String publishAddress = wsBackendLinkConfigurationProperties.getBackendPublishAddress();
        
        WebServiceFeature[] webServiceFeatures = new WebServiceFeature[] {mtom, wsPolicyFeature};
                
        QName backendWebServiceName = DomibusConnectorBackendWSService.DomibusConnectorBackendWebService;               
        String wsdl = DomibusConnectorBackendWSService.WSDL_LOCATION.toString();
        
        EndpointImpl endpointImpl = new EndpointImpl(bus, backendWebService, null, wsdl, webServiceFeatures);        
                                
        endpointImpl.setServiceName(backendWebServiceName);
        endpointImpl.setProperties(configureWsProperties());
        
        LOGGER.info("publishing endpoint [{}] to [{}]", endpointImpl, publishAddress);
     
        endpointImpl.publish(publishAddress); 
        
        return endpointImpl;
    }
   
    Map<String, Object> configureWsProperties() {
        Map<String, Object> outProps = new HashMap<>();
        

//        outProps.put("ws-security.username", getUser());
        outProps.put("ws-security.callback-handler", passwordCallback());
//        outProps.put("ws-security.validate.token", true);
//        outProps.put("ws-security.ut.validator", new MyUsernameTokenValidator());
        
        return outProps;
    }
    
    
//    private Interceptor<? extends Message> configureIncomingUsernameTokenInterceptor() {
//        
//        Map<String,Object> props = new HashMap<>();
//        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
//        props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
//        props.put(WSHandlerConstants.PW_CALLBACK_REF, passwordCallback());
//
//        
//        //WSS4JStaxInInterceptor 
//        //WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(props);        
//        WSS4JStaxInInterceptor inInterceptor = new WSS4JStaxInInterceptor();
//        
//        
//        //TODO: set properties on interceptor
//        return inInterceptor;
//    }
//    
//    private  List<Interceptor<? extends Message>> configureOutgoingInterceptors() {
//        List<Interceptor<? extends Message>> outInterceptors = new ArrayList<>();
//        
//        
//        WSS4JStaxOutInterceptor outInterceptor = new WSS4JStaxOutInterceptor();
//        outInterceptor.setAllowMTOM(true);                  
//        outInterceptors.add(outInterceptor);
//        
//        return outInterceptors;
//    }
    

//    @Bean("gwSubmissionClient")
//    public DomibusConnectorSubmissionWS domibusConnectorSubmissionWSClient() {
//        DomibusConnectorSubmissionWSService domibusConnectorSubmissionWSService = new DomibusConnectorSubmissionWSService();
//                
//        MTOMFeature mtom = new MTOMFeature();
//        
//        DomibusConnectorSubmissionWS serviceClient = domibusConnectorSubmissionWSService.getPort(DomibusConnectorSubmissionWS.class, mtom);
//               
//        return serviceClient;
//    }                           
    
    
    @Bean("myPasswordCallback")
    public CallbackHandler passwordCallback() {
        return new BackendPasswordCallback();
    }

        
    private static class BackendPasswordCallback implements CallbackHandler {

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
            LOGGER.debug("BackendPasswordCallback called with user: [{}]", pc.getIdentifier());
            System.out.println("HANDLING PASSWORD CALLBACK....");
            if ("bob".equals(pc.getIdentifier()))  {
                pc.setPassword("test"); //password is compared later with client
            }            
        }        
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
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Loading Policy failed", e);
        }
    }
    
}

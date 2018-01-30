
package eu.domibus.connector.ws.backend.link.spring;

import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.io.IOException;
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
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JStaxInInterceptor;
import org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JStaxOutInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
import org.apache.wss4j.common.WSS4JConstants;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
public class WSBackendLinkContextConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSBackendLinkContextConfiguration.class);
    
    @Autowired
    private Bus bus;

    @Autowired
    private DomibusConnectorBackendWebService backendWebService;
    
    @Autowired
    WSBackendLinkConfigurationProperties wsBackendLinkConfigurationProperties;
    
    
    @Bean    
    public Endpoint endpoint() {           
        
        
        String publishAddress = wsBackendLinkConfigurationProperties.getBackendPublishAddress();
        MTOMFeature mtom = new MTOMFeature();
        WebServiceFeature[] webServiceFeatures = new WebServiceFeature[] {mtom};
        
        EndpointImpl endpointImpl = new EndpointImpl(bus, backendWebService, null, webServiceFeatures);        
                
        QName backendWebServiceName = DomibusConnectorBackendWSService.DomibusConnectorBackendWebService;
        endpointImpl.setServiceName(backendWebServiceName);
        
        
        SOAPBinding binding = (SOAPBinding)endpointImpl.getBinding();
        binding.setMTOMEnabled(true);
        
        
        LOGGER.info("publishing endpoint [{}] to [{}]", endpointImpl, publishAddress);
        
        //endpointImpl.getInInterceptors().add(configureIncomingUsernameTokenInterceptor());
        
        
        endpointImpl.getInInterceptors().add(configureIncomingUsernameTokenInterceptor());
//        endpointImpl.getInInterceptors().add(new LoggingOutInterceptor());
//        endpointImpl.getInInterceptors().add(new MyInterceptor());
//        
        endpointImpl.publish(publishAddress); 
        
        //org.apache.cxf.interceptor.LoggingOutInterceptor;
        
        return endpointImpl;
    }
   
    
    
    private Interceptor<? extends Message> configureIncomingUsernameTokenInterceptor() {
        
        Map<String,Object> props = new HashMap<>();
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        props.put(WSHandlerConstants.PW_CALLBACK_REF, passwordCallback());

        
        
        WSS4JStaxInInterceptor inInterceptor = new WSS4JStaxInInterceptor(props);        
        
        //TODO: set properties on interceptor
        return inInterceptor;
    }
    
    private  List<Interceptor<? extends Message>> configureOutgoingInterceptors() {
        List<Interceptor<? extends Message>> outInterceptors = new ArrayList<>();
        
        
        WSS4JStaxOutInterceptor outInterceptor = new WSS4JStaxOutInterceptor();
        outInterceptor.setAllowMTOM(true);                  
        outInterceptors.add(outInterceptor);
        
        return outInterceptors;
    }
    

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
    
    
    @Bean
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
    
    private static class MyInterceptor extends AbstractPhaseInterceptor<Message> {

        public MyInterceptor() {
            super(Phase.PRE_STREAM);
        }

        @Override
        public void handleMessage(Message message) throws Fault {
            System.out.println("HANDLE MESSAGE %n%n##################################");
        }

    }
    
}

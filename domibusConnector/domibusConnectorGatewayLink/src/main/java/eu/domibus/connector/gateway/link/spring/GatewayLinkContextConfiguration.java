
package eu.domibus.connector.gateway.link.spring;

import eu.domibus.connector.ws.delivery.service.DomibusConnectorDeliveryWS;
import eu.domibus.connector.ws.delivery.service.DomibusConnectorDeliveryWSService;
import eu.domibus.connector.ws.submission.service.DomibusConnectorSubmissionWS;
import eu.domibus.connector.ws.submission.service.DomibusConnectorSubmissionWSService;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JStaxOutInterceptor;
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
public class GatewayLinkContextConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(GatewayLinkContextConfiguration.class);
    
    @Autowired
    private Bus bus;

    @Autowired
    private DomibusConnectorDeliveryWS deliveryMessageToCxfServerPort;
    
    @Autowired
    private GatewayLinkWsServiceProperties gatewayLinkPublishedServiceProperties;
    
    @Bean
    public Endpoint endpoint() {               
        EndpointImpl endpointImpl = new EndpointImpl(bus, deliveryMessageToCxfServerPort);        
                
        QName SERVICE_NAME = DomibusConnectorDeliveryWSService.DomibusConnectorDeliveryWebService; 
        endpointImpl.setServiceName(SERVICE_NAME);
        
        SOAPBinding binding = (SOAPBinding)endpointImpl.getBinding();
        binding.setMTOMEnabled(true);
                        
        String publishAddress = gatewayLinkPublishedServiceProperties.getPublishAddress();
        LOGGER.debug("publishing endpoint [{}] to [{}]", endpointImpl, publishAddress);
        
        endpointImpl.publish(publishAddress); 
                
        //TODO: implement security policy here:
//        WSS4JStaxOutInterceptor outInterceptor = new WSS4JStaxOutInterceptor();
//        outInterceptor.setAllowMTOM(true);                
//        WSS4JStaxInInterceptor inInterceptor = new WSS4JStaxInInterceptor();        
//        endpointImpl.setInInterceptors(interceptors);
//        endpointImpl.setOutInterceptors(interceptors);
                                
        return endpointImpl;
    }
    

    @Bean("gwSubmissionClient")
    public DomibusConnectorSubmissionWS domibusConnectorSubmissionWSClient() {
        DomibusConnectorSubmissionWSService domibusConnectorSubmissionWSService = new DomibusConnectorSubmissionWSService();
                
        MTOMFeature mtom = new MTOMFeature();
        
        DomibusConnectorSubmissionWS serviceClient = domibusConnectorSubmissionWSService.getPort(DomibusConnectorSubmissionWS.class, mtom);
               
        return serviceClient;
                                
    }
}


package eu.domibus.connector.gateway.link.spring;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWSService;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWSService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;


/**
 * Configures the GW-Link to use webservices
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@Profile("gw-ws-link")
//@ImportResource("classpath:/spring/context/DomibusConnectorGatewayLinkContext.xml")
public class GatewayLinkContextConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(GatewayLinkContextConfiguration.class);
    
    @Autowired
    private DomibusConnectorGatewayDeliveryWebService deliveryMessageToCxfServerPort;
    
    @Autowired
    private GatewayLinkWsServiceProperties gatewayLinkPublishedServiceProperties;
    
    @Bean("gwBus")
    public SpringBus springBusGwLinkWs() {
        SpringBus bus = new SpringBus();
        bus.setId("gwBus");
        return bus;
    }
    
    @Bean
    public ServletRegistrationBean gwLinkWsServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        cxfServlet.setBus(springBusGwLinkWs());
 
        ServletRegistrationBean servletBean = new ServletRegistrationBean(cxfServlet, "/services/gw/*");
        servletBean.setName("gwLinkWsServlet");
        return servletBean;
    }
    
    @Bean("messageDeliveryEndpoint")
    public Endpoint endpoint() {               
        EndpointImpl endpointImpl = new EndpointImpl(springBusGwLinkWs(), deliveryMessageToCxfServerPort);        
                
        QName SERVICE_NAME = DomibusConnectorGatewayDeliveryWSService.DomibusConnectorDeliveryWebService; 
        endpointImpl.setServiceName(SERVICE_NAME);
        
        SOAPBinding binding = (SOAPBinding)endpointImpl.getBinding();
        binding.setMTOMEnabled(true);
                                
        String publishAddress = gatewayLinkPublishedServiceProperties.getPublishAddress();
        endpointImpl.setAddress(publishAddress);
        endpointImpl.publish(publishAddress);
                
        System.out.println("PUBLISHING " + endpointImpl);
        
        LOGGER.info("publishing endpoint [{}] to [{}]", endpointImpl, publishAddress);

                                
        return endpointImpl;
    }
    

    @Bean("gwSubmissionClient")
    public DomibusConnectorGatewaySubmissionWebService domibusConnectorSubmissionWSClient() {
        DomibusConnectorGatewaySubmissionWSService domibusConnectorSubmissionWSService = new DomibusConnectorGatewaySubmissionWSService();
                
        MTOMFeature mtom = new MTOMFeature();
        
        DomibusConnectorGatewaySubmissionWebService serviceClient = domibusConnectorSubmissionWSService.getPort(DomibusConnectorGatewaySubmissionWebService.class, mtom);
               
        return serviceClient;
                                
    }
}

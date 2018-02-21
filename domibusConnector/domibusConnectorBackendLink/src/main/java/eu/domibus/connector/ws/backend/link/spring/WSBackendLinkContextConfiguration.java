
package eu.domibus.connector.ws.backend.link.spring;

import eu.domibus.connector.backend.ws.helper.WsPolicyLoader;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
//@Import(BackendPersistenceConfig.class)
//@ImportResource({"classpath:services/DomibusConnectorBackendWebServiceConfig.xml"})
@EnableJms
public class WSBackendLinkContextConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSBackendLinkContextConfiguration.class);
    
    @Autowired
    ApplicationContext applicationContext;
    
    @Autowired
    @Qualifier("connectorBackendImpl")
    DomibusConnectorBackendWebService domibusConnectorBackendImpl;
    
    @Autowired
    WsPolicyLoader wsPolicyLoader;
    
    @Bean
    public SpringBus springBusBackendLinkWs() {
        SpringBus bus = new SpringBus();
        bus.setId("backend");
        return bus;
    }

    @Bean
    public ServletRegistrationBean backendLinkWsServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        cxfServlet.setBus(springBusBackendLinkWs());

        ServletRegistrationBean servletBean = new ServletRegistrationBean(cxfServlet, "/services/backend/*");
        servletBean.setName("backendLinkWsServlet");
        return servletBean;
    }


    
    @Bean
    public Endpoint connectorBackendWebService() {
        Bus bus = springBusBackendLinkWs();
                
        EndpointImpl endpoint = new EndpointImpl(bus, domibusConnectorBackendImpl);

        
        Map<String, Object> props = new HashMap<>();
        props.put("security.signature.properties", "eu/domibus/connector/ws/backend/link/ws/decrypt.properties");
        props.put("security.signature.username", "connector");
        props.put("security.encryption.properties", "eu/domibus/connector/ws/backend/link/ws/decrypt.properties");
        props.put("mtom-enabled", "true");
        props.put("security.store.bytes.in.attachment", "true");
        
        endpoint.setProperties(props);
        
        List<Feature> features = new ArrayList<>();
        WSPolicyFeature wsPolicyFeature = wsPolicyLoader.loadPolicyFeature();
        LOGGER.debug("adding WSPolicy feature [{}] to endpoint [{}]", wsPolicyFeature, endpoint);
        features.add(wsPolicyFeature);        
        endpoint.setFeatures(features);
        
                
        endpoint.setAddress("backend");
        endpoint.publish("backend");
        
        LOGGER.debug("publish endpoint [{}]", endpoint);
        System.out.println("PUBLISHING BACKEND LINK WS");
        
        return endpoint;
    }
 
    
    
    
}

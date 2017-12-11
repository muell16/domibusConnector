/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.gwc.spring;

import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.gwc.DomibusConnectorGatewayWebserviceClientImpl;
import eu.domibus.connector.gwc.helper.DownloadMessageHelper;
import eu.domibus.connector.gwc.helper.SendMessageHelper;
import eu.domibus.connector.gwc.util.CommonMessageHelper;
import eu.ecodex.discovery.DiscoveryClient;
import eu.ecodex.discovery.DiscoveryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * WebContext to initialize the default GW Client
 *  default GW Client is a connection to a domibusGateway
 * @author spindlest
 */
@Configuration
@ImportResource({
    "classpath:/spring/context/DomibusConnectorGatewayWebserviceClientContext.xml", //load wsdl client init 
})
public class DomibusConnectorGatewayWebserviceClientContext {
    
    
    
    //if there is no bean of type DomibusConnectorGatewayWebserviceClient.class
    //in spring context
    //spring is creating this bean
    @ConditionalOnMissingBean(DomibusConnectorGatewayWebserviceClient.class)      
    @Bean
    public DomibusConnectorGatewayWebserviceClientImpl domibusConnectorGatewayWebserviceClientImpl() {
        return new DomibusConnectorGatewayWebserviceClientImpl();
    }
    
    @Bean
    public SendMessageHelper sendMessageHelper() {
        return new SendMessageHelper();
    }
    
    @Bean
    public DownloadMessageHelper downloadMessageHelper() {
        return new DownloadMessageHelper();
    }
    
    @Value("${dynamic.discovery.sml.resolver.address}")    
    private String resolverAddress;
    
    @Bean
    public DiscoveryClient discoveryClient() throws DiscoveryException {
        return new DiscoveryClient(resolverAddress);
    }
    
    @Bean
    public CommonMessageHelper commonMessageHelper() {
        return new CommonMessageHelper();        
    }
    
}

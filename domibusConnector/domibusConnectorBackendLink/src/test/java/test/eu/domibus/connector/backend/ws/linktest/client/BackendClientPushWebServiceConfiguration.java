
package test.eu.domibus.connector.backend.ws.linktest.client;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWSService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */



@Configuration
@Profile("ws-backendclient-server")
@ImportResource("classpath:/test/testclient_pushdelivery.xml")
public class BackendClientPushWebServiceConfiguration {

    public static final String PUSH_DELIVERED_MESSAGES_LIST_BEAN_NAME = "deliveredMessagesListBean";

    @Bean
    @ConditionalOnMissingBean
    public static PropertySourcesPlaceholderConfigurer
            propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean(PUSH_DELIVERED_MESSAGES_LIST_BEAN_NAME)
    public LinkedBlockingQueue<DomibusConnectorMessageType> domibusConnectorMessageTypeList() {
        return new LinkedBlockingQueue<>();
    }
            
    
    @Bean("backendDeliveryWebService")
    public DomibusConnectorBackendDeliveryWebService domibusConnectorBackendDeliveryWebService() {
        return new DummyDomibusConnectorBackendDeliveryWebServiceImpl();
    }
    
    @Bean("backendDeliveryWebServiceName")
    public QName serviceName() {
        return DomibusConnectorBackendDeliveryWSService.DomibusConnectorBackendDeliveryWebService;
    }

    private static class DummyDomibusConnectorBackendDeliveryWebServiceImpl implements DomibusConnectorBackendDeliveryWebService {
        
        private final static Logger LOGGER = LoggerFactory.getLogger(DummyDomibusConnectorBackendDeliveryWebServiceImpl.class);

        @Resource
        WebServiceContext webServiceContext;
        
        @Autowired
        LinkedBlockingQueue<DomibusConnectorMessageType> domibusConnectorMessageTypeList;
        
        @Value("${ws.backendclient.name}")
        String backendClientName;
                
        @Override
        public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
            LOGGER.debug("deliverMessage [{}]", deliverMessageRequest);
            String name = webServiceContext.getUserPrincipal().getName();
            LOGGER.debug("message client with name: [{}]", name);
            
            DomibsConnectorAcknowledgementType ack = new DomibsConnectorAcknowledgementType();
            domibusConnectorMessageTypeList.add(deliverMessageRequest);
            ack.setResult(true);
            ack.setMessageId(backendClientName + "msg" + UUID.randomUUID().toString());
            return ack;
        }

    }

    
    
}

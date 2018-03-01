
package eu.domibus.connector.backend.ws.link.spring;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.ws.link.impl.PushMessageToBackendClient;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.backend.ws.link.impl.MessageToBackendClientWaitQueue;
import eu.domibus.connector.backend.ws.link.impl.ToBackendClientJmsBasedWaitQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * Configure the backend link web services
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@ImportResource({"classpath:services/DomibusConnectorBackendWebServiceConfig.xml"})
@EnableJms
@PropertySource("classpath:/eu/domibus/connector/backend/config/backend-default-config.properties")
public class WSBackendLinkContextConfiguration {

//    @Bean
//    public MessageToBackendClientWaitQueue toBackendClientJmsBasedWaitQueue() {
//        ToBackendClientJmsBasedWaitQueue toBackendClientJmsBasedWaitQueue = new ToBackendClientJmsBasedWaitQueue();
//        return toBackendClientJmsBasedWaitQueue;
//    }
//
//
//    @Autowired
//    BackendClientInfoPersistenceService backendClientInfoPersistenceService;
//
//    @Autowired(required = false)
//    PushMessageToBackendClient pushBackend;
//
//    /*
//     * creates a integrationFlow gateway which implements DomibusConnectorBackendDeliveryService
//     * so controller can deliverMessages to backend into the flow
//     */
//    @Bean
//    public GatewayProxyFactoryBean receiveMessageFromController() {
//        GatewayProxyFactoryBean gatewayProxyFactoryBean = new GatewayProxyFactoryBean();
//        gatewayProxyFactoryBean.setServiceInterface(DomibusConnectorBackendDeliveryService.class);
//        gatewayProxyFactoryBean.setDefaultRequestChannelName("backend.fromconnector");
//        return gatewayProxyFactoryBean;
//    }


}

package eu.domibus.connector.ws.backend.link.spring;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.ws.backend.link.impl.MessageToBackendClientWaitQueue;
import eu.domibus.connector.ws.backend.link.impl.ToBackendClientJmsBasedWaitQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.jms.annotation.EnableJms;

/**
 * Configure the backend link web services
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@ImportResource({"classpath:services/DomibusConnectorBackendWebServiceConfig.xml"})
@EnableJms
@PropertySource("classpath:/eu/domibus/connector/backend/config/backend-default-config.properties")
@EnableIntegration
public class WSBackendLinkContextConfiguration {

    @Bean
    public MessageToBackendClientWaitQueue toBackendClientJmsBasedWaitQueue() {
        ToBackendClientJmsBasedWaitQueue toBackendClientJmsBasedWaitQueue = new ToBackendClientJmsBasedWaitQueue();

        return toBackendClientJmsBasedWaitQueue;
    }

    @Autowired
    BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    @Bean
    public GatewayProxyFactoryBean gw() {
        GatewayProxyFactoryBean gatewayProxyFactoryBean = new GatewayProxyFactoryBean();
        gatewayProxyFactoryBean.setServiceInterface(DomibusConnectorBackendDeliveryService.class);
        gatewayProxyFactoryBean.setDefaultRequestChannelName("backend.fromconnector");
        return gatewayProxyFactoryBean;
    }



    @Bean
    public IntegrationFlow fromConnectorToBackendFlow() {
        IntegrationFlows.from("backend.fromconnector")
                .transform( (DomibusConnectorMessage msg) -> {
                    DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage();
                    backendMessage.setDomibusConnectorMessage(msg);
                    return backendMessage;
                })
                .transform( (DomibusConnectorBackendMessage backendMessage) -> {
                    DomibusConnectorBackendClientInfo backendClientInfoByServiceName = backendClientInfoPersistenceService.getBackendClientInfoByServiceName(backendMessage.getDomibusConnectorMessage().getMessageDetails().getService());
                    backendMessage.setBackendClientInfo(backendClientInfoByServiceName);
                    return backendMessage;
                })
                .channel("backend.jmsqueue")
                .get();



    }



//    @Primary
//    @Bean
//    @ServiceActivator(inputChannel = "backend.fromconnector")
//    public DomibusConnectorBackendDeliveryService domibusConnectorBackendDeliveryService() {
//
//    }



}

package eu.domibus.connector.gateway.link.jms.spring;


import eu.domibus.connector.gateway.link.jms.impl.GatewayLinkAsyncDeliveryService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorService;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.xml.namespace.QName;

@Configuration
@EnableJms
@Profile("gwlink-jms")
public class GatewayLinkJmsConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayLinkJmsConfiguration.class);

    @Autowired
    private JmsConnectionFactoryConfiguration factoryConfiguration;

    @Autowired
    private GatewayLinkAsyncDeliveryService deliveryServiceImplementor;

    @Autowired
    private GatewayLinkJmsProperties gatewayLinkJmsProperties;

    @PostConstruct  //TODO: maybe replace with return Server
    public void newJmsConfiguration() {

//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//        connectionFactory.setBrokerURL("tcp://localhost:61616");
        ConnectionFactory connectionFactory = factoryConfiguration.getJmsConnectionFactory();

        String toConnectorMessageQueue = gatewayLinkJmsProperties.getToConnectorMessageQueue();
        LOGGER.debug("Setting deliveryService to listen on queue: [{}]", toConnectorMessageQueue);

        JMSConfiguration jmsConfig = new JMSConfiguration();
        jmsConfig.setTargetDestination(toConnectorMessageQueue);
        jmsConfig.setConnectionFactory(connectionFactory);
        JMSConfigFeature jmsFeature = new JMSConfigFeature();
        jmsFeature.setJmsConfig(jmsConfig);

        JaxWsServerFactoryBean proxyFactory = new JaxWsServerFactoryBean();
        proxyFactory.setServiceClass(DomibusConnectorAsyncDeliverToConnectorService.class);
//        proxyFactory.setServiceName(new QName("deliverMessageRequest"));
//        proxyFactory.setEndpointName(new QName("deliverMessageRequest"));
        proxyFactory.setAddress("jms://");
        proxyFactory.getFeatures().add(jmsFeature);
        proxyFactory.setServiceBean(deliveryServiceImplementor);

        Server server = proxyFactory.create();
        server.start();
    }




}

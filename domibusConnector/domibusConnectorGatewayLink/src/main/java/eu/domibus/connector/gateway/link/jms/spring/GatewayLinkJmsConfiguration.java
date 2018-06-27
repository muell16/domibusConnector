package eu.domibus.connector.gateway.link.jms.spring;


import eu.domibus.connector.gateway.link.jms.impl.GatewayLinkAsyncDeliveryService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorService;
import eu.domibus.connector.link.common.WsPolicyLoader;
import org.apache.cxf.binding.BindingConfiguration;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.apache.cxf.transport.jms.JMSConstants;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Properties;

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

    @Bean  //TODO: maybe replace with return Server
    public Server newJmsConfiguration() {

        ConnectionFactory connectionFactory = factoryConfiguration.getJmsConnectionFactory();

        String toConnectorMessageQueue = gatewayLinkJmsProperties.getToConnectorMessageQueue();
        LOGGER.debug("Setting deliveryService to listen on queue: [{}]", toConnectorMessageQueue);

        JMSConfiguration jmsConfig = new JMSConfiguration();
        jmsConfig.setTargetDestination(toConnectorMessageQueue);
        jmsConfig.setConnectionFactory(connectionFactory);
        jmsConfig.setMessageType(JMSConstants.BINARY_MESSAGE_TYPE);
        JMSConfigFeature jmsFeature = new JMSConfigFeature();
        jmsFeature.setJmsConfig(jmsConfig);

        JaxWsServerFactoryBean proxyFactory = new JaxWsServerFactoryBean();
        proxyFactory.setServiceClass(DomibusConnectorAsyncDeliverToConnectorService.class);
//        proxyFactory.setServiceName(new QName("deliverMessageRequest"));
//        proxyFactory.setEndpointName(new QName("deliverMessageRequest"));
        proxyFactory.setAddress("jms://");
        proxyFactory.getFeatures().add(jmsFeature);
        proxyFactory.getFeatures().add(loadWsPolicyFeature());
        proxyFactory.setServiceBean(deliveryServiceImplementor);

        proxyFactory.setProperties(loadSecurityProperties());

        Server server = proxyFactory.create();
        return server;
//        server.start();
    }

    private Feature loadWsPolicyFeature() {
        WsPolicyLoader policyLoader = new WsPolicyLoader(this.gatewayLinkJmsProperties.getSecurityPolicy());
        return policyLoader.loadPolicyFeature();
    }


    private HashMap<String, Object> loadSecurityProperties() {
        HashMap<String, Object> map = new HashMap<>();

        //TODO: load Configured Properties
        Properties p = new Properties();

        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", "jks");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", "12345");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", "classpath:/keystores/gwlink-keystore.jks");

        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", "gwlink");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password", "12345");

        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", "12345");
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", "classpath:/keystores/gwlink-keystore.jks");

        map.put("security.signature.properties", p);
        map.put("security.signature.username", "gwlink"); //alias for signature (private key)

        map.put("security.encryption.properties", p);
        map.put("security.encrpytion.username", "testgw"); //alias for encryption (public key)

        map.put("security.store.bytes.in.attachment", true);
        map.put("security.enable.streaming", true);

        return map;
    }


}

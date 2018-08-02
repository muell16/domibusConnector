package eu.domibus.connector.gateway.link.jms.spring;


import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;
import eu.domibus.connector.gateway.link.jms.impl.HandleFromGwToConnectorDeliveredMessage;
import eu.domibus.connector.gateway.link.jms.impl.HandleFromGwToConnectorSentResponse;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorReceiveResponseService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncSubmitToGatewayReceiveResponseService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncSubmitToGatewayService;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
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

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Properties;

import static eu.domibus.connector.tools.logging.LoggingMarker.CONFIG;

@Configuration
@Profile("gwlink-jms")
public class GatewayLinkJmsConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayLinkJmsConfiguration.class);

    @Autowired
    private JmsConnectionFactoryConfiguration factoryConfiguration;

    @Autowired
    private HandleFromGwToConnectorDeliveredMessage deliveryServiceImplementor;

    @Autowired
    private HandleFromGwToConnectorSentResponse handleFromGwToConnectorSentResponse;

    @Autowired
    private GatewayLinkJmsProperties gatewayLinkJmsProperties;

    /**
     * Configure endpoint to listen on toConnectorResponseQueue
     * for responses from GW
     * @return the Server endpoint
     */
    @Bean
    public Server asnycSubmitToGatewayReceiveResponseJmsEndpoint() {
        String toConnectorResponseQueue = gatewayLinkJmsProperties.getToConnectorResponseQueue();
        LOGGER.debug(CONFIG, "Setting handleFromGwToConnectorSentResponse to listen on queue: [{}]", toConnectorResponseQueue);

        return connectJmsEndpointQueueWithBean(toConnectorResponseQueue,
                DomibusConnectorAsyncSubmitToGatewayReceiveResponseService.class,
                handleFromGwToConnectorSentResponse);
    }

    /**
     * Configure endpoint to listen on toConnectorMessageQueue
     * for message from GW
     * @return the Server endpoint
     */
    @Bean
    public Server asyncDeliverToConnectorJmsEndpoint() {
        String toConnectorMessageQueue = gatewayLinkJmsProperties.getToConnectorMessageQueue();
        LOGGER.debug(CONFIG, "Setting handleFromGwToConnectorDeliveredMessage to listen on queue: [{}]", toConnectorMessageQueue);
        return connectJmsEndpointQueueWithBean(toConnectorMessageQueue,
                DomibusConnectorAsyncDeliverToConnectorService.class,
                deliveryServiceImplementor);
    }

    /**
     * Configure cxf client for sending messages over jms queue
     * to GW
     * @return - the cxf client
     */
    @Bean
    public DomibusConnectorAsyncSubmitToGatewayService submitToGatewayService() {
        String submitToGwQueueName = gatewayLinkJmsProperties.getToGatewayMessageQueue();
        LOGGER.debug(CONFIG, "Configuring submit to Gateway JMS Queue to [{}]", submitToGwQueueName);
        return createClient(submitToGwQueueName, DomibusConnectorAsyncSubmitToGatewayService.class);
    }

    /**
     * Configure cxf client for sending a message reponse over jms queue
     * back to GW
     * @return - the cxf client
     */
    @Bean
    public DomibusConnectorAsyncDeliverToConnectorReceiveResponseService sendResponseService() {
        String sendResponseToGwQueueName = gatewayLinkJmsProperties.getToGatewayResponseQueue();
        LOGGER.debug(CONFIG, "Configuring response to Gateway JMS Queue to [{}]", sendResponseToGwQueueName);
        return createClient(sendResponseToGwQueueName, DomibusConnectorAsyncDeliverToConnectorReceiveResponseService.class);
    }

    private Server connectJmsEndpointQueueWithBean(String queueName, Class clazz, Object bean) {
        JaxWsServerFactoryBean proxyFactory = new JaxWsServerFactoryBean();
        proxyFactory.setServiceClass(clazz);
        proxyFactory.setAddress("jms://");
        proxyFactory.getFeatures().add(loadJmsFeature(queueName));
        proxyFactory.getFeatures().add(loadWsPolicyFeature());
        proxyFactory.setServiceBean(bean);

        proxyFactory.setProperties(loadSecurityProperties());

        Server server = proxyFactory.create();
        return server;
    }

    private <T> T createClient(String queueName, Class<T> serviceClazz) {


        JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
        proxyFactory.setServiceClass(serviceClazz);
        proxyFactory.setAddress("jms://");
        proxyFactory.getFeatures().add(loadJmsFeature(queueName));
        proxyFactory.getFeatures().add(loadWsPolicyFeature());
        proxyFactory.setProperties(loadSecurityProperties());


        T client = (T) proxyFactory.create();
        return client;
    }


    private JMSConfigFeature loadJmsFeature(String queueName) {
        ConnectionFactory connectionFactory = factoryConfiguration.getJmsConnectionFactory();
        JMSConfiguration jmsConfig = new JMSConfiguration();
        jmsConfig.setTargetDestination(queueName);
        jmsConfig.setConnectionFactory(connectionFactory);
        jmsConfig.setMessageType(JMSConstants.BINARY_MESSAGE_TYPE);
        JMSConfigFeature jmsFeature = new JMSConfigFeature();
        jmsFeature.setJmsConfig(jmsConfig);
        return jmsFeature;
    }

    private Feature loadWsPolicyFeature() {
        WsPolicyLoader policyLoader = new WsPolicyLoader(this.gatewayLinkJmsProperties.getSecurityPolicy());
        return policyLoader.loadPolicyFeature();
    }


    private HashMap<String, Object> loadSecurityProperties() {
        HashMap<String, Object> map = new HashMap<>();

        //TODO: load Configuration Properties
        Properties p = new Properties();

        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", "jks");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", "12345");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", "classpath:/keystores/gwlink-keystore.jks");

        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", "gwlink");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password", "12345");

        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", "12345");
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", "classpath:/keystores/gwlink-keystore.jks");

        LOGGER.trace(CONFIG, "Setting security.signature.properties to [{}]", p);
        map.put("security.signature.properties", p);
        String sigUsername = "gwlink";
        LOGGER.trace(CONFIG, "Setting security.signature.username to [{}]", sigUsername);
        map.put("security.signature.username", sigUsername); //alias for signature (private key)

        LOGGER.trace(CONFIG, "Setting security.encryption.properties to [{}]", p);
        map.put("security.encryption.properties", p);

        String encUsername = "testgw";
        LOGGER.trace(CONFIG, "Setting security.encrpytion.username to [{}]", encUsername);
        map.put("security.encrpytion.username", encUsername); //alias for encryption (public key)

        map.put("security.store.bytes.in.attachment", true);
        map.put("security.enable.streaming", true);

        return map;
    }


}

package eu.domibus.connector.gateway.link.jms.spring;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.jms.ConnectionFactory;

@Configuration
@Profile("gwlink-jms")
public class JmsConnectionFactoryConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsConnectionFactoryConfiguration.class);

    public static final String GATEWAY_LINK_JMS_CONNECTION_FACTORY_BEAN_NAME = "";

    @Autowired
    private GatewayLinkJmsProperties gatewayLinkJmsProperties;

    //May collide with the embedded ConnectionFactory!
    @Bean
    public ConnectionFactory getJmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        LOGGER.debug("Setting broker URL for connection Factory to: [{}]", gatewayLinkJmsProperties.getBrokerUrl());
        connectionFactory.setBrokerURL(gatewayLinkJmsProperties.getBrokerUrl());
        return connectionFactory;
    }


}

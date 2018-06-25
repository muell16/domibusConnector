package eu.domibus.connector.gateway.link.jms.helper;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.gateway.link.jms.impl.GatewayLinkAsyncDeliveryService;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryAsyncService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.jms.ConnectionFactoryFeature;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.jms.ConnectionFactory;
import javax.xml.namespace.QName;
import java.util.ArrayList;

public class TestGatewayToDeliveryServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestGatewayToDeliveryServiceClient.class);
    public static final String TO_CONNECTOR_MESSAGE_QUEUE_NAME = "eu.domibus.connector.external.gatewayToControllerQueue";

    private final ConnectionFactory connectionFactory;

    public TestGatewayToDeliveryServiceClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void deliverMessge(DomibusConnectorMessageType message ) {

        JMSConfiguration jmsConfig = new JMSConfiguration();
        jmsConfig.setTargetDestination(TO_CONNECTOR_MESSAGE_QUEUE_NAME);
        jmsConfig.setConnectionFactory(connectionFactory);
        JMSConfigFeature jmsFeature = new JMSConfigFeature();
        jmsFeature.setJmsConfig(jmsConfig);


        JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
        proxyFactory.setServiceClass(DomibusConnectorGatewayDeliveryAsyncService.class);
        proxyFactory.setServiceName(new QName("deliverMessageRequest"));
        proxyFactory.setEndpointName(new QName("deliverMessageRequest"));
        proxyFactory.setAddress("jms://");
        proxyFactory.getFeatures().add(jmsFeature);

        DomibusConnectorGatewayDeliveryAsyncService client = (DomibusConnectorGatewayDeliveryAsyncService) proxyFactory.create();

        client.deliverMessage(message);

    }


}

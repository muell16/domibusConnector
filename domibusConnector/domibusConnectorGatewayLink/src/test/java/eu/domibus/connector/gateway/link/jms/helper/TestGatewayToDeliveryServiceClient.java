package eu.domibus.connector.gateway.link.jms.helper;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageResponseType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorService;
import eu.domibus.connector.link.common.WsPolicyLoader;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.jms.JMSConfigFeature;
import org.apache.cxf.transport.jms.JMSConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.jms.ConnectionFactory;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Properties;

public class TestGatewayToDeliveryServiceClient implements DomibusConnectorAsyncDeliverToConnectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestGatewayToDeliveryServiceClient.class);
    public static final String TO_CONNECTOR_MESSAGE_QUEUE_NAME = "eu.domibus.connector.external.gatewayToControllerQueue";

    private final ConnectionFactory connectionFactory;

    public TestGatewayToDeliveryServiceClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public void deliverMessage(DomibusConnectorMessageType message ) {

        DomibusConnectorAsyncDeliverToConnectorService client = this.createClient();
        client.deliverMessage(message);

    }




    @Override
    public void deliverResponse(DomibusConnectorMessageResponseType response) {

        DomibusConnectorAsyncDeliverToConnectorService client = this.createClient();
        client.deliverResponse(response);
    }

    private DomibusConnectorAsyncDeliverToConnectorService createClient() {
        JMSConfiguration jmsConfig = new JMSConfiguration();
        jmsConfig.setTargetDestination(TO_CONNECTOR_MESSAGE_QUEUE_NAME);
        jmsConfig.setConnectionFactory(connectionFactory);
        JMSConfigFeature jmsFeature = new JMSConfigFeature();
        jmsFeature.setJmsConfig(jmsConfig);

        JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
        proxyFactory.setServiceClass(DomibusConnectorAsyncDeliverToConnectorService.class);
//        proxyFactory.setServiceName(new QName("deliverMessageRequest"));
//        proxyFactory.setEndpointName(new QName("deliverMessageRequest"));
        proxyFactory.setAddress("jms://");
        proxyFactory.getFeatures().add(jmsFeature);
        proxyFactory.getFeatures().add(wsPolicyFeature());


        proxyFactory.setProperties(generateSecurityProperties());


        DomibusConnectorAsyncDeliverToConnectorService client = (DomibusConnectorAsyncDeliverToConnectorService) proxyFactory.create();
        return client;
    }

    private Feature wsPolicyFeature() {
        WsPolicyLoader policyLoader = new WsPolicyLoader(new ClassPathResource("/wsdl/backend.policy.xml"));
        return policyLoader.loadPolicyFeature();
    }

    private HashMap<String, Object> generateSecurityProperties() {
        HashMap<String, Object> map = new HashMap<>();

        //TODO: load Properties
        Properties p = new Properties();

        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", "jks");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", "12345");


        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", "classpath:/keystores/gwlink-keystore.jks");

        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", "gwlink");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password", "12345");


//        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", this.getTrust().getStore().getPassword());
//        try {
//            LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.truststore.file={}]", this.getTrust().getStore().getPath());
//            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", this.getTrust().getStore().getPathUrlAsString());
//        } catch (Exception e) {
//            LOGGER.info("Trust Store Property: [" + PREFIX + ".trust.store.path]" +
//                            "\n cannot be processed. Using the configured key store [{}] as trust store",
//                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));
//
//            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));
//            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", p.getProperty("org.apache.wss4j.crypto.merlin.keystore.password"));
//        }
//        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(this.getTrust().isLoadCaCerts()));



        map.put("security.signature.properties", p);
        map.put("security.encryption.properties", p);
        map.put("mtom-enabled", true);
        map.put("security.store.bytes.in.attachment", true);
        map.put("security.enable.streaming", true);


        return map;
    }


}

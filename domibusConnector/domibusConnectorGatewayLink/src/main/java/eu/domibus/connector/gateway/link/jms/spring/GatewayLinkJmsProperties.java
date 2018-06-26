package eu.domibus.connector.gateway.link.jms.spring;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component //("GatewayLinkJmsServiceProperties")
@Profile("gwlink-jms")
@PropertySource("classpath:/eu/domibus/connector/gateway/link/jms/spring/gatewaylinkjms-default.properties")
@ConfigurationProperties(prefix = "connector.gatewaylink.jms")
public class GatewayLinkJmsProperties {

    private String brokerUrl;

    private String brokerUsername;

    private String brokerPassword;

    private String toGatewayMessageQueue;

    private String toGatewayResponseQueue;

    private String toGatewayMessageErrorQueue;

    private String toGatewayResponseErrorQueue;

    private String toConnectorMessageQueue;

    private String toConnectorResponseQueue;

    private String toConnectorMessageErrorQueue;

    private String toConnectorResponseErrorQueue;

    private Resource securityPolicy;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getBrokerUsername() {
        return brokerUsername;
    }

    public void setBrokerUsername(String brokerUsername) {
        this.brokerUsername = brokerUsername;
    }

    public String getBrokerPassword() {
        return brokerPassword;
    }

    public void setBrokerPassword(String brokerPassword) {
        this.brokerPassword = brokerPassword;
    }

    public String getToGatewayMessageQueue() {
        return toGatewayMessageQueue;
    }

    public void setToGatewayMessageQueue(String toGatewayMessageQueue) {
        this.toGatewayMessageQueue = toGatewayMessageQueue;
    }

    public String getToGatewayResponseQueue() {
        return toGatewayResponseQueue;
    }

    public void setToGatewayResponseQueue(String toGatewayResponseQueue) {
        this.toGatewayResponseQueue = toGatewayResponseQueue;
    }

    public String getToGatewayMessageErrorQueue() {
        return toGatewayMessageErrorQueue;
    }

    public void setToGatewayMessageErrorQueue(String toGatewayMessageErrorQueue) {
        this.toGatewayMessageErrorQueue = toGatewayMessageErrorQueue;
    }

    public String getToGatewayResponseErrorQueue() {
        return toGatewayResponseErrorQueue;
    }

    public void setToGatewayResponseErrorQueue(String toGatewayResponseErrorQueue) {
        this.toGatewayResponseErrorQueue = toGatewayResponseErrorQueue;
    }

    public String getToConnectorMessageQueue() {
        return toConnectorMessageQueue;
    }

    public void setToConnectorMessageQueue(String toConnectorMessageQueue) {
        this.toConnectorMessageQueue = toConnectorMessageQueue;
    }

    public String getToConnectorResponseQueue() {
        return toConnectorResponseQueue;
    }

    public void setToConnectorResponseQueue(String toConnectorResponseQueue) {
        this.toConnectorResponseQueue = toConnectorResponseQueue;
    }

    public String getToConnectorMessageErrorQueue() {
        return toConnectorMessageErrorQueue;
    }

    public void setToConnectorMessageErrorQueue(String toConnectorMessageErrorQueue) {
        this.toConnectorMessageErrorQueue = toConnectorMessageErrorQueue;
    }

    public String getToConnectorResponseErrorQueue() {
        return toConnectorResponseErrorQueue;
    }

    public void setToConnectorResponseErrorQueue(String toConnectorResponseErrorQueue) {
        this.toConnectorResponseErrorQueue = toConnectorResponseErrorQueue;
    }

    public Resource getSecurityPolicy() {
        return securityPolicy;
    }

    public void setSecurityPolicy(Resource securityPolicy) {
        this.securityPolicy = securityPolicy;
    }
}

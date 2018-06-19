package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;

@Component("evidencesTimeoutConfigurationProperties")
@ConfigurationProperties(prefix="connector.controller.evidence")
@Validated
@PropertySource("classpath:/eu/domibus/connector/controller/spring/default-connector.properties")
@SuppressWarnings("findbugs:NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR") //suppress sonar warning
public class EvidencesTimeoutConfigurationProperties {

    public static final String BEAN_NAME = "evidencesTimeoutConfigurationProperties";

    /**
     * This property configures if timeouts for messages
     * should be checked!
     */
    private boolean timeoutActive;

    /**
     * This property defines the how often the timeouts for the
     * evidences should be checked
     * the default value is 1 minute (60000ms)
     */
    @Nonnull
    private Duration checkTimeout;

    /**
     * This property defines the timeout how long the connector should
     * wait for an relayREMMD evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @Nonnull
    private Duration relayREMMDTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an deliveryTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * * The default value is 24 hours
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @Nonnull
    private Duration deliveryTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an retrievalTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @Nonnull
    private Duration retrievalTimeout;


    public boolean isTimeoutActive() {
        return timeoutActive;
    }

    public void setTimeoutActive(boolean timeoutActive) {
        this.timeoutActive = timeoutActive;
    }

    public Duration getRelayREMMDTimeout() {
        return relayREMMDTimeout;
    }

    public void setRelayREMMDTimeout(Duration relayREMMDTimeout) {
        this.relayREMMDTimeout = relayREMMDTimeout;
    }

    public Duration getDeliveryTimeout() {
        return deliveryTimeout;
    }

    public void setDeliveryTimeout(Duration deliveryTimeout) {
        this.deliveryTimeout = deliveryTimeout;
    }

    public Duration getRetrievalTimeout() {
        return retrievalTimeout;
    }

    public void setRetrievalTimeout(Duration retrievalTimeout) {
        this.retrievalTimeout = retrievalTimeout;
    }

    public Duration getCheckTimeout() {
        return checkTimeout;
    }

    public void setCheckTimeout(Duration checkTimeout) {
        this.checkTimeout = checkTimeout;
    }


}

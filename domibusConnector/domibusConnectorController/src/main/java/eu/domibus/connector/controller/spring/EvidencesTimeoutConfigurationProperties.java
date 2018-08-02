package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

@Component(EvidencesTimeoutConfigurationProperties.BEAN_NAME)
@ConfigurationProperties(prefix="connector.controller.evidence")
@Validated
@PropertySource("classpath:/eu/domibus/connector/controller/spring/default-connector.properties")
@SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR") //suppress findbugs warning
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
    @NotNull
    private Duration checkTimeout;

    /**
     * This property defines the timeout how long the connector should
     * wait for an relayREMMD evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @NotNull
    private Duration relayREMMDTimeout;

    /**
     * This property defines the timeout how long the connector should
     * wait for an relayREMMD evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 12 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according warnTimeoutProcessor is started
     */
    @NotNull
    private Duration relayREMMDWarnTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an deliveryTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @NotNull
    private Duration deliveryTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an deliveryTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according warnTimeoutProcessor is started
     */
    @NotNull
    private Duration deliveryWarnTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an retrievalTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @NotNull
    private Duration retrievalTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an retrievalTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according warnTmeoutProcessor is started
     */
    @NotNull
    private Duration retrievalWarnTimeout;


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

    public Duration getRelayREMMDWarnTimeout() {
        return relayREMMDWarnTimeout;
    }

    public void setRelayREMMDWarnTimeout(Duration relayREMMDWarnTimeout) {
        this.relayREMMDWarnTimeout = relayREMMDWarnTimeout;
    }

    public Duration getDeliveryWarnTimeout() {
        return deliveryWarnTimeout;
    }

    public void setDeliveryWarnTimeout(Duration deliveryWarnTimeout) {
        this.deliveryWarnTimeout = deliveryWarnTimeout;
    }

    public Duration getRetrievalWarnTimeout() {
        return retrievalWarnTimeout;
    }

    public void setRetrievalWarnTimeout(Duration retrievalWarnTimeout) {
        this.retrievalWarnTimeout = retrievalWarnTimeout;
    }
}

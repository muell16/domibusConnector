package eu.domibus.connector.controller.spring;

import eu.domibus.connector.lib.spring.DomibusConnectorDuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
    private boolean timeoutActive = true;

    /**
     * This property defines the how often the timeouts for the
     * evidences should be checked
     * the default value is 1 minute (60000ms)
     */
    @NotNull
    private DomibusConnectorDuration checkTimeout;

    /**
     * This property defines the timeout how long the connector should
     * wait for an relayREMMD evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @NotNull
    private DomibusConnectorDuration relayREMMDTimeout;

    /**
     * This property defines the timeout how long the connector should
     * wait for an relayREMMD evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 12 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according warnTimeoutProcessor is started
     */
    @NotNull
    private DomibusConnectorDuration relayREMMDWarnTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an deliveryTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @NotNull
    private DomibusConnectorDuration deliveryTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an deliveryTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according warnTimeoutProcessor is started
     */
    @NotNull
    private DomibusConnectorDuration deliveryWarnTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an retrievalTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according timeoutProcessor is started
     */
    @NotNull
    private DomibusConnectorDuration retrievalTimeout;

    /**
     * this property defines the timeout how long the connector should
     * wait for an retrievalTimeout evidence message after a message has been
     * successfully submitted to the gateway
     * The default value is 24 hours
     * 0 disables the timeoutProcessor
     * If the timeout exceeds the according warnTmeoutProcessor is started
     */
    @NotNull
    private DomibusConnectorDuration retrievalWarnTimeout;


    public boolean isTimeoutActive() {
        return timeoutActive;
    }

    public void setTimeoutActive(boolean timeoutActive) {
        this.timeoutActive = timeoutActive;
    }

    public DomibusConnectorDuration getRelayREMMDTimeout() {
        return relayREMMDTimeout;
    }

    public void setRelayREMMDTimeout(DomibusConnectorDuration relayREMMDTimeout) {
        this.relayREMMDTimeout = relayREMMDTimeout;
    }

    public DomibusConnectorDuration getDeliveryTimeout() {
        return deliveryTimeout;
    }

    public void setDeliveryTimeout(DomibusConnectorDuration deliveryTimeout) {
        this.deliveryTimeout = deliveryTimeout;
    }

    public DomibusConnectorDuration getRetrievalTimeout() {
        return retrievalTimeout;
    }

    public void setRetrievalTimeout(DomibusConnectorDuration retrievalTimeout) {
        this.retrievalTimeout = retrievalTimeout;
    }

    public DomibusConnectorDuration getCheckTimeout() {
        return checkTimeout;
    }

    public void setCheckTimeout(DomibusConnectorDuration checkTimeout) {
        this.checkTimeout = checkTimeout;
    }

    public DomibusConnectorDuration getRelayREMMDWarnTimeout() {
        return relayREMMDWarnTimeout;
    }

    public void setRelayREMMDWarnTimeout(DomibusConnectorDuration relayREMMDWarnTimeout) {
        this.relayREMMDWarnTimeout = relayREMMDWarnTimeout;
    }

    public DomibusConnectorDuration getDeliveryWarnTimeout() {
        return deliveryWarnTimeout;
    }

    public void setDeliveryWarnTimeout(DomibusConnectorDuration deliveryWarnTimeout) {
        this.deliveryWarnTimeout = deliveryWarnTimeout;
    }

    public DomibusConnectorDuration getRetrievalWarnTimeout() {
        return retrievalWarnTimeout;
    }

    public void setRetrievalWarnTimeout(DomibusConnectorDuration retrievalWarnTimeout) {
        this.retrievalWarnTimeout = retrievalWarnTimeout;
    }
}

package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "properties")
public class ConnectorMessageLaneProperties {

    /**
     * is the loopback processing enabled
     * if enabled the suffix _1 will be removed from any
     * incoming EBMS message id
     */
    private boolean loopbackEnabled = true;

    public boolean isLoopbackEnabled() {
        return loopbackEnabled;
    }

    public void setLoopbackEnabled(boolean loopbackEnabled) {
        this.loopbackEnabled = loopbackEnabled;
    }

    /**
     * should the by the connector created evidences
     * transported back to the backend
     *  this would affect all kind of evidences which
     *  are originally created by the connector
     *  <ul>
     *      <li>The automatically created SubmissionAcceptance</li>
     *      <li>The by a evidence trigger message created evidence message</li>
     *  </ul>
     *
     *  The default is true
     */
    private boolean sendGeneratedEvidencesToBackend = true;

    public boolean isSendGeneratedEvidencesToBackend() {
        return sendGeneratedEvidencesToBackend;
    }

    public void setSendGeneratedEvidencesToBackend(boolean sendGeneratedEvidencesToBackend) {
        this.sendGeneratedEvidencesToBackend = sendGeneratedEvidencesToBackend;
    }
}

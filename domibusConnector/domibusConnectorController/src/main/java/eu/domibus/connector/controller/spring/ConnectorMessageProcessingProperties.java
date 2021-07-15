package eu.domibus.connector.controller.spring;

import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ConnectorMessageProcessingProperties.PREFIX)
public class ConnectorMessageProcessingProperties {

    public static final String PREFIX = "processing";

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
    @ConfigurationLabel("Transport generated evidence back")
    private boolean sendGeneratedEvidencesToBackend = true;

    /**
     * Should the connector create the EBMS id
     */
    @ConfigurationLabel("EBMS generation enabled")
    private boolean ebmsIdGeneratorEnabled = true;

    private String ebmsIdSuffix = "ecodex.eu";

    public boolean isSendGeneratedEvidencesToBackend() {
        return sendGeneratedEvidencesToBackend;
    }

    public void setSendGeneratedEvidencesToBackend(boolean sendGeneratedEvidencesToBackend) {
        this.sendGeneratedEvidencesToBackend = sendGeneratedEvidencesToBackend;
    }

    public boolean isEbmsIdGeneratorEnabled() {
        return ebmsIdGeneratorEnabled;
    }

    public void setEbmsIdGeneratorEnabled(boolean ebmsIdGeneratorEnabled) {
        this.ebmsIdGeneratorEnabled = ebmsIdGeneratorEnabled;
    }

    public String getEbmsIdSuffix() {
        return ebmsIdSuffix;
    }

    public void setEbmsIdSuffix(String ebmsIdSuffix) {
        this.ebmsIdSuffix = ebmsIdSuffix;
    }
}

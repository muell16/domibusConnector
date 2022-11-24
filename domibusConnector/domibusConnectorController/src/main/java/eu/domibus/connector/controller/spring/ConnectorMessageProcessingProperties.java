package eu.domibus.connector.controller.spring;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@BusinessDomainScoped
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

    private String pModeFile;   //TODO: JUEUSW-597

    private PModeVerificationMode outgoingPModeVerificationMode = PModeVerificationMode.RELAXED;

    private PModeVerificationMode incomingPModeVerificationMode = PModeVerificationMode.STRICT;

    /**
     * defines if the RETRIEVAL EVIDENCE is enabled
     *  if yes timeouts and other actions will also depend on retrieval evidence
     *  otherwise the connector will just ignore any evidence of type RETRIEVAL
     *  in respect to message state. But if possible (business message still available)
     *  the RETRIEVAL will still be forwarded to any backend system
     *
     */
    private boolean retrievalEnabled = false;

    public PModeVerificationMode getOutgoingPModeVerificationMode() {
        return outgoingPModeVerificationMode;
    }

    public void setOutgoingPModeVerificationMode(PModeVerificationMode outgoingPModeVerificationMode) {
        this.outgoingPModeVerificationMode = outgoingPModeVerificationMode;
    }

    public PModeVerificationMode getIncomingPModeVerificationMode() {
        return incomingPModeVerificationMode;
    }

    public void setIncomingPModeVerificationMode(PModeVerificationMode incomingPModeVerificationMode) {
        this.incomingPModeVerificationMode = incomingPModeVerificationMode;
    }

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

    public boolean isRetrievalEnabled() {
        return retrievalEnabled;
    }

    public void setRetrievalEnabled(boolean retrievalEnabled) {
        this.retrievalEnabled = retrievalEnabled;
    }

    public static enum PModeVerificationMode {
        CREATE, RELAXED, STRICT;
    }

    public String getpModeFile() {
        return pModeFile;
    }

    public void setpModeFile(String pModeFile) {
        this.pModeFile = pModeFile;
    }
}

package eu.domibus.connector.controller.spring;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
@BusinessDomainScoped
@ConfigurationProperties(prefix = ConnectorMessageProcessingProperties.PREFIX)
@Valid

@Getter
@Setter
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

    private PModeConfig pModeConfig;

    private PModeVerificationMode outgoingPModeVerificationMode = PModeVerificationMode.RELAXED;

    private PModeVerificationMode incomingPModeVerificationMode = PModeVerificationMode.CREATE;

    /**
     * defines if the RETRIEVAL EVIDENCE is enabled
     *  if yes timeouts and other actions will also depend on retrieval evidence
     *  otherwise the connector will just ignore any evidence of type RETRIEVAL
     *  in respect to message state. But if possible (business message still available)
     *  the RETRIEVAL will still be forwarded to any backend system
     *
     */
    private boolean retrievalEnabled = false;


    public static enum PModeVerificationMode {
        CREATE, RELAXED;
    }

    @Getter
    @Setter
    public static class PModeConfig {
        public static final String PREFIX = ConnectorMessageProcessingProperties.PREFIX + ".p-mode-config";
        @NotBlank
        @NotNull
        private String pModeLocation;
        @CheckForNull
        private String description;
        @CheckForNull
        private LocalDateTime uploadDate;
        @CheckForNull
        private String changedBy;
    }


}

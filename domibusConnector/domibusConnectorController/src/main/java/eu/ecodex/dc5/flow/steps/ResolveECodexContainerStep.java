package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.processor.steps.MessageProcessStep;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.ecodex.dc5.message.model.DC5MessageAttachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ResolveECodexContainerStep {

    private static final Logger LOGGER = LogManager.getLogger(ResolveECodexContainerStep.class);

    private final DomibusConnectorSecurityToolkit securityToolkit;

    public ResolveECodexContainerStep(DomibusConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }

    @Step(name = "ResolveECodexContainerStep")
    public DC5Message executeStep(DC5Message message) {
        message = securityToolkit.validateContainer(message);

        //maybe call mapper here
        if (message.getMessageContent().getEcodexContent().getBusinessXml() != null) {
            //TODO: copy attachment
            DC5MessageAttachment businessXml = message.getMessageContent().getEcodexContent().getBusinessXml();
            message.getMessageContent().getBusinessContent().setBusinessXml(businessXml);
        }
        LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Successfully resolved eCodexContainer");
        return message;
    }

}

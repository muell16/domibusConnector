package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.processor.steps.MessageProcessStep;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.ecodex.dc5.message.model.DC5MessageAttachment;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class BuildECodexContainerStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildECodexContainerStep.class);

    private final DomibusConnectorSecurityToolkit securityToolkit;

    public BuildECodexContainerStep(DomibusConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }

    @Step(name = "BuildECodexContainerStep")
    public DC5Message executeStep(DC5Message msg) {
        msg = securityToolkit.buildContainer(msg);
        //TODO: copy business xml
        //maybe call mapper here
//        DC5MessageAttachment businessXml = msg.getMessageContent().getBusinessContent().getBusinessXml();
//        msg.getMessageContent().getEcodexContent().setBusinessXml(businessXml);
        LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully crated e-Codex Container");
        return msg;
    }

}

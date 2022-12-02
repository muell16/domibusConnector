package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.processor.steps.MessageProcessStep;
import eu.domibus.connector.controller.routing.DCMessageRoutingConfigurationProperties;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * This step looks up the correct backend name
 *
 */
@Component
public class LookupGatewayNameStep {

    private static final Logger LOGGER = LogManager.getLogger(LookupGatewayNameStep.class);

    private final DCMessageRoutingConfigurationProperties dcMessageRoutingConfigurationProperties;

    public LookupGatewayNameStep(DCMessageRoutingConfigurationProperties dcMessageRoutingConfigurationProperties) {
        this.dcMessageRoutingConfigurationProperties = dcMessageRoutingConfigurationProperties;
    }

    @Step(name = "LookupGatewayNameStep")
    public DC5Message executeStep(DC5Message message) {
        LOGGER.debug("Connector currently only supports ONE gateway per domain!");
        if (StringUtils.hasText(message.getGatewayLinkName())) {
            //return when already set
            return message;
        }
        message.setGatewayLinkName(dcMessageRoutingConfigurationProperties.getDefaultGatewayName());
        return message;
    }

}

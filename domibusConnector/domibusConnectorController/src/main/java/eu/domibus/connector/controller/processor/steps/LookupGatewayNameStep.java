package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.routing.DCMessageRoutingConfigurationProperties;
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
public class LookupGatewayNameStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(LookupGatewayNameStep.class);

    private final DCMessageRoutingConfigurationProperties dcMessageRoutingConfigurationProperties;

    public LookupGatewayNameStep(DCMessageRoutingConfigurationProperties dcMessageRoutingConfigurationProperties) {
        this.dcMessageRoutingConfigurationProperties = dcMessageRoutingConfigurationProperties;
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "LookupGatewayNameStep")
    public boolean executeStep(DC5Message message) {
        if (StringUtils.hasText(message.getGatewayLinkName())) {
            //return when already set
            return true;
        }
        message.setGatewayLinkName(dcMessageRoutingConfigurationProperties.getDefaultGatewayName());
        return true;
    }

}

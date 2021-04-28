package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.common.service.ConfigurationPropertyLoaderService;
import eu.domibus.connector.controller.spring.ConnectorMessageLaneProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;


/**
 * This processing step generates a EBMS id for the message
 *  -) the EBMSID is only created if the EBMSID of the by the backend provided message is empty
 *
 */
@Component
public class GenerateEbmsIdStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(GenerateEbmsIdStep.class);

    private final ConfigurationPropertyLoaderService configurationPropertyLoaderService;

    public GenerateEbmsIdStep(ConfigurationPropertyLoaderService configurationPropertyLoaderService) {
        this.configurationPropertyLoaderService = configurationPropertyLoaderService;
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "GenerateEbmsIdStep")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        ConnectorMessageLaneProperties connectorMessageLaneProperties = configurationPropertyLoaderService.loadConfiguration(domibusConnectorMessage.getMessageLaneId(), ConnectorMessageLaneProperties.class);
        if (connectorMessageLaneProperties.isEbmsIdGeneratorEnabled()) {
            LOGGER.debug("Setting EBMS id within connector is enabled");
            if (StringUtils.isEmpty(domibusConnectorMessage.getMessageDetails().getEbmsMessageId())) {
                String ebmsId = UUID.randomUUID().toString() + "@" + connectorMessageLaneProperties.getEbmsIdSuffix();
                domibusConnectorMessage.getMessageDetails().setEbmsMessageId(ebmsId);
                LOGGER.info("Setting EBMS id to [{}]", ebmsId);
            }
        }
        return  true;
    }
}

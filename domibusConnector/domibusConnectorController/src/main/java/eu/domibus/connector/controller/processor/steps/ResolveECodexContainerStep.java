package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResolveECodexContainerStep implements MessageProcessStep {

    private final DomibusConnectorSecurityToolkit securityToolkit;

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "ResolveECodexContainerStep")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        securityToolkit.validateContainer(domibusConnectorMessage);
        log.info(LoggingMarker.BUSINESS_LOG, "Successfully resolved eCodexContainer");
        return true;
    }

}

package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.routing.DCMessageRoutingConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This step looks up the correct backend name
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LookupBackendNameStep implements MessageProcessStep {

    private final DCMessageRoutingConfigurationProperties dcMessageRoutingConfigurationProperties;

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "LookupBackendNameStep")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        String defaultBackendName = dcMessageRoutingConfigurationProperties.getDefaultBackendName();
        if (dcMessageRoutingConfigurationProperties.isEnabled()) {
            log.debug("Backend routing is enabled");
            Optional<String> first = dcMessageRoutingConfigurationProperties.getRules().stream()
                    .filter(r -> r.getMatchClause().matches(domibusConnectorMessage))
                    .map(DCMessageRoutingConfigurationProperties.RoutingRule::getBackendName)
                    .findFirst();
            if (first.isPresent()) {
                String backendName = first.get();
                log.info(LoggingMarker.BUSINESS_LOG, "Looked up backend name [{}] for message", backendName);
                domibusConnectorMessage.getMessageDetails().setConnectorBackendClientName(backendName);
            } else {
                log.warn(LoggingMarker.BUSINESS_LOG, "No backend rule pattern has matched! Applying default backend name [{}]!",
                        defaultBackendName);
                domibusConnectorMessage.getMessageDetails().setConnectorBackendClientName(defaultBackendName);
            }
        } else {
            log.debug("Backend routing is disabled, applying default backend name [{}]!", dcMessageRoutingConfigurationProperties.getDefaultBackendName());
            domibusConnectorMessage.getMessageDetails().setConnectorBackendClientName(defaultBackendName);
        }
        return true;
    }

}

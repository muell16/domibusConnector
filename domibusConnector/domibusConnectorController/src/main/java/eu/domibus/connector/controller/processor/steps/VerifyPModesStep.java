package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DomibusConnectorPModeService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.MDCHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerifyPModesStep implements MessageProcessStep {

    private final DomibusConnectorPModeService pModeService;

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "VerifyPModes")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        DomibusConnectorMessageLane.MessageLaneId messageLaneId = domibusConnectorMessage.getMessageLaneId();
        DomibusConnectorMessageDetails messageDetails = domibusConnectorMessage.getMessageDetails();

        Optional<DomibusConnectorService> service = pModeService.getConfiguredSingle(messageLaneId, messageDetails.getService());
        if (service.isPresent()) {
            messageDetails.setService(service.get());
        } else {
            //TODO: improve exception
            throw new RuntimeException("error, service not configured!" + messageDetails.getService());
        }
        //TODO: add parties, actions, ...
        return true;
    }


}

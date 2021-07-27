package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DomibusConnectorPModeService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VerifyPModesStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(VerifyPModesStep.class);
    private final DomibusConnectorPModeService pModeService;

    public VerifyPModesStep(DomibusConnectorPModeService pModeService) {
        this.pModeService = pModeService;
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "VerifyPModes")
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {
        DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId = domibusConnectorMessage.getMessageLaneId();
        DomibusConnectorMessageDetails messageDetails = domibusConnectorMessage.getMessageDetails();

        Optional<DomibusConnectorAction> action = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getAction());
        if (action.isPresent()) {
            messageDetails.setAction(action.get());
        } else {
            LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The action [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getAction());
            //TODO: improve exception
            throw new RuntimeException("error, action not configured:" + messageDetails.getAction());
        }


        Optional<DomibusConnectorService> service = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getService());
        if (service.isPresent()) {
            messageDetails.setService(service.get());
        } else {
            LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The service [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getService());
            //TODO: improve exception
            throw new RuntimeException("error, service not configured!" + messageDetails.getService());
        }

        Optional<DomibusConnectorParty> toParty = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getToParty());
        if (toParty.isPresent()) {
            messageDetails.setToParty(toParty.get());
        } else {
            LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The toParty [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getToParty());
            //TODO: improve exception
            throw new RuntimeException("error, party not configured:" + messageDetails.getToParty());
        }

        Optional<DomibusConnectorParty> fromParty = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getFromParty());
        if (fromParty.isPresent()) {
            messageDetails.setToParty(fromParty.get());
        } else {
            LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The toParty [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getFromParty());
            //TODO: improve exception
            throw new RuntimeException("error, party not configured:" + messageDetails.getFromParty());
        }

        return true;
    }


}

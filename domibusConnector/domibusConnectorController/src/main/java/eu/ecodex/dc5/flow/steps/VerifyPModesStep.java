package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.pmode.BusinessScopedPModeService;
import eu.ecodex.dc5.pmode.DC5PmodeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties.PModeVerificationMode.*;

@Component
@RequiredArgsConstructor
public class VerifyPModesStep {

    private static final Logger LOGGER = LogManager.getLogger(VerifyPModesStep.class);
//    private final DomibusConnectorPModeService pModeService;
    private final ConnectorMessageProcessingProperties connectorMessageProcessingProperties;
    private final BusinessScopedPModeService businessScopedPModeService;

//    private boolean executeStep(DC5Message DC5Message,
//                                ConnectorMessageProcessingProperties.PModeVerificationMode verificationMode) {
//        LOGGER.debug("Verifying PModes with verification mode [{}]", verificationMode);
//        DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId = DC5Message.getMessageLaneId();
//        DC5Ebms messageDetails = DC5Message.getEbmsData();
//
//        if (verificationMode == ConnectorMessageProcessingProperties.PModeVerificationMode.RELAXED) {

//            Optional<DC5Action> action = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getAction());
//            if (action.isPresent()) {
//                messageDetails.setAction(action.get());
//            } else {
//                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The action [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getAction());
//                //TODO: improve exception
//                throw new RuntimeException("error, action not configured:" + messageDetails.getAction());
//            }
//
//            Optional<DC5Service> service = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getService());
//            if (service.isPresent()) {
//                messageDetails.setService(service.get());
//            } else {
//                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The service [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getService());
//                //TODO: improve exception
//                throw new RuntimeException("error, service not configured!" + messageDetails.getService());
//            }

//            if (!StringUtils.hasText(messageDetails.getToParty().getPartyIdType())) {
//                LOGGER.debug("PMode verification mode is relaxed. Assuming ToParty PartyIdType [{}] as empty!", messageDetails.getToParty().getPartyIdType());
//                messageDetails.getToParty().setPartyIdType(null);
//            }

//            an empty role is valid!
//            if (!StringUtils.hasText(messageDetails.getToParty().getRole())) {
//                LOGGER.debug("PMode verification mode is relaxed. Assuming ToParty Role [{}] as empty!", messageDetails.getToParty().getRole());
//                messageDetails.getToParty().setRole(null);
//            }
//            Optional<DomibusConnectorParty> toParty = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getToParty());
//            if (toParty.isPresent()) {
//                messageDetails.setToParty(toParty.get());
//            } else {
//                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The toParty [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getToParty());
//                //TODO: improve exception
//                throw new RuntimeException("error, party not configured:" + messageDetails.getToParty());
//            }

//            if (!StringUtils.hasText(messageDetails.getFromParty().getPartyIdType())) {
//                LOGGER.debug("PMode verification mode is relaxed. Assuming FromParty PartyIdType [{}] as empty!", messageDetails.getFromParty().getPartyIdType());
//                messageDetails.getFromParty().setPartyIdType(null);
//            }
//          an empty role is valid!
//            if (!StringUtils.hasText(messageDetails.getFromParty().getRole())) {
//                LOGGER.debug("PMode verification mode is relaxed. Assuming FromParty Role [{}] as empty!", messageDetails.getFromParty().getRole());
//                messageDetails.getFromParty().setRole(null);
//            }
//            Optional<DomibusConnectorParty> fromParty = pModeService.getConfiguredSingle(businessDomainId, messageDetails.getFromParty());
//            if (fromParty.isPresent()) {
//                messageDetails.setFromParty(fromParty.get());
//            } else {
//                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The toParty [{}] is not configured on connector. Check your uploaded p-Modes!", messageDetails.getFromParty());
//                //TODO: improve exception
//                throw new RuntimeException("error, party not configured:" + messageDetails.getFromParty());
//            }
//        }
//        if (verificationMode == ConnectorMessageProcessingProperties.PModeVerificationMode.CREATE) {
//            LOGGER.warn("PMode verification mode " + ConnectorMessageProcessingProperties.PModeVerificationMode.CREATE + " is not supported!");
//        }
//        if (verificationMode == ConnectorMessageProcessingProperties.PModeVerificationMode.STRICT) {
//            LOGGER.warn("PMode verification mode " + ConnectorMessageProcessingProperties.PModeVerificationMode.STRICT + " is experimental feature!");
//        }
//
//        return true;
//    }



    @Step(name = "VerifyPModesOutgoing")
    public DC5Message verifyOutgoing(DC5Message message) {
        ConnectorMessageProcessingProperties.PModeVerificationMode outgoingPModeVerificationMode = connectorMessageProcessingProperties.getOutgoingPModeVerificationMode();

        if (outgoingPModeVerificationMode == RELAXED) {
            verifyRelaxed(message);
        } else if (outgoingPModeVerificationMode == CREATE) {
            //DO nothing...
        } else {
            throw new IllegalArgumentException("Unknown pModeVerification Mode!");
        }
        return message;
    }

    private void verifyRelaxed(DC5Message message) {
        DC5Ebms ebmsData = message.getEbmsData();
        DC5Service service = ebmsData.getService();
        DC5Action action = ebmsData.getAction();
        service = lookupService(service);

        DC5PmodeService.PModeLeg leg = verifyLeg(service, action);
        DC5PmodeService.PModeProcess businessProcess = leg.getBusinessProcess();

        if (ebmsData.getInitiator().getPartnerRole() == null) {
            ebmsData.getInitiator().setPartnerRole(DC5Role.builder()
                            .role(businessProcess.getInitiatorRole())
                            .roleType(DC5RoleType.INITIATOR)
                    .build());
        }

        if (ebmsData.getResponder().getPartnerRole() == null) {
            ebmsData.getResponder().setPartnerRole(DC5Role.builder()
                    .role(businessProcess.getInitiatorRole())
                    .roleType(DC5RoleType.RESPONDER)
                    .build());
        }




    }

    private DC5PmodeService.PModeLeg verifyLeg(DC5Service service, DC5Action action) {
        List<DC5PmodeService.PModeLeg> collect = businessScopedPModeService.getCurrentPModeSet().getLegs()
                .stream().filter(l ->
                        StringUtils.equals(l.getAction().getAction(), action.getAction()) &&
                                StringUtils.equals(l.getService().getService(), service.getService()) &&
                                StringUtils.equals(l.getService().getServiceType(), service.getServiceType())
                ).collect(Collectors.toList());
        if (collect.size() == 1) {
            return collect.get(0);
        } else {
            String error = String.format("The provided information for service and action is not sufficient, found %d results", collect.size());
            throw new PModeVerificationException(ErrorCode.P_MODE_SERVICE_ACTION_LEG_NOT_FOUND, error);
        }


    }

    private DC5Service lookupService(DC5Service service) {
        List<DC5PmodeService.PModeService> availableServices = businessScopedPModeService
                .getCurrentPModeSet()
                .getServices();
        List<DC5PmodeService.PModeService> collect = businessScopedPModeService
                .getCurrentPModeSet()
                .getServices()
                .stream()
                .filter(s ->
                        (StringUtils.isBlank(service.getService()) || StringUtils.equals(service.getService(), s.getService()) ) &&
                        (StringUtils.isBlank(service.getServiceType()) || StringUtils.equals(service.getServiceType(), s.getServiceType()))
                )
                .collect(Collectors.toList());
        if (collect.size() == 1) {
            DC5PmodeService.PModeService s = collect.get(0);
            service.setService(s.getService());
            service.setServiceType(s.getServiceType());
            return service;
        } else {
            String error = String.format("The provided information for service [%s] is not sufficient, found %d results in available services [%s]",
                    service,
                    collect.size(),
                    availableServices.stream().map(Object::toString).collect(Collectors.joining(","))
                    );
            throw new PModeVerificationException(ErrorCode.P_MODE_SERVICE_NOT_FOUND, error);
        }
    }

    @Step(name = "VerifyPModesIncoming")
    public DC5Message verifyIncoming(DC5Message message) {
        ConnectorMessageProcessingProperties.PModeVerificationMode outgoingPModeVerificationMode = connectorMessageProcessingProperties.getOutgoingPModeVerificationMode();

        if (outgoingPModeVerificationMode == RELAXED) {
            verifyRelaxed(message);
        } else if (outgoingPModeVerificationMode == CREATE) {
            //DO nothing...
        } else {
            throw new IllegalArgumentException("Unknown pModeVerification Mode!");
        }
        return message;
    }

    public static class PModeVerificationException extends DomibusConnectorControllerException {

        public PModeVerificationException(ErrorCode errorCode, String msg) {
            super(errorCode, msg);
        }
    }

}

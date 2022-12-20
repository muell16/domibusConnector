package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.processor.steps.MessageProcessStep;
import eu.domibus.connector.controller.routing.DCRoutingRulesManagerImpl;
import eu.domibus.connector.controller.routing.LinkPartnerRoutingRule;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * The routing occurs in the following order:
 *
 * 1. refToMessageId: If the transported message relates to an EBMS id of an already processed business message. The backend_name of this message is used.
 *
 * 2. ConversationId: If the transported message contains a conversationId where a business message has already been processed. The backend_name of this message is used.
 *
 * 3. Routing Rule: If the backend_name is still empty, the routing rules are processed.
 *
 * 4. Default Backend: If the backend_name is still empty the default backend will be used.
 *
 */
@Component
@RequiredArgsConstructor
public class LookupBackendNameStep implements MessageProcessStep {

    private static final Logger LOGGER = LogManager.getLogger(LookupBackendNameStep.class);

    private final DCRoutingRulesManagerImpl dcRoutingConfigManager;
    private final DC5MessageRepo messageRepo; // TODO: replace with a service?
    private final FindBusinessMessageByMsgId dc5MessageService;


    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "LookupBackendNameStep")
    public boolean executeStep(DC5Message message) {
        if (message.getBackendLinkName() != null) {
            //return when already set
            return true;
        }
        DomibusConnectorLinkPartner.LinkPartnerName backendName = null;

        //Lookup backend by conversation id
        String conversationId = message.getEbmsData().getConversationId();
        if (StringUtils.hasText(conversationId)) {
            final List<DC5Message> listMsg = dc5MessageService.findBusinessMsgByConversationId(conversationId);
            final Optional<DC5Message> any = listMsg.stream().findAny();
            if (any.isPresent()) {
                backendName = any.get().getBackendLinkName();
            }
        }
        if (backendName != null) {
            LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "ConversationId [{}] is used to set backend to [{}]", conversationId, backendName);
            message.setBackendLinkName(backendName);
            return true;
        }

        //lookup backend by rules and default backend
        DomibusConnectorLinkPartner.LinkPartnerName defaultBackendName = DomibusConnectorLinkPartner.LinkPartnerName.of(dcRoutingConfigManager.getDefaultBackendName(message.getMessageLaneId()));
        if (dcRoutingConfigManager.isBackendRoutingEnabled(message.getMessageLaneId())) {
            LOGGER.debug("Backend routing is enabled");
            message.setBackendLinkName(
                    dcRoutingConfigManager.getBackendRoutingRules(message.getMessageLaneId())
                    .values()
                    .stream()
                    .sorted(LinkPartnerRoutingRule.getComparator())
                    .filter(r -> r.getMatchClause().matches(message))
                    .map(LinkPartnerRoutingRule::getLinkName)
                    .findFirst()
                    .map(bName -> {
                        LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Looked up backend name [{}] for message", bName);
                        return DomibusConnectorLinkPartner.LinkPartnerName.of(bName);
                    })
                    .orElseGet(() -> {
                        LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "No backend rule pattern has matched! Applying default backend name [{}]!");
                        return defaultBackendName;
                    })
            );
        } else {
            LOGGER.debug("Backend routing is disabled, applying default backend name [{}]!", dcRoutingConfigManager.getDefaultBackendName(message.getMessageLaneId()));
            message.setBackendLinkName(defaultBackendName);
        }
        return true;
    }

}

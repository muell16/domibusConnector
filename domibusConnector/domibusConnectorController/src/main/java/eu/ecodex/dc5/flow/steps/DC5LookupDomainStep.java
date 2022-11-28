package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.controller.exception.DomainMatchingException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.domibus.connector.controller.routing.DCDomainRoutingManager;
import eu.domibus.connector.controller.routing.DomainRoutingRule;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DC5LookupDomainStep {

    private static final Logger LOGGER = LogManager.getLogger(DC5LookupDomainStep.class);
    private final DCBusinessDomainManager domainManager;
    private final FindBusinessMessageByMsgId msgService;
    private final DCDomainRoutingManager dcDomainRoutingManager;

    @Step(name = "LookupDomainStep")
    public DC5Message lookupDomain(DC5Message msg) {
        //TODO: implement Business domain matching rules here!

        // 1. Ist eine ReftoMessageID vorhanden -> referenzierte Nachricht raussuchen und derselben Domain zuordnen
        final Optional<DC5Message> businessMsgByRefToMsgId = msgService.findBusinessMsgByRefToMsgId(msg);
        if (businessMsgByRefToMsgId.isPresent()) {
            final DomibusConnectorBusinessDomain.BusinessDomainId id = businessMsgByRefToMsgId.get().getBusinessDomainId();
            msg.setBusinessDomainId(id);
            LOGGER.debug("Associated msg [{}] with domain [{}].", msg, id);
            return msg;
        }

        // 2. Gibt es bereits Nachrichten mit derselben ConversationID -> Nachricht ist derselben Domain zuzuordnen
        final List<DC5Message> businessMsgByConversationId = msgService.findBusinessMsgByConversationId(msg.getEbmsData().getConversationId());
        final Optional<DC5Message> any = businessMsgByConversationId.stream().findAny();
        if (any.isPresent()) {
            final DomibusConnectorBusinessDomain.BusinessDomainId id = any.get().getBusinessDomainId();
            msg.setBusinessDomainId(id);
            LOGGER.debug("Associated msg [{}] with domain [{}].", msg, id);
            return msg;
        }

        // 3. Gibt es DomainRoutingRules die zu dieser Nachricht matchen -> diese anwenden
        final List<DomibusConnectorBusinessDomain.BusinessDomainId> validBusinessDomains = domainManager.getValidBusinessDomains();
        for (DomibusConnectorBusinessDomain.BusinessDomainId id : validBusinessDomains) {

            final List<DomainRoutingRule> collect = dcDomainRoutingManager.getDomainRoutingRules(id)
                    .values()
                    .stream()
                    .sorted(DomainRoutingRule.getComparator())
                    .filter(r -> r.getMatchClause().matches(msg))
                    .collect(Collectors.toList());

            if (collect.size() == 1) {
                msg.setBusinessDomainId(id);
                LOGGER.debug("Associated msg [{}] with domain [{}] by applying rule [{}]", msg, id, collect.get(0));
                return msg;
            } else if (collect.size() > 1) {
                final String rules = collect.stream().map(DomainRoutingRule::toString).reduce("", (acc, next) -> acc + ", " + next.toString());
                throw new DomainMatchingException(ErrorCode.DOMAIN_MATCHING_ERROR, String.format("Multiple domain routing rules apply to msg: %s", rules));
            }
        }
        throw new DomainMatchingException(ErrorCode.DOMAIN_MATCHING_ERROR, "Msg could not be associated with any domain!");
    }
}

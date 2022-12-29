package eu.ecodex.dc5.flow.steps;

import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.domibus.connector.controller.exception.DomainMatchingException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.domibus.connector.controller.routing.DCDomainRoutingManager;
import eu.domibus.connector.controller.routing.DomainRoutingRule;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
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
        DC5BusinessDomain.BusinessDomainId currentBusinessDomain = CurrentBusinessDomain.getCurrentBusinessDomain();
        if (currentBusinessDomain != null) {
            msg.setBusinessDomainId(currentBusinessDomain);
            LOGGER.info("Associated msg [{}] with domain [{}] from ThreadContext.", msg, currentBusinessDomain);
            return msg;
        }
        // 1. If the field refToMsgId exists (either in backend or ebms data) then
        // -> lookup the referred msg and associate incoming message with that domain.
        final Optional<DC5Message> businessMsgByRefToMsgId = msgService.findBusinessMsgByRefToMsgId(msg);
        if (businessMsgByRefToMsgId.isPresent()) {
            final DC5BusinessDomain.BusinessDomainId id = businessMsgByRefToMsgId.get().getBusinessDomainId();
            msg.setBusinessDomainId(id);
            LOGGER.info("Associated msg [{}] with domain [{}].", msg, id);
            return msg;
        }

        // 2. If there are messages with the same ConversationID
        // -> then associate messsage with domain of those messages.
        final Optional<DC5Message> any = Optional.ofNullable(msg.getEbmsData())
                .flatMap(ebmsData -> Optional.ofNullable(ebmsData.getConversationId()))
                .flatMap(s -> msgService.findBusinessMsgByConversationId(s).stream().findFirst());

        if (any.isPresent()) {
            final DC5BusinessDomain.BusinessDomainId id = any.get().getBusinessDomainId();
            msg.setBusinessDomainId(id);
            LOGGER.info("Associated msg [{}] with domain [{}].", msg, id);
            return msg;
        }

        // 3. If there are domain routing rules
        // -> they should be applied.

        List<Tuple> matchingDomains = new ArrayList<>();
        final List<DC5BusinessDomain> validBusinessDomains = domainManager.getValidBusinessDomainsAllData();
        for (DC5BusinessDomain domain : validBusinessDomains) {
            dcDomainRoutingManager.getDomainRoutingRules(domain.getId())
                    .values()
                    .stream()
                    .sorted(DomainRoutingRule.getComparator())
                    .filter(r -> r.getMatchClause().matches(msg))
                    .findAny()
                    .ifPresent( matchingRule ->
                matchingDomains.add(new Tuple(domain.getId(), matchingRule)));
        }

        if (matchingDomains.size() == 1) {
            Tuple match = matchingDomains.get(0);
            msg.setBusinessDomainId(match.domainid);
            LOGGER.info("Associated msg [{}] with domain [{}] by applying rule [{}]", msg, match.domainid, match.routingRule);
            return msg;
        } else if (matchingDomains.size() > 1) {
            final String matchingRules = matchingDomains.stream()
                    .map(t -> String.format("Rule [%s] matches domain [%s]", t.routingRule, t.domainid))
                    .collect(Collectors.joining(","));
            throw new DomainMatchingException(msg.getConnectorMessageId(), ErrorCode.MULTIPLE_DOMAIN_MATCHING_ERROR, String.format("Multiple domain routing rules [%s] apply to msg: [%s]", matchingRules, msg));
        }

        throw new DomainMatchingException(msg.getConnectorMessageId(), ErrorCode.DOMAIN_MATCHING_ERROR, String.format("No domain apply to msg: [%s]", msg));
    }

    private static class Tuple {
        DC5BusinessDomain.BusinessDomainId domainid;
        DomainRoutingRule routingRule;
        public Tuple(DC5BusinessDomain.BusinessDomainId domainid, DomainRoutingRule y) {
            this.domainid = domainid;
            this.routingRule = y;
        }
    }
}

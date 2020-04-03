package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;

public class DomibusConnectorMessageDetailsBuilder {

    private String backendMessageId;
    private String ebmsMessageId;
    private String refToMessageId;
    private String conversationId;
    private String originalSender;
    private String finalRecipient;
    private DomibusConnectorService service;
    private DomibusConnectorAction action;
    private DomibusConnectorParty fromParty;
    private DomibusConnectorParty toParty;

    private DomibusConnectorMessageDetailsBuilder() {}

    public static DomibusConnectorMessageDetailsBuilder create() {
        return new DomibusConnectorMessageDetailsBuilder();
    }

    public DomibusConnectorMessageDetailsBuilder withBackendMessageId(String backendMessageId) {
        this.backendMessageId = backendMessageId;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withEbmsMessageId(String ebmsMessageId) {
        this.ebmsMessageId = ebmsMessageId;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withRefToMessageId(String refToMessageId) {
        this.refToMessageId = refToMessageId;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withConversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withOriginalSender(String originalSender) {
        this.originalSender = originalSender;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withFinalRecipient(String finalRecipient) {
        this.finalRecipient = finalRecipient;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withService(String serviceName, String serviceType) {
        this.service = new DomibusConnectorService(serviceName, serviceType);
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withService(DomibusConnectorService service) {
        this.service = service;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withAction(String action) {
        this.action = new DomibusConnectorAction(action, true);
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withAction(DomibusConnectorAction action) {
        this.action = action;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withFromParty(DomibusConnectorParty fromParty) {
        this.fromParty = fromParty;
        return this;
    }

    public DomibusConnectorMessageDetailsBuilder withToParty(DomibusConnectorParty toParty) {
        this.toParty = toParty;
        return this;
    }

    public DomibusConnectorMessageDetails build() {
        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        details.setAction(this.action);
        details.setService(this.service);
        details.setConversationId(this.conversationId);
        details.setEbmsMessageId(this.ebmsMessageId);
        details.setFinalRecipient(this.finalRecipient);
        details.setOriginalSender(this.originalSender);
        details.setFromParty(this.fromParty);
        details.setToParty(this.toParty);
        details.setRefToMessageId(this.refToMessageId);
        return details;
    }

}
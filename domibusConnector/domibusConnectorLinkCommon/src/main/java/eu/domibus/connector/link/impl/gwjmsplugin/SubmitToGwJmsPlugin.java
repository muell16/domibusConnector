package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.MapMessage;

@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
@Service
public class SubmitToGwJmsPlugin implements SubmitToLink {

    public static final String MESSAGE_TYPE_PROPERTY_NAME = "messageType";

    public static final String SUBMIT_MESSAGE_VALUE = "submitMessage";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private GwJmsPluginConfigurationProperties configurationProperties;


    @Override
    public void submitToLink(DomibusConnectorMessage message) throws DomibusConnectorSubmitToLinkException {
        final DomibusConnectorMessageDetails msgDetails = message.getMessageDetails();

        jmsTemplate.send(configurationProperties.getInQueue(), session -> {
            MapMessage msg = session.createMapMessage();

            msg.setStringProperty(MESSAGE_TYPE_PROPERTY_NAME, SUBMIT_MESSAGE_VALUE);
            msg.setJMSCorrelationID(message.getConnectorMessageId());


            msg.setStringProperty("service", msgDetails.getService().getService());
            msg.setStringProperty("serviceType", message.getMessageDetails().getService().getServiceType());
            msg.setStringProperty("action", message.getMessageDetails().getAction().getAction());
            //message ids
            msg.setStringProperty("messageId", msgDetails.getEbmsMessageId());
            msg.setStringProperty("conversationId", msgDetails.getConversationId());
            msg.setStringProperty("refToMessageId", msgDetails.getRefToMessageId());
            //from party
            msg.setStringProperty("fromPartyId", msgDetails.getFromParty().getPartyId());
            msg.setStringProperty("fromPartyIdType", msgDetails.getFromParty().getPartyIdType());
            msg.setStringProperty("fromPartyRole", msgDetails.getFromParty().getRole());
            //to party
            msg.setStringProperty("toPartyId", msgDetails.getToParty().getPartyId());
            msg.setStringProperty("toPartyIdType", msgDetails.getToParty().getPartyIdType());
            msg.setStringProperty("toPartyRole", msgDetails.getToParty().getRole());
            //final recipient original sender
            msg.setStringProperty("originalSender", msgDetails.getOriginalSender());
            msg.setStringProperty("finalRecipient", msgDetails.getFinalRecipient());



            return msg;
        });
    }




}


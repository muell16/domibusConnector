package eu.ecodex.connector.gwc.helper;

import javax.activation.DataHandler;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.AgreementRef;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import backend.ecodex.org.SendRequest;
import backend.ecodex.org.SendResponse;
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class SendMessageHelper {

    private static final String AGREEMENT_REF = "Agr123";
    private static final String PMODE = "EE_Form_A";

    private ECodexConnectorProperties connectorProperties;
    private ECodexConnectorPersistenceService persistenceService;

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void buildMessage(SendRequest request, Messaging ebMSHeaderInfo, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = new UserMessage();

        buildSendRequestAndPayloadInfo(userMessage, request, message);

        userMessage.setPartyInfo(buildPartyInfo(message.getMessageDetails()));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        info.setRefToMessageId(message.getMessageDetails().getRefToMessageId());

        userMessage.setMessageInfo(info);

        ebMSHeaderInfo.getUserMessage().add(userMessage);
    }

    private void buildSendRequestAndPayloadInfo(UserMessage userMessage, SendRequest request, Message message)
            throws ECodexConnectorGatewayWebserviceClientException {
        PayloadInfo pli = new PayloadInfo();

        if (message.getMessageContent() != null) {
            DataHandler contentHandler = new DataHandler(new String(message.getMessageContent().getECodexContent()),
                    "text/xml");
            request.getPayload().add(contentHandler);
            pli.getPartInfo().add(buildPartInfo("ECodexContentXML"));
        }

        if (message.getAttachments() != null) {
            for (MessageAttachment attachment : message.getAttachments()) {
                DataHandler dh = new DataHandler(attachment.getAttachment(), attachment.getMimeType());
                request.getPayload().add(dh);
                pli.getPartInfo().add(buildPartInfo(attachment.getName()));
            }
        }

        if (message.getConfirmations() != null) {
            // Pick only the last produced evidence
            DataHandler evidenceHandler = new DataHandler(new String(message.getConfirmations()
                    .get(message.getConfirmations().size() - 1).getEvidence()), "text/xml");
            request.getPayload().add(evidenceHandler);
            pli.getPartInfo().add(
                    buildPartInfo(message.getConfirmations().get(message.getConfirmations().size() - 1)
                            .getEvidenceType().toString()));
        }

        userMessage.setPayloadInfo(pli);

        if (request.getPayload().isEmpty()) {
            throw new ECodexConnectorGatewayWebserviceClientException("No payload to send. Message without content?");
        }
    }

    private PartInfo buildPartInfo(String description) {
        PartInfo pi = new PartInfo();
        Description desc = new Description();
        desc.setValue(description);
        pi.setDescription(desc);

        return pi;
    }

    public void extractEbmsMessageIdAndPersistIntoDB(SendResponse response, Message message) {
        if (response.getMessageID() != null && !response.getMessageID().isEmpty()) {
            String ebmsMessageId = response.getMessageID().get(0);
            if (!ebmsMessageId.isEmpty()) {
                message.getDbMessage().setEbmsMessageId(ebmsMessageId);
                persistenceService.mergeMessageWithDatabase(message);
            }
        }
    }

    private PartyInfo buildPartyInfo(MessageDetails messageDetails) {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        partyId.setValue(connectorProperties.getGatewayName());
        from.getPartyId().add(partyId);
        from.setRole(connectorProperties.getGatewayRole());
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue(messageDetails.getToPartner().getName());
        to.getPartyId().add(partyId2);
        to.setRole(messageDetails.getToPartner().getRole());
        partyInfo.setTo(to);

        return partyInfo;
    }

    private CollaborationInfo buildCollaborationInfo(MessageDetails messageDetails) {
        CollaborationInfo info = new CollaborationInfo();

        info.setAction(messageDetails.getAction().toString());
        Service service = new Service();
        service.setValue(messageDetails.getService().toString());
        info.setService(service);

        AgreementRef ref = new AgreementRef();
        ref.setPmode(PMODE);
        ref.setValue(AGREEMENT_REF);
        info.setAgreementRef(ref);

        info.setConversationId(messageDetails.getConversationId());

        return info;
    }

}

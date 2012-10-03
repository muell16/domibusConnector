package eu.ecodex.connector.gwc;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.AgreementRef;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import backend.ecodex.org.BackendInterface;
import backend.ecodex.org.DownloadMessageFault;
import backend.ecodex.org.DownloadMessageRequest;
import backend.ecodex.org.DownloadMessageResponse;
import backend.ecodex.org.ListPendingMessagesFault;
import backend.ecodex.org.ListPendingMessagesResponse;
import backend.ecodex.org.SendMessageFault;
import backend.ecodex.org.SendRequest;
import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.MessageState;
import eu.ecodex.connector.common.MessageStateLogger;

public class ECodexConnectorGatewayWebserviceClientImpl implements ECodexConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorGatewayWebserviceClientImpl.class);

    private static final String GATEWAY_ROLE = "GW";
    private static final String AGREEMENT_REF = "Agr123";
    private static final String PMODE = "EE_Form_A";
    private static final String SERVICE = "EPO";
    private static final String ACTION = "Form_A";

    private BackendInterface gatewayBackendWebservice;
    private MessageStateLogger messageStateLogger;
    private ECodexConnectorProperties connectorProperties;

    public void setGatewayBackendWebservice(BackendInterface gatewayBackendWebservice) {
        this.gatewayBackendWebservice = gatewayBackendWebservice;
    }

    public void setMessageStateLogger(MessageStateLogger messageStateLogger) {
        this.messageStateLogger = messageStateLogger;
    }

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    @Override
    public void sendMessageWithReference(byte[] content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(byte[] content) {
        SendRequest request = null;
        try {
            request = buildSendRequest(content);
        } catch (IOException e1) {
            LOGGER.error("Could not build sendRequest! ", e1);
        }

        Messaging ebMSHeaderInfo = new Messaging();

        UserMessage userMessage = new UserMessage();

        userMessage.setPartyInfo(buildPartyInfo());

        userMessage.setCollaborationInfo(buildCollaborationInfo());

        ebMSHeaderInfo.getUserMessage().add(userMessage);

        try {
            gatewayBackendWebservice.sendMessage(request, ebMSHeaderInfo);
        } catch (SendMessageFault e) {
            LOGGER.error("sendMessage failed: ", e);
        }
    }

    @Override
    public String[] listPendingMessages() {
        LOGGER.debug("started... ");
        try {
            ListPendingMessagesResponse response = gatewayBackendWebservice
                    .listPendingMessages(createEmptyListPendingMessagesRequest());

            LOGGER.debug(response.getMessageID().toString());
            return response.getMessageID().toArray(new String[response.getMessageID().size()]);
        } catch (ListPendingMessagesFault e) {
            LOGGER.error("Could not execute! ", e);
        }
        return null;

    }

    @Override
    public byte[] downloadMessage(String messageId) {

        Holder<DownloadMessageResponse> response = new Holder<DownloadMessageResponse>();
        Holder<Messaging> ebMSHeader = new Holder<Messaging>();

        DownloadMessageRequest request = new DownloadMessageRequest();
        request.setMessageID(messageId);

        try {
            gatewayBackendWebservice.downloadMessage(request, response, ebMSHeader);
            messageStateLogger.logMessageState(request.getMessageID(), MessageState.RECEIVED);
            LOGGER.debug("Successfully downloaded message with id [{}]", request.getMessageID());
        } catch (DownloadMessageFault e) {
            LOGGER.error("Could not execute! ", e);
        }

        if (response.value != null && response.value.getPayload() != null && !response.value.getPayload().isEmpty()) {
            try {
                ByteArrayInputStream is = (ByteArrayInputStream) response.value.getPayload().get(0).getContent();
                byte[] b = new byte[is.available()];
                is.read(b);

                return b;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("Message {} contains no payload!", request.getMessageID());
        }

        return null;
    }

    private Element createEmptyListPendingMessagesRequest() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document document = db.newDocument();
        Element request = document.createElement("listPendingMessagesRequest");

        return request;
    }

    private SendRequest buildSendRequest(byte[] content) throws IOException {
        SendRequest request = new SendRequest();

        DataHandler handler = new DataHandler(content, "text/xml");

        request.getPayload().add(handler);

        return request;
    }

    private PartyInfo buildPartyInfo() {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        partyId.setValue(connectorProperties.getGatewayAsSenderPartyId());
        from.getPartyId().add(partyId);
        from.setRole(GATEWAY_ROLE);
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue("");
        to.getPartyId().add(partyId2);
        to.setRole(GATEWAY_ROLE);
        partyInfo.setTo(to);

        return partyInfo;
    }

    private CollaborationInfo buildCollaborationInfo() {
        CollaborationInfo info = new CollaborationInfo();

        info.setAction(ACTION);
        Service service = new Service();
        service.setValue(SERVICE);
        info.setService(service);

        AgreementRef ref = new AgreementRef();
        ref.setPmode(PMODE);
        ref.setValue(AGREEMENT_REF);
        info.setAgreementRef(ref);

        return info;
    }

}

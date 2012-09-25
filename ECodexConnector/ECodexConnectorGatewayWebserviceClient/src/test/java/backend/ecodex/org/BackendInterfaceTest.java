package backend.ecodex.org;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.AgreementRef;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test/context/testContext.xml" })
public class BackendInterfaceTest extends AbstractJUnit4SpringContextTests {

    Logger LOGGER = LoggerFactory.getLogger(BackendInterfaceTest.class);

    private static final String TO = "EE";
    private static final String FROM = "AT";
    private static final String ROLE = "GW";
    private static final String AGREEMENT_REF = "Agr123";
    private static final String PMODE = "EE_Form_A";
    private static final String SERVICE = "EPO";
    private static final String ACTION = "Form_A";

    @Autowired
    private BackendInterface sendingGatewayClient;

    @Autowired
    private BackendInterface receivingGatewayClient;

    @Test
    public void testSendMessage() {
        SendRequest request = null;
        try {
            request = buildSendRequest();
        } catch (IOException e1) {
            LOGGER.error("Could not build sendRequest! ", e1);
        }

        Messaging ebMSHeaderInfo = new Messaging();

        UserMessage userMessage = new UserMessage();

        userMessage.setPartyInfo(buildPartyInfo());

        userMessage.setCollaborationInfo(buildCollaborationInfo());

        ebMSHeaderInfo.getUserMessage().add(userMessage);

        try {
            sendingGatewayClient.sendMessage(request, ebMSHeaderInfo);
        } catch (SendMessageFault e) {
            LOGGER.error("sendMessage failed: ", e);
        }
    }

    @Test
    public void testListPendingMessagesAndDownload() {

        ListPendingMessagesResponse response = null;
        try {
            response = receivingGatewayClient.listPendingMessages(createEmptyListPendingMessagesRequest());
        } catch (ListPendingMessagesFault e) {
            e.printStackTrace();
        }

        if (response != null && !response.getMessageID().isEmpty()) {
            LOGGER.info("Found {} messages pending...", response.getMessageID().size());
            for (String messageId : response.getMessageID()) {
                downloadMessage(messageId);
            }
        }
    }

    private void downloadMessage(String messageId) {
        LOGGER.info("Try to download message with the id [{}]", messageId);

        Holder<DownloadMessageResponse> response = new Holder<DownloadMessageResponse>();
        Holder<Messaging> ebMSHeader = new Holder<Messaging>();

        DownloadMessageRequest request = new DownloadMessageRequest();
        request.setMessageID(messageId);

        try {
            receivingGatewayClient.downloadMessage(request, response, ebMSHeader);
        } catch (DownloadMessageFault e) {
            e.printStackTrace();
        }

        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);

        Assert.assertEquals(userMessage.getPartyInfo().getFrom().getPartyId().get(0).getValue(), FROM);
        Assert.assertEquals(userMessage.getPartyInfo().getFrom().getRole(), ROLE);
        Assert.assertEquals(userMessage.getPartyInfo().getTo().getPartyId().get(0).getValue(), TO);
        Assert.assertEquals(userMessage.getPartyInfo().getTo().getRole(), ROLE);
        Assert.assertEquals(userMessage.getCollaborationInfo().getAction(), ACTION);
        Assert.assertEquals(userMessage.getCollaborationInfo().getService().getValue(), SERVICE);
        Assert.assertEquals(userMessage.getCollaborationInfo().getAgreementRef().getPmode(), PMODE);
        Assert.assertEquals(userMessage.getCollaborationInfo().getAgreementRef().getValue(), AGREEMENT_REF);

        try {
            ByteArrayInputStream is = (ByteArrayInputStream) response.value.getPayload().get(0).getContent();
            byte[] b = new byte[is.available()];
            is.read(b);
            String s = new String(b);

            Assert.assertEquals(getPayloadAsString(), s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SendRequest buildSendRequest() throws IOException {
        SendRequest request = new SendRequest();

        DataHandler handler = new DataHandler(getPayloadAsString(), "text/xml");

        request.getPayload().add(handler);

        return request;
    }

    private String getPayloadAsString() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("payload.xml");

        byte[] content = IOUtils.toByteArray(is);

        return new String(content);
    }

    private PartyInfo buildPartyInfo() {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        partyId.setValue(FROM);
        from.getPartyId().add(partyId);
        from.setRole(ROLE);
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue(TO);
        to.getPartyId().add(partyId2);
        to.setRole(ROLE);
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
}

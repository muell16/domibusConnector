package eu.ecodex.connector.gwc.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageDetails;

public class CommonMessageHelper {

    public static final String XML_MIME_TYPE = "text/xml";
    public static final String APPLICATION_MIME_TYPE = "application/octet-stream";
    public static final String CONTENT_PDF_NAME = "ContentPDF";
    public static final String CONTENT_XML_NAME = "ECodexContentXML";

    private ECodexConnectorProperties connectorProperties;
    private ECodexConnectorPersistenceService persistenceService;

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void persistEbmsMessageIdIntoDatabase(String ebmsMessageId, Message message) {
        if (!ebmsMessageId.isEmpty()) {
            message.getDbMessage().setEbmsMessageId(ebmsMessageId);
            persistenceService.mergeMessageWithDatabase(message);
        }
    }

    public void addPartInfoToPayloadInfo(String name, UserMessage userMessage, String href) {

        PartInfo pi = new PartInfo();

        pi.setHref(href);

        Description desc = new Description();

        desc.setValue(name);

        pi.setDescription(desc);

        userMessage.getPayloadInfo().getPartInfo().add(pi);
    }

    public Element createEmptyListPendingMessagesRequest() {
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

    public DataHandler buildByteArrayDataHandler(byte[] data, String mimeType) {
        DataSource ds = new ByteArrayDataSource(data, mimeType);
        DataHandler dh = new DataHandler(ds);

        return dh;
    }

    public UserMessage buildUserMessage(Message message) {

        UserMessage userMessage = new UserMessage();

        userMessage.setMessageProperties(buildMessageProperties(message));

        userMessage.setPartyInfo(buildPartyInfo(message.getMessageDetails()));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        info.setRefToMessageId(message.getMessageDetails().getRefToMessageId());

        userMessage.setMessageInfo(info);

        userMessage.setPayloadInfo(buildEmptyPayloadInfo());

        return userMessage;
    }

    private PayloadInfo buildEmptyPayloadInfo() {
        PayloadInfo pli = new PayloadInfo();
        return pli;
    }

    private MessageProperties buildMessageProperties(Message message) {
        if (message.getMessageDetails().getFinalRecipient() == null
                && message.getMessageDetails().getOriginalSender() == null) {
            return null;
        }
        MessageProperties mp = new MessageProperties();

        if (message.getMessageDetails().getFinalRecipient() != null) {

            Property finalRecipient = new Property();
            finalRecipient.setName("finalRecipient");
            finalRecipient.setValue(message.getMessageDetails().getFinalRecipient());
            mp.getProperty().add(finalRecipient);
        }

        if (message.getMessageDetails().getOriginalSender() != null) {
            Property originalSender = new Property();
            originalSender.setName("originalSender");
            originalSender.setValue(message.getMessageDetails().getOriginalSender());
            mp.getProperty().add(originalSender);
        }

        return mp;
    }

    private PartyInfo buildPartyInfo(MessageDetails messageDetails) {
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();
        if (messageDetails.getFromPartner() != null) {
            partyId.setValue(messageDetails.getFromPartner().getName());
            from.setRole(messageDetails.getFromPartner().getRole());
        } else {
            partyId.setValue(connectorProperties.getGatewayName());
            from.setRole(connectorProperties.getGatewayRole());
        }
        from.getPartyId().add(partyId);
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

        info.setConversationId(messageDetails.getConversationId());

        // AgreementRef ref = new AgreementRef();
        // ref.setValue("dummy");
        // info.setAgreementRef(ref);

        return info;
    }

    public static final String parseRootElement(byte[] data) throws SAXException, IOException,
            ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document document = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(data));

        return getRootElementName(document);
    }

    private static String getRootElementName(final Object obj) {
        if (obj == null || !(obj instanceof Node)) {
            throw new IllegalArgumentException("Argument must be of type Node.");
        }

        Node node = (Node) obj;
        // Read just the element name without the namespace prefix
        String rootElementName = node.getFirstChild().getLocalName();
        return rootElementName;
    }
}

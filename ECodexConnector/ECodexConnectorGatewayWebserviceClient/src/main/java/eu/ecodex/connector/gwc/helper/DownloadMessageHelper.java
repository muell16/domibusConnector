package eu.ecodex.connector.gwc.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import backend.ecodex.org._1_0.DownloadMessageResponse;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ECodexConnectorPayload;
import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.enums.ServiceEnum;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class DownloadMessageHelper {

    public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = convertUserMessageToMessageDetails(userMessage);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        List<DataHandler> payload = response.value.getPayload();
        if (payload != null && !payload.isEmpty() && userMessage.getPayloadInfo() != null
                && userMessage.getPayloadInfo().getPartInfo() != null
                && userMessage.getPayloadInfo().getPartInfo().size() == payload.size()) {

            for (DataHandler dh : payload) {
                String rootElement = null;
                try {
                    rootElement = parseRootElement(dh);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!rootElement.equals("ecodexConnectorPayload")) {
                    continue;
                }

                ECodexConnectorPayload payloadType = extractPayload(dh);

                switch (payloadType.getPayloadType()) {
                case CONTENT_XML:
                    extractMessageContent(payloadType.getPayloadContent(), message.getMessageContent());
                    break;
                case EVIDENCE:
                    message.addConfirmation(extractMessageConfirmation(payloadType));
                    break;
                case ATTACHMENT:
                    message.addAttachment(extractAttachments(payloadType));
                    break;
                default:
                    throw new ECodexConnectorGatewayWebserviceClientException("Payload type unknown: "
                            + payloadType.getPayloadType().name());
                }
            }
        } else {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Payload is empty or payload size does not match PayloadInfo size!");
        }

        return message;
    }

    private ECodexConnectorPayload extractPayload(DataHandler dh)
            throws ECodexConnectorGatewayWebserviceClientException {
        // byte[] dhContent = null;
        // try {
        // dhContent = convertDataHandlerContentToByteArray(dh);
        // } catch (IOException e) {
        // throw new ECodexConnectorGatewayWebserviceClientException(
        // "Content of DataHandler of payload could not be read out as byte[]!",
        // e);
        // }

        ECodexConnectorPayload payload;
        try {
            payload = byteArrayToECodexConnectorPayload(dh);
        } catch (Exception e) {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Could not transform data handler content to connector payload object!", e);
        }

        return payload;

    }

    private ECodexConnectorPayload byteArrayToECodexConnectorPayload(DataHandler dh) throws JAXBException,
            SAXException, IOException, ParserConfigurationException {

        InputStream is = dh.getInputStream();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = factory.newDocumentBuilder().parse(is);

        JAXBContext ctx = JAXBContext.newInstance(ECodexConnectorPayload.class);

        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        JAXBElement<ECodexConnectorPayload> jaxbElement = unmarshaller
                .unmarshal(document, ECodexConnectorPayload.class);

        return jaxbElement.getValue();
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

    private void extractMessageContent(DataHandler dh, MessageContent content) {
        try {
            content.setECodexContent(convertDataHandlerContentToByteArray(dh));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MessageConfirmation extractMessageConfirmation(ECodexConnectorPayload payloadType) {
        MessageConfirmation confirmation = new MessageConfirmation();
        ECodexEvidenceType evidenceType = ECodexEvidenceType.valueOf(payloadType.getName());
        confirmation.setEvidenceType(evidenceType);
        try {
            confirmation.setEvidence(convertDataHandlerContentToByteArray(payloadType.getPayloadContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return confirmation;
    }

    private MessageAttachment extractAttachments(ECodexConnectorPayload payloadType) {
        try {
            byte[] b = convertDataHandlerContentToByteArray(payloadType.getPayloadContent());

            MessageAttachment attachment = new MessageAttachment();
            attachment.setAttachment(b);
            attachment.setMimeType(payloadType.getMimeType());
            attachment.setName(payloadType.getName());
            return attachment;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] convertDataHandlerContentToByteArray(DataHandler dh) throws IOException {
        InputStream is = dh.getInputStream();
        byte[] b = new byte[is.available()];
        is.read(b);
        return b;
    }

    private MessageDetails convertUserMessageToMessageDetails(UserMessage userMessage) {
        MessageDetails details = new MessageDetails();

        details.setEbmsMessageId(userMessage.getMessageInfo().getMessageId());
        details.setRefToMessageId(userMessage.getMessageInfo().getRefToMessageId());
        details.setConversationId(userMessage.getCollaborationInfo().getConversationId());

        String actionString = userMessage.getCollaborationInfo().getAction();
        try {
            ActionEnum action = ActionEnum.valueOf(actionString);
            details.setAction(action);
        } catch (IllegalArgumentException e) {
            // LOGGER.error("No action {} found!", actionString);
        }

        String serviceString = userMessage.getCollaborationInfo().getService().getValue();
        try {
            ServiceEnum service = ServiceEnum.valueOf(serviceString);
            details.setService(service);
        } catch (IllegalArgumentException e) {
            // LOGGER.error("No service {} found!", actionString);
        }

        From from = userMessage.getPartyInfo().getFrom();
        PartnerEnum fromPartner = PartnerEnum.findValue(from.getPartyId().get(0).getValue(), from.getRole());
        details.setFromPartner(fromPartner);

        To to = userMessage.getPartyInfo().getTo();
        PartnerEnum toPartner = PartnerEnum.findValue(to.getPartyId().get(0).getValue(), to.getRole());
        details.setToPartner(toPartner);

        MessageProperties mp = userMessage.getMessageProperties();
        if (mp != null) {
            List<Property> properties = mp.getProperty();
            if (properties != null && !properties.isEmpty()) {
                for (Property property : properties) {
                    if (property.getName().equals("finalRecipient")) {
                        details.setFinalRecipient(property.getValue());
                    }
                    if (property.getName().equals("originalSender")) {
                        details.setOriginalSender(property.getValue());
                    }
                }
            }
        }

        return details;
    }

    private final String parseRootElement(DataHandler dh) throws SAXException, IOException,
            ParserConfigurationException {
        byte[] data = convertDataHandlerContentToByteArray(dh);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document document = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(data));

        return getRootElementName(document);
    }

    private String getRootElementName(final Object obj) {
        if (obj == null || !(obj instanceof Node)) {
            throw new IllegalArgumentException("Argument must be of type Node.");
        }

        Node node = (Node) obj;
        // Read just the element name without the namespace prefix
        String rootElementName = node.getFirstChild().getLocalName();
        return rootElementName;
    }
}

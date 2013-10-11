package eu.ecodex.connector.gwc._1_0;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import backend.ecodex.org._1_0.DownloadMessageResponse;
import eu.e_codex.namespace.ecodex.ecodexconnectorpayload.v1.ECodexConnectorPayload;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.gwc.util.CommonMessageHelper;

public class DownloadMessageHelper {

    private CommonMessageHelper commonMessageHelper;

    public void setCommonMessageHelper(CommonMessageHelper commonMessageHelper) {
        this.commonMessageHelper = commonMessageHelper;
    }

    public Message convertDownloadIntoMessage(Holder<DownloadMessageResponse> response, Holder<Messaging> ebMSHeader)
            throws ECodexConnectorGatewayWebserviceClientException {
        UserMessage userMessage = ebMSHeader.value.getUserMessage().get(0);
        MessageDetails details = commonMessageHelper.convertUserMessageToMessageDetails(userMessage);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        List<DataHandler> payload = response.value.getPayload();
        if (payload != null && !payload.isEmpty() && userMessage.getPayloadInfo() != null
                && userMessage.getPayloadInfo().getPartInfo() != null
                && userMessage.getPayloadInfo().getPartInfo().size() == payload.size()) {

            for (DataHandler dh : payload) {
                String rootElement = null;
                try {
                    byte[] data = convertDataHandlerContentToByteArray(dh);
                    rootElement = CommonMessageHelper.parseRootElement(data);
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
                case CONTENT_PDF:
                    extractMessageContentPDF(payloadType.getPayloadContent(), message.getMessageContent());
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

    private void extractMessageContent(DataHandler dh, MessageContent content) {
        try {
            content.setECodexContent(convertDataHandlerContentToByteArray(dh));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractMessageContentPDF(DataHandler dh, MessageContent content) {
        try {
            content.setPdfDocument(convertDataHandlerContentToByteArray(dh));
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

}

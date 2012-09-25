package eu.ecodex.connector.gwc;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import backend.ecodex.org.BackendInterface;
import backend.ecodex.org.DownloadMessageFault;
import backend.ecodex.org.DownloadMessageRequest;
import backend.ecodex.org.DownloadMessageResponse;
import backend.ecodex.org.ListPendingMessagesFault;
import backend.ecodex.org.ListPendingMessagesResponse;
import eu.ecodex.connector.common.MessageState;
import eu.ecodex.connector.common.MessageStateLogger;

public class ECodexConnectorGatewayWebserviceClientImpl implements ECodexConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorGatewayWebserviceClientImpl.class);

    private BackendInterface gatewayBackendWebservice;
    private MessageStateLogger messageStateLogger;

    public void setGatewayBackendWebservice(BackendInterface gatewayBackendWebservice) {
        this.gatewayBackendWebservice = gatewayBackendWebservice;
    }

    public void setMessageStateLogger(MessageStateLogger messageStateLogger) {
        this.messageStateLogger = messageStateLogger;
    }

    @Override
    public void sendMessageWithReference(byte[] content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(byte[] content) {
        // TODO Auto-generated method stub

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
            messageStateLogger.logMessageState(messageId, MessageState.SENT_TO_GATEWAY);
            LOGGER.debug(ebMSHeader.value.toString());
        } catch (DownloadMessageFault e) {
            LOGGER.error("Could not execute! ", e);
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

}

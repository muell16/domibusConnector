package eu.ecodex.connector.gwc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cxf.common.util.StringUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartProperties;
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

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageDetails;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.discovery.DiscoveryClient;
import eu.ecodex.discovery.DiscoveryException;
import eu.ecodex.discovery.Metadata;
import eu.ecodex.discovery.names.ECodexNamingScheme;

public class CommonMessageHelper {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CommonMessageHelper.class);

    private static final String ENDPOINT_ADDRESS_PROPERTY_NAME = "EndpointAddress";
    private static final String ORIGINAL_SENDER_PROPERTY_NAME = "originalSender";
    private static final String FINAL_RECIPIENT_PROPERTY_NAME = "finalRecipient";
    private static final String ORIGINAL_MESSAGE_ID = "originalMessageId";
    public static final String XML_MIME_TYPE = "text/xml";
    public static final String APPLICATION_MIME_TYPE = "application/octet-stream";
    public static final String CONTENT_PDF_NAME = "ContentPDF";
    public static final String CONTENT_XML_NAME = "ECodexContentXML";
    public static final String PARTPROPERTY_NAME = "description";
    public static final String BODYLOAD_HREF_PREFIX = "#";

    private CommonConnectorProperties connectorProperties;
    private DomibusConnectorPersistenceService persistenceService;
    private DiscoveryClient dynamicDiscoveryClient;

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setDynamicDiscoveryClient(DiscoveryClient dynamicDiscoveryClient) {
        this.dynamicDiscoveryClient = dynamicDiscoveryClient;
    }

    public void persistEbmsMessageIdIntoDatabase(String ebmsMessageId, Message message) {
        if (!ebmsMessageId.isEmpty()) {
            message.getDbMessage().setEbmsMessageId(ebmsMessageId);
            persistenceService.mergeMessageWithDatabase(message);
        }
    }

    public String generateCID(String name) {
        String cid = name + UUID.randomUUID().toString();

        return cid;
    }

    public void addPartInfoToPayloadInfo(String name, String value, UserMessage userMessage, String href) {

        PartInfo pi = new PartInfo();

        pi.setHref(href);

        PartProperties props = new PartProperties();

        Property prop = new Property();
        prop.setName(name);
        prop.setValue(value);
        props.getProperty().add(prop);

        pi.setPartProperties(props);

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

    public UserMessage buildUserMessage(Message message) throws ECodexConnectorGatewayWebserviceClientException {

        UserMessage userMessage = new UserMessage();

        userMessage.setMessageProperties(buildMessageProperties(message));

        userMessage.setPartyInfo(buildPartyInfo(message));

        userMessage.setCollaborationInfo(buildCollaborationInfo(message.getMessageDetails()));

        MessageInfo info = new MessageInfo();

        if (message.getMessageDetails().getRefToMessageId() != null
                && !message.getMessageDetails().getRefToMessageId().isEmpty())
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
            finalRecipient.setName(FINAL_RECIPIENT_PROPERTY_NAME);
            finalRecipient.setValue(message.getMessageDetails().getFinalRecipient());
            mp.getProperty().add(finalRecipient);
        }

        if (message.getMessageDetails().getOriginalSender() != null) {
            Property originalSender = new Property();
            originalSender.setName(ORIGINAL_SENDER_PROPERTY_NAME);
            originalSender.setValue(message.getMessageDetails().getOriginalSender());
            mp.getProperty().add(originalSender);
        }

        if (connectorProperties.isUseDynamicDiscovery()) {
            String endpointAddress = null;
            try {
                endpointAddress = resolveEndpointAddress(message.getMessageDetails());
            } catch (DiscoveryException e) {
                e.printStackTrace();
            }
            if (endpointAddress != null) {
                Property ddEndpointAddress = new Property();
                ddEndpointAddress.setName(ENDPOINT_ADDRESS_PROPERTY_NAME);
                ddEndpointAddress.setValue(endpointAddress);
                mp.getProperty().add(ddEndpointAddress);
            }
        }

        return mp;
    }

    private String resolveEndpointAddress(MessageDetails messageDetails) throws DiscoveryException {

        final SortedMap<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put(Metadata.PROCESS_ID, messageDetails.getService().getService());
        metadata.put(Metadata.DOCUMENT_OR_ACTION_ID, messageDetails.getAction().getAction());
        metadata.put(Metadata.SENDING_END_ENTITY_ID, messageDetails.getOriginalSender());
        metadata.put(Metadata.RECEIVING_END_ENTITY_ID, messageDetails.getFinalRecipient());
        metadata.put(Metadata.COMMUNITY, connectorProperties.getDynamicDiscoveryCommunity());
        metadata.put(Metadata.ENVIRONMENT, connectorProperties.getDynamicDiscoveryEnvironment());
        metadata.put(Metadata.COUNTRY_CODE_OR_EU, messageDetails.getToParty().getPartyId());
        metadata.put(Metadata.NORMALISATION_ALGORITHM, connectorProperties.getDynamicDiscoveryNormalisationAlgorithm()); // use
                                                                                                                         // hashed
        // identifiers
        metadata.put(Metadata.SUFFIX, "bdxl.e-codex.eu");
        metadata.put(Metadata.NAMING_SCHEME, new ECodexNamingScheme());

        LOGGER.info(
                "Calling dynamic discovery with parameters processId={}, documentOrActionId={}, sendingEndEntityId={}, receivingEndEntityId={}, "
                        + "community={}, environment={}, countryCode={}, normalisationAlgorithm={}, suffix={}",
                new Object[] { metadata.get(Metadata.PROCESS_ID), metadata.get(Metadata.DOCUMENT_OR_ACTION_ID),
                        metadata.get(Metadata.SENDING_END_ENTITY_ID), metadata.get(Metadata.RECEIVING_END_ENTITY_ID),
                        metadata.get(Metadata.COMMUNITY), metadata.get(Metadata.ENVIRONMENT),
                        metadata.get(Metadata.COUNTRY_CODE_OR_EU), metadata.get(Metadata.NORMALISATION_ALGORITHM),
                        metadata.get(Metadata.SUFFIX) });

        dynamicDiscoveryClient.resolveMetadata(metadata);

        final String endpointAddress = (String) metadata.get(Metadata.ENDPOINT_ADDRESS);

        if (endpointAddress == null || endpointAddress.isEmpty()) {
            LOGGER.info("Dynamic discovery could not find an endpoint address!");
        }

        return endpointAddress;
    }

    private PartyInfo buildPartyInfo(Message message) throws ECodexConnectorGatewayWebserviceClientException {
        MessageDetails details = message.getMessageDetails();
        PartyInfo partyInfo = new PartyInfo();

        From from = new From();
        PartyId partyId = new PartyId();

        if (details.getFromParty() == null) {
            DomibusConnectorParty fromParty = persistenceService.getParty(connectorProperties.getGatewayName(),
                    connectorProperties.getGatewayRole());
            if (fromParty == null) {
                throw new ECodexConnectorGatewayWebserviceClientException(
                        "Could not find own configured party in database!");
            }
            details.setFromParty(fromParty);
            persistenceService.mergeMessageWithDatabase(message);
        }

        partyId.setValue(details.getFromParty().getPartyId());
        partyId.setType(details.getFromParty().getPartyIdType());
        from.setRole(details.getFromParty().getRole());

        from.getPartyId().add(partyId);
        partyInfo.setFrom(from);

        To to = new To();
        PartyId partyId2 = new PartyId();
        partyId2.setValue(details.getToParty().getPartyId());
        partyId2.setType(details.getToParty().getPartyIdType());
        to.getPartyId().add(partyId2);
        to.setRole(details.getToParty().getRole());
        partyInfo.setTo(to);

        return partyInfo;
    }

    private CollaborationInfo buildCollaborationInfo(MessageDetails messageDetails) {
        CollaborationInfo info = new CollaborationInfo();

        info.setAction(messageDetails.getAction().getAction());
        Service service = new Service();
        service.setValue(messageDetails.getService().getService());
        info.setService(service);

        info.setConversationId(messageDetails.getConversationId());

        return info;
    }

    public boolean isMessageEvidence(Message message) {
        return message.getMessageDetails().getAction().getAction().equals("RelayREMMDAcceptanceRejection")
                || message.getMessageDetails().getAction().getAction().equals("DeliveryNonDeliveryToRecipient")
                || message.getMessageDetails().getAction().getAction().equals("RetrievalNonRetrievalToRecipient");
    }

    public MessageDetails convertUserMessageToMessageDetails(UserMessage userMessage)
            throws ECodexConnectorGatewayWebserviceClientException {
        MessageDetails details = new MessageDetails();

        details.setEbmsMessageId(userMessage.getMessageInfo().getMessageId());
        details.setRefToMessageId(userMessage.getMessageInfo().getRefToMessageId());
        details.setConversationId(userMessage.getCollaborationInfo().getConversationId());

        String actionString = userMessage.getCollaborationInfo().getAction();
        if (actionString.contains(":")) {
            actionString = actionString.substring(actionString.lastIndexOf(":") + 1);
        }
        DomibusConnectorAction action = persistenceService.getAction(actionString);
        if (action == null) {
            throw new ECodexConnectorGatewayWebserviceClientException("Could not find Action in database for value "
                    + actionString);
        }
        details.setAction(action);

        String serviceString = userMessage.getCollaborationInfo().getService().getValue();
        if (serviceString.contains(":")) {
            serviceString = serviceString.substring(serviceString.lastIndexOf(":") + 1);
        }
        DomibusConnectorService service = persistenceService.getService(serviceString);
        if (service == null) {
            throw new ECodexConnectorGatewayWebserviceClientException("Could not find Service in database for value "
                    + serviceString);
        }
        details.setService(service);

        From from = userMessage.getPartyInfo().getFrom();
        String fromPartnerId = from.getPartyId().get(0).getValue();
        if (fromPartnerId.contains(":")) {
            fromPartnerId = fromPartnerId.substring(fromPartnerId.lastIndexOf(":") + 1);
        }
        DomibusConnectorParty fromPartner = persistenceService.getParty(fromPartnerId, from.getRole());
        if (fromPartner == null) {
            throw new ECodexConnectorGatewayWebserviceClientException("Could not find Party in database for PartyId "
                    + from.getPartyId().get(0).getValue() + " and Role " + from.getRole() + " as FromParty");
        }
        details.setFromParty(fromPartner);

        To to = userMessage.getPartyInfo().getTo();
        String toPartnerId = to.getPartyId().get(0).getValue();
        if (toPartnerId.contains(":")) {
            toPartnerId = toPartnerId.substring(toPartnerId.lastIndexOf(":") + 1);
        }
        DomibusConnectorParty toPartner = persistenceService.getParty(toPartnerId, to.getRole());
        if (toPartner == null) {
            throw new ECodexConnectorGatewayWebserviceClientException("Could not find Party in database for PartyId "
                    + to.getPartyId().get(0).getValue() + " and Role " + to.getRole() + " as ToParty");
        }
        details.setToParty(toPartner);

        MessageProperties mp = userMessage.getMessageProperties();
        if (mp != null) {
            List<Property> properties = mp.getProperty();
            if (properties != null && !properties.isEmpty()) {
                for (Property property : properties) {
                    if (property.getName().equals(FINAL_RECIPIENT_PROPERTY_NAME)) {
                        details.setFinalRecipient(property.getValue());
                    }
                    if (property.getName().equals(ORIGINAL_SENDER_PROPERTY_NAME)) {
                        details.setOriginalSender(property.getValue());
                    }
                    if (property.getName().equals(ORIGINAL_MESSAGE_ID)
                            && StringUtils.isEmpty(details.getRefToMessageId())
                            && !StringUtils.isEmpty(property.getValue())) {
                        details.setRefToMessageId(property.getValue());
                    }
                }
            }
        }

        return details;
    }

    public String printXML(final Object object, final Class<?>... initializationClasses) throws JAXBException,
            IOException {
        JAXBContext ctx = JAXBContext.newInstance(initializationClasses);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Marshaller marshaller = ctx.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, byteArrayOutputStream);

        byte[] buffer = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        return new String(buffer, "UTF-8");
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

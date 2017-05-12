
package connector.domibus.eu.domibusconnectorgatewayservice._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the connector.domibus.eu.domibusconnectorgatewayservice._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DomibusConnectorMessage_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "DomibusConnectorMessage");
    private final static QName _DomibusConnectorAcknowledgement_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "DomibusConnectorAcknowledgement");
    private final static QName _DomibusConnectorGatewayServiceFault_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "DomibusConnectorGatewayServiceFault");
    private final static QName _PendingMessagesRequest_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "PendingMessagesRequest");
    private final static QName _DomibusConnectorMessages_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "DomibusConnectorMessages");
    private final static QName _MessageStatusRequest_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "MessageStatusRequest");
    private final static QName _MessageErrorsRequest_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "MessageErrorsRequest");
    private final static QName _MessageErrors_QNAME = new QName("http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", "MessageErrors");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: connector.domibus.eu.domibusconnectorgatewayservice._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MessageType }
     * 
     */
    public MessageType createMessageType() {
        return new MessageType();
    }

    /**
     * Create an instance of {@link AcknowledgementType }
     * 
     */
    public AcknowledgementType createAcknowledgementType() {
        return new AcknowledgementType();
    }

    /**
     * Create an instance of {@link MessagesType }
     * 
     */
    public MessagesType createMessagesType() {
        return new MessagesType();
    }

    /**
     * Create an instance of {@link MessageErrorLogEntriesType }
     * 
     */
    public MessageErrorLogEntriesType createMessageErrorLogEntriesType() {
        return new MessageErrorLogEntriesType();
    }

    /**
     * Create an instance of {@link MessageDetailsType }
     * 
     */
    public MessageDetailsType createMessageDetailsType() {
        return new MessageDetailsType();
    }

    /**
     * Create an instance of {@link MessagePropertiesType }
     * 
     */
    public MessagePropertiesType createMessagePropertiesType() {
        return new MessagePropertiesType();
    }

    /**
     * Create an instance of {@link MessagePropertyType }
     * 
     */
    public MessagePropertyType createMessagePropertyType() {
        return new MessagePropertyType();
    }

    /**
     * Create an instance of {@link MessagePartyInfoType }
     * 
     */
    public MessagePartyInfoType createMessagePartyInfoType() {
        return new MessagePartyInfoType();
    }

    /**
     * Create an instance of {@link MessageCollaborationInfoType }
     * 
     */
    public MessageCollaborationInfoType createMessageCollaborationInfoType() {
        return new MessageCollaborationInfoType();
    }

    /**
     * Create an instance of {@link AgreementRefType }
     * 
     */
    public AgreementRefType createAgreementRefType() {
        return new AgreementRefType();
    }

    /**
     * Create an instance of {@link PartyType }
     * 
     */
    public PartyType createPartyType() {
        return new PartyType();
    }

    /**
     * Create an instance of {@link MessageContentType }
     * 
     */
    public MessageContentType createMessageContentType() {
        return new MessageContentType();
    }

    /**
     * Create an instance of {@link MessageAttachmentType }
     * 
     */
    public MessageAttachmentType createMessageAttachmentType() {
        return new MessageAttachmentType();
    }

    /**
     * Create an instance of {@link MessageErrorLogEntryType }
     * 
     */
    public MessageErrorLogEntryType createMessageErrorLogEntryType() {
        return new MessageErrorLogEntryType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "DomibusConnectorMessage")
    public JAXBElement<MessageType> createDomibusConnectorMessage(MessageType value) {
        return new JAXBElement<MessageType>(_DomibusConnectorMessage_QNAME, MessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AcknowledgementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "DomibusConnectorAcknowledgement")
    public JAXBElement<AcknowledgementType> createDomibusConnectorAcknowledgement(AcknowledgementType value) {
        return new JAXBElement<AcknowledgementType>(_DomibusConnectorAcknowledgement_QNAME, AcknowledgementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "DomibusConnectorGatewayServiceFault")
    public JAXBElement<String> createDomibusConnectorGatewayServiceFault(String value) {
        return new JAXBElement<String>(_DomibusConnectorGatewayServiceFault_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "PendingMessagesRequest")
    public JAXBElement<String> createPendingMessagesRequest(String value) {
        return new JAXBElement<String>(_PendingMessagesRequest_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessagesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "DomibusConnectorMessages")
    public JAXBElement<MessagesType> createDomibusConnectorMessages(MessagesType value) {
        return new JAXBElement<MessagesType>(_DomibusConnectorMessages_QNAME, MessagesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "MessageStatusRequest")
    public JAXBElement<String> createMessageStatusRequest(String value) {
        return new JAXBElement<String>(_MessageStatusRequest_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "MessageErrorsRequest")
    public JAXBElement<String> createMessageErrorsRequest(String value) {
        return new JAXBElement<String>(_MessageErrorsRequest_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageErrorLogEntriesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/", name = "MessageErrors")
    public JAXBElement<MessageErrorLogEntriesType> createMessageErrors(MessageErrorLogEntriesType value) {
        return new JAXBElement<MessageErrorLogEntriesType>(_MessageErrors_QNAME, MessageErrorLogEntriesType.class, null, value);
    }

}

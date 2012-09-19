//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * <p>Java-Klasse für REMEvidenceType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="REMEvidenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}EventCode" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}EventReasons" minOccurs="0"/>
 *         &lt;element name="EvidenceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}EvidenceIssuerPolicyID" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}EvidenceIssuerDetails"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}SenderAuthenticationDetails" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}RecipientAuthenticationDetails" minOccurs="0"/>
 *         &lt;element name="EventTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="SubmissionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="ReplyTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="ReplyToAddress" type="{http://uri.etsi.org/02640/v2#}AttributedElectronicAddressType"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}SenderDetails"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}RecipientsDetails"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}RecipientsDelegatesDetails" minOccurs="0"/>
 *         &lt;element name="EvidenceRefersToRecipient" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}SenderMessageDetails" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}NotificationMessageDetails" minOccurs="0"/>
 *         &lt;element name="ForwardedToExternalSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}TransactionLogInformation" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}Extensions" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REMEvidenceType", propOrder = {
    "eventCode",
    "eventReasons",
    "evidenceIdentifier",
    "evidenceIssuerPolicyID",
    "evidenceIssuerDetails",
    "senderAuthenticationDetails",
    "recipientAuthenticationDetails",
    "eventTime",
    "submissionTime",
    "replyTo",
    "replyToAddress",
    "senderDetails",
    "recipientsDetails",
    "recipientsDelegatesDetails",
    "evidenceRefersToRecipient",
    "senderMessageDetails",
    "notificationMessageDetails",
    "forwardedToExternalSystem",
    "transactionLogInformation",
    "extensions",
    "signature"
})
public class REMEvidenceType {

    @XmlElement(name = "EventCode")
    @XmlSchemaType(name = "anyURI")
    protected String eventCode;
    @XmlElement(name = "EventReasons")
    protected EventReasonsType eventReasons;
    @XmlElement(name = "EvidenceIdentifier", required = true)
    protected String evidenceIdentifier;
    @XmlElement(name = "EvidenceIssuerPolicyID")
    protected EvidenceIssuerPolicyIDType evidenceIssuerPolicyID;
    @XmlElement(name = "EvidenceIssuerDetails", required = true)
    protected EntityDetailsType evidenceIssuerDetails;
    @XmlElement(name = "SenderAuthenticationDetails")
    protected AuthenticationDetailsType senderAuthenticationDetails;
    @XmlElement(name = "RecipientAuthenticationDetails")
    protected AuthenticationDetailsType recipientAuthenticationDetails;
    @XmlElement(name = "EventTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar eventTime;
    @XmlElement(name = "SubmissionTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar submissionTime;
    @XmlElement(name = "ReplyTo")
    protected String replyTo;
    @XmlElement(name = "ReplyToAddress")
    protected AttributedElectronicAddressType replyToAddress;
    @XmlElement(name = "SenderDetails", required = true)
    protected EntityDetailsType senderDetails;
    @XmlElement(name = "RecipientsDetails", required = true)
    protected EntityDetailsListType recipientsDetails;
    @XmlElement(name = "RecipientsDelegatesDetails")
    protected RecipientsDelegatesType recipientsDelegatesDetails;
    @XmlElement(name = "EvidenceRefersToRecipient")
    protected BigInteger evidenceRefersToRecipient;
    @XmlElement(name = "SenderMessageDetails")
    protected MessageDetailsType senderMessageDetails;
    @XmlElement(name = "NotificationMessageDetails")
    protected MessageDetailsType notificationMessageDetails;
    @XmlElement(name = "ForwardedToExternalSystem")
    protected String forwardedToExternalSystem;
    @XmlElement(name = "TransactionLogInformation")
    protected TransactionLogInformationType transactionLogInformation;
    @XmlElement(name = "Extensions")
    protected ExtensionsListType extensions;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Ruft den Wert der eventCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventCode() {
        return eventCode;
    }

    /**
     * Legt den Wert der eventCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventCode(String value) {
        this.eventCode = value;
    }

    /**
     * Ruft den Wert der eventReasons-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EventReasonsType }
     *     
     */
    public EventReasonsType getEventReasons() {
        return eventReasons;
    }

    /**
     * Legt den Wert der eventReasons-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EventReasonsType }
     *     
     */
    public void setEventReasons(EventReasonsType value) {
        this.eventReasons = value;
    }

    /**
     * Ruft den Wert der evidenceIdentifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvidenceIdentifier() {
        return evidenceIdentifier;
    }

    /**
     * Legt den Wert der evidenceIdentifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvidenceIdentifier(String value) {
        this.evidenceIdentifier = value;
    }

    /**
     * Ruft den Wert der evidenceIssuerPolicyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EvidenceIssuerPolicyIDType }
     *     
     */
    public EvidenceIssuerPolicyIDType getEvidenceIssuerPolicyID() {
        return evidenceIssuerPolicyID;
    }

    /**
     * Legt den Wert der evidenceIssuerPolicyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EvidenceIssuerPolicyIDType }
     *     
     */
    public void setEvidenceIssuerPolicyID(EvidenceIssuerPolicyIDType value) {
        this.evidenceIssuerPolicyID = value;
    }

    /**
     * Ruft den Wert der evidenceIssuerDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EntityDetailsType }
     *     
     */
    public EntityDetailsType getEvidenceIssuerDetails() {
        return evidenceIssuerDetails;
    }

    /**
     * Legt den Wert der evidenceIssuerDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityDetailsType }
     *     
     */
    public void setEvidenceIssuerDetails(EntityDetailsType value) {
        this.evidenceIssuerDetails = value;
    }

    /**
     * Ruft den Wert der senderAuthenticationDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationDetailsType }
     *     
     */
    public AuthenticationDetailsType getSenderAuthenticationDetails() {
        return senderAuthenticationDetails;
    }

    /**
     * Legt den Wert der senderAuthenticationDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationDetailsType }
     *     
     */
    public void setSenderAuthenticationDetails(AuthenticationDetailsType value) {
        this.senderAuthenticationDetails = value;
    }

    /**
     * Ruft den Wert der recipientAuthenticationDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationDetailsType }
     *     
     */
    public AuthenticationDetailsType getRecipientAuthenticationDetails() {
        return recipientAuthenticationDetails;
    }

    /**
     * Legt den Wert der recipientAuthenticationDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationDetailsType }
     *     
     */
    public void setRecipientAuthenticationDetails(AuthenticationDetailsType value) {
        this.recipientAuthenticationDetails = value;
    }

    /**
     * Ruft den Wert der eventTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventTime() {
        return eventTime;
    }

    /**
     * Legt den Wert der eventTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventTime(XMLGregorianCalendar value) {
        this.eventTime = value;
    }

    /**
     * Ruft den Wert der submissionTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSubmissionTime() {
        return submissionTime;
    }

    /**
     * Legt den Wert der submissionTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSubmissionTime(XMLGregorianCalendar value) {
        this.submissionTime = value;
    }

    /**
     * Ruft den Wert der replyTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Legt den Wert der replyTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplyTo(String value) {
        this.replyTo = value;
    }

    /**
     * Ruft den Wert der replyToAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AttributedElectronicAddressType }
     *     
     */
    public AttributedElectronicAddressType getReplyToAddress() {
        return replyToAddress;
    }

    /**
     * Legt den Wert der replyToAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributedElectronicAddressType }
     *     
     */
    public void setReplyToAddress(AttributedElectronicAddressType value) {
        this.replyToAddress = value;
    }

    /**
     * Ruft den Wert der senderDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EntityDetailsType }
     *     
     */
    public EntityDetailsType getSenderDetails() {
        return senderDetails;
    }

    /**
     * Legt den Wert der senderDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityDetailsType }
     *     
     */
    public void setSenderDetails(EntityDetailsType value) {
        this.senderDetails = value;
    }

    /**
     * Ruft den Wert der recipientsDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EntityDetailsListType }
     *     
     */
    public EntityDetailsListType getRecipientsDetails() {
        return recipientsDetails;
    }

    /**
     * Legt den Wert der recipientsDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityDetailsListType }
     *     
     */
    public void setRecipientsDetails(EntityDetailsListType value) {
        this.recipientsDetails = value;
    }

    /**
     * Ruft den Wert der recipientsDelegatesDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RecipientsDelegatesType }
     *     
     */
    public RecipientsDelegatesType getRecipientsDelegatesDetails() {
        return recipientsDelegatesDetails;
    }

    /**
     * Legt den Wert der recipientsDelegatesDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RecipientsDelegatesType }
     *     
     */
    public void setRecipientsDelegatesDetails(RecipientsDelegatesType value) {
        this.recipientsDelegatesDetails = value;
    }

    /**
     * Ruft den Wert der evidenceRefersToRecipient-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEvidenceRefersToRecipient() {
        return evidenceRefersToRecipient;
    }

    /**
     * Legt den Wert der evidenceRefersToRecipient-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEvidenceRefersToRecipient(BigInteger value) {
        this.evidenceRefersToRecipient = value;
    }

    /**
     * Ruft den Wert der senderMessageDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MessageDetailsType }
     *     
     */
    public MessageDetailsType getSenderMessageDetails() {
        return senderMessageDetails;
    }

    /**
     * Legt den Wert der senderMessageDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDetailsType }
     *     
     */
    public void setSenderMessageDetails(MessageDetailsType value) {
        this.senderMessageDetails = value;
    }

    /**
     * Ruft den Wert der notificationMessageDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MessageDetailsType }
     *     
     */
    public MessageDetailsType getNotificationMessageDetails() {
        return notificationMessageDetails;
    }

    /**
     * Legt den Wert der notificationMessageDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDetailsType }
     *     
     */
    public void setNotificationMessageDetails(MessageDetailsType value) {
        this.notificationMessageDetails = value;
    }

    /**
     * Ruft den Wert der forwardedToExternalSystem-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForwardedToExternalSystem() {
        return forwardedToExternalSystem;
    }

    /**
     * Legt den Wert der forwardedToExternalSystem-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForwardedToExternalSystem(String value) {
        this.forwardedToExternalSystem = value;
    }

    /**
     * Ruft den Wert der transactionLogInformation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TransactionLogInformationType }
     *     
     */
    public TransactionLogInformationType getTransactionLogInformation() {
        return transactionLogInformation;
    }

    /**
     * Legt den Wert der transactionLogInformation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionLogInformationType }
     *     
     */
    public void setTransactionLogInformation(TransactionLogInformationType value) {
        this.transactionLogInformation = value;
    }

    /**
     * Ruft den Wert der extensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsListType }
     *     
     */
    public ExtensionsListType getExtensions() {
        return extensions;
    }

    /**
     * Legt den Wert der extensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsListType }
     *     
     */
    public void setExtensions(ExtensionsListType value) {
        this.extensions = value;
    }

    /**
     * Ruft den Wert der signature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Legt den Wert der signature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}

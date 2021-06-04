//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.etsi.uri._01903.v1_3.AnyType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.etsi.uri._02640.v2 package. 
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

    private final static QName _RelayREMMDAcceptanceRejection_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RelayREMMDAcceptanceRejection");
    private final static QName _ReceivedFromNonREMSystem_QNAME = new QName("http://uri.etsi.org/02640/v2#", "ReceivedFromNonREMSystem");
    private final static QName _NotificationMessageDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "NotificationMessageDetails");
    private final static QName _NamePostalAddress_QNAME = new QName("http://uri.etsi.org/02640/v2#", "NamePostalAddress");
    private final static QName _SenderMessageDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "SenderMessageDetails");
    private final static QName _PostalAddress_QNAME = new QName("http://uri.etsi.org/02640/v2#", "PostalAddress");
    private final static QName _SenderDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "SenderDetails");
    private final static QName _TransactionLog_QNAME = new QName("http://uri.etsi.org/02640/v2#", "TransactionLog");
    private final static QName _AcceptanceRejectionByRecipient_QNAME = new QName("http://uri.etsi.org/02640/v2#", "AcceptanceRejectionByRecipient");
    private final static QName _RetrievalNonRetrievalByRecipient_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RetrievalNonRetrievalByRecipient");
    private final static QName _AttributedElectronicAddress_QNAME = new QName("http://uri.etsi.org/02640/v2#", "AttributedElectronicAddress");
    private final static QName _EventCode_QNAME = new QName("http://uri.etsi.org/02640/v2#", "EventCode");
    private final static QName _RecipientAuthenticationDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RecipientAuthenticationDetails");
    private final static QName _TransactionLogInformation_QNAME = new QName("http://uri.etsi.org/02640/v2#", "TransactionLogInformation");
    private final static QName _RecipientsDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RecipientsDetails");
    private final static QName _Extensions_QNAME = new QName("http://uri.etsi.org/02640/v2#", "Extensions");
    private final static QName _RecipientsDelegatesDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RecipientsDelegatesDetails");
    private final static QName _SenderAuthenticationDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "SenderAuthenticationDetails");
    private final static QName _EventReasons_QNAME = new QName("http://uri.etsi.org/02640/v2#", "EventReasons");
    private final static QName _EntityName_QNAME = new QName("http://uri.etsi.org/02640/v2#", "EntityName");
    private final static QName _DownloadNonDownloadByRecipient_QNAME = new QName("http://uri.etsi.org/02640/v2#", "DownloadNonDownloadByRecipient");
    private final static QName _DeliveryNonDeliveryToRecipient_QNAME = new QName("http://uri.etsi.org/02640/v2#", "DeliveryNonDeliveryToRecipient");
    private final static QName _Extension_QNAME = new QName("http://uri.etsi.org/02640/v2#", "Extension");
    private final static QName _CertificateDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "CertificateDetails");
    private final static QName _EvidenceIssuerDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "EvidenceIssuerDetails");
    private final static QName _NamesPostalAddresses_QNAME = new QName("http://uri.etsi.org/02640/v2#", "NamesPostalAddresses");
    private final static QName _CertSignatureDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "CertSignatureDetails");
    private final static QName _RelayREMMDFailure_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RelayREMMDFailure");
    private final static QName _RelayToNonREMSystem_QNAME = new QName("http://uri.etsi.org/02640/v2#", "RelayToNonREMSystem");
    private final static QName _CertIDAndSignature_QNAME = new QName("http://uri.etsi.org/02640/v2#", "CertIDAndSignature");
    private final static QName _EventReason_QNAME = new QName("http://uri.etsi.org/02640/v2#", "EventReason");
    private final static QName _SubmissionAcceptanceRejection_QNAME = new QName("http://uri.etsi.org/02640/v2#", "SubmissionAcceptanceRejection");
    private final static QName _EvidenceIssuerPolicyID_QNAME = new QName("http://uri.etsi.org/02640/v2#", "EvidenceIssuerPolicyID");
    private final static QName _RecipientsDelegatesTypeDelegateDetails_QNAME = new QName("http://uri.etsi.org/02640/v2#", "DelegateDetails");
    private final static QName _RecipientsDelegatesTypeDelegatingRecipients_QNAME = new QName("http://uri.etsi.org/02640/v2#", "DelegatingRecipients");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.etsi.uri._02640.v2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AttributedElectronicAddressType }
     * 
     */
    public AttributedElectronicAddressType createAttributedElectronicAddressType() {
        return new AttributedElectronicAddressType();
    }

    /**
     * Create an instance of {@link MessageDetailsType }
     * 
     */
    public MessageDetailsType createMessageDetailsType() {
        return new MessageDetailsType();
    }

    /**
     * Create an instance of {@link REMEvidenceType }
     * 
     */
    public REMEvidenceType createREMEvidenceType() {
        return new REMEvidenceType();
    }

    /**
     * Create an instance of {@link EntityNameType }
     * 
     */
    public EntityNameType createEntityNameType() {
        return new EntityNameType();
    }

    /**
     * Create an instance of {@link EventReasonsType }
     * 
     */
    public EventReasonsType createEventReasonsType() {
        return new EventReasonsType();
    }

    /**
     * Create an instance of {@link ExtensionType }
     * 
     */
    public ExtensionType createExtensionType() {
        return new ExtensionType();
    }

    /**
     * Create an instance of {@link EvidenceIssuerPolicyIDType }
     * 
     */
    public EvidenceIssuerPolicyIDType createEvidenceIssuerPolicyIDType() {
        return new EvidenceIssuerPolicyIDType();
    }

    /**
     * Create an instance of {@link TransactionLogInformationType }
     * 
     */
    public TransactionLogInformationType createTransactionLogInformationType() {
        return new TransactionLogInformationType();
    }

    /**
     * Create an instance of {@link AuthenticationDetailsType }
     * 
     */
    public AuthenticationDetailsType createAuthenticationDetailsType() {
        return new AuthenticationDetailsType();
    }

    /**
     * Create an instance of {@link CertificateDetailsType }
     * 
     */
    public CertificateDetailsType createCertificateDetailsType() {
        return new CertificateDetailsType();
    }

    /**
     * Create an instance of {@link EntityDetailsListType }
     * 
     */
    public EntityDetailsListType createEntityDetailsListType() {
        return new EntityDetailsListType();
    }

    /**
     * Create an instance of {@link NamePostalAddressType }
     * 
     */
    public NamePostalAddressType createNamePostalAddressType() {
        return new NamePostalAddressType();
    }

    /**
     * Create an instance of {@link ExtensionsListType }
     * 
     */
    public ExtensionsListType createExtensionsListType() {
        return new ExtensionsListType();
    }

    /**
     * Create an instance of {@link PostalAddressType }
     * 
     */
    public PostalAddressType createPostalAddressType() {
        return new PostalAddressType();
    }

    /**
     * Create an instance of {@link NamesPostalAddressListType }
     * 
     */
    public NamesPostalAddressListType createNamesPostalAddressListType() {
        return new NamesPostalAddressListType();
    }

    /**
     * Create an instance of {@link EntityDetailsType }
     * 
     */
    public EntityDetailsType createEntityDetailsType() {
        return new EntityDetailsType();
    }

    /**
     * Create an instance of {@link CertSignatureDetailsType }
     * 
     */
    public CertSignatureDetailsType createCertSignatureDetailsType() {
        return new CertSignatureDetailsType();
    }

    /**
     * Create an instance of {@link RecipientsDelegatesType }
     * 
     */
    public RecipientsDelegatesType createRecipientsDelegatesType() {
        return new RecipientsDelegatesType();
    }

    /**
     * Create an instance of {@link EventReasonType }
     * 
     */
    public EventReasonType createEventReasonType() {
        return new EventReasonType();
    }

    /**
     * Create an instance of {@link CertIDAndSignatureType }
     * 
     */
    public CertIDAndSignatureType createCertIDAndSignatureType() {
        return new CertIDAndSignatureType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RelayREMMDAcceptanceRejection")
    public JAXBElement<REMEvidenceType> createRelayREMMDAcceptanceRejection(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_RelayREMMDAcceptanceRejection_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "ReceivedFromNonREMSystem")
    public JAXBElement<REMEvidenceType> createReceivedFromNonREMSystem(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_ReceivedFromNonREMSystem_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "NotificationMessageDetails")
    public JAXBElement<MessageDetailsType> createNotificationMessageDetails(MessageDetailsType value) {
        return new JAXBElement<MessageDetailsType>(_NotificationMessageDetails_QNAME, MessageDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NamePostalAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "NamePostalAddress")
    public JAXBElement<NamePostalAddressType> createNamePostalAddress(NamePostalAddressType value) {
        return new JAXBElement<NamePostalAddressType>(_NamePostalAddress_QNAME, NamePostalAddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "SenderMessageDetails")
    public JAXBElement<MessageDetailsType> createSenderMessageDetails(MessageDetailsType value) {
        return new JAXBElement<MessageDetailsType>(_SenderMessageDetails_QNAME, MessageDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostalAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "PostalAddress")
    public JAXBElement<PostalAddressType> createPostalAddress(PostalAddressType value) {
        return new JAXBElement<PostalAddressType>(_PostalAddress_QNAME, PostalAddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "SenderDetails")
    public JAXBElement<EntityDetailsType> createSenderDetails(EntityDetailsType value) {
        return new JAXBElement<EntityDetailsType>(_SenderDetails_QNAME, EntityDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "TransactionLog")
    public JAXBElement<AnyType> createTransactionLog(AnyType value) {
        return new JAXBElement<AnyType>(_TransactionLog_QNAME, AnyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "AcceptanceRejectionByRecipient")
    public JAXBElement<REMEvidenceType> createAcceptanceRejectionByRecipient(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_AcceptanceRejectionByRecipient_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RetrievalNonRetrievalByRecipient")
    public JAXBElement<REMEvidenceType> createRetrievalNonRetrievalByRecipient(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_RetrievalNonRetrievalByRecipient_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedElectronicAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "AttributedElectronicAddress")
    public JAXBElement<AttributedElectronicAddressType> createAttributedElectronicAddress(AttributedElectronicAddressType value) {
        return new JAXBElement<AttributedElectronicAddressType>(_AttributedElectronicAddress_QNAME, AttributedElectronicAddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "EventCode")
    public JAXBElement<String> createEventCode(String value) {
        return new JAXBElement<String>(_EventCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RecipientAuthenticationDetails")
    public JAXBElement<AuthenticationDetailsType> createRecipientAuthenticationDetails(AuthenticationDetailsType value) {
        return new JAXBElement<AuthenticationDetailsType>(_RecipientAuthenticationDetails_QNAME, AuthenticationDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionLogInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "TransactionLogInformation")
    public JAXBElement<TransactionLogInformationType> createTransactionLogInformation(TransactionLogInformationType value) {
        return new JAXBElement<TransactionLogInformationType>(_TransactionLogInformation_QNAME, TransactionLogInformationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityDetailsListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RecipientsDetails")
    public JAXBElement<EntityDetailsListType> createRecipientsDetails(EntityDetailsListType value) {
        return new JAXBElement<EntityDetailsListType>(_RecipientsDetails_QNAME, EntityDetailsListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensionsListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "Extensions")
    public JAXBElement<ExtensionsListType> createExtensions(ExtensionsListType value) {
        return new JAXBElement<ExtensionsListType>(_Extensions_QNAME, ExtensionsListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecipientsDelegatesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RecipientsDelegatesDetails")
    public JAXBElement<RecipientsDelegatesType> createRecipientsDelegatesDetails(RecipientsDelegatesType value) {
        return new JAXBElement<RecipientsDelegatesType>(_RecipientsDelegatesDetails_QNAME, RecipientsDelegatesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "SenderAuthenticationDetails")
    public JAXBElement<AuthenticationDetailsType> createSenderAuthenticationDetails(AuthenticationDetailsType value) {
        return new JAXBElement<AuthenticationDetailsType>(_SenderAuthenticationDetails_QNAME, AuthenticationDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventReasonsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "EventReasons")
    public JAXBElement<EventReasonsType> createEventReasons(EventReasonsType value) {
        return new JAXBElement<EventReasonsType>(_EventReasons_QNAME, EventReasonsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "EntityName")
    public JAXBElement<EntityNameType> createEntityName(EntityNameType value) {
        return new JAXBElement<EntityNameType>(_EntityName_QNAME, EntityNameType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "DownloadNonDownloadByRecipient")
    public JAXBElement<REMEvidenceType> createDownloadNonDownloadByRecipient(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_DownloadNonDownloadByRecipient_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "DeliveryNonDeliveryToRecipient")
    public JAXBElement<REMEvidenceType> createDeliveryNonDeliveryToRecipient(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_DeliveryNonDeliveryToRecipient_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "Extension")
    public JAXBElement<ExtensionType> createExtension(ExtensionType value) {
        return new JAXBElement<ExtensionType>(_Extension_QNAME, ExtensionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CertificateDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "CertificateDetails")
    public JAXBElement<CertificateDetailsType> createCertificateDetails(CertificateDetailsType value) {
        return new JAXBElement<CertificateDetailsType>(_CertificateDetails_QNAME, CertificateDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "EvidenceIssuerDetails")
    public JAXBElement<EntityDetailsType> createEvidenceIssuerDetails(EntityDetailsType value) {
        return new JAXBElement<EntityDetailsType>(_EvidenceIssuerDetails_QNAME, EntityDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NamesPostalAddressListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "NamesPostalAddresses")
    public JAXBElement<NamesPostalAddressListType> createNamesPostalAddresses(NamesPostalAddressListType value) {
        return new JAXBElement<NamesPostalAddressListType>(_NamesPostalAddresses_QNAME, NamesPostalAddressListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CertSignatureDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "CertSignatureDetails")
    public JAXBElement<CertSignatureDetailsType> createCertSignatureDetails(CertSignatureDetailsType value) {
        return new JAXBElement<CertSignatureDetailsType>(_CertSignatureDetails_QNAME, CertSignatureDetailsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RelayREMMDFailure")
    public JAXBElement<REMEvidenceType> createRelayREMMDFailure(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_RelayREMMDFailure_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "RelayToNonREMSystem")
    public JAXBElement<REMEvidenceType> createRelayToNonREMSystem(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_RelayToNonREMSystem_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CertIDAndSignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "CertIDAndSignature")
    public JAXBElement<CertIDAndSignatureType> createCertIDAndSignature(CertIDAndSignatureType value) {
        return new JAXBElement<CertIDAndSignatureType>(_CertIDAndSignature_QNAME, CertIDAndSignatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventReasonType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "EventReason")
    public JAXBElement<EventReasonType> createEventReason(EventReasonType value) {
        return new JAXBElement<EventReasonType>(_EventReason_QNAME, EventReasonType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link REMEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "SubmissionAcceptanceRejection")
    public JAXBElement<REMEvidenceType> createSubmissionAcceptanceRejection(REMEvidenceType value) {
        return new JAXBElement<REMEvidenceType>(_SubmissionAcceptanceRejection_QNAME, REMEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EvidenceIssuerPolicyIDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "EvidenceIssuerPolicyID")
    public JAXBElement<EvidenceIssuerPolicyIDType> createEvidenceIssuerPolicyID(EvidenceIssuerPolicyIDType value) {
        return new JAXBElement<EvidenceIssuerPolicyIDType>(_EvidenceIssuerPolicyID_QNAME, EvidenceIssuerPolicyIDType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityDetailsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "DelegateDetails", scope = RecipientsDelegatesType.class)
    public JAXBElement<EntityDetailsType> createRecipientsDelegatesTypeDelegateDetails(EntityDetailsType value) {
        return new JAXBElement<EntityDetailsType>(_RecipientsDelegatesTypeDelegateDetails_QNAME, EntityDetailsType.class, RecipientsDelegatesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link BigInteger }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/02640/v2#", name = "DelegatingRecipients", scope = RecipientsDelegatesType.class)
    public JAXBElement<List<BigInteger>> createRecipientsDelegatesTypeDelegatingRecipients(List<BigInteger> value) {
        return new JAXBElement<List<BigInteger>>(_RecipientsDelegatesTypeDelegatingRecipients_QNAME, ((Class) List.class), RecipientsDelegatesType.class, ((List<BigInteger> ) value));
    }

}

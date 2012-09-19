//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02231.v2;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für TSLSchemeInformationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TSLSchemeInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TSLVersionIdentifier" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="TSLSequenceNumber" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TSLType"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeOperatorName"/>
 *         &lt;element name="SchemeOperatorAddress" type="{http://uri.etsi.org/02231/v2#}AddressType"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeName"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeInformationURI"/>
 *         &lt;element name="StatusDeterminationApproach" type="{http://uri.etsi.org/02231/v2#}NonEmptyURIType"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeTypeCommunityRules" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeTerritory" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PolicyOrLegalNotice" minOccurs="0"/>
 *         &lt;element name="HistoricalInformationPeriod" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PointersToOtherTSL" minOccurs="0"/>
 *         &lt;element name="ListIssueDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}NextUpdate"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}DistributionPoints" minOccurs="0"/>
 *         &lt;element name="SchemeExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSLSchemeInformationType", propOrder = {
    "tslVersionIdentifier",
    "tslSequenceNumber",
    "tslType",
    "schemeOperatorName",
    "schemeOperatorAddress",
    "schemeName",
    "schemeInformationURI",
    "statusDeterminationApproach",
    "schemeTypeCommunityRules",
    "schemeTerritory",
    "policyOrLegalNotice",
    "historicalInformationPeriod",
    "pointersToOtherTSL",
    "listIssueDateTime",
    "nextUpdate",
    "distributionPoints",
    "schemeExtensions"
})
public class TSLSchemeInformationType {

    @XmlElement(name = "TSLVersionIdentifier", required = true)
    protected BigInteger tslVersionIdentifier;
    @XmlElement(name = "TSLSequenceNumber", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger tslSequenceNumber;
    @XmlElement(name = "TSLType", required = true)
    protected String tslType;
    @XmlElement(name = "SchemeOperatorName", required = true)
    protected InternationalNamesType schemeOperatorName;
    @XmlElement(name = "SchemeOperatorAddress", required = true)
    protected AddressType schemeOperatorAddress;
    @XmlElement(name = "SchemeName", required = true)
    protected InternationalNamesType schemeName;
    @XmlElement(name = "SchemeInformationURI", required = true)
    protected NonEmptyMultiLangURIListType schemeInformationURI;
    @XmlElement(name = "StatusDeterminationApproach", required = true)
    protected String statusDeterminationApproach;
    @XmlElement(name = "SchemeTypeCommunityRules")
    protected NonEmptyURIListType schemeTypeCommunityRules;
    @XmlElement(name = "SchemeTerritory")
    protected String schemeTerritory;
    @XmlElement(name = "PolicyOrLegalNotice")
    protected PolicyOrLegalnoticeType policyOrLegalNotice;
    @XmlElement(name = "HistoricalInformationPeriod", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger historicalInformationPeriod;
    @XmlElement(name = "PointersToOtherTSL")
    protected OtherTSLPointersType pointersToOtherTSL;
    @XmlElement(name = "ListIssueDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar listIssueDateTime;
    @XmlElement(name = "NextUpdate", required = true)
    protected NextUpdateType nextUpdate;
    @XmlElement(name = "DistributionPoints")
    protected ElectronicAddressType distributionPoints;
    @XmlElement(name = "SchemeExtensions")
    protected ExtensionsListType schemeExtensions;

    /**
     * Ruft den Wert der tslVersionIdentifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTSLVersionIdentifier() {
        return tslVersionIdentifier;
    }

    /**
     * Legt den Wert der tslVersionIdentifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTSLVersionIdentifier(BigInteger value) {
        this.tslVersionIdentifier = value;
    }

    /**
     * Ruft den Wert der tslSequenceNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTSLSequenceNumber() {
        return tslSequenceNumber;
    }

    /**
     * Legt den Wert der tslSequenceNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTSLSequenceNumber(BigInteger value) {
        this.tslSequenceNumber = value;
    }

    /**
     * Ruft den Wert der tslType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSLType() {
        return tslType;
    }

    /**
     * Legt den Wert der tslType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSLType(String value) {
        this.tslType = value;
    }

    /**
     * Ruft den Wert der schemeOperatorName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getSchemeOperatorName() {
        return schemeOperatorName;
    }

    /**
     * Legt den Wert der schemeOperatorName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setSchemeOperatorName(InternationalNamesType value) {
        this.schemeOperatorName = value;
    }

    /**
     * Ruft den Wert der schemeOperatorAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getSchemeOperatorAddress() {
        return schemeOperatorAddress;
    }

    /**
     * Legt den Wert der schemeOperatorAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setSchemeOperatorAddress(AddressType value) {
        this.schemeOperatorAddress = value;
    }

    /**
     * Ruft den Wert der schemeName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getSchemeName() {
        return schemeName;
    }

    /**
     * Legt den Wert der schemeName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setSchemeName(InternationalNamesType value) {
        this.schemeName = value;
    }

    /**
     * Ruft den Wert der schemeInformationURI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getSchemeInformationURI() {
        return schemeInformationURI;
    }

    /**
     * Legt den Wert der schemeInformationURI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setSchemeInformationURI(NonEmptyMultiLangURIListType value) {
        this.schemeInformationURI = value;
    }

    /**
     * Ruft den Wert der statusDeterminationApproach-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusDeterminationApproach() {
        return statusDeterminationApproach;
    }

    /**
     * Legt den Wert der statusDeterminationApproach-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusDeterminationApproach(String value) {
        this.statusDeterminationApproach = value;
    }

    /**
     * Ruft den Wert der schemeTypeCommunityRules-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyURIListType }
     *     
     */
    public NonEmptyURIListType getSchemeTypeCommunityRules() {
        return schemeTypeCommunityRules;
    }

    /**
     * Legt den Wert der schemeTypeCommunityRules-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyURIListType }
     *     
     */
    public void setSchemeTypeCommunityRules(NonEmptyURIListType value) {
        this.schemeTypeCommunityRules = value;
    }

    /**
     * Ruft den Wert der schemeTerritory-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemeTerritory() {
        return schemeTerritory;
    }

    /**
     * Legt den Wert der schemeTerritory-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemeTerritory(String value) {
        this.schemeTerritory = value;
    }

    /**
     * Ruft den Wert der policyOrLegalNotice-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PolicyOrLegalnoticeType }
     *     
     */
    public PolicyOrLegalnoticeType getPolicyOrLegalNotice() {
        return policyOrLegalNotice;
    }

    /**
     * Legt den Wert der policyOrLegalNotice-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicyOrLegalnoticeType }
     *     
     */
    public void setPolicyOrLegalNotice(PolicyOrLegalnoticeType value) {
        this.policyOrLegalNotice = value;
    }

    /**
     * Ruft den Wert der historicalInformationPeriod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHistoricalInformationPeriod() {
        return historicalInformationPeriod;
    }

    /**
     * Legt den Wert der historicalInformationPeriod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHistoricalInformationPeriod(BigInteger value) {
        this.historicalInformationPeriod = value;
    }

    /**
     * Ruft den Wert der pointersToOtherTSL-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link OtherTSLPointersType }
     *     
     */
    public OtherTSLPointersType getPointersToOtherTSL() {
        return pointersToOtherTSL;
    }

    /**
     * Legt den Wert der pointersToOtherTSL-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherTSLPointersType }
     *     
     */
    public void setPointersToOtherTSL(OtherTSLPointersType value) {
        this.pointersToOtherTSL = value;
    }

    /**
     * Ruft den Wert der listIssueDateTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getListIssueDateTime() {
        return listIssueDateTime;
    }

    /**
     * Legt den Wert der listIssueDateTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setListIssueDateTime(XMLGregorianCalendar value) {
        this.listIssueDateTime = value;
    }

    /**
     * Ruft den Wert der nextUpdate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NextUpdateType }
     *     
     */
    public NextUpdateType getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Legt den Wert der nextUpdate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NextUpdateType }
     *     
     */
    public void setNextUpdate(NextUpdateType value) {
        this.nextUpdate = value;
    }

    /**
     * Ruft den Wert der distributionPoints-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ElectronicAddressType }
     *     
     */
    public ElectronicAddressType getDistributionPoints() {
        return distributionPoints;
    }

    /**
     * Legt den Wert der distributionPoints-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ElectronicAddressType }
     *     
     */
    public void setDistributionPoints(ElectronicAddressType value) {
        this.distributionPoints = value;
    }

    /**
     * Ruft den Wert der schemeExtensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsListType }
     *     
     */
    public ExtensionsListType getSchemeExtensions() {
        return schemeExtensions;
    }

    /**
     * Legt den Wert der schemeExtensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsListType }
     *     
     */
    public void setSchemeExtensions(ExtensionsListType value) {
        this.schemeExtensions = value;
    }

}

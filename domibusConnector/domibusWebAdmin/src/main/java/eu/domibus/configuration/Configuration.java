
package eu.domibus.configuration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="mpcs"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="mpc" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                           &lt;attribute name="retention_downloaded" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                           &lt;attribute name="retention_undownloaded" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                           &lt;attribute name="default" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                           &lt;attribute name="enabled" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                           &lt;attribute name="qualifiedName" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="businessProcesses"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="roles"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="role" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="parties"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="partyIdTypes"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="partyIdType" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                               &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="party" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="identifier" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="partyId" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                               &lt;attribute name="partyIdType" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                     &lt;attribute name="userName" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="password" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="endpoint" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *                                     &lt;attribute name="allowChunking" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="meps"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="mep" maxOccurs="unbounded" form="unqualified"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *                                     &lt;attribute name="legs" type="{http://www.w3.org/2001/XMLSchema}integer" default="1" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="binding" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="properties"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="property" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="key" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="datatype" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="propertySet" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="propertyRef" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="property" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="payloadProfiles"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="payload" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="cid" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="mimeType" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="inBody" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                     &lt;attribute name="schemaFile" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *                                     &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                                     &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="payloadProfile" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="attachment" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="maxSize" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="securities"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="security" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="policy" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="signatureMethod" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="errorHandlings"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="errorHandling" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="errorAsResponse" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                                     &lt;attribute name="businessErrorNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                                     &lt;attribute name="businessErrorNotifyConsumer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                                     &lt;attribute name="deliveryFailureNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="agreements"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="agreement" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
 *                                     &lt;attribute name="type" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="services"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="service" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="type" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="actions"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="action" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="as4"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="receptionAwareness" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="retry" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="duplicateDetection" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="reliability" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="replyPattern" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="nonRepudiation" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="legConfigurations"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="legConfiguration" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="service" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="action" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="security" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="defaultMpc" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="receptionAwareness" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="reliability" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="propertySet" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="payloadProfile" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="errorHandling" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                     &lt;attribute name="compressPayloads" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="process" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="initiatorParties" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="initiatorParty" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="responderParties" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="responderParty" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="legs"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="leg" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
 *                           &lt;attribute name="responderRole" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="agreement" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="binding" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="mep" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="initiatorRole" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="party" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "configuration")
public class Configuration {

    @XmlElement(required = true)
    protected Configuration.Mpcs mpcs;
    @XmlElement(required = true)
    protected Configuration.BusinessProcesses businessProcesses;
    @XmlAttribute(name = "party", required = true)
    protected String party;

    /**
     * Gets the value of the mpcs property.
     * 
     * @return
     *     possible object is
     *     {@link Configuration.Mpcs }
     *     
     */
    public Configuration.Mpcs getMpcs() {
        return mpcs;
    }

    /**
     * Sets the value of the mpcs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Configuration.Mpcs }
     *     
     */
    public void setMpcs(Configuration.Mpcs value) {
        this.mpcs = value;
    }

    /**
     * Gets the value of the businessProcesses property.
     * 
     * @return
     *     possible object is
     *     {@link Configuration.BusinessProcesses }
     *     
     */
    public Configuration.BusinessProcesses getBusinessProcesses() {
        return businessProcesses;
    }

    /**
     * Sets the value of the businessProcesses property.
     * 
     * @param value
     *     allowed object is
     *     {@link Configuration.BusinessProcesses }
     *     
     */
    public void setBusinessProcesses(Configuration.BusinessProcesses value) {
        this.businessProcesses = value;
    }

    /**
     * Gets the value of the party property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParty() {
        return party;
    }

    /**
     * Sets the value of the party property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParty(String value) {
        this.party = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="roles"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="role" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="parties"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="partyIdTypes"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="partyIdType" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                     &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="party" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="identifier" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="partyId" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                     &lt;attribute name="partyIdType" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                           &lt;attribute name="userName" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="password" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="endpoint" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *                           &lt;attribute name="allowChunking" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="meps"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="mep" maxOccurs="unbounded" form="unqualified"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *                           &lt;attribute name="legs" type="{http://www.w3.org/2001/XMLSchema}integer" default="1" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="binding" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="properties"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="property" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="key" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="datatype" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="propertySet" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="propertyRef" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="property" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="payloadProfiles"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="payload" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="cid" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="mimeType" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="inBody" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                           &lt;attribute name="schemaFile" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *                           &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
     *                           &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="payloadProfile" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="attachment" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="maxSize" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="securities"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="security" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="policy" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="signatureMethod" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="errorHandlings"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="errorHandling" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="errorAsResponse" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                           &lt;attribute name="businessErrorNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                           &lt;attribute name="businessErrorNotifyConsumer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                           &lt;attribute name="deliveryFailureNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="agreements"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="agreement" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
     *                           &lt;attribute name="type" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="services"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="service" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="type" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="actions"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="action" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="as4"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="receptionAwareness" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="retry" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="duplicateDetection" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="reliability" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="replyPattern" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="nonRepudiation" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="legConfigurations"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="legConfiguration" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="service" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="action" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="security" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="defaultMpc" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="receptionAwareness" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="reliability" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="propertySet" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="payloadProfile" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="errorHandling" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                           &lt;attribute name="compressPayloads" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="process" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="initiatorParties" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="initiatorParty" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="responderParties" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="responderParty" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="legs"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="leg" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                 &lt;attribute name="responderRole" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="agreement" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="binding" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="mep" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="initiatorRole" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "roles",
        "parties",
        "meps",
        "properties",
        "payloadProfiles",
        "securities",
        "errorHandlings",
        "agreements",
        "services",
        "actions",
        "as4",
        "legConfigurations",
        "process"
    })
    public static class BusinessProcesses {

        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Roles roles;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Parties parties;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Meps meps;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Properties properties;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.PayloadProfiles payloadProfiles;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Securities securities;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.ErrorHandlings errorHandlings;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Agreements agreements;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Services services;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.Actions actions;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.As4 as4;
        @XmlElement(required = true)
        protected Configuration.BusinessProcesses.LegConfigurations legConfigurations;
        @XmlElement(required = true)
        protected List<Configuration.BusinessProcesses.Process> process;

        /**
         * Gets the value of the roles property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Roles }
         *     
         */
        public Configuration.BusinessProcesses.Roles getRoles() {
            return roles;
        }

        /**
         * Sets the value of the roles property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Roles }
         *     
         */
        public void setRoles(Configuration.BusinessProcesses.Roles value) {
            this.roles = value;
        }

        /**
         * Gets the value of the parties property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Parties }
         *     
         */
        public Configuration.BusinessProcesses.Parties getParties() {
            return parties;
        }

        /**
         * Sets the value of the parties property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Parties }
         *     
         */
        public void setParties(Configuration.BusinessProcesses.Parties value) {
            this.parties = value;
        }

        /**
         * Gets the value of the meps property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Meps }
         *     
         */
        public Configuration.BusinessProcesses.Meps getMeps() {
            return meps;
        }

        /**
         * Sets the value of the meps property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Meps }
         *     
         */
        public void setMeps(Configuration.BusinessProcesses.Meps value) {
            this.meps = value;
        }

        /**
         * Gets the value of the properties property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Properties }
         *     
         */
        public Configuration.BusinessProcesses.Properties getProperties() {
            return properties;
        }

        /**
         * Sets the value of the properties property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Properties }
         *     
         */
        public void setProperties(Configuration.BusinessProcesses.Properties value) {
            this.properties = value;
        }

        /**
         * Gets the value of the payloadProfiles property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.PayloadProfiles }
         *     
         */
        public Configuration.BusinessProcesses.PayloadProfiles getPayloadProfiles() {
            return payloadProfiles;
        }

        /**
         * Sets the value of the payloadProfiles property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.PayloadProfiles }
         *     
         */
        public void setPayloadProfiles(Configuration.BusinessProcesses.PayloadProfiles value) {
            this.payloadProfiles = value;
        }

        /**
         * Gets the value of the securities property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Securities }
         *     
         */
        public Configuration.BusinessProcesses.Securities getSecurities() {
            return securities;
        }

        /**
         * Sets the value of the securities property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Securities }
         *     
         */
        public void setSecurities(Configuration.BusinessProcesses.Securities value) {
            this.securities = value;
        }

        /**
         * Gets the value of the errorHandlings property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.ErrorHandlings }
         *     
         */
        public Configuration.BusinessProcesses.ErrorHandlings getErrorHandlings() {
            return errorHandlings;
        }

        /**
         * Sets the value of the errorHandlings property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.ErrorHandlings }
         *     
         */
        public void setErrorHandlings(Configuration.BusinessProcesses.ErrorHandlings value) {
            this.errorHandlings = value;
        }

        /**
         * Gets the value of the agreements property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Agreements }
         *     
         */
        public Configuration.BusinessProcesses.Agreements getAgreements() {
            return agreements;
        }

        /**
         * Sets the value of the agreements property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Agreements }
         *     
         */
        public void setAgreements(Configuration.BusinessProcesses.Agreements value) {
            this.agreements = value;
        }

        /**
         * Gets the value of the services property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Services }
         *     
         */
        public Configuration.BusinessProcesses.Services getServices() {
            return services;
        }

        /**
         * Sets the value of the services property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Services }
         *     
         */
        public void setServices(Configuration.BusinessProcesses.Services value) {
            this.services = value;
        }

        /**
         * Gets the value of the actions property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.Actions }
         *     
         */
        public Configuration.BusinessProcesses.Actions getActions() {
            return actions;
        }

        /**
         * Sets the value of the actions property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.Actions }
         *     
         */
        public void setActions(Configuration.BusinessProcesses.Actions value) {
            this.actions = value;
        }

        /**
         * Gets the value of the as4 property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.As4 }
         *     
         */
        public Configuration.BusinessProcesses.As4 getAs4() {
            return as4;
        }

        /**
         * Sets the value of the as4 property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.As4 }
         *     
         */
        public void setAs4(Configuration.BusinessProcesses.As4 value) {
            this.as4 = value;
        }

        /**
         * Gets the value of the legConfigurations property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.BusinessProcesses.LegConfigurations }
         *     
         */
        public Configuration.BusinessProcesses.LegConfigurations getLegConfigurations() {
            return legConfigurations;
        }

        /**
         * Sets the value of the legConfigurations property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.BusinessProcesses.LegConfigurations }
         *     
         */
        public void setLegConfigurations(Configuration.BusinessProcesses.LegConfigurations value) {
            this.legConfigurations = value;
        }

        /**
         * Gets the value of the process property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the process property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProcess().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Configuration.BusinessProcesses.Process }
         * 
         * 
         */
        public List<Configuration.BusinessProcesses.Process> getProcess() {
            if (process == null) {
                process = new ArrayList<Configuration.BusinessProcesses.Process>();
            }
            return this.process;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="action" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "action"
        })
        public static class Actions {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Actions.Action> action;

            /**
             * Gets the value of the action property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the action property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAction().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Actions.Action }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Actions.Action> getAction() {
                if (action == null) {
                    action = new ArrayList<Configuration.BusinessProcesses.Actions.Action>();
                }
                return this.action;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Action {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
                protected String value;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="agreement" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
         *                 &lt;attribute name="type" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "agreement"
        })
        public static class Agreements {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Agreements.Agreement> agreement;

            /**
             * Gets the value of the agreement property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the agreement property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAgreement().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Agreements.Agreement }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Agreements.Agreement> getAgreement() {
                if (agreement == null) {
                    agreement = new ArrayList<Configuration.BusinessProcesses.Agreements.Agreement>();
                }
                return this.agreement;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
             *       &lt;attribute name="type" use="required" type="{http://domibus.eu/configuration}max255-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Agreement {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
                protected String value;
                @XmlAttribute(name = "type", required = true)
                protected String type;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the type property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getType() {
                    return type;
                }

                /**
                 * Sets the value of the type property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setType(String value) {
                    this.type = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="receptionAwareness" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="retry" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="duplicateDetection" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="reliability" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="replyPattern" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="nonRepudiation" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "receptionAwareness",
            "reliability"
        })
        public static class As4 {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.As4 .ReceptionAwareness> receptionAwareness;
            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.As4 .Reliability> reliability;

            /**
             * Gets the value of the receptionAwareness property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the receptionAwareness property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getReceptionAwareness().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.As4 .ReceptionAwareness }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.As4 .ReceptionAwareness> getReceptionAwareness() {
                if (receptionAwareness == null) {
                    receptionAwareness = new ArrayList<Configuration.BusinessProcesses.As4 .ReceptionAwareness>();
                }
                return this.receptionAwareness;
            }

            /**
             * Gets the value of the reliability property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the reliability property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getReliability().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.As4 .Reliability }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.As4 .Reliability> getReliability() {
                if (reliability == null) {
                    reliability = new ArrayList<Configuration.BusinessProcesses.As4 .Reliability>();
                }
                return this.reliability;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="retry" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="duplicateDetection" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ReceptionAwareness {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "retry")
                protected String retry;
                @XmlAttribute(name = "duplicateDetection")
                protected String duplicateDetection;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the retry property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getRetry() {
                    return retry;
                }

                /**
                 * Sets the value of the retry property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setRetry(String value) {
                    this.retry = value;
                }

                /**
                 * Gets the value of the duplicateDetection property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDuplicateDetection() {
                    return duplicateDetection;
                }

                /**
                 * Sets the value of the duplicateDetection property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDuplicateDetection(String value) {
                    this.duplicateDetection = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="replyPattern" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="nonRepudiation" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Reliability {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "replyPattern", required = true)
                protected String replyPattern;
                @XmlAttribute(name = "nonRepudiation", required = true)
                protected String nonRepudiation;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the replyPattern property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getReplyPattern() {
                    return replyPattern;
                }

                /**
                 * Sets the value of the replyPattern property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setReplyPattern(String value) {
                    this.replyPattern = value;
                }

                /**
                 * Gets the value of the nonRepudiation property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNonRepudiation() {
                    return nonRepudiation;
                }

                /**
                 * Sets the value of the nonRepudiation property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNonRepudiation(String value) {
                    this.nonRepudiation = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="errorHandling" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="errorAsResponse" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *                 &lt;attribute name="businessErrorNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *                 &lt;attribute name="businessErrorNotifyConsumer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *                 &lt;attribute name="deliveryFailureNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "errorHandling"
        })
        public static class ErrorHandlings {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.ErrorHandlings.ErrorHandling> errorHandling;

            /**
             * Gets the value of the errorHandling property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the errorHandling property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getErrorHandling().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.ErrorHandlings.ErrorHandling }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.ErrorHandlings.ErrorHandling> getErrorHandling() {
                if (errorHandling == null) {
                    errorHandling = new ArrayList<Configuration.BusinessProcesses.ErrorHandlings.ErrorHandling>();
                }
                return this.errorHandling;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="errorAsResponse" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
             *       &lt;attribute name="businessErrorNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
             *       &lt;attribute name="businessErrorNotifyConsumer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
             *       &lt;attribute name="deliveryFailureNotifyProducer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ErrorHandling {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "errorAsResponse", required = true)
                protected boolean errorAsResponse;
                @XmlAttribute(name = "businessErrorNotifyProducer", required = true)
                protected boolean businessErrorNotifyProducer;
                @XmlAttribute(name = "businessErrorNotifyConsumer", required = true)
                protected boolean businessErrorNotifyConsumer;
                @XmlAttribute(name = "deliveryFailureNotifyProducer", required = true)
                protected boolean deliveryFailureNotifyProducer;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the errorAsResponse property.
                 * 
                 */
                public boolean isErrorAsResponse() {
                    return errorAsResponse;
                }

                /**
                 * Sets the value of the errorAsResponse property.
                 * 
                 */
                public void setErrorAsResponse(boolean value) {
                    this.errorAsResponse = value;
                }

                /**
                 * Gets the value of the businessErrorNotifyProducer property.
                 * 
                 */
                public boolean isBusinessErrorNotifyProducer() {
                    return businessErrorNotifyProducer;
                }

                /**
                 * Sets the value of the businessErrorNotifyProducer property.
                 * 
                 */
                public void setBusinessErrorNotifyProducer(boolean value) {
                    this.businessErrorNotifyProducer = value;
                }

                /**
                 * Gets the value of the businessErrorNotifyConsumer property.
                 * 
                 */
                public boolean isBusinessErrorNotifyConsumer() {
                    return businessErrorNotifyConsumer;
                }

                /**
                 * Sets the value of the businessErrorNotifyConsumer property.
                 * 
                 */
                public void setBusinessErrorNotifyConsumer(boolean value) {
                    this.businessErrorNotifyConsumer = value;
                }

                /**
                 * Gets the value of the deliveryFailureNotifyProducer property.
                 * 
                 */
                public boolean isDeliveryFailureNotifyProducer() {
                    return deliveryFailureNotifyProducer;
                }

                /**
                 * Sets the value of the deliveryFailureNotifyProducer property.
                 * 
                 */
                public void setDeliveryFailureNotifyProducer(boolean value) {
                    this.deliveryFailureNotifyProducer = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="legConfiguration" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="service" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="action" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="security" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="defaultMpc" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="receptionAwareness" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="reliability" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="propertySet" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="payloadProfile" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="errorHandling" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="compressPayloads" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "legConfiguration"
        })
        public static class LegConfigurations {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.LegConfigurations.LegConfiguration> legConfiguration;

            /**
             * Gets the value of the legConfiguration property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the legConfiguration property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getLegConfiguration().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.LegConfigurations.LegConfiguration }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.LegConfigurations.LegConfiguration> getLegConfiguration() {
                if (legConfiguration == null) {
                    legConfiguration = new ArrayList<Configuration.BusinessProcesses.LegConfigurations.LegConfiguration>();
                }
                return this.legConfiguration;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="service" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="action" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="security" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="defaultMpc" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="receptionAwareness" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="reliability" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="propertySet" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="payloadProfile" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="errorHandling" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="compressPayloads" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LegConfiguration {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "service", required = true)
                protected String service;
                @XmlAttribute(name = "action", required = true)
                protected String action;
                @XmlAttribute(name = "security", required = true)
                protected String security;
                @XmlAttribute(name = "defaultMpc", required = true)
                protected String defaultMpc;
                @XmlAttribute(name = "receptionAwareness", required = true)
                protected String receptionAwareness;
                @XmlAttribute(name = "reliability", required = true)
                protected String reliability;
                @XmlAttribute(name = "propertySet")
                protected String propertySet;
                @XmlAttribute(name = "payloadProfile")
                protected String payloadProfile;
                @XmlAttribute(name = "errorHandling", required = true)
                protected String errorHandling;
                @XmlAttribute(name = "compressPayloads", required = true)
                protected String compressPayloads;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the service property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getService() {
                    return service;
                }

                /**
                 * Sets the value of the service property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setService(String value) {
                    this.service = value;
                }

                /**
                 * Gets the value of the action property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAction() {
                    return action;
                }

                /**
                 * Sets the value of the action property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAction(String value) {
                    this.action = value;
                }

                /**
                 * Gets the value of the security property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSecurity() {
                    return security;
                }

                /**
                 * Sets the value of the security property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSecurity(String value) {
                    this.security = value;
                }

                /**
                 * Gets the value of the defaultMpc property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDefaultMpc() {
                    return defaultMpc;
                }

                /**
                 * Sets the value of the defaultMpc property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDefaultMpc(String value) {
                    this.defaultMpc = value;
                }

                /**
                 * Gets the value of the receptionAwareness property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getReceptionAwareness() {
                    return receptionAwareness;
                }

                /**
                 * Sets the value of the receptionAwareness property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setReceptionAwareness(String value) {
                    this.receptionAwareness = value;
                }

                /**
                 * Gets the value of the reliability property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getReliability() {
                    return reliability;
                }

                /**
                 * Sets the value of the reliability property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setReliability(String value) {
                    this.reliability = value;
                }

                /**
                 * Gets the value of the propertySet property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPropertySet() {
                    return propertySet;
                }

                /**
                 * Sets the value of the propertySet property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPropertySet(String value) {
                    this.propertySet = value;
                }

                /**
                 * Gets the value of the payloadProfile property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPayloadProfile() {
                    return payloadProfile;
                }

                /**
                 * Sets the value of the payloadProfile property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPayloadProfile(String value) {
                    this.payloadProfile = value;
                }

                /**
                 * Gets the value of the errorHandling property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getErrorHandling() {
                    return errorHandling;
                }

                /**
                 * Sets the value of the errorHandling property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setErrorHandling(String value) {
                    this.errorHandling = value;
                }

                /**
                 * Gets the value of the compressPayloads property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCompressPayloads() {
                    return compressPayloads;
                }

                /**
                 * Sets the value of the compressPayloads property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCompressPayloads(String value) {
                    this.compressPayloads = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="mep" maxOccurs="unbounded" form="unqualified"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
         *                 &lt;attribute name="legs" type="{http://www.w3.org/2001/XMLSchema}integer" default="1" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="binding" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "mep",
            "binding"
        })
        public static class Meps {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Meps.Mep> mep;
            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Meps.Binding> binding;

            /**
             * Gets the value of the mep property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the mep property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getMep().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Meps.Mep }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Meps.Mep> getMep() {
                if (mep == null) {
                    mep = new ArrayList<Configuration.BusinessProcesses.Meps.Mep>();
                }
                return this.mep;
            }

            /**
             * Gets the value of the binding property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the binding property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getBinding().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Meps.Binding }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Meps.Binding> getBinding() {
                if (binding == null) {
                    binding = new ArrayList<Configuration.BusinessProcesses.Meps.Binding>();
                }
                return this.binding;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Binding {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
                @XmlSchemaType(name = "anyURI")
                protected String value;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
             *       &lt;attribute name="legs" type="{http://www.w3.org/2001/XMLSchema}integer" default="1" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Mep {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
                @XmlSchemaType(name = "anyURI")
                protected String value;
                @XmlAttribute(name = "legs")
                protected BigInteger legs;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the legs property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getLegs() {
                    if (legs == null) {
                        return new BigInteger("1");
                    } else {
                        return legs;
                    }
                }

                /**
                 * Sets the value of the legs property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setLegs(BigInteger value) {
                    this.legs = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="partyIdTypes"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="partyIdType" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="party" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="identifier" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="partyId" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                           &lt;attribute name="partyIdType" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                 &lt;attribute name="userName" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="password" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="endpoint" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
         *                 &lt;attribute name="allowChunking" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "partyIdTypes",
            "party"
        })
        public static class Parties {

            @XmlElement(required = true)
            protected Configuration.BusinessProcesses.Parties.PartyIdTypes partyIdTypes;
            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Parties.Party> party;

            /**
             * Gets the value of the partyIdTypes property.
             * 
             * @return
             *     possible object is
             *     {@link Configuration.BusinessProcesses.Parties.PartyIdTypes }
             *     
             */
            public Configuration.BusinessProcesses.Parties.PartyIdTypes getPartyIdTypes() {
                return partyIdTypes;
            }

            /**
             * Sets the value of the partyIdTypes property.
             * 
             * @param value
             *     allowed object is
             *     {@link Configuration.BusinessProcesses.Parties.PartyIdTypes }
             *     
             */
            public void setPartyIdTypes(Configuration.BusinessProcesses.Parties.PartyIdTypes value) {
                this.partyIdTypes = value;
            }

            /**
             * Gets the value of the party property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the party property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getParty().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Parties.Party }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Parties.Party> getParty() {
                if (party == null) {
                    party = new ArrayList<Configuration.BusinessProcesses.Parties.Party>();
                }
                return this.party;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="identifier" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="partyId" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *                 &lt;attribute name="partyIdType" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *       &lt;attribute name="userName" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="password" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="endpoint" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
             *       &lt;attribute name="allowChunking" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "identifier"
            })
            public static class Party {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.Parties.Party.Identifier> identifier;
                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "userName")
                protected String userName;
                @XmlAttribute(name = "password")
                protected String password;
                @XmlAttribute(name = "endpoint", required = true)
                @XmlSchemaType(name = "anyURI")
                protected String endpoint;
                @XmlAttribute(name = "allowChunking")
                protected String allowChunking;

                /**
                 * Gets the value of the identifier property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the identifier property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getIdentifier().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.Parties.Party.Identifier }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.Parties.Party.Identifier> getIdentifier() {
                    if (identifier == null) {
                        identifier = new ArrayList<Configuration.BusinessProcesses.Parties.Party.Identifier>();
                    }
                    return this.identifier;
                }

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the userName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getUserName() {
                    return userName;
                }

                /**
                 * Sets the value of the userName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setUserName(String value) {
                    this.userName = value;
                }

                /**
                 * Gets the value of the password property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPassword() {
                    return password;
                }

                /**
                 * Sets the value of the password property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPassword(String value) {
                    this.password = value;
                }

                /**
                 * Gets the value of the endpoint property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEndpoint() {
                    return endpoint;
                }

                /**
                 * Sets the value of the endpoint property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEndpoint(String value) {
                    this.endpoint = value;
                }

                /**
                 * Gets the value of the allowChunking property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAllowChunking() {
                    return allowChunking;
                }

                /**
                 * Sets the value of the allowChunking property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAllowChunking(String value) {
                    this.allowChunking = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="partyId" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *       &lt;attribute name="partyIdType" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Identifier {

                    @XmlAttribute(name = "partyId", required = true)
                    protected String partyId;
                    @XmlAttribute(name = "partyIdType", required = true)
                    protected String partyIdType;

                    /**
                     * Gets the value of the partyId property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPartyId() {
                        return partyId;
                    }

                    /**
                     * Sets the value of the partyId property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPartyId(String value) {
                        this.partyId = value;
                    }

                    /**
                     * Gets the value of the partyIdType property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPartyIdType() {
                        return partyIdType;
                    }

                    /**
                     * Sets the value of the partyIdType property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPartyIdType(String value) {
                        this.partyIdType = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="partyIdType" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "partyIdType"
            })
            public static class PartyIdTypes {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType> partyIdType;

                /**
                 * Gets the value of the partyIdType property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the partyIdType property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPartyIdType().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType> getPartyIdType() {
                    if (partyIdType == null) {
                        partyIdType = new ArrayList<Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType>();
                    }
                    return this.partyIdType;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class PartyIdType {

                    @XmlAttribute(name = "name", required = true)
                    protected String name;
                    @XmlAttribute(name = "value", required = true)
                    @XmlSchemaType(name = "anyURI")
                    protected String value;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="payload" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="cid" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="mimeType" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="inBody" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                 &lt;attribute name="schemaFile" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
         *                 &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
         *                 &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="payloadProfile" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="attachment" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="maxSize" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "payload",
            "payloadProfile"
        })
        public static class PayloadProfiles {

            protected List<Configuration.BusinessProcesses.PayloadProfiles.Payload> payload;
            protected List<Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile> payloadProfile;

            /**
             * Gets the value of the payload property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the payload property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPayload().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.PayloadProfiles.Payload }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.PayloadProfiles.Payload> getPayload() {
                if (payload == null) {
                    payload = new ArrayList<Configuration.BusinessProcesses.PayloadProfiles.Payload>();
                }
                return this.payload;
            }

            /**
             * Gets the value of the payloadProfile property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the payloadProfile property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPayloadProfile().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile> getPayloadProfile() {
                if (payloadProfile == null) {
                    payloadProfile = new ArrayList<Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile>();
                }
                return this.payloadProfile;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="cid" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="mimeType" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="inBody" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *       &lt;attribute name="schemaFile" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
             *       &lt;attribute name="maxSize" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
             *       &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Payload {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "cid", required = true)
                protected String cid;
                @XmlAttribute(name = "mimeType")
                protected String mimeType;
                @XmlAttribute(name = "inBody")
                protected String inBody;
                @XmlAttribute(name = "schemaFile")
                @XmlSchemaType(name = "anyURI")
                protected String schemaFile;
                @XmlAttribute(name = "maxSize")
                protected BigInteger maxSize;
                @XmlAttribute(name = "required", required = true)
                protected boolean required;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the cid property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCid() {
                    return cid;
                }

                /**
                 * Sets the value of the cid property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCid(String value) {
                    this.cid = value;
                }

                /**
                 * Gets the value of the mimeType property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getMimeType() {
                    return mimeType;
                }

                /**
                 * Sets the value of the mimeType property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setMimeType(String value) {
                    this.mimeType = value;
                }

                /**
                 * Gets the value of the inBody property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getInBody() {
                    return inBody;
                }

                /**
                 * Sets the value of the inBody property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setInBody(String value) {
                    this.inBody = value;
                }

                /**
                 * Gets the value of the schemaFile property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSchemaFile() {
                    return schemaFile;
                }

                /**
                 * Sets the value of the schemaFile property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSchemaFile(String value) {
                    this.schemaFile = value;
                }

                /**
                 * Gets the value of the maxSize property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getMaxSize() {
                    return maxSize;
                }

                /**
                 * Sets the value of the maxSize property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setMaxSize(BigInteger value) {
                    this.maxSize = value;
                }

                /**
                 * Gets the value of the required property.
                 * 
                 */
                public boolean isRequired() {
                    return required;
                }

                /**
                 * Sets the value of the required property.
                 * 
                 */
                public void setRequired(boolean value) {
                    this.required = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="attachment" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="maxSize" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "attachment"
            })
            public static class PayloadProfile {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile.Attachment> attachment;
                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "maxSize", required = true)
                protected BigInteger maxSize;

                /**
                 * Gets the value of the attachment property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the attachment property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getAttachment().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile.Attachment }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile.Attachment> getAttachment() {
                    if (attachment == null) {
                        attachment = new ArrayList<Configuration.BusinessProcesses.PayloadProfiles.PayloadProfile.Attachment>();
                    }
                    return this.attachment;
                }

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the maxSize property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getMaxSize() {
                    return maxSize;
                }

                /**
                 * Sets the value of the maxSize property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setMaxSize(BigInteger value) {
                    this.maxSize = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Attachment {

                    @XmlAttribute(name = "name", required = true)
                    protected String name;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="initiatorParties" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="initiatorParty" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="responderParties" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="responderParty" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="legs"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="leg" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *       &lt;attribute name="responderRole" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="agreement" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="binding" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="mep" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="initiatorRole" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "initiatorParties",
            "responderParties",
            "legs"
        })
        public static class Process {

            protected Configuration.BusinessProcesses.Process.InitiatorParties initiatorParties;
            protected Configuration.BusinessProcesses.Process.ResponderParties responderParties;
            @XmlElement(required = true)
            protected Configuration.BusinessProcesses.Process.Legs legs;
            @XmlAttribute(name = "name", required = true)
            protected String name;
            @XmlAttribute(name = "responderRole", required = true)
            protected String responderRole;
            @XmlAttribute(name = "agreement")
            protected String agreement;
            @XmlAttribute(name = "binding", required = true)
            protected String binding;
            @XmlAttribute(name = "mep", required = true)
            protected String mep;
            @XmlAttribute(name = "initiatorRole", required = true)
            protected String initiatorRole;

            /**
             * Gets the value of the initiatorParties property.
             * 
             * @return
             *     possible object is
             *     {@link Configuration.BusinessProcesses.Process.InitiatorParties }
             *     
             */
            public Configuration.BusinessProcesses.Process.InitiatorParties getInitiatorParties() {
                return initiatorParties;
            }

            /**
             * Sets the value of the initiatorParties property.
             * 
             * @param value
             *     allowed object is
             *     {@link Configuration.BusinessProcesses.Process.InitiatorParties }
             *     
             */
            public void setInitiatorParties(Configuration.BusinessProcesses.Process.InitiatorParties value) {
                this.initiatorParties = value;
            }

            /**
             * Gets the value of the responderParties property.
             * 
             * @return
             *     possible object is
             *     {@link Configuration.BusinessProcesses.Process.ResponderParties }
             *     
             */
            public Configuration.BusinessProcesses.Process.ResponderParties getResponderParties() {
                return responderParties;
            }

            /**
             * Sets the value of the responderParties property.
             * 
             * @param value
             *     allowed object is
             *     {@link Configuration.BusinessProcesses.Process.ResponderParties }
             *     
             */
            public void setResponderParties(Configuration.BusinessProcesses.Process.ResponderParties value) {
                this.responderParties = value;
            }

            /**
             * Gets the value of the legs property.
             * 
             * @return
             *     possible object is
             *     {@link Configuration.BusinessProcesses.Process.Legs }
             *     
             */
            public Configuration.BusinessProcesses.Process.Legs getLegs() {
                return legs;
            }

            /**
             * Sets the value of the legs property.
             * 
             * @param value
             *     allowed object is
             *     {@link Configuration.BusinessProcesses.Process.Legs }
             *     
             */
            public void setLegs(Configuration.BusinessProcesses.Process.Legs value) {
                this.legs = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the responderRole property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getResponderRole() {
                return responderRole;
            }

            /**
             * Sets the value of the responderRole property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setResponderRole(String value) {
                this.responderRole = value;
            }

            /**
             * Gets the value of the agreement property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAgreement() {
                return agreement;
            }

            /**
             * Sets the value of the agreement property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAgreement(String value) {
                this.agreement = value;
            }

            /**
             * Gets the value of the binding property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBinding() {
                return binding;
            }

            /**
             * Sets the value of the binding property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBinding(String value) {
                this.binding = value;
            }

            /**
             * Gets the value of the mep property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMep() {
                return mep;
            }

            /**
             * Sets the value of the mep property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMep(String value) {
                this.mep = value;
            }

            /**
             * Gets the value of the initiatorRole property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInitiatorRole() {
                return initiatorRole;
            }

            /**
             * Sets the value of the initiatorRole property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInitiatorRole(String value) {
                this.initiatorRole = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="initiatorParty" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "initiatorParty"
            })
            public static class InitiatorParties {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty> initiatorParty;

                /**
                 * Gets the value of the initiatorParty property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the initiatorParty property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getInitiatorParty().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty> getInitiatorParty() {
                    if (initiatorParty == null) {
                        initiatorParty = new ArrayList<Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty>();
                    }
                    return this.initiatorParty;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class InitiatorParty {

                    @XmlAttribute(name = "name", required = true)
                    protected String name;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="leg" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "leg"
            })
            public static class Legs {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.Process.Legs.Leg> leg;

                /**
                 * Gets the value of the leg property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the leg property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getLeg().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.Process.Legs.Leg }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.Process.Legs.Leg> getLeg() {
                    if (leg == null) {
                        leg = new ArrayList<Configuration.BusinessProcesses.Process.Legs.Leg>();
                    }
                    return this.leg;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Leg {

                    @XmlAttribute(name = "name", required = true)
                    protected String name;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="responderParty" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "responderParty"
            })
            public static class ResponderParties {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.Process.ResponderParties.ResponderParty> responderParty;

                /**
                 * Gets the value of the responderParty property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the responderParty property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getResponderParty().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.Process.ResponderParties.ResponderParty }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.Process.ResponderParties.ResponderParty> getResponderParty() {
                    if (responderParty == null) {
                        responderParty = new ArrayList<Configuration.BusinessProcesses.Process.ResponderParties.ResponderParty>();
                    }
                    return this.responderParty;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class ResponderParty {

                    @XmlAttribute(name = "name", required = true)
                    protected String name;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="property" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="key" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="datatype" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="propertySet" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="propertyRef" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;attribute name="property" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "property",
            "propertySet"
        })
        public static class Properties {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Properties.Property> property;
            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Properties.PropertySet> propertySet;

            /**
             * Gets the value of the property property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the property property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getProperty().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Properties.Property }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Properties.Property> getProperty() {
                if (property == null) {
                    property = new ArrayList<Configuration.BusinessProcesses.Properties.Property>();
                }
                return this.property;
            }

            /**
             * Gets the value of the propertySet property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the propertySet property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPropertySet().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Properties.PropertySet }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Properties.PropertySet> getPropertySet() {
                if (propertySet == null) {
                    propertySet = new ArrayList<Configuration.BusinessProcesses.Properties.PropertySet>();
                }
                return this.propertySet;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="key" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="datatype" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="required" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Property {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "key", required = true)
                protected String key;
                @XmlAttribute(name = "datatype", required = true)
                protected String datatype;
                @XmlAttribute(name = "required", required = true)
                protected boolean required;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the key property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getKey() {
                    return key;
                }

                /**
                 * Sets the value of the key property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setKey(String value) {
                    this.key = value;
                }

                /**
                 * Gets the value of the datatype property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDatatype() {
                    return datatype;
                }

                /**
                 * Sets the value of the datatype property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDatatype(String value) {
                    this.datatype = value;
                }

                /**
                 * Gets the value of the required property.
                 * 
                 */
                public boolean isRequired() {
                    return required;
                }

                /**
                 * Sets the value of the required property.
                 * 
                 */
                public void setRequired(boolean value) {
                    this.required = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="propertyRef" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;attribute name="property" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "propertyRef"
            })
            public static class PropertySet {

                @XmlElement(required = true)
                protected List<Configuration.BusinessProcesses.Properties.PropertySet.PropertyRef> propertyRef;
                @XmlAttribute(name = "name", required = true)
                protected String name;

                /**
                 * Gets the value of the propertyRef property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the propertyRef property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPropertyRef().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.BusinessProcesses.Properties.PropertySet.PropertyRef }
                 * 
                 * 
                 */
                public List<Configuration.BusinessProcesses.Properties.PropertySet.PropertyRef> getPropertyRef() {
                    if (propertyRef == null) {
                        propertyRef = new ArrayList<Configuration.BusinessProcesses.Properties.PropertySet.PropertyRef>();
                    }
                    return this.propertyRef;
                }

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;attribute name="property" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class PropertyRef {

                    @XmlAttribute(name = "property", required = true)
                    protected String property;

                    /**
                     * Gets the value of the property property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getProperty() {
                        return property;
                    }

                    /**
                     * Sets the value of the property property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setProperty(String value) {
                        this.property = value;
                    }

                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="role" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "role"
        })
        public static class Roles {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Roles.Role> role;

            /**
             * Gets the value of the role property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the role property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getRole().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Roles.Role }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Roles.Role> getRole() {
                if (role == null) {
                    role = new ArrayList<Configuration.BusinessProcesses.Roles.Role>();
                }
                return this.role;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Role {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
                protected String value;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="security" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="policy" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="signatureMethod" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "security"
        })
        public static class Securities {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Securities.Security> security;

            /**
             * Gets the value of the security property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the security property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getSecurity().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Securities.Security }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Securities.Security> getSecurity() {
                if (security == null) {
                    security = new ArrayList<Configuration.BusinessProcesses.Securities.Security>();
                }
                return this.security;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="policy" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="signatureMethod" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Security {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "policy", required = true)
                protected String policy;
                @XmlAttribute(name = "signatureMethod", required = true)
                protected String signatureMethod;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the policy property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPolicy() {
                    return policy;
                }

                /**
                 * Sets the value of the policy property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPolicy(String value) {
                    this.policy = value;
                }

                /**
                 * Gets the value of the signatureMethod property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSignatureMethod() {
                    return signatureMethod;
                }

                /**
                 * Sets the value of the signatureMethod property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSignatureMethod(String value) {
                    this.signatureMethod = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="service" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *                 &lt;attribute name="type" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "service"
        })
        public static class Services {

            @XmlElement(required = true)
            protected List<Configuration.BusinessProcesses.Services.Service> service;

            /**
             * Gets the value of the service property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the service property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getService().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Configuration.BusinessProcesses.Services.Service }
             * 
             * 
             */
            public List<Configuration.BusinessProcesses.Services.Service> getService() {
                if (service == null) {
                    service = new ArrayList<Configuration.BusinessProcesses.Services.Service>();
                }
                return this.service;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="value" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *       &lt;attribute name="type" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Service {

                @XmlAttribute(name = "name", required = true)
                protected String name;
                @XmlAttribute(name = "value", required = true)
                protected String value;
                @XmlAttribute(name = "type")
                protected String type;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the type property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getType() {
                    return type;
                }

                /**
                 * Sets the value of the type property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setType(String value) {
                    this.type = value;
                }

            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="mpc" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *                 &lt;attribute name="retention_downloaded" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
     *                 &lt;attribute name="retention_undownloaded" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
     *                 &lt;attribute name="default" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                 &lt;attribute name="enabled" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *                 &lt;attribute name="qualifiedName" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "mpc"
    })
    public static class Mpcs {

        @XmlElement(required = true)
        protected List<Configuration.Mpcs.Mpc> mpc;

        /**
         * Gets the value of the mpc property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the mpc property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMpc().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Configuration.Mpcs.Mpc }
         * 
         * 
         */
        public List<Configuration.Mpcs.Mpc> getMpc() {
            if (mpc == null) {
                mpc = new ArrayList<Configuration.Mpcs.Mpc>();
            }
            return this.mpc;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;attribute name="name" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *       &lt;attribute name="retention_downloaded" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
         *       &lt;attribute name="retention_undownloaded" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
         *       &lt;attribute name="default" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *       &lt;attribute name="enabled" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
         *       &lt;attribute name="qualifiedName" use="required" type="{http://domibus.eu/configuration}max255-non-empty-string" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Mpc {

            @XmlAttribute(name = "name", required = true)
            protected String name;
            @XmlAttribute(name = "retention_downloaded", required = true)
            protected BigInteger retentionDownloaded;
            @XmlAttribute(name = "retention_undownloaded", required = true)
            protected BigInteger retentionUndownloaded;
            @XmlAttribute(name = "default", required = true)
            protected boolean _default;
            @XmlAttribute(name = "enabled", required = true)
            protected boolean enabled;
            @XmlAttribute(name = "qualifiedName", required = true)
            protected String qualifiedName;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the retentionDownloaded property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getRetentionDownloaded() {
                return retentionDownloaded;
            }

            /**
             * Sets the value of the retentionDownloaded property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setRetentionDownloaded(BigInteger value) {
                this.retentionDownloaded = value;
            }

            /**
             * Gets the value of the retentionUndownloaded property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getRetentionUndownloaded() {
                return retentionUndownloaded;
            }

            /**
             * Sets the value of the retentionUndownloaded property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setRetentionUndownloaded(BigInteger value) {
                this.retentionUndownloaded = value;
            }

            /**
             * Gets the value of the default property.
             * 
             */
            public boolean isDefault() {
                return _default;
            }

            /**
             * Sets the value of the default property.
             * 
             */
            public void setDefault(boolean value) {
                this._default = value;
            }

            /**
             * Gets the value of the enabled property.
             * 
             */
            public boolean isEnabled() {
                return enabled;
            }

            /**
             * Sets the value of the enabled property.
             * 
             */
            public void setEnabled(boolean value) {
                this.enabled = value;
            }

            /**
             * Gets the value of the qualifiedName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getQualifiedName() {
                return qualifiedName;
            }

            /**
             * Sets the value of the qualifiedName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setQualifiedName(String value) {
                this.qualifiedName = value;
            }

        }

    }

}


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
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der mpcs-Eigenschaft ab.
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
     * Legt den Wert der mpcs-Eigenschaft fest.
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
     * Ruft den Wert der businessProcesses-Eigenschaft ab.
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
     * Legt den Wert der businessProcesses-Eigenschaft fest.
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
     * Ruft den Wert der party-Eigenschaft ab.
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
     * Legt den Wert der party-Eigenschaft fest.
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
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
         * Ruft den Wert der roles-Eigenschaft ab.
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
         * Legt den Wert der roles-Eigenschaft fest.
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
         * Ruft den Wert der parties-Eigenschaft ab.
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
         * Legt den Wert der parties-Eigenschaft fest.
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
         * Ruft den Wert der meps-Eigenschaft ab.
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
         * Legt den Wert der meps-Eigenschaft fest.
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
         * Ruft den Wert der properties-Eigenschaft ab.
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
         * Legt den Wert der properties-Eigenschaft fest.
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
         * Ruft den Wert der payloadProfiles-Eigenschaft ab.
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
         * Legt den Wert der payloadProfiles-Eigenschaft fest.
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
         * Ruft den Wert der securities-Eigenschaft ab.
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
         * Legt den Wert der securities-Eigenschaft fest.
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
         * Ruft den Wert der errorHandlings-Eigenschaft ab.
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
         * Legt den Wert der errorHandlings-Eigenschaft fest.
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
         * Ruft den Wert der agreements-Eigenschaft ab.
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
         * Legt den Wert der agreements-Eigenschaft fest.
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
         * Ruft den Wert der services-Eigenschaft ab.
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
         * Legt den Wert der services-Eigenschaft fest.
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
         * Ruft den Wert der actions-Eigenschaft ab.
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
         * Legt den Wert der actions-Eigenschaft fest.
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
         * Ruft den Wert der as4-Eigenschaft ab.
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
         * Legt den Wert der as4-Eigenschaft fest.
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
         * Ruft den Wert der legConfigurations-Eigenschaft ab.
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
         * Legt den Wert der legConfigurations-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der value-Eigenschaft ab.
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
                 * Legt den Wert der value-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der value-Eigenschaft ab.
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
                 * Legt den Wert der value-Eigenschaft fest.
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
                 * Ruft den Wert der type-Eigenschaft ab.
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
                 * Legt den Wert der type-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der retry-Eigenschaft ab.
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
                 * Legt den Wert der retry-Eigenschaft fest.
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
                 * Ruft den Wert der duplicateDetection-Eigenschaft ab.
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
                 * Legt den Wert der duplicateDetection-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der replyPattern-Eigenschaft ab.
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
                 * Legt den Wert der replyPattern-Eigenschaft fest.
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
                 * Ruft den Wert der nonRepudiation-Eigenschaft ab.
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
                 * Legt den Wert der nonRepudiation-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der errorAsResponse-Eigenschaft ab.
                 * 
                 */
                public boolean isErrorAsResponse() {
                    return errorAsResponse;
                }

                /**
                 * Legt den Wert der errorAsResponse-Eigenschaft fest.
                 * 
                 */
                public void setErrorAsResponse(boolean value) {
                    this.errorAsResponse = value;
                }

                /**
                 * Ruft den Wert der businessErrorNotifyProducer-Eigenschaft ab.
                 * 
                 */
                public boolean isBusinessErrorNotifyProducer() {
                    return businessErrorNotifyProducer;
                }

                /**
                 * Legt den Wert der businessErrorNotifyProducer-Eigenschaft fest.
                 * 
                 */
                public void setBusinessErrorNotifyProducer(boolean value) {
                    this.businessErrorNotifyProducer = value;
                }

                /**
                 * Ruft den Wert der businessErrorNotifyConsumer-Eigenschaft ab.
                 * 
                 */
                public boolean isBusinessErrorNotifyConsumer() {
                    return businessErrorNotifyConsumer;
                }

                /**
                 * Legt den Wert der businessErrorNotifyConsumer-Eigenschaft fest.
                 * 
                 */
                public void setBusinessErrorNotifyConsumer(boolean value) {
                    this.businessErrorNotifyConsumer = value;
                }

                /**
                 * Ruft den Wert der deliveryFailureNotifyProducer-Eigenschaft ab.
                 * 
                 */
                public boolean isDeliveryFailureNotifyProducer() {
                    return deliveryFailureNotifyProducer;
                }

                /**
                 * Legt den Wert der deliveryFailureNotifyProducer-Eigenschaft fest.
                 * 
                 */
                public void setDeliveryFailureNotifyProducer(boolean value) {
                    this.deliveryFailureNotifyProducer = value;
                }

            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der service-Eigenschaft ab.
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
                 * Legt den Wert der service-Eigenschaft fest.
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
                 * Ruft den Wert der action-Eigenschaft ab.
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
                 * Legt den Wert der action-Eigenschaft fest.
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
                 * Ruft den Wert der security-Eigenschaft ab.
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
                 * Legt den Wert der security-Eigenschaft fest.
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
                 * Ruft den Wert der defaultMpc-Eigenschaft ab.
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
                 * Legt den Wert der defaultMpc-Eigenschaft fest.
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
                 * Ruft den Wert der receptionAwareness-Eigenschaft ab.
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
                 * Legt den Wert der receptionAwareness-Eigenschaft fest.
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
                 * Ruft den Wert der reliability-Eigenschaft ab.
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
                 * Legt den Wert der reliability-Eigenschaft fest.
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
                 * Ruft den Wert der propertySet-Eigenschaft ab.
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
                 * Legt den Wert der propertySet-Eigenschaft fest.
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
                 * Ruft den Wert der payloadProfile-Eigenschaft ab.
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
                 * Legt den Wert der payloadProfile-Eigenschaft fest.
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
                 * Ruft den Wert der errorHandling-Eigenschaft ab.
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
                 * Legt den Wert der errorHandling-Eigenschaft fest.
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
                 * Ruft den Wert der compressPayloads-Eigenschaft ab.
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
                 * Legt den Wert der compressPayloads-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der value-Eigenschaft ab.
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
                 * Legt den Wert der value-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der value-Eigenschaft ab.
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
                 * Legt den Wert der value-Eigenschaft fest.
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
                 * Ruft den Wert der legs-Eigenschaft ab.
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
                 * Legt den Wert der legs-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * Ruft den Wert der partyIdTypes-Eigenschaft ab.
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
             * Legt den Wert der partyIdTypes-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der userName-Eigenschaft ab.
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
                 * Legt den Wert der userName-Eigenschaft fest.
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
                 * Ruft den Wert der password-Eigenschaft ab.
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
                 * Legt den Wert der password-Eigenschaft fest.
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
                 * Ruft den Wert der endpoint-Eigenschaft ab.
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
                 * Legt den Wert der endpoint-Eigenschaft fest.
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
                 * Ruft den Wert der allowChunking-Eigenschaft ab.
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
                 * Legt den Wert der allowChunking-Eigenschaft fest.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der partyId-Eigenschaft ab.
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
                     * Legt den Wert der partyId-Eigenschaft fest.
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
                     * Ruft den Wert der partyIdType-Eigenschaft ab.
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
                     * Legt den Wert der partyIdType-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der name-Eigenschaft ab.
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
                     * Legt den Wert der name-Eigenschaft fest.
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
                     * Ruft den Wert der value-Eigenschaft ab.
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
                     * Legt den Wert der value-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der cid-Eigenschaft ab.
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
                 * Legt den Wert der cid-Eigenschaft fest.
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
                 * Ruft den Wert der mimeType-Eigenschaft ab.
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
                 * Legt den Wert der mimeType-Eigenschaft fest.
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
                 * Ruft den Wert der inBody-Eigenschaft ab.
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
                 * Legt den Wert der inBody-Eigenschaft fest.
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
                 * Ruft den Wert der schemaFile-Eigenschaft ab.
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
                 * Legt den Wert der schemaFile-Eigenschaft fest.
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
                 * Ruft den Wert der maxSize-Eigenschaft ab.
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
                 * Legt den Wert der maxSize-Eigenschaft fest.
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
                 * Ruft den Wert der required-Eigenschaft ab.
                 * 
                 */
                public boolean isRequired() {
                    return required;
                }

                /**
                 * Legt den Wert der required-Eigenschaft fest.
                 * 
                 */
                public void setRequired(boolean value) {
                    this.required = value;
                }

            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der maxSize-Eigenschaft ab.
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
                 * Legt den Wert der maxSize-Eigenschaft fest.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der name-Eigenschaft ab.
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
                     * Legt den Wert der name-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * Ruft den Wert der initiatorParties-Eigenschaft ab.
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
             * Legt den Wert der initiatorParties-Eigenschaft fest.
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
             * Ruft den Wert der responderParties-Eigenschaft ab.
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
             * Legt den Wert der responderParties-Eigenschaft fest.
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
             * Ruft den Wert der legs-Eigenschaft ab.
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
             * Legt den Wert der legs-Eigenschaft fest.
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
             * Ruft den Wert der name-Eigenschaft ab.
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
             * Legt den Wert der name-Eigenschaft fest.
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
             * Ruft den Wert der responderRole-Eigenschaft ab.
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
             * Legt den Wert der responderRole-Eigenschaft fest.
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
             * Ruft den Wert der agreement-Eigenschaft ab.
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
             * Legt den Wert der agreement-Eigenschaft fest.
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
             * Ruft den Wert der binding-Eigenschaft ab.
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
             * Legt den Wert der binding-Eigenschaft fest.
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
             * Ruft den Wert der mep-Eigenschaft ab.
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
             * Legt den Wert der mep-Eigenschaft fest.
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
             * Ruft den Wert der initiatorRole-Eigenschaft ab.
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
             * Legt den Wert der initiatorRole-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der name-Eigenschaft ab.
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
                     * Legt den Wert der name-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der name-Eigenschaft ab.
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
                     * Legt den Wert der name-Eigenschaft fest.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der name-Eigenschaft ab.
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
                     * Legt den Wert der name-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der key-Eigenschaft ab.
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
                 * Legt den Wert der key-Eigenschaft fest.
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
                 * Ruft den Wert der datatype-Eigenschaft ab.
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
                 * Legt den Wert der datatype-Eigenschaft fest.
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
                 * Ruft den Wert der required-Eigenschaft ab.
                 * 
                 */
                public boolean isRequired() {
                    return required;
                }

                /**
                 * Legt den Wert der required-Eigenschaft fest.
                 * 
                 */
                public void setRequired(boolean value) {
                    this.required = value;
                }

            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * <p>Java-Klasse für anonymous complex type.
                 * 
                 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                     * Ruft den Wert der property-Eigenschaft ab.
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
                     * Legt den Wert der property-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der value-Eigenschaft ab.
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
                 * Legt den Wert der value-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der policy-Eigenschaft ab.
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
                 * Legt den Wert der policy-Eigenschaft fest.
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
                 * Ruft den Wert der signatureMethod-Eigenschaft ab.
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
                 * Legt den Wert der signatureMethod-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
                 * Ruft den Wert der name-Eigenschaft ab.
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
                 * Legt den Wert der name-Eigenschaft fest.
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
                 * Ruft den Wert der value-Eigenschaft ab.
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
                 * Legt den Wert der value-Eigenschaft fest.
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
                 * Ruft den Wert der type-Eigenschaft ab.
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
                 * Legt den Wert der type-Eigenschaft fest.
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
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
             * Ruft den Wert der name-Eigenschaft ab.
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
             * Legt den Wert der name-Eigenschaft fest.
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
             * Ruft den Wert der retentionDownloaded-Eigenschaft ab.
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
             * Legt den Wert der retentionDownloaded-Eigenschaft fest.
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
             * Ruft den Wert der retentionUndownloaded-Eigenschaft ab.
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
             * Legt den Wert der retentionUndownloaded-Eigenschaft fest.
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
             * Ruft den Wert der default-Eigenschaft ab.
             * 
             */
            public boolean isDefault() {
                return _default;
            }

            /**
             * Legt den Wert der default-Eigenschaft fest.
             * 
             */
            public void setDefault(boolean value) {
                this._default = value;
            }

            /**
             * Ruft den Wert der enabled-Eigenschaft ab.
             * 
             */
            public boolean isEnabled() {
                return enabled;
            }

            /**
             * Legt den Wert der enabled-Eigenschaft fest.
             * 
             */
            public void setEnabled(boolean value) {
                this.enabled = value;
            }

            /**
             * Ruft den Wert der qualifiedName-Eigenschaft ab.
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
             * Legt den Wert der qualifiedName-Eigenschaft fest.
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

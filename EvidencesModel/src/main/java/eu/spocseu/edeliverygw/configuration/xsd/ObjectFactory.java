//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.18 at 01:31:03 PM MESZ 
//


package eu.spocseu.edeliverygw.configuration.xsd;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.eu_spocs.uri.configuration.edelivery package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.eu_spocs.uri.configuration.edelivery
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EDeliveryDetail.PostalAdress }
     * 
     */
    public EDeliveryDetail.PostalAdress createEDeliveryDetailPostalAdress() {
        return new EDeliveryDetail.PostalAdress();
    }

    /**
     * Create an instance of {@link EDeliveryDetail }
     * 
     */
    public EDeliveryDetail createEDeliveryDetail() {
        return new EDeliveryDetail();
    }

    /**
     * Create an instance of {@link EDeliveryDetail.Server }
     * 
     */
    public EDeliveryDetail.Server createEDeliveryDetailServer() {
        return new EDeliveryDetail.Server();
    }

}

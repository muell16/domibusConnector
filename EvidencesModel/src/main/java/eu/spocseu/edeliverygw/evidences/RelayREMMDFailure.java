/* ---------------------------------------------------------------------------
             COMPETITIVENESS AND INNOVATION FRAMEWORK PROGRAMME
                   ICT Policy Support Programme (ICT PSP)
           Preparing the implementation of the Services Directive
                   ICT PSP call identifier: ICT PSP-2008-2
             ICT PSP main Theme identifier: CIP-ICT-PSP.2008.1.1
                           Project acronym: SPOCS
   Project full title: Simple Procedures Online for Cross-border Services
                         Grant agreement no.: 238935
                               www.eu-spocs.eu
------------------------------------------------------------------------------
    WP3 Interoperable delivery, eSafe, secure and interoperable exchanges
                       and acknowledgement of receipt
------------------------------------------------------------------------------
        Open module implementing the eSafe document exchange protocol
------------------------------------------------------------------------------

$URL: svn:https://svnext.bos-bremen.de/SPOCS/AllWpImplementation/EDelivery-Gateway
$Date: 2010-05-13 18:55:57 +0200 (Do, 14. Okt 2010) $
$Revision: 86 $

See SPOCS_WP3_LICENSE_URL for license information
--------------------------------------------------------------------------- */
package eu.spocseu.edeliverygw.evidences;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.etsi.uri._02640.v2.ObjectFactory;
import org.etsi.uri._02640.v2.REMEvidenceType;

import eu.spocseu.edeliverygw.JaxbContextHolder;

/**
 * This class represents a RelayREMMDFailure evidence. It helps to create the
 * underlying REMEvidenceType JAXB object of the XSD structure.
 * 
 * @author Lindemann
 * 
 */
public class RelayREMMDFailure extends Evidence
{

	/**
	 * This constructor creates this RelayREMMDFailure evidence with the given
	 * JAXB object and the configuration.
	 * 
	 * @param evidenceType
	 *            The JAXB object.
	 */
	public RelayREMMDFailure(REMEvidenceType evidenceType)
	{
		super(evidenceType);

	}

	/**
	 * This method serializes the underlying JAXB object.
	 * 
	 * @param out
	 *            The output stream that the information will be streamed into.
	 */
	public void serialize(OutputStream out) throws JAXBException
	{
		JaxbContextHolder
				.getSpocsJaxBContext()
				.createMarshaller()
				.marshal(new ObjectFactory().createRelayREMMDFailure(jaxbObj),
					out);

	}

}

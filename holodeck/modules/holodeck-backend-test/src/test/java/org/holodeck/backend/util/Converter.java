/*
 *
 */
package org.holodeck.backend.util;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.util.StAXUtils;

/**
 * The Class Converter.
 */
public class Converter {


	/**
	 * Convert file to messaging e.
	 *
	 * @param messageFile
	 *            the message file
	 * @return the org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704. messaging e
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * @throws DownloadMessageServiceException
	 *             the download message service exception
	 */
	public static org.holodeck.backend.client.types.MessagingE convertFileToMessagingE(InputStream inputStream) throws Exception {
		XMLStreamReader xmlReader = StAXUtils.createXMLStreamReader(inputStream);
		return org.holodeck.backend.client.types.MessagingE.Factory.parse(xmlReader);
	}
}
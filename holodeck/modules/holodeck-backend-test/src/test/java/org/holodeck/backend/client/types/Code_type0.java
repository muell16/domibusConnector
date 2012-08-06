package org.holodeck.backend.client.types;

import org.apache.axis2.databinding.ADBException;

/**
 * The Class Code_type0.
 */
public class Code_type0 implements org.apache.axis2.databinding.ADBBean {

	/** The Constant MY_QNAME. */
	public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("", "code_type0", "");

	/**
	 * Generate prefix.
	 *
	 * @param namespace the namespace
	 * @return the java.lang. string
	 */
	private static java.lang.String generatePrefix(java.lang.String namespace) {
		if (namespace.equals("")) {
			return "";
		}
		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/** The local code_type0. */
	protected java.lang.String localCode_type0;

	/** The _table_. */
	private static java.util.HashMap _table_ = new java.util.HashMap();

	// Constructor
	/**
	 * Instantiates a new code_type0.
	 *
	 * @param value the value
	 * @param isRegisterValue the is register value
	 */
	protected Code_type0(java.lang.String value, boolean isRegisterValue) {
		localCode_type0 = value;
		if (isRegisterValue) {
			_table_.put(localCode_type0, this);
		}
	}

	/** The Constant _ERROR_GENERAL_001. */
	public static final java.lang.String _ERROR_GENERAL_001 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_GENERAL_001");

	/** The Constant _ERROR_GENERAL_002. */
	public static final java.lang.String _ERROR_GENERAL_002 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_GENERAL_002");

	/** The Constant _ERROR_GENERAL_003. */
	public static final java.lang.String _ERROR_GENERAL_003 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_GENERAL_003");

	/** The Constant _ERROR_SEND_001. */
	public static final java.lang.String _ERROR_SEND_001 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_SEND_001");

	/** The Constant _ERROR_SEND_002. */
	public static final java.lang.String _ERROR_SEND_002 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_SEND_002");

	/** The Constant _ERROR_SEND_003. */
	public static final java.lang.String _ERROR_SEND_003 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_SEND_003");

	/** The Constant _ERROR_SEND_004. */
	public static final java.lang.String _ERROR_SEND_004 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_SEND_004");

	/** The Constant _ERROR_DOWNLOAD_001. */
	public static final java.lang.String _ERROR_DOWNLOAD_001 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_DOWNLOAD_001");

	/** The Constant _ERROR_DOWNLOAD_002. */
	public static final java.lang.String _ERROR_DOWNLOAD_002 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_DOWNLOAD_002");

	/** The Constant _ERROR_DOWNLOAD_003. */
	public static final java.lang.String _ERROR_DOWNLOAD_003 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToString("ERROR_DOWNLOAD_003");

	/** The Constant ERROR_GENERAL_001. */
	public static final Code_type0 ERROR_GENERAL_001 = new Code_type0(_ERROR_GENERAL_001, true);

	/** The Constant ERROR_GENERAL_002. */
	public static final Code_type0 ERROR_GENERAL_002 = new Code_type0(_ERROR_GENERAL_002, true);

	/** The Constant ERROR_GENERAL_003. */
	public static final Code_type0 ERROR_GENERAL_003 = new Code_type0(_ERROR_GENERAL_003, true);

	/** The Constant ERROR_SEND_001. */
	public static final Code_type0 ERROR_SEND_001 = new Code_type0(_ERROR_SEND_001, true);

	/** The Constant ERROR_SEND_002. */
	public static final Code_type0 ERROR_SEND_002 = new Code_type0(_ERROR_SEND_002, true);

	/** The Constant ERROR_SEND_003. */
	public static final Code_type0 ERROR_SEND_003 = new Code_type0(_ERROR_SEND_003, true);

	/** The Constant ERROR_SEND_004. */
	public static final Code_type0 ERROR_SEND_004 = new Code_type0(_ERROR_SEND_004, true);

	/** The Constant ERROR_DOWNLOAD_001. */
	public static final Code_type0 ERROR_DOWNLOAD_001 = new Code_type0(_ERROR_DOWNLOAD_001, true);

	/** The Constant ERROR_DOWNLOAD_002. */
	public static final Code_type0 ERROR_DOWNLOAD_002 = new Code_type0(_ERROR_DOWNLOAD_002, true);

	/** The Constant ERROR_DOWNLOAD_003. */
	public static final Code_type0 ERROR_DOWNLOAD_003 = new Code_type0(_ERROR_DOWNLOAD_003, true);

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public java.lang.String getValue() {
		return localCode_type0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(java.lang.Object obj) {
		return (obj == this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return toString().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public java.lang.String toString() {
		return localCode_type0.toString();
	}

	/**
	 * Checks if is reader mtom aware.
	 *
	 * @param reader the reader
	 * @return true, if is reader mtom aware
	 */
	public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) {
		boolean isReaderMTOMAware = false;
		try {
			isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader
					.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
		} catch (java.lang.IllegalArgumentException e) {
			isReaderMTOMAware = false;
		}
		return isReaderMTOMAware;
	}

	/**
	 * Gets the oM element.
	 *
	 * @param parentQName the parent q name
	 * @param factory the factory
	 * @return the oM element
	 * @throws ADBException the aDB exception
	 */
	public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
			final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException
	{
		org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME) {
			public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
					throws javax.xml.stream.XMLStreamException
			{
				Code_type0.this.serialize(MY_QNAME, factory, xmlWriter);
			}
		};
		return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME, factory, dataSource);
	}

	/* (non-Javadoc)
	 * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, org.apache.axiom.om.OMFactory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter)
	 */
	public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
			org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
	{
		serialize(parentQName, factory, xmlWriter, false);
	}

	/* (non-Javadoc)
	 * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, org.apache.axiom.om.OMFactory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter, boolean)
	 */
	public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
			org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter, boolean serializeType)
			throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
	{
		//We can safely assume an element has only one type associated with it
		java.lang.String namespace = parentQName.getNamespaceURI();
		java.lang.String localName = parentQName.getLocalPart();
		if (!namespace.equals("")) {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				xmlWriter.writeStartElement(prefix, localName, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			} else {
				xmlWriter.writeStartElement(namespace, localName);
			}
		} else {
			xmlWriter.writeStartElement(localName);
		}
		// add the type details if this is used in a simple type
		if (serializeType) {
			java.lang.String namespacePrefix = registerPrefix(xmlWriter, "");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
						+ ":code_type0", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "code_type0", xmlWriter);
			}
		}
		if (localCode_type0 == null) {
			throw new org.apache.axis2.databinding.ADBException("Value cannot be null !!");
		} else {
			xmlWriter.writeCharacters(localCode_type0);
		}
		xmlWriter.writeEndElement();
	}

	/**
	 * Write attribute.
	 *
	 * @param prefix the prefix
	 * @param namespace the namespace
	 * @param attName the att name
	 * @param attValue the att value
	 * @param xmlWriter the xml writer
	 * @throws XMLStreamException the xML stream exception
	 */
	private void writeAttribute(java.lang.String prefix, java.lang.String namespace, java.lang.String attName,
			java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{
		if (xmlWriter.getPrefix(namespace) == null) {
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}
		xmlWriter.writeAttribute(namespace, attName, attValue);
	}

	/**
	 * Write attribute.
	 *
	 * @param namespace the namespace
	 * @param attName the att name
	 * @param attValue the att value
	 * @param xmlWriter the xml writer
	 * @throws XMLStreamException the xML stream exception
	 */
	private void writeAttribute(java.lang.String namespace, java.lang.String attName, java.lang.String attValue,
			javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
	{
		if (namespace.equals("")) {
			xmlWriter.writeAttribute(attName, attValue);
		} else {
			registerPrefix(xmlWriter, namespace);
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}
	}

	/**
	 * Write q name attribute.
	 *
	 * @param namespace the namespace
	 * @param attName the att name
	 * @param qname the qname
	 * @param xmlWriter the xml writer
	 * @throws XMLStreamException the xML stream exception
	 */
	private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
			javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{
		java.lang.String attributeNamespace = qname.getNamespaceURI();
		java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
		if (attributePrefix == null) {
			attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
		}
		java.lang.String attributeValue;
		if (attributePrefix.trim().length() > 0) {
			attributeValue = attributePrefix + ":" + qname.getLocalPart();
		} else {
			attributeValue = qname.getLocalPart();
		}
		if (namespace.equals("")) {
			xmlWriter.writeAttribute(attName, attributeValue);
		} else {
			registerPrefix(xmlWriter, namespace);
			xmlWriter.writeAttribute(namespace, attName, attributeValue);
		}
	}

	/**
	 * Write q name.
	 *
	 * @param qname the qname
	 * @param xmlWriter the xml writer
	 * @throws XMLStreamException the xML stream exception
	 */
	private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{
		java.lang.String namespaceURI = qname.getNamespaceURI();
		if (namespaceURI != null) {
			java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
			if (prefix == null) {
				prefix = generatePrefix(namespaceURI);
				xmlWriter.writeNamespace(prefix, namespaceURI);
				xmlWriter.setPrefix(prefix, namespaceURI);
			}
			if (prefix.trim().length() > 0) {
				xmlWriter.writeCharacters(prefix + ":"
						+ org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			} else {
				// i.e this is the default namespace
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		} else {
			xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
		}
	}

	/**
	 * Write q names.
	 *
	 * @param qnames the qnames
	 * @param xmlWriter the xml writer
	 * @throws XMLStreamException the xML stream exception
	 */
	private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException
	{
		if (qnames != null) {
			// we have to store this data until last moment since it is not possible to write any
			// namespace data after writing the charactor data
			java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
			java.lang.String namespaceURI = null;
			java.lang.String prefix = null;
			for (int i = 0; i < qnames.length; i++) {
				if (i > 0) {
					stringToWrite.append(" ");
				}
				namespaceURI = qnames[i].getNamespaceURI();
				if (namespaceURI != null) {
					prefix = xmlWriter.getPrefix(namespaceURI);
					if ((prefix == null) || (prefix.length() == 0)) {
						prefix = generatePrefix(namespaceURI);
						xmlWriter.writeNamespace(prefix, namespaceURI);
						xmlWriter.setPrefix(prefix, namespaceURI);
					}
					if (prefix.trim().length() > 0) {
						stringToWrite
								.append(prefix)
								.append(":")
								.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qnames[i]));
					}
				} else {
					stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToString(qnames[i]));
				}
			}
			xmlWriter.writeCharacters(stringToWrite.toString());
		}
	}

	/**
	 * Register prefix.
	 *
	 * @param xmlWriter the xml writer
	 * @param namespace the namespace
	 * @return the java.lang. string
	 * @throws XMLStreamException the xML stream exception
	 */
	private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
			throws javax.xml.stream.XMLStreamException
	{
		java.lang.String prefix = xmlWriter.getPrefix(namespace);
		if (prefix == null) {
			prefix = generatePrefix(namespace);
			while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
				prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
			}
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}
		return prefix;
	}

	/* (non-Javadoc)
	 * @see org.apache.axis2.databinding.ADBBean#getPullParser(javax.xml.namespace.QName)
	 */
	public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
			throws org.apache.axis2.databinding.ADBException
	{
		//We can safely assume an element has only one type associated with it
		return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME,
				new java.lang.Object[] { org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
						org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCode_type0) }, null);
	}

	/**
	 * The Class Factory.
	 */
	public static class Factory {

		/**
		 * From value.
		 *
		 * @param value the value
		 * @return the code_type0
		 * @throws IllegalArgumentException the illegal argument exception
		 */
		public static Code_type0 fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
			Code_type0 enumeration = (Code_type0) _table_.get(value);
			if (enumeration == null)
				throw new java.lang.IllegalArgumentException();
			return enumeration;
		}

		/**
		 * From string.
		 *
		 * @param value the value
		 * @param namespaceURI the namespace uri
		 * @return the code_type0
		 * @throws IllegalArgumentException the illegal argument exception
		 */
		public static Code_type0 fromString(java.lang.String value, java.lang.String namespaceURI)
				throws java.lang.IllegalArgumentException
		{
			try {
				return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));
			} catch (java.lang.Exception e) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		/**
		 * From string.
		 *
		 * @param xmlStreamReader the xml stream reader
		 * @param content the content
		 * @return the code_type0
		 */
		public static Code_type0 fromString(javax.xml.stream.XMLStreamReader xmlStreamReader,
				java.lang.String content)
		{
			if (content.indexOf(":") > -1) {
				java.lang.String prefix = content.substring(0, content.indexOf(":"));
				java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
				return Code_type0.Factory.fromString(content, namespaceUri);
			} else {
				return Code_type0.Factory.fromString(content, "");
			}
		}

		/**
		 * Parses the.
		 *
		 * @param reader the reader
		 * @return the code_type0
		 * @throws Exception the exception
		 */
		public static Code_type0 parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			Code_type0 object = null;
			// initialize a hash map to keep values
			java.util.Map attributeMap = new java.util.HashMap();
			java.util.List extraAttributeList = new java.util.ArrayList();
			int event;
			java.lang.String nillableValue = null;
			java.lang.String prefix = "";
			java.lang.String namespaceuri = "";
			try {
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				while (!reader.isEndElement()) {
					if (reader.isStartElement() || reader.hasText()) {
						java.lang.String content = reader.getElementText();
						if (content.indexOf(":") > 0) {
							// this seems to be a Qname so find the namespace and send
							prefix = content.substring(0, content.indexOf(":"));
							namespaceuri = reader.getNamespaceURI(prefix);
							object = Code_type0.Factory.fromString(content, namespaceuri);
						} else {
							// this seems to be not a qname send and empty namespace incase of it is
							// check is done in fromString method
							object = Code_type0.Factory.fromString(content, "");
						}
					} else {
						reader.next();
					}
				} // end of while loop
			} catch (javax.xml.stream.XMLStreamException e) {
				throw new java.lang.Exception(e);
			}
			return object;
		}
	}//end of factory class
}
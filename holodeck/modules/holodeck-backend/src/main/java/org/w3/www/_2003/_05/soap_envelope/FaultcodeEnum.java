/*
 * 
 */
package org.w3.www._2003._05.soap_envelope;

/**
 * The Class FaultcodeEnum.
 */
public class FaultcodeEnum implements org.apache.axis2.databinding.ADBBean {
	
	/** The Constant MY_QNAME. */
	public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
			"http://www.w3.org/2003/05/soap-envelope", "faultcodeEnum", "ns3");

	/**
	 * Generate prefix.
	 *
	 * @param namespace the namespace
	 * @return the java.lang. string
	 */
	private static java.lang.String generatePrefix(java.lang.String namespace) {
		if (namespace.equals("http://www.w3.org/2003/05/soap-envelope")) {
			return "ns3";
		}
		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/** The local faultcode enum. */
	protected javax.xml.namespace.QName localFaultcodeEnum;
	
	/** The _table_. */
	private static java.util.HashMap _table_ = new java.util.HashMap();

	// Constructor
	/**
	 * Instantiates a new faultcode enum.
	 *
	 * @param value the value
	 * @param isRegisterValue the is register value
	 */
	protected FaultcodeEnum(javax.xml.namespace.QName value, boolean isRegisterValue) {
		localFaultcodeEnum = value;
		if (isRegisterValue) {
			_table_.put(localFaultcodeEnum, this);
		}
	}

	/** The Constant _value1. */
	public static final javax.xml.namespace.QName _value1 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToQName("tns:DataEncodingUnknown", "http://www.w3.org/2003/05/soap-envelope");
	
	/** The Constant _value2. */
	public static final javax.xml.namespace.QName _value2 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToQName("tns:MustUnderstand", "http://www.w3.org/2003/05/soap-envelope");
	
	/** The Constant _value3. */
	public static final javax.xml.namespace.QName _value3 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToQName("tns:Receiver", "http://www.w3.org/2003/05/soap-envelope");
	
	/** The Constant _value4. */
	public static final javax.xml.namespace.QName _value4 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToQName("tns:Sender", "http://www.w3.org/2003/05/soap-envelope");
	
	/** The Constant _value5. */
	public static final javax.xml.namespace.QName _value5 = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToQName("tns:VersionMismatch", "http://www.w3.org/2003/05/soap-envelope");
	
	/** The Constant value1. */
	public static final FaultcodeEnum value1 = new FaultcodeEnum(_value1, true);
	
	/** The Constant value2. */
	public static final FaultcodeEnum value2 = new FaultcodeEnum(_value2, true);
	
	/** The Constant value3. */
	public static final FaultcodeEnum value3 = new FaultcodeEnum(_value3, true);
	
	/** The Constant value4. */
	public static final FaultcodeEnum value4 = new FaultcodeEnum(_value4, true);
	
	/** The Constant value5. */
	public static final FaultcodeEnum value5 = new FaultcodeEnum(_value5, true);

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public javax.xml.namespace.QName getValue() {
		return localFaultcodeEnum;
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
		return localFaultcodeEnum.toString();
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
				FaultcodeEnum.this.serialize(MY_QNAME, factory, xmlWriter);
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
			java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.w3.org/2003/05/soap-envelope");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
						+ ":faultcodeEnum", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "faultcodeEnum", xmlWriter);
			}
		}
		if (localFaultcodeEnum == null) {
			throw new org.apache.axis2.databinding.ADBException("Value cannot be null !!");
		} else {
			writeQName(localFaultcodeEnum, xmlWriter);
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
						stringToWrite.append(prefix).append(":")
								.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qnames[i]));
					}
				} else {
					stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
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
		return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME, new java.lang.Object[] {
				org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
				org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFaultcodeEnum) }, null);
	}

	/**
	 * The Class Factory.
	 */
	public static class Factory {
		
		/**
		 * From value.
		 *
		 * @param value the value
		 * @return the faultcode enum
		 * @throws IllegalArgumentException the illegal argument exception
		 */
		public static FaultcodeEnum fromValue(javax.xml.namespace.QName value)
				throws java.lang.IllegalArgumentException
		{
			FaultcodeEnum enumeration = (FaultcodeEnum) _table_.get(value);
			if (enumeration == null)
				throw new java.lang.IllegalArgumentException();
			return enumeration;
		}

		/**
		 * From string.
		 *
		 * @param value the value
		 * @param namespaceURI the namespace uri
		 * @return the faultcode enum
		 * @throws IllegalArgumentException the illegal argument exception
		 */
		public static FaultcodeEnum fromString(java.lang.String value, java.lang.String namespaceURI)
				throws java.lang.IllegalArgumentException
		{
			try {
				return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToQName(value, namespaceURI));
			} catch (java.lang.Exception e) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		/**
		 * From string.
		 *
		 * @param xmlStreamReader the xml stream reader
		 * @param content the content
		 * @return the faultcode enum
		 */
		public static FaultcodeEnum fromString(javax.xml.stream.XMLStreamReader xmlStreamReader,
				java.lang.String content)
		{
			if (content.indexOf(":") > -1) {
				java.lang.String prefix = content.substring(0, content.indexOf(":"));
				java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
				return FaultcodeEnum.Factory.fromString(content, namespaceUri);
			} else {
				return FaultcodeEnum.Factory.fromString(content, "");
			}
		}

		/**
		 * Parses the.
		 *
		 * @param reader the reader
		 * @return the faultcode enum
		 * @throws Exception the exception
		 */
		public static FaultcodeEnum parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			FaultcodeEnum object = null;
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
							object = FaultcodeEnum.Factory.fromString(content, namespaceuri);
						} else {
							// this seems to be not a qname send and empty namespace incase of it is
							// check is done in fromString method
							object = FaultcodeEnum.Factory.fromString(content, "");
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

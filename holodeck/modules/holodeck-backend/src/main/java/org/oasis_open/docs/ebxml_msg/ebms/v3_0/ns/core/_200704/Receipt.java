/*
 * 
 */
package org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704;

/**
 * The Class Receipt.
 */
public class Receipt implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = Receipt Namespace URI =
	 * http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/ Namespace Prefix = ns5
	 */
	/**
	 * Generate prefix.
	 *
	 * @param namespace the namespace
	 * @return the java.lang. string
	 */
	private static java.lang.String generatePrefix(java.lang.String namespace) {
		if (namespace.equals("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/")) {
			return "ns5";
		}
		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/** The local extra element. */
	protected org.apache.axiom.om.OMElement[] localExtraElement;

	/**
	 * Gets the extra element.
	 *
	 * @return the extra element
	 */
	public org.apache.axiom.om.OMElement[] getExtraElement() {
		return localExtraElement;
	}

	/**
	 * Validate extra element.
	 *
	 * @param param the param
	 */
	protected void validateExtraElement(org.apache.axiom.om.OMElement[] param) {
		if ((param != null) && (param.length < 1)) {
			throw new java.lang.RuntimeException();
		}
	}

	/**
	 * Sets the extra element.
	 *
	 * @param param the new extra element
	 */
	public void setExtraElement(org.apache.axiom.om.OMElement[] param) {
		validateExtraElement(param);
		this.localExtraElement = param;
	}

	/**
	 * Adds the extra element.
	 *
	 * @param param the param
	 */
	public void addExtraElement(org.apache.axiom.om.OMElement param) {
		if (localExtraElement == null) {
			localExtraElement = new org.apache.axiom.om.OMElement[] {};
		}
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localExtraElement);
		list.add(param);
		this.localExtraElement = (org.apache.axiom.om.OMElement[]) list.toArray(new org.apache.axiom.om.OMElement[list
				.size()]);
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
		org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName) {
			public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
					throws javax.xml.stream.XMLStreamException
			{
				Receipt.this.serialize(parentQName, factory, xmlWriter);
			}
		};
		return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);
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
		java.lang.String prefix = null;
		java.lang.String namespace = null;
		prefix = parentQName.getPrefix();
		namespace = parentQName.getNamespaceURI();
		if ((namespace != null) && (namespace.trim().length() > 0)) {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
			} else {
				if (prefix == null) {
					prefix = generatePrefix(namespace);
				}
				xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		} else {
			xmlWriter.writeStartElement(parentQName.getLocalPart());
		}
		if (serializeType) {
			java.lang.String namespacePrefix = registerPrefix(xmlWriter,
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
						namespacePrefix + ":Receipt", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Receipt", xmlWriter);
			}
		}
		if (localExtraElement != null) {
			for (int i = 0; i < localExtraElement.length; i++) {
				if (localExtraElement[i] != null) {
					localExtraElement[i].serialize(xmlWriter);
				} else {
					throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
				}
			}
		} else {
			throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
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
		java.util.ArrayList elementList = new java.util.ArrayList();
		java.util.ArrayList attribList = new java.util.ArrayList();
		if (localExtraElement != null) {
			for (int i = 0; i < localExtraElement.length; i++) {
				if (localExtraElement[i] != null) {
					elementList.add(new javax.xml.namespace.QName("", "extraElement"));
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToString(localExtraElement[i]));
				} else {
					throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
				}
			}
		} else {
			throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
		}
		return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(),
				attribList.toArray());
	}

	/**
	 * The Class Factory.
	 */
	public static class Factory {
		
		/**
		 * Parses the.
		 *
		 * @param reader the reader
		 * @return the receipt
		 * @throws Exception the exception
		 */
		public static Receipt parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			Receipt object = new Receipt();
			int event;
			java.lang.String nillableValue = null;
			java.lang.String prefix = "";
			java.lang.String namespaceuri = "";
			try {
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
					java.lang.String fullTypeName = reader.getAttributeValue(
							"http://www.w3.org/2001/XMLSchema-instance", "type");
					if (fullTypeName != null) {
						java.lang.String nsPrefix = null;
						if (fullTypeName.indexOf(":") > -1) {
							nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
						}
						nsPrefix = nsPrefix == null ? "" : nsPrefix;
						java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);
						if (!"Receipt".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (Receipt) backend.ecodex.org.ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				reader.next();
				java.util.ArrayList list1 = new java.util.ArrayList();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()) {
					// Process the array and step past its final element's end.
					boolean loopDone1 = false;
					while (!loopDone1) {
						event = reader.getEventType();
						if (javax.xml.stream.XMLStreamConstants.START_ELEMENT == event) {
							// We need to wrap the reader so that it produces a fake START_DOCUEMENT event
							org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder1 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(
									new org.apache.axis2.util.StreamWrapper(reader), reader.getName());
							list1.add(builder1.getOMElement());
							reader.next();
							if (reader.isEndElement()) {
								// we have two countinuos end elements
								loopDone1 = true;
							}
						} else if (javax.xml.stream.XMLStreamConstants.END_ELEMENT == event) {
							loopDone1 = true;
						} else {
							reader.next();
						}
					}
					object.setExtraElement((org.apache.axiom.om.OMElement[]) org.apache.axis2.databinding.utils.ConverterUtil
							.convertToArray(org.apache.axiom.om.OMElement.class, list1));
				} // End of if for expected property start element
				else {
					// A start element we are not expecting indicates an invalid parameter was passed
					throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
							+ reader.getLocalName());
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement())
					// A start element we are not expecting indicates a trailing invalid property
					throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
							+ reader.getLocalName());
			} catch (javax.xml.stream.XMLStreamException e) {
				throw new java.lang.Exception(e);
			}
			return object;
		}
	}//end of factory class
}

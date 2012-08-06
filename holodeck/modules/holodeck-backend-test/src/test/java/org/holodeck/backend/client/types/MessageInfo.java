package org.holodeck.backend.client.types;

import org.apache.axis2.databinding.ADBException;

/**
 * The Class MessageInfo.
 */
public class MessageInfo implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = MessageInfo Namespace URI =
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

	/** The local timestamp. */
	protected java.util.Calendar localTimestamp;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local timestamp tracker. */
	protected boolean localTimestampTracker = false;

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public java.util.Calendar getTimestamp() {
		return localTimestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param param the new timestamp
	 */
	public void setTimestamp(java.util.Calendar param) {
		if (param != null) {
			//update the setting tracker
			localTimestampTracker = true;
		} else {
			localTimestampTracker = false;
		}
		this.localTimestamp = param;
	}

	/** The local message id. */
	protected NonEmptyString localMessageId;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local message id tracker. */
	protected boolean localMessageIdTracker = false;

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public NonEmptyString getMessageId() {
		return localMessageId;
	}

	/**
	 * Sets the message id.
	 *
	 * @param param the new message id
	 */
	public void setMessageId(NonEmptyString param) {
		if (param != null) {
			//update the setting tracker
			localMessageIdTracker = true;
		} else {
			localMessageIdTracker = false;
		}
		this.localMessageId = param;
	}

	/** The local ref to message id. */
	protected NonEmptyString localRefToMessageId;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local ref to message id tracker. */
	protected boolean localRefToMessageIdTracker = false;

	/**
	 * Gets the ref to message id.
	 *
	 * @return the ref to message id
	 */
	public NonEmptyString getRefToMessageId() {
		return localRefToMessageId;
	}

	/**
	 * Sets the ref to message id.
	 *
	 * @param param the new ref to message id
	 */
	public void setRefToMessageId(NonEmptyString param) {
		if (param != null) {
			//update the setting tracker
			localRefToMessageIdTracker = true;
		} else {
			localRefToMessageIdTracker = false;
		}
		this.localRefToMessageId = param;
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
		org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,
				parentQName) {
			public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
					throws javax.xml.stream.XMLStreamException
			{
				MessageInfo.this.serialize(parentQName, factory, xmlWriter);
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
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
						+ ":MessageInfo", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "MessageInfo", xmlWriter);
			}
		}
		if (localTimestampTracker) {
			namespace = "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/";
			if (!namespace.equals("")) {
				prefix = xmlWriter.getPrefix(namespace);
				if (prefix == null) {
					prefix = generatePrefix(namespace);
					xmlWriter.writeStartElement(prefix, "Timestamp", namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				} else {
					xmlWriter.writeStartElement(namespace, "Timestamp");
				}
			} else {
				xmlWriter.writeStartElement("Timestamp");
			}
			if (localTimestamp == null) {
				// write the nil attribute
				throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");
			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
						.convertToString(localTimestamp));
			}
			xmlWriter.writeEndElement();
		}
		if (localMessageIdTracker) {
			if (localMessageId == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageId cannot be null!!");
			}
			localMessageId.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageId"), factory,
					xmlWriter);
		}
		if (localRefToMessageIdTracker) {
			if (localRefToMessageId == null) {
				throw new org.apache.axis2.databinding.ADBException("RefToMessageId cannot be null!!");
			}
			localRefToMessageId.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "RefToMessageId"), factory,
					xmlWriter);
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
		java.util.ArrayList elementList = new java.util.ArrayList();
		java.util.ArrayList attribList = new java.util.ArrayList();
		if (localTimestampTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Timestamp"));
			if (localTimestamp != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");
			}
		}
		if (localMessageIdTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageId"));
			if (localMessageId == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageId cannot be null!!");
			}
			elementList.add(localMessageId);
		}
		if (localRefToMessageIdTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "RefToMessageId"));
			if (localRefToMessageId == null) {
				throw new org.apache.axis2.databinding.ADBException("RefToMessageId cannot be null!!");
			}
			elementList.add(localRefToMessageId);
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
		 * @return the message info
		 * @throws Exception the exception
		 */
		public static MessageInfo parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			MessageInfo object = new MessageInfo();
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
						if (!"MessageInfo".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (MessageInfo) ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				reader.next();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Timestamp")
								.equals(reader.getName())) {
					java.lang.String content = reader.getElementText();
					object.setTimestamp(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageId")
								.equals(reader.getName())) {
					object.setMessageId(NonEmptyString.Factory.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "RefToMessageId")
								.equals(reader.getName())) {
					object.setRefToMessageId(NonEmptyString.Factory.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
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
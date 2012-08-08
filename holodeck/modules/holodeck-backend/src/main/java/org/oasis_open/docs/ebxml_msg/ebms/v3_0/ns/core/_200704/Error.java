/*
 * 
 */
package org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704;

/**
 * The Class Error.
 */
public class Error implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = Error Namespace URI =
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

	/** The local description. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description localDescription;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local description tracker. */
	protected boolean localDescriptionTracker = false;

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description getDescription() {
		return localDescription;
	}

	/**
	 * Sets the description.
	 *
	 * @param param the new description
	 */
	public void setDescription(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description param) {
		if (param != null) {
			//update the setting tracker
			localDescriptionTracker = true;
		} else {
			localDescriptionTracker = false;
		}
		this.localDescription = param;
	}

	/** The local error detail. */
	protected org.apache.axis2.databinding.types.Token localErrorDetail;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local error detail tracker. */
	protected boolean localErrorDetailTracker = false;

	/**
	 * Gets the error detail.
	 *
	 * @return the error detail
	 */
	public org.apache.axis2.databinding.types.Token getErrorDetail() {
		return localErrorDetail;
	}

	/**
	 * Sets the error detail.
	 *
	 * @param param the new error detail
	 */
	public void setErrorDetail(org.apache.axis2.databinding.types.Token param) {
		if (param != null) {
			//update the setting tracker
			localErrorDetailTracker = true;
		} else {
			localErrorDetailTracker = false;
		}
		this.localErrorDetail = param;
	}

	/** The local category. */
	protected org.apache.axis2.databinding.types.Token localCategory;

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public org.apache.axis2.databinding.types.Token getCategory() {
		return localCategory;
	}

	/**
	 * Sets the category.
	 *
	 * @param param the new category
	 */
	public void setCategory(org.apache.axis2.databinding.types.Token param) {
		this.localCategory = param;
	}

	/** The local ref to message in error. */
	protected org.apache.axis2.databinding.types.Token localRefToMessageInError;

	/**
	 * Gets the ref to message in error.
	 *
	 * @return the ref to message in error
	 */
	public org.apache.axis2.databinding.types.Token getRefToMessageInError() {
		return localRefToMessageInError;
	}

	/**
	 * Sets the ref to message in error.
	 *
	 * @param param the new ref to message in error
	 */
	public void setRefToMessageInError(org.apache.axis2.databinding.types.Token param) {
		this.localRefToMessageInError = param;
	}

	/** The local error code. */
	protected org.apache.axis2.databinding.types.Token localErrorCode;

	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public org.apache.axis2.databinding.types.Token getErrorCode() {
		return localErrorCode;
	}

	/**
	 * Sets the error code.
	 *
	 * @param param the new error code
	 */
	public void setErrorCode(org.apache.axis2.databinding.types.Token param) {
		this.localErrorCode = param;
	}

	/** The local origin. */
	protected org.apache.axis2.databinding.types.Token localOrigin;

	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public org.apache.axis2.databinding.types.Token getOrigin() {
		return localOrigin;
	}

	/**
	 * Sets the origin.
	 *
	 * @param param the new origin
	 */
	public void setOrigin(org.apache.axis2.databinding.types.Token param) {
		this.localOrigin = param;
	}

	/** The local severity. */
	protected org.apache.axis2.databinding.types.Token localSeverity;

	/**
	 * Gets the severity.
	 *
	 * @return the severity
	 */
	public org.apache.axis2.databinding.types.Token getSeverity() {
		return localSeverity;
	}

	/**
	 * Sets the severity.
	 *
	 * @param param the new severity
	 */
	public void setSeverity(org.apache.axis2.databinding.types.Token param) {
		this.localSeverity = param;
	}

	/** The local short description. */
	protected org.apache.axis2.databinding.types.Token localShortDescription;

	/**
	 * Gets the short description.
	 *
	 * @return the short description
	 */
	public org.apache.axis2.databinding.types.Token getShortDescription() {
		return localShortDescription;
	}

	/**
	 * Sets the short description.
	 *
	 * @param param the new short description
	 */
	public void setShortDescription(org.apache.axis2.databinding.types.Token param) {
		this.localShortDescription = param;
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
				Error.this.serialize(parentQName, factory, xmlWriter);
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
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":Error",
						xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Error", xmlWriter);
			}
		}
		if (localCategory != null) {
			writeAttribute("", "category",
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCategory), xmlWriter);
		}
		if (localRefToMessageInError != null) {
			writeAttribute("", "refToMessageInError",
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRefToMessageInError),
					xmlWriter);
		}
		if (localErrorCode != null) {
			writeAttribute("", "errorCode",
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorCode), xmlWriter);
		} else {
			throw new org.apache.axis2.databinding.ADBException("required attribute localErrorCode is null");
		}
		if (localOrigin != null) {
			writeAttribute("", "origin", org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrigin),
					xmlWriter);
		}
		if (localSeverity != null) {
			writeAttribute("", "severity",
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSeverity), xmlWriter);
		} else {
			throw new org.apache.axis2.databinding.ADBException("required attribute localSeverity is null");
		}
		if (localShortDescription != null) {
			writeAttribute("", "shortDescription",
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShortDescription), xmlWriter);
		}
		if (localDescriptionTracker) {
			if (localDescription == null) {
				throw new org.apache.axis2.databinding.ADBException("Description cannot be null!!");
			}
			localDescription.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Description"), factory,
					xmlWriter);
		}
		if (localErrorDetailTracker) {
			namespace = "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/";
			if (!namespace.equals("")) {
				prefix = xmlWriter.getPrefix(namespace);
				if (prefix == null) {
					prefix = generatePrefix(namespace);
					xmlWriter.writeStartElement(prefix, "ErrorDetail", namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				} else {
					xmlWriter.writeStartElement(namespace, "ErrorDetail");
				}
			} else {
				xmlWriter.writeStartElement("ErrorDetail");
			}
			if (localErrorDetail == null) {
				// write the nil attribute
				throw new org.apache.axis2.databinding.ADBException("ErrorDetail cannot be null!!");
			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
						.convertToString(localErrorDetail));
			}
			xmlWriter.writeEndElement();
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
		if (localDescriptionTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Description"));
			if (localDescription == null) {
				throw new org.apache.axis2.databinding.ADBException("Description cannot be null!!");
			}
			elementList.add(localDescription);
		}
		if (localErrorDetailTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "ErrorDetail"));
			if (localErrorDetail != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorDetail));
			} else {
				throw new org.apache.axis2.databinding.ADBException("ErrorDetail cannot be null!!");
			}
		}
		attribList.add(new javax.xml.namespace.QName("", "category"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCategory));
		attribList.add(new javax.xml.namespace.QName("", "refToMessageInError"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRefToMessageInError));
		attribList.add(new javax.xml.namespace.QName("", "errorCode"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorCode));
		attribList.add(new javax.xml.namespace.QName("", "origin"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrigin));
		attribList.add(new javax.xml.namespace.QName("", "severity"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSeverity));
		attribList.add(new javax.xml.namespace.QName("", "shortDescription"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShortDescription));
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
		 * @return the error
		 * @throws Exception the exception
		 */
		public static Error parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			Error object = new Error();
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
						if (!"Error".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (Error) backend.ecodex.org.ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				// handle attribute "category"
				java.lang.String tempAttribCategory = reader.getAttributeValue(null, "category");
				if (tempAttribCategory != null) {
					java.lang.String content = tempAttribCategory;
					object.setCategory(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToToken(tempAttribCategory));
				} else {
				}
				handledAttributes.add("category");
				// handle attribute "refToMessageInError"
				java.lang.String tempAttribRefToMessageInError = reader.getAttributeValue(null, "refToMessageInError");
				if (tempAttribRefToMessageInError != null) {
					java.lang.String content = tempAttribRefToMessageInError;
					object.setRefToMessageInError(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToToken(tempAttribRefToMessageInError));
				} else {
				}
				handledAttributes.add("refToMessageInError");
				// handle attribute "errorCode"
				java.lang.String tempAttribErrorCode = reader.getAttributeValue(null, "errorCode");
				if (tempAttribErrorCode != null) {
					java.lang.String content = tempAttribErrorCode;
					object.setErrorCode(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToToken(tempAttribErrorCode));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Required attribute errorCode is missing");
				}
				handledAttributes.add("errorCode");
				// handle attribute "origin"
				java.lang.String tempAttribOrigin = reader.getAttributeValue(null, "origin");
				if (tempAttribOrigin != null) {
					java.lang.String content = tempAttribOrigin;
					object.setOrigin(org.apache.axis2.databinding.utils.ConverterUtil.convertToToken(tempAttribOrigin));
				} else {
				}
				handledAttributes.add("origin");
				// handle attribute "severity"
				java.lang.String tempAttribSeverity = reader.getAttributeValue(null, "severity");
				if (tempAttribSeverity != null) {
					java.lang.String content = tempAttribSeverity;
					object.setSeverity(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToToken(tempAttribSeverity));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Required attribute severity is missing");
				}
				handledAttributes.add("severity");
				// handle attribute "shortDescription"
				java.lang.String tempAttribShortDescription = reader.getAttributeValue(null, "shortDescription");
				if (tempAttribShortDescription != null) {
					java.lang.String content = tempAttribShortDescription;
					object.setShortDescription(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToToken(tempAttribShortDescription));
				} else {
				}
				handledAttributes.add("shortDescription");
				reader.next();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Description")
								.equals(reader.getName())) {
					object.setDescription(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description.Factory
							.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "ErrorDetail")
								.equals(reader.getName())) {
					java.lang.String content = reader.getElementText();
					object.setErrorDetail(org.apache.axis2.databinding.utils.ConverterUtil.convertToToken(content));
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

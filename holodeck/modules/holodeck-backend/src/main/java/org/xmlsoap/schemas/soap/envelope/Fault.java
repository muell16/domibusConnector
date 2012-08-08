/*
 * 
 */
package org.xmlsoap.schemas.soap.envelope;

/**
 * The Class Fault.
 */
public class Fault implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = Fault Namespace URI =
	 * http://schemas.xmlsoap.org/soap/envelope/ Namespace Prefix = ns2
	 */
	/**
	 * Generate prefix.
	 *
	 * @param namespace the namespace
	 * @return the java.lang. string
	 */
	private static java.lang.String generatePrefix(java.lang.String namespace) {
		if (namespace.equals("http://schemas.xmlsoap.org/soap/envelope/")) {
			return "ns2";
		}
		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/** The local faultcode. */
	protected javax.xml.namespace.QName localFaultcode;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local faultcode tracker. */
	protected boolean localFaultcodeTracker = false;

	/**
	 * Gets the faultcode.
	 *
	 * @return the faultcode
	 */
	public javax.xml.namespace.QName getFaultcode() {
		return localFaultcode;
	}

	/**
	 * Sets the faultcode.
	 *
	 * @param param the new faultcode
	 */
	public void setFaultcode(javax.xml.namespace.QName param) {
		if (param != null) {
			//update the setting tracker
			localFaultcodeTracker = true;
		} else {
			localFaultcodeTracker = false;
		}
		this.localFaultcode = param;
	}

	/** The local faultstring. */
	protected java.lang.String localFaultstring;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local faultstring tracker. */
	protected boolean localFaultstringTracker = false;

	/**
	 * Gets the faultstring.
	 *
	 * @return the faultstring
	 */
	public java.lang.String getFaultstring() {
		return localFaultstring;
	}

	/**
	 * Sets the faultstring.
	 *
	 * @param param the new faultstring
	 */
	public void setFaultstring(java.lang.String param) {
		if (param != null) {
			//update the setting tracker
			localFaultstringTracker = true;
		} else {
			localFaultstringTracker = false;
		}
		this.localFaultstring = param;
	}

	/** The local faultactor. */
	protected org.apache.axis2.databinding.types.URI localFaultactor;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local faultactor tracker. */
	protected boolean localFaultactorTracker = false;

	/**
	 * Gets the faultactor.
	 *
	 * @return the faultactor
	 */
	public org.apache.axis2.databinding.types.URI getFaultactor() {
		return localFaultactor;
	}

	/**
	 * Sets the faultactor.
	 *
	 * @param param the new faultactor
	 */
	public void setFaultactor(org.apache.axis2.databinding.types.URI param) {
		if (param != null) {
			//update the setting tracker
			localFaultactorTracker = true;
		} else {
			localFaultactorTracker = false;
		}
		this.localFaultactor = param;
	}

	/** The local detail. */
	protected org.xmlsoap.schemas.soap.envelope.Detail localDetail;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local detail tracker. */
	protected boolean localDetailTracker = false;

	/**
	 * Gets the detail.
	 *
	 * @return the detail
	 */
	public org.xmlsoap.schemas.soap.envelope.Detail getDetail() {
		return localDetail;
	}

	/**
	 * Sets the detail.
	 *
	 * @param param the new detail
	 */
	public void setDetail(org.xmlsoap.schemas.soap.envelope.Detail param) {
		if (param != null) {
			//update the setting tracker
			localDetailTracker = true;
		} else {
			localDetailTracker = false;
		}
		this.localDetail = param;
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
				Fault.this.serialize(parentQName, factory, xmlWriter);
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
			java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://schemas.xmlsoap.org/soap/envelope/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":Fault",
						xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Fault", xmlWriter);
			}
		}
		if (localFaultcodeTracker) {
			namespace = "";
			if (!namespace.equals("")) {
				prefix = xmlWriter.getPrefix(namespace);
				if (prefix == null) {
					prefix = generatePrefix(namespace);
					xmlWriter.writeStartElement(prefix, "faultcode", namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				} else {
					xmlWriter.writeStartElement(namespace, "faultcode");
				}
			} else {
				xmlWriter.writeStartElement("faultcode");
			}
			if (localFaultcode == null) {
				// write the nil attribute
				throw new org.apache.axis2.databinding.ADBException("faultcode cannot be null!!");
			} else {
				writeQName(localFaultcode, xmlWriter);
			}
			xmlWriter.writeEndElement();
		}
		if (localFaultstringTracker) {
			namespace = "";
			if (!namespace.equals("")) {
				prefix = xmlWriter.getPrefix(namespace);
				if (prefix == null) {
					prefix = generatePrefix(namespace);
					xmlWriter.writeStartElement(prefix, "faultstring", namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				} else {
					xmlWriter.writeStartElement(namespace, "faultstring");
				}
			} else {
				xmlWriter.writeStartElement("faultstring");
			}
			if (localFaultstring == null) {
				// write the nil attribute
				throw new org.apache.axis2.databinding.ADBException("faultstring cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localFaultstring);
			}
			xmlWriter.writeEndElement();
		}
		if (localFaultactorTracker) {
			namespace = "";
			if (!namespace.equals("")) {
				prefix = xmlWriter.getPrefix(namespace);
				if (prefix == null) {
					prefix = generatePrefix(namespace);
					xmlWriter.writeStartElement(prefix, "faultactor", namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				} else {
					xmlWriter.writeStartElement(namespace, "faultactor");
				}
			} else {
				xmlWriter.writeStartElement("faultactor");
			}
			if (localFaultactor == null) {
				// write the nil attribute
				throw new org.apache.axis2.databinding.ADBException("faultactor cannot be null!!");
			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
						.convertToString(localFaultactor));
			}
			xmlWriter.writeEndElement();
		}
		if (localDetailTracker) {
			if (localDetail == null) {
				throw new org.apache.axis2.databinding.ADBException("detail cannot be null!!");
			}
			localDetail.serialize(new javax.xml.namespace.QName("", "detail"), factory, xmlWriter);
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
		if (localFaultcodeTracker) {
			elementList.add(new javax.xml.namespace.QName("", "faultcode"));
			if (localFaultcode != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFaultcode));
			} else {
				throw new org.apache.axis2.databinding.ADBException("faultcode cannot be null!!");
			}
		}
		if (localFaultstringTracker) {
			elementList.add(new javax.xml.namespace.QName("", "faultstring"));
			if (localFaultstring != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFaultstring));
			} else {
				throw new org.apache.axis2.databinding.ADBException("faultstring cannot be null!!");
			}
		}
		if (localFaultactorTracker) {
			elementList.add(new javax.xml.namespace.QName("", "faultactor"));
			if (localFaultactor != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFaultactor));
			} else {
				throw new org.apache.axis2.databinding.ADBException("faultactor cannot be null!!");
			}
		}
		if (localDetailTracker) {
			elementList.add(new javax.xml.namespace.QName("", "detail"));
			if (localDetail == null) {
				throw new org.apache.axis2.databinding.ADBException("detail cannot be null!!");
			}
			elementList.add(localDetail);
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
		 * @return the fault
		 * @throws Exception the exception
		 */
		public static Fault parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			Fault object = new Fault();
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
						if (!"Fault".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (Fault) backend.ecodex.org.ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				reader.next();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement() && new javax.xml.namespace.QName("", "faultcode").equals(reader.getName())) {
					java.lang.String content = reader.getElementText();
					int index = content.indexOf(":");
					if (index > 0) {
						prefix = content.substring(0, index);
					} else {
						prefix = "";
					}
					namespaceuri = reader.getNamespaceURI(prefix);
					object.setFaultcode(org.apache.axis2.databinding.utils.ConverterUtil.convertToQName(content,
							namespaceuri));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "faultstring").equals(reader.getName())) {
					java.lang.String content = reader.getElementText();
					object.setFaultstring(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement() && new javax.xml.namespace.QName("", "faultactor").equals(reader.getName())) {
					java.lang.String content = reader.getElementText();
					object.setFaultactor(org.apache.axis2.databinding.utils.ConverterUtil.convertToAnyURI(content));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement() && new javax.xml.namespace.QName("", "detail").equals(reader.getName())) {
					object.setDetail(org.xmlsoap.schemas.soap.envelope.Detail.Factory.parse(reader));
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

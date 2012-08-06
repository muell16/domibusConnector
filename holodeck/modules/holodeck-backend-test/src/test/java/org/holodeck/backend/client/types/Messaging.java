package org.holodeck.backend.client.types;

import org.apache.axis2.databinding.ADBException;

/**
 * The Class Messaging.
 */
public class Messaging implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = Messaging Namespace URI =
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

	/** The local signal message. */
	protected SignalMessage[] localSignalMessage;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local signal message tracker. */
	protected boolean localSignalMessageTracker = false;

	/**
	 * Gets the signal message.
	 *
	 * @return the signal message
	 */
	public SignalMessage[] getSignalMessage() {
		return localSignalMessage;
	}

	/**
	 * Validate signal message.
	 *
	 * @param param the param
	 */
	protected void validateSignalMessage(SignalMessage[] param) {
	}

	/**
	 * Sets the signal message.
	 *
	 * @param param the new signal message
	 */
	public void setSignalMessage(SignalMessage[] param) {
		validateSignalMessage(param);
		if (param != null) {
			//update the setting tracker
			localSignalMessageTracker = true;
		} else {
			localSignalMessageTracker = false;
		}
		this.localSignalMessage = param;
	}

	/**
	 * Adds the signal message.
	 *
	 * @param param the param
	 */
	public void addSignalMessage(SignalMessage param) {
		if (localSignalMessage == null) {
			localSignalMessage = new SignalMessage[] {};
		}
		//update the setting tracker
		localSignalMessageTracker = true;
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localSignalMessage);
		list.add(param);
		this.localSignalMessage = (SignalMessage[]) list.toArray(new SignalMessage[list.size()]);
	}

	/** The local user message. */
	protected UserMessage[] localUserMessage;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local user message tracker. */
	protected boolean localUserMessageTracker = false;

	/**
	 * Gets the user message.
	 *
	 * @return the user message
	 */
	public UserMessage[] getUserMessage() {
		return localUserMessage;
	}

	/**
	 * Validate user message.
	 *
	 * @param param the param
	 */
	protected void validateUserMessage(UserMessage[] param) {
	}

	/**
	 * Sets the user message.
	 *
	 * @param param the new user message
	 */
	public void setUserMessage(UserMessage[] param) {
		validateUserMessage(param);
		if (param != null) {
			//update the setting tracker
			localUserMessageTracker = true;
		} else {
			localUserMessageTracker = false;
		}
		this.localUserMessage = param;
	}

	/**
	 * Adds the user message.
	 *
	 * @param param the param
	 */
	public void addUserMessage(UserMessage param) {
		if (localUserMessage == null) {
			localUserMessage = new UserMessage[] {};
		}
		//update the setting tracker
		localUserMessageTracker = true;
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localUserMessage);
		list.add(param);
		this.localUserMessage = (UserMessage[]) list.toArray(new UserMessage[list.size()]);
	}

	/** The local extra element. */
	protected org.apache.axiom.om.OMElement[] localExtraElement;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local extra element tracker. */
	protected boolean localExtraElementTracker = false;

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
	}

	/**
	 * Sets the extra element.
	 *
	 * @param param the new extra element
	 */
	public void setExtraElement(org.apache.axiom.om.OMElement[] param) {
		validateExtraElement(param);
		if (param != null) {
			//update the setting tracker
			localExtraElementTracker = true;
		} else {
			localExtraElementTracker = false;
		}
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
		//update the setting tracker
		localExtraElementTracker = true;
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localExtraElement);
		list.add(param);
		this.localExtraElement = (org.apache.axiom.om.OMElement[]) list
				.toArray(new org.apache.axiom.om.OMElement[list.size()]);
	}

	/** The local id. */
	protected org.apache.axis2.databinding.types.Id localId;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public org.apache.axis2.databinding.types.Id getId() {
		return localId;
	}

	/**
	 * Sets the id.
	 *
	 * @param param the new id
	 */
	public void setId(org.apache.axis2.databinding.types.Id param) {
		this.localId = param;
	}

	/** The local must understand11. */
	protected MustUnderstand_type0 localMustUnderstand11;

	/**
	 * Gets the must understand11.
	 *
	 * @return the must understand11
	 */
	public MustUnderstand_type0 getMustUnderstand11() {
		return localMustUnderstand11;
	}

	/**
	 * Sets the must understand11.
	 *
	 * @param param the new must understand11
	 */
	public void setMustUnderstand11(MustUnderstand_type0 param) {
		this.localMustUnderstand11 = param;
	}

	/** The local must understand12. */
	protected boolean localMustUnderstand12 = org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("0");

	/**
	 * Gets the must understand12.
	 *
	 * @return the must understand12
	 */
	public boolean getMustUnderstand12() {
		return localMustUnderstand12;
	}

	/**
	 * Sets the must understand12.
	 *
	 * @param param the new must understand12
	 */
	public void setMustUnderstand12(boolean param) {
		this.localMustUnderstand12 = param;
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
				Messaging.this.serialize(parentQName, factory, xmlWriter);
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
						+ ":Messaging", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Messaging", xmlWriter);
			}
		}
		if (localId != null) {
			writeAttribute("", "id", org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId),
					xmlWriter);
		}
		if (localMustUnderstand11 != null) {
			writeAttribute("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand",
					localMustUnderstand11.toString(), xmlWriter);
		}
		if (true) {
			writeAttribute("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand",
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMustUnderstand12), xmlWriter);
		}
		if (localSignalMessageTracker) {
			if (localSignalMessage != null) {
				for (int i = 0; i < localSignalMessage.length; i++) {
					if (localSignalMessage[i] != null) {
						localSignalMessage[i].serialize(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "SignalMessage"),
								factory, xmlWriter);
					} else {
						// we don't have to do any thing since minOccures is zero
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("SignalMessage cannot be null!!");
			}
		}
		if (localUserMessageTracker) {
			if (localUserMessage != null) {
				for (int i = 0; i < localUserMessage.length; i++) {
					if (localUserMessage[i] != null) {
						localUserMessage[i].serialize(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "UserMessage"),
								factory, xmlWriter);
					} else {
						// we don't have to do any thing since minOccures is zero
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("UserMessage cannot be null!!");
			}
		}
		if (localExtraElementTracker) {
			if (localExtraElement != null) {
				for (int i = 0; i < localExtraElement.length; i++) {
					if (localExtraElement[i] != null) {
						localExtraElement[i].serialize(xmlWriter);
					} else {
						// we have to do nothing since minOccures zero
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
			}
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
		if (localSignalMessageTracker) {
			if (localSignalMessage != null) {
				for (int i = 0; i < localSignalMessage.length; i++) {
					if (localSignalMessage[i] != null) {
						elementList.add(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "SignalMessage"));
						elementList.add(localSignalMessage[i]);
					} else {
						// nothing to do
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("SignalMessage cannot be null!!");
			}
		}
		if (localUserMessageTracker) {
			if (localUserMessage != null) {
				for (int i = 0; i < localUserMessage.length; i++) {
					if (localUserMessage[i] != null) {
						elementList.add(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "UserMessage"));
						elementList.add(localUserMessage[i]);
					} else {
						// nothing to do
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("UserMessage cannot be null!!");
			}
		}
		if (localExtraElementTracker) {
			if (localExtraElement != null) {
				for (int i = 0; i < localExtraElement.length; i++) {
					if (localExtraElement[i] != null) {
						elementList.add(new javax.xml.namespace.QName("", "extraElement"));
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localExtraElement[i]));
					} else {
						// have to do nothing
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
			}
		}
		attribList.add(new javax.xml.namespace.QName("", "id"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
		attribList.add(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand"));
		attribList.add(localMustUnderstand11.toString());
		attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMustUnderstand12));
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
		 * @return the messaging
		 * @throws Exception the exception
		 */
		public static Messaging parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			Messaging object = new Messaging();
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
						if (!"Messaging".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (Messaging) ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				// handle attribute "id"
				java.lang.String tempAttribId = reader.getAttributeValue(null, "id");
				if (tempAttribId != null) {
					java.lang.String content = tempAttribId;
					object.setId(org.apache.axis2.databinding.utils.ConverterUtil.convertToID(tempAttribId));
				} else {
				}
				handledAttributes.add("id");
				// handle attribute "mustUnderstand"
				java.lang.String tempAttribMustUnderstand11 = reader.getAttributeValue(
						"http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand");
				if (tempAttribMustUnderstand11 != null) {
					java.lang.String content = tempAttribMustUnderstand11;
					object.setMustUnderstand11(MustUnderstand_type0.Factory.fromString(
							reader, tempAttribMustUnderstand11));
				} else {
				}
				handledAttributes.add("mustUnderstand");
				// handle attribute "mustUnderstand"
				java.lang.String tempAttribMustUnderstand12 = reader.getAttributeValue(
						"http://www.w3.org/2003/05/soap-envelope", "mustUnderstand");
				if (tempAttribMustUnderstand12 != null) {
					java.lang.String content = tempAttribMustUnderstand12;
					object.setMustUnderstand12((org.apache.axis2.databinding.utils.ConverterUtil
							.convertToBoolean(tempAttribMustUnderstand12)));
				} else {
				}
				handledAttributes.add("mustUnderstand");
				reader.next();
				java.util.ArrayList list1 = new java.util.ArrayList();
				java.util.ArrayList list2 = new java.util.ArrayList();
				java.util.ArrayList list3 = new java.util.ArrayList();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "SignalMessage")
								.equals(reader.getName())) {
					// Process the array and step past its final element's end.
					list1.add(SignalMessage.Factory.parse(reader));
					//loop until we find a start element that is not part of this array
					boolean loopDone1 = false;
					while (!loopDone1) {
						// We should be at the end element, but make sure
						while (!reader.isEndElement())
							reader.next();
						// Step out of this element
						reader.next();
						// Step to next element event.
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();
						if (reader.isEndElement()) {
							//two continuous end elements means we are exiting the xml structure
							loopDone1 = true;
						} else {
							if (new javax.xml.namespace.QName(
									"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/",
									"SignalMessage").equals(reader.getName())) {
								list1.add(SignalMessage.Factory.parse(reader));
							} else {
								loopDone1 = true;
							}
						}
					}
					// call the converter utility  to convert and set the array
					object.setSignalMessage((SignalMessage[]) org.apache.axis2.databinding.utils.ConverterUtil
							.convertToArray(SignalMessage.class, list1));
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "UserMessage")
								.equals(reader.getName())) {
					// Process the array and step past its final element's end.
					list2.add(UserMessage.Factory.parse(reader));
					//loop until we find a start element that is not part of this array
					boolean loopDone2 = false;
					while (!loopDone2) {
						// We should be at the end element, but make sure
						while (!reader.isEndElement())
							reader.next();
						// Step out of this element
						reader.next();
						// Step to next element event.
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();
						if (reader.isEndElement()) {
							//two continuous end elements means we are exiting the xml structure
							loopDone2 = true;
						} else {
							if (new javax.xml.namespace.QName(
									"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "UserMessage")
									.equals(reader.getName())) {
								list2.add(UserMessage.Factory.parse(reader));
							} else {
								loopDone2 = true;
							}
						}
					}
					// call the converter utility  to convert and set the array
					object.setUserMessage((UserMessage[]) org.apache.axis2.databinding.utils.ConverterUtil
							.convertToArray(UserMessage.class, list2));
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()) {
					// Process the array and step past its final element's end.
					boolean loopDone3 = false;
					while (!loopDone3) {
						event = reader.getEventType();
						if (javax.xml.stream.XMLStreamConstants.START_ELEMENT == event) {
							// We need to wrap the reader so that it produces a fake START_DOCUEMENT event
							org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder3 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(
									new org.apache.axis2.util.StreamWrapper(reader), reader.getName());
							list3.add(builder3.getOMElement());
							reader.next();
							if (reader.isEndElement()) {
								// we have two countinuos end elements
								loopDone3 = true;
							}
						} else if (javax.xml.stream.XMLStreamConstants.END_ELEMENT == event) {
							loopDone3 = true;
						} else {
							reader.next();
						}
					}
					object.setExtraElement((org.apache.axiom.om.OMElement[]) org.apache.axis2.databinding.utils.ConverterUtil
							.convertToArray(org.apache.axiom.om.OMElement.class, list3));
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
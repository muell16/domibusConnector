/*
 * 
 */
package org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704;

/**
 * The Class UserMessage.
 */
public class UserMessage implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = UserMessage Namespace URI =
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

	/** The local message info. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo localMessageInfo;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local message info tracker. */
	protected boolean localMessageInfoTracker = false;

	/**
	 * Gets the message info.
	 *
	 * @return the message info
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo getMessageInfo() {
		return localMessageInfo;
	}

	/**
	 * Sets the message info.
	 *
	 * @param param the new message info
	 */
	public void setMessageInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo param) {
		if (param != null) {
			//update the setting tracker
			localMessageInfoTracker = true;
		} else {
			localMessageInfoTracker = false;
		}
		this.localMessageInfo = param;
	}

	/** The local party info. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo localPartyInfo;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local party info tracker. */
	protected boolean localPartyInfoTracker = false;

	/**
	 * Gets the party info.
	 *
	 * @return the party info
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo getPartyInfo() {
		return localPartyInfo;
	}

	/**
	 * Sets the party info.
	 *
	 * @param param the new party info
	 */
	public void setPartyInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo param) {
		if (param != null) {
			//update the setting tracker
			localPartyInfoTracker = true;
		} else {
			localPartyInfoTracker = false;
		}
		this.localPartyInfo = param;
	}

	/** The local collaboration info. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo localCollaborationInfo;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local collaboration info tracker. */
	protected boolean localCollaborationInfoTracker = false;

	/**
	 * Gets the collaboration info.
	 *
	 * @return the collaboration info
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo getCollaborationInfo() {
		return localCollaborationInfo;
	}

	/**
	 * Sets the collaboration info.
	 *
	 * @param param the new collaboration info
	 */
	public void setCollaborationInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo param) {
		if (param != null) {
			//update the setting tracker
			localCollaborationInfoTracker = true;
		} else {
			localCollaborationInfoTracker = false;
		}
		this.localCollaborationInfo = param;
	}

	/** The local message properties. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties localMessageProperties;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local message properties tracker. */
	protected boolean localMessagePropertiesTracker = false;

	/**
	 * Gets the message properties.
	 *
	 * @return the message properties
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties getMessageProperties() {
		return localMessageProperties;
	}

	/**
	 * Sets the message properties.
	 *
	 * @param param the new message properties
	 */
	public void setMessageProperties(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties param) {
		if (param != null) {
			//update the setting tracker
			localMessagePropertiesTracker = true;
		} else {
			localMessagePropertiesTracker = false;
		}
		this.localMessageProperties = param;
	}

	/** The local payload info. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo localPayloadInfo;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local payload info tracker. */
	protected boolean localPayloadInfoTracker = false;

	/**
	 * Gets the payload info.
	 *
	 * @return the payload info
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo getPayloadInfo() {
		return localPayloadInfo;
	}

	/**
	 * Sets the payload info.
	 *
	 * @param param the new payload info
	 */
	public void setPayloadInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo param) {
		if (param != null) {
			//update the setting tracker
			localPayloadInfoTracker = true;
		} else {
			localPayloadInfoTracker = false;
		}
		this.localPayloadInfo = param;
	}

	/** The local mpc. */
	protected org.apache.axis2.databinding.types.URI localMpc;

	/**
	 * Gets the mpc.
	 *
	 * @return the mpc
	 */
	public org.apache.axis2.databinding.types.URI getMpc() {
		return localMpc;
	}

	/**
	 * Sets the mpc.
	 *
	 * @param param the new mpc
	 */
	public void setMpc(org.apache.axis2.databinding.types.URI param) {
		this.localMpc = param;
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
				UserMessage.this.serialize(parentQName, factory, xmlWriter);
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
						+ ":UserMessage", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "UserMessage", xmlWriter);
			}
		}
		if (localMpc != null) {
			writeAttribute("", "mpc", org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMpc),
					xmlWriter);
		}
		if (localMessageInfoTracker) {
			if (localMessageInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageInfo cannot be null!!");
			}
			localMessageInfo.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageInfo"), factory,
					xmlWriter);
		}
		if (localPartyInfoTracker) {
			if (localPartyInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("PartyInfo cannot be null!!");
			}
			localPartyInfo.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyInfo"), factory, xmlWriter);
		}
		if (localCollaborationInfoTracker) {
			if (localCollaborationInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("CollaborationInfo cannot be null!!");
			}
			localCollaborationInfo.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "CollaborationInfo"), factory,
					xmlWriter);
		}
		if (localMessagePropertiesTracker) {
			if (localMessageProperties == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageProperties cannot be null!!");
			}
			localMessageProperties.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageProperties"), factory,
					xmlWriter);
		}
		if (localPayloadInfoTracker) {
			if (localPayloadInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("PayloadInfo cannot be null!!");
			}
			localPayloadInfo.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PayloadInfo"), factory,
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
		if (localMessageInfoTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageInfo"));
			if (localMessageInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageInfo cannot be null!!");
			}
			elementList.add(localMessageInfo);
		}
		if (localPartyInfoTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyInfo"));
			if (localPartyInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("PartyInfo cannot be null!!");
			}
			elementList.add(localPartyInfo);
		}
		if (localCollaborationInfoTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "CollaborationInfo"));
			if (localCollaborationInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("CollaborationInfo cannot be null!!");
			}
			elementList.add(localCollaborationInfo);
		}
		if (localMessagePropertiesTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageProperties"));
			if (localMessageProperties == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageProperties cannot be null!!");
			}
			elementList.add(localMessageProperties);
		}
		if (localPayloadInfoTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PayloadInfo"));
			if (localPayloadInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("PayloadInfo cannot be null!!");
			}
			elementList.add(localPayloadInfo);
		}
		attribList.add(new javax.xml.namespace.QName("", "mpc"));
		attribList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMpc));
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
		 * @return the user message
		 * @throws Exception the exception
		 */
		public static UserMessage parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			UserMessage object = new UserMessage();
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
						if (!"UserMessage".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (UserMessage) backend.ecodex.org.ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				// handle attribute "mpc"
				java.lang.String tempAttribMpc = reader.getAttributeValue(null, "mpc");
				if (tempAttribMpc != null) {
					java.lang.String content = tempAttribMpc;
					object.setMpc(org.apache.axis2.databinding.utils.ConverterUtil.convertToAnyURI(tempAttribMpc));
				} else {
				}
				handledAttributes.add("mpc");
				reader.next();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageInfo")
								.equals(reader.getName())) {
					object.setMessageInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo.Factory
							.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyInfo")
								.equals(reader.getName())) {
					object.setPartyInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo.Factory
							.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "CollaborationInfo")
								.equals(reader.getName())) {
					object.setCollaborationInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo.Factory
							.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageProperties")
								.equals(reader.getName())) {
					object.setMessageProperties(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties.Factory
							.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PayloadInfo")
								.equals(reader.getName())) {
					object.setPayloadInfo(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo.Factory
							.parse(reader));
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

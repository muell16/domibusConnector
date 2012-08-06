/*
 * 
 */
package org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704;

/**
 * The Class From.
 */
public class From implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = From Namespace URI =
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

	/** The local party id. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[] localPartyId;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local party id tracker. */
	protected boolean localPartyIdTracker = false;

	/**
	 * Gets the party id.
	 *
	 * @return the party id
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[] getPartyId() {
		return localPartyId;
	}

	/**
	 * Validate party id.
	 *
	 * @param param the param
	 */
	protected void validatePartyId(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[] param) {
	}

	/**
	 * Sets the party id.
	 *
	 * @param param the new party id
	 */
	public void setPartyId(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[] param) {
		validatePartyId(param);
		if (param != null) {
			//update the setting tracker
			localPartyIdTracker = true;
		} else {
			localPartyIdTracker = false;
		}
		this.localPartyId = param;
	}

	/**
	 * Adds the party id.
	 *
	 * @param param the param
	 */
	public void addPartyId(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId param) {
		if (localPartyId == null) {
			localPartyId = new org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[] {};
		}
		//update the setting tracker
		localPartyIdTracker = true;
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localPartyId);
		list.add(param);
		this.localPartyId = (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[]) list
				.toArray(new org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[list.size()]);
	}

	/** The local role. */
	protected org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.NonEmptyString localRole;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
	 * used to determine whether to include this field in the serialized XML
	 */
	/** The local role tracker. */
	protected boolean localRoleTracker = false;

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.NonEmptyString getRole() {
		return localRole;
	}

	/**
	 * Sets the role.
	 *
	 * @param param the new role
	 */
	public void setRole(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.NonEmptyString param) {
		if (param != null) {
			//update the setting tracker
			localRoleTracker = true;
		} else {
			localRoleTracker = false;
		}
		this.localRole = param;
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
				From.this.serialize(parentQName, factory, xmlWriter);
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
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":From",
						xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "From", xmlWriter);
			}
		}
		if (localPartyIdTracker) {
			if (localPartyId != null) {
				for (int i = 0; i < localPartyId.length; i++) {
					if (localPartyId[i] != null) {
						localPartyId[i].serialize(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyId"), factory,
								xmlWriter);
					} else {
						// we don't have to do any thing since minOccures is zero
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("PartyId cannot be null!!");
			}
		}
		if (localRoleTracker) {
			if (localRole == null) {
				throw new org.apache.axis2.databinding.ADBException("Role cannot be null!!");
			}
			localRole.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Role"), factory, xmlWriter);
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
		if (localPartyIdTracker) {
			if (localPartyId != null) {
				for (int i = 0; i < localPartyId.length; i++) {
					if (localPartyId[i] != null) {
						elementList.add(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyId"));
						elementList.add(localPartyId[i]);
					} else {
						// nothing to do
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("PartyId cannot be null!!");
			}
		}
		if (localRoleTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Role"));
			if (localRole == null) {
				throw new org.apache.axis2.databinding.ADBException("Role cannot be null!!");
			}
			elementList.add(localRole);
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
		 * @return the from
		 * @throws Exception the exception
		 */
		public static From parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			From object = new From();
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
						if (!"From".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (From) backend.ecodex.org.ExtensionMapper.getTypeObject(nsUri, type, reader);
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
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyId")
								.equals(reader.getName())) {
					// Process the array and step past its final element's end.
					list1.add(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId.Factory.parse(reader));
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
									"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PartyId")
									.equals(reader.getName())) {
								list1.add(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId.Factory
										.parse(reader));
							} else {
								loopDone1 = true;
							}
						}
					}
					// call the converter utility  to convert and set the array
					object.setPartyId((org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId[]) org.apache.axis2.databinding.utils.ConverterUtil
							.convertToArray(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId.class,
									list1));
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Role").equals(reader
								.getName())) {
					object.setRole(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.NonEmptyString.Factory
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

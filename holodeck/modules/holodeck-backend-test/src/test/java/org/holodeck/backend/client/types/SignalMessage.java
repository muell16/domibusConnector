package org.holodeck.backend.client.types;

import javax.xml.stream.XMLStreamException;

import org.apache.axis2.databinding.ADBException;

/**
 * The Class SignalMessage.
 */
public class SignalMessage implements org.apache.axis2.databinding.ADBBean {
	/*
	 * This type was generated from the piece of schema that had name = SignalMessage Namespace URI =
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
	protected MessageInfo localMessageInfo;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local message info tracker. */
	protected boolean localMessageInfoTracker = false;

	/**
	 * Gets the message info.
	 *
	 * @return the message info
	 */
	public MessageInfo getMessageInfo() {
		return localMessageInfo;
	}

	/**
	 * Sets the message info.
	 *
	 * @param param the new message info
	 */
	public void setMessageInfo(MessageInfo param) {
		if (param != null) {
			//update the setting tracker
			localMessageInfoTracker = true;
		} else {
			localMessageInfoTracker = false;
		}
		this.localMessageInfo = param;
	}

	/** The local pull request. */
	protected PullRequest localPullRequest;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local pull request tracker. */
	protected boolean localPullRequestTracker = false;

	/**
	 * Gets the pull request.
	 *
	 * @return the pull request
	 */
	public PullRequest getPullRequest() {
		return localPullRequest;
	}

	/**
	 * Sets the pull request.
	 *
	 * @param param the new pull request
	 */
	public void setPullRequest(PullRequest param) {
		if (param != null) {
			//update the setting tracker
			localPullRequestTracker = true;
		} else {
			localPullRequestTracker = false;
		}
		this.localPullRequest = param;
	}

	/** The local receipt. */
	protected Receipt localReceipt;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local receipt tracker. */
	protected boolean localReceiptTracker = false;

	/**
	 * Gets the receipt.
	 *
	 * @return the receipt
	 */
	public Receipt getReceipt() {
		return localReceipt;
	}

	/**
	 * Sets the receipt.
	 *
	 * @param param the new receipt
	 */
	public void setReceipt(Receipt param) {
		if (param != null) {
			//update the setting tracker
			localReceiptTracker = true;
		} else {
			localReceiptTracker = false;
		}
		this.localReceipt = param;
	}

	/** The local error. */
	protected Error[] localError;
	/*
	 * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will
	 * be used to determine whether to include this field in the serialized XML
	 */
	/** The local error tracker. */
	protected boolean localErrorTracker = false;

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public Error[] getError() {
		return localError;
	}

	/**
	 * Validate error.
	 *
	 * @param param the param
	 */
	protected void validateError(Error[] param) {
	}

	/**
	 * Sets the error.
	 *
	 * @param param the new error
	 */
	public void setError(Error[] param) {
		validateError(param);
		if (param != null) {
			//update the setting tracker
			localErrorTracker = true;
		} else {
			localErrorTracker = false;
		}
		this.localError = param;
	}

	/**
	 * Adds the error.
	 *
	 * @param param the param
	 */
	public void addError(Error param) {
		if (localError == null) {
			localError = new Error[] {};
		}
		//update the setting tracker
		localErrorTracker = true;
		java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localError);
		list.add(param);
		this.localError = (Error[]) list.toArray(new Error[list.size()]);
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
				SignalMessage.this.serialize(parentQName, factory, xmlWriter);
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
						+ ":SignalMessage", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SignalMessage",
						xmlWriter);
			}
		}
		if (localMessageInfoTracker) {
			if (localMessageInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageInfo cannot be null!!");
			}
			localMessageInfo.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageInfo"), factory,
					xmlWriter);
		}
		if (localPullRequestTracker) {
			if (localPullRequest == null) {
				throw new org.apache.axis2.databinding.ADBException("PullRequest cannot be null!!");
			}
			localPullRequest.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PullRequest"), factory,
					xmlWriter);
		}
		if (localReceiptTracker) {
			if (localReceipt == null) {
				throw new org.apache.axis2.databinding.ADBException("Receipt cannot be null!!");
			}
			localReceipt.serialize(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Receipt"), factory,
					xmlWriter);
		}
		if (localErrorTracker) {
			if (localError != null) {
				for (int i = 0; i < localError.length; i++) {
					if (localError[i] != null) {
						localError[i].serialize(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Error"),
								factory, xmlWriter);
					} else {
						// we don't have to do any thing since minOccures is zero
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
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
		if (localMessageInfoTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageInfo"));
			if (localMessageInfo == null) {
				throw new org.apache.axis2.databinding.ADBException("MessageInfo cannot be null!!");
			}
			elementList.add(localMessageInfo);
		}
		if (localPullRequestTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PullRequest"));
			if (localPullRequest == null) {
				throw new org.apache.axis2.databinding.ADBException("PullRequest cannot be null!!");
			}
			elementList.add(localPullRequest);
		}
		if (localReceiptTracker) {
			elementList.add(new javax.xml.namespace.QName(
					"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Receipt"));
			if (localReceipt == null) {
				throw new org.apache.axis2.databinding.ADBException("Receipt cannot be null!!");
			}
			elementList.add(localReceipt);
		}
		if (localErrorTracker) {
			if (localError != null) {
				for (int i = 0; i < localError.length; i++) {
					if (localError[i] != null) {
						elementList.add(new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Error"));
						elementList.add(localError[i]);
					} else {
						// nothing to do
					}
				}
			} else {
				throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
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
		 * @return the signal message
		 * @throws Exception the exception
		 */
		public static SignalMessage parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
			SignalMessage object = new SignalMessage();
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
						if (!"SignalMessage".equals(type)) {
							//find namespace for the prefix
							java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
							return (SignalMessage) ExtensionMapper.getTypeObject(nsUri, type, reader);
						}
					}
				}
				// Note all attributes that were handled. Used to differ normal attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();
				reader.next();
				java.util.ArrayList list4 = new java.util.ArrayList();
				java.util.ArrayList list5 = new java.util.ArrayList();
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "MessageInfo")
								.equals(reader.getName())) {
					object.setMessageInfo(MessageInfo.Factory.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "PullRequest")
								.equals(reader.getName())) {
					object.setPullRequest(PullRequest.Factory.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Receipt")
								.equals(reader.getName())) {
					object.setReceipt(Receipt.Factory.parse(reader));
					reader.next();
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()
						&& new javax.xml.namespace.QName(
								"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Error")
								.equals(reader.getName())) {
					// Process the array and step past its final element's end.
					list4.add(Error.Factory.parse(reader));
					//loop until we find a start element that is not part of this array
					boolean loopDone4 = false;
					while (!loopDone4) {
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
							loopDone4 = true;
						} else {
							if (new javax.xml.namespace.QName(
									"http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", "Error")
									.equals(reader.getName())) {
								list4.add(Error.Factory.parse(reader));
							} else {
								loopDone4 = true;
							}
						}
					}
					// call the converter utility  to convert and set the array
					object.setError((Error[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
							Error.class, list4));
				} // End of if for expected property start element
				else {
				}
				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();
				if (reader.isStartElement()) {
					// Process the array and step past its final element's end.
					boolean loopDone5 = false;
					while (!loopDone5) {
						event = reader.getEventType();
						if (javax.xml.stream.XMLStreamConstants.START_ELEMENT == event) {
							// We need to wrap the reader so that it produces a fake START_DOCUEMENT event
							org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder5 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(
									new org.apache.axis2.util.StreamWrapper(reader), reader.getName());
							list5.add(builder5.getOMElement());
							reader.next();
							if (reader.isEndElement()) {
								// we have two countinuos end elements
								loopDone5 = true;
							}
						} else if (javax.xml.stream.XMLStreamConstants.END_ELEMENT == event) {
							loopDone5 = true;
						} else {
							reader.next();
						}
					}
					object.setExtraElement((org.apache.axiom.om.OMElement[]) org.apache.axis2.databinding.utils.ConverterUtil
							.convertToArray(org.apache.axiom.om.OMElement.class, list5));
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
package org.holodeck.backend.client.types;


/**
 * The Class ExtensionMapper.
 */
public class ExtensionMapper {

	/**
	 * Gets the type object.
	 *
	 * @param namespaceURI the namespace uri
	 * @param typeName the type name
	 * @param reader the reader
	 * @return the type object
	 * @throws Exception the exception
	 */
	public static java.lang.Object getTypeObject(java.lang.String namespaceURI, java.lang.String typeName,
			javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
	{
		if ("http://org.ecodex.backend".equals(namespaceURI) && "code_type1".equals(typeName)) {
			return Code_type1.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PullRequest".equals(typeName)) {
			return PullRequest.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Error".equals(typeName)) {
			return Error.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "encodingStyle".equals(typeName)) {
			return EncodingStyle.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Body".equals(typeName)) {
			return Body.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Description".equals(typeName)) {
			return Description.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Fault".equals(typeName)) {
			return Fault3.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Messaging".equals(typeName)) {
			return Messaging.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "MessageInfo".equals(typeName)) {
			return MessageInfo.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Schema".equals(typeName)) {
			return Schema.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "To".equals(typeName)) {
			return To.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Envelope".equals(typeName)) {
			return Envelope.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "CollaborationInfo".equals(typeName)) {
			return CollaborationInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "subcode".equals(typeName)) {
			return Subcode.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "SignalMessage".equals(typeName)) {
			return SignalMessage.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Envelope".equals(typeName)) {
			return Envelope0.Factory.parse(reader);
		}
		if ("http://org.ecodex.backend".equals(namespaceURI) && "code_type1".equals(typeName)) {
			return Code_type1.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "faultcodeEnum".equals(typeName)) {
			return FaultcodeEnum.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Body".equals(typeName)) {
			return Body2.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Fault".equals(typeName)) {
			return Fault.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "NotUnderstoodType".equals(typeName)) {
			return NotUnderstoodType.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Property".equals(typeName)) {
			return Property.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartyInfo".equals(typeName)) {
			return PartyInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/XML/1998/namespace".equals(namespaceURI) && "lang_type0".equals(typeName)) {
			return Lang_type0.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartProperties".equals(typeName)) {
			return PartProperties.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "AgreementRef".equals(typeName)) {
			return AgreementRef.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "UpgradeType".equals(typeName)) {
			return UpgradeType.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Header".equals(typeName)) {
			return Header1.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "MessageProperties".equals(typeName)) {
			return MessageProperties.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "detail".equals(typeName)) {
			return Detail.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "non-empty-string".equals(typeName)) {
			return NonEmptyString.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Service".equals(typeName)) {
			return Service.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI)
				&& "mustUnderstand_type0".equals(typeName)) {
			return MustUnderstand_type0.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartyId".equals(typeName)) {
			return PartyId.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartInfo".equals(typeName)) {
			return PartInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "faultreason".equals(typeName)) {
			return Faultreason.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "reasontext".equals(typeName)) {
			return Reasontext.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "faultcode".equals(typeName)) {
			return Faultcode.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "detail".equals(typeName)) {
			return DetailE.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "From".equals(typeName)) {
			return From.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Receipt".equals(typeName)) {
			return Receipt.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "UserMessage".equals(typeName)) {
			return UserMessage.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PayloadInfo".equals(typeName)) {
			return PayloadInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "SupportedEnvType".equals(typeName)) {
			return SupportedEnvType.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Header".equals(typeName)) {
			return Header.Factory.parse(reader);
		}
		throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
	}
}
/*
 * 
 */
package backend.ecodex.org;

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
			return backend.ecodex.org.Code.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PullRequest".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PullRequest.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Error".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Error.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "encodingStyle".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.EncodingStyle.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Body".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.Body.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Description".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Description.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Fault".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Fault3.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Messaging".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "MessageInfo".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Schema".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Schema.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "To".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Envelope".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.Envelope.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "CollaborationInfo".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "subcode".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Subcode.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "SignalMessage".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.SignalMessage.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Envelope".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Envelope0.Factory.parse(reader);
		}
		if ("http://org.ecodex.backend".equals(namespaceURI) && "code_type1".equals(typeName)) {
			return backend.ecodex.org.Code.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "faultcodeEnum".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.FaultcodeEnum.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Body".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Body2.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Fault".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.Fault.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "NotUnderstoodType".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.NotUnderstoodType.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Property".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartyInfo".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/XML/1998/namespace".equals(namespaceURI) && "lang_type0".equals(typeName)) {
			return org.w3.www.xml._1998.namespace.Lang_type0.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartProperties".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartProperties.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "AgreementRef".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.AgreementRef.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "UpgradeType".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.UpgradeType.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "Header".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Header1.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "MessageProperties".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "detail".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.Detail.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "non-empty-string".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.NonEmptyString.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Service".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "mustUnderstand_type0".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.MustUnderstand_type0.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartyId".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PartInfo".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "faultreason".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Faultreason.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "reasontext".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Reasontext.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "faultcode".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.Faultcode.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "detail".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.DetailE.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "From".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "Receipt".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Receipt.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "UserMessage".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage.Factory.parse(reader);
		}
		if ("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/".equals(namespaceURI)
				&& "PayloadInfo".equals(typeName)) {
			return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo.Factory.parse(reader);
		}
		if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI) && "SupportedEnvType".equals(typeName)) {
			return org.w3.www._2003._05.soap_envelope.SupportedEnvType.Factory.parse(reader);
		}
		if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI) && "Header".equals(typeName)) {
			return org.xmlsoap.schemas.soap.envelope.Header.Factory.parse(reader);
		}
		throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
	}
}

/*
 * 
 */
package org.holodeck.backend.module;

import org.holodeck.backend.module.exception.DownloadMessageFault;
import org.holodeck.backend.module.exception.ListPendingMessagesFault;
import org.holodeck.backend.module.exception.SendMessageFault;
import org.holodeck.backend.module.exception.SendMessageWithReferenceFault;

/**
 * The Class BackendServiceMessageReceiverInOut.
 */
public class BackendServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {
	
	/* (non-Javadoc)
	 * @see org.apache.axis2.receivers.AbstractInOutMessageReceiver#invokeBusinessLogic(org.apache.axis2.context.MessageContext, org.apache.axis2.context.MessageContext)
	 */
	public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext,
			org.apache.axis2.context.MessageContext newMsgContext) throws org.apache.axis2.AxisFault
	{
		try {
			// get the implementation class for the Web Service
			Object obj = getTheImplementationObject(msgContext);
			BackendServiceSkeleton skel = (BackendServiceSkeleton) obj;
			//Out Envelop
			org.apache.axiom.soap.SOAPEnvelope envelope = null;
			//Find the axisOperation that has been set by the Dispatch phase.
			org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
			if (op == null) {
				throw new org.apache.axis2.AxisFault(
						"Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
			}
			java.lang.String methodName;
			if ((op.getName() != null)
					&& ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName()
							.getLocalPart())) != null)) {
				if ("sendMessageWithReference".equals(methodName)) {
					backend.ecodex.org.SendRequestURL wrappedParam = (backend.ecodex.org.SendRequestURL) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							backend.ecodex.org.SendRequestURL.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));
					org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging = (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE) fromOM(
							msgContext.getEnvelope().getHeader().getFirstElement(),
							org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));
					skel.sendMessageWithReference(messaging, wrappedParam);
					envelope = toEnvelope(getSOAPFactory(msgContext));
				} else if ("sendMessage".equals(methodName)) {
					backend.ecodex.org.SendRequest wrappedParam = (backend.ecodex.org.SendRequest) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							backend.ecodex.org.SendRequest.class, getEnvelopeNamespaces(msgContext.getEnvelope()));
					org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging = (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE) fromOM(
							msgContext.getEnvelope().getHeader().getFirstElement(),
							org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));
					skel.sendMessage(messaging, wrappedParam);
					envelope = toEnvelope(getSOAPFactory(msgContext));
				} else if ("listPendingMessages".equals(methodName)) {
					backend.ecodex.org.ListPendingMessagesResponse listPendingMessagesResponse10 = null;
					backend.ecodex.org.ListPendingMessagesRequest wrappedParam = (backend.ecodex.org.ListPendingMessagesRequest) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							backend.ecodex.org.ListPendingMessagesRequest.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));
					listPendingMessagesResponse10 = skel.listPendingMessages(wrappedParam);
					envelope = toEnvelope(getSOAPFactory(msgContext), listPendingMessagesResponse10, false);
				} else if ("downloadMessage".equals(methodName)) {
					org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messagingE12 = null;
					backend.ecodex.org.DownloadMessageResponse downloadMessageResponse = new backend.ecodex.org.DownloadMessageResponse();
					backend.ecodex.org.DownloadMessageRequest wrappedParam = (backend.ecodex.org.DownloadMessageRequest) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							backend.ecodex.org.DownloadMessageRequest.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));
					messagingE12 = skel.downloadMessage(downloadMessageResponse, wrappedParam);
					envelope = toEnvelope(getSOAPFactory(msgContext), messagingE12, downloadMessageResponse,
							false);
				} else {
					throw new java.lang.RuntimeException("method not found");
				}
				newMsgContext.setEnvelope(envelope);
			}
		} catch (SendMessageFault e) {
			msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME, "FaultDetail");
			org.apache.axis2.AxisFault f = createAxisFault(e);
			if (e.getFaultMessage() != null) {
				f.setDetail(toOM(e.getFaultMessage(), false));
			}
			throw f;
		} catch (ListPendingMessagesFault e) {
			msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME, "FaultDetail");
			org.apache.axis2.AxisFault f = createAxisFault(e);
			if (e.getFaultMessage() != null) {
				f.setDetail(toOM(e.getFaultMessage(), false));
			}
			throw f;
		} catch (SendMessageWithReferenceFault e) {
			msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME, "FaultDetail");
			org.apache.axis2.AxisFault f = createAxisFault(e);
			if (e.getFaultMessage() != null) {
				f.setDetail(toOM(e.getFaultMessage(), false));
			}
			throw f;
		} catch (DownloadMessageFault e) {
			msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME, "FaultDetail");
			org.apache.axis2.AxisFault f = createAxisFault(e);
			if (e.getFaultMessage() != null) {
				f.setDetail(toOM(e.getFaultMessage(), false));
			}
			throw f;
		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	//
	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.SendRequestURL param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(backend.ecodex.org.SendRequestURL.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.FaultDetail param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(backend.ecodex.org.FaultDetail.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(
			org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.SendRequest param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(backend.ecodex.org.SendRequest.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

    /**
     * To om.
     *
     * @param param the param
     * @param optimizeContent the optimize content
     * @return the org.apache.axiom.om. om element
     * @throws AxisFault the axis fault
     */
    private  org.apache.axiom.om.OMElement  toOM(backend.ecodex.org.ListPendingMessagesRequest param, boolean optimizeContent)
    throws org.apache.axis2.AxisFault {


                try{
                     return param.getOMElement(backend.ecodex.org.ListPendingMessagesRequest.MY_QNAME,
                                  org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                } catch(org.apache.axis2.databinding.ADBException e){
                    throw org.apache.axis2.AxisFault.makeFault(e);
                }


    }

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.ListPendingMessagesResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(backend.ecodex.org.ListPendingMessagesResponse.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.DownloadMessageRequest param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(backend.ecodex.org.DownloadMessageRequest.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.DownloadMessageResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(backend.ecodex.org.DownloadMessageResponse.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * To envelope.
	 *
	 * @param factory the factory
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.soap. soap envelope
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			backend.ecodex.org.ListPendingMessagesResponse param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			if (param != null)
				emptyEnvelope.getBody().addChild(
						param.getOMElement(backend.ecodex.org.ListPendingMessagesResponse.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * Wraplist pending messages.
	 *
	 * @return the backend.ecodex.org. list pending messages response
	 */
	private backend.ecodex.org.ListPendingMessagesResponse wraplistPendingMessages() {
		backend.ecodex.org.ListPendingMessagesResponse wrappedElement = new backend.ecodex.org.ListPendingMessagesResponse();
		return wrappedElement;
	}

	/**
	 * To envelope.
	 *
	 * @param factory the factory
	 * @param messaging the messaging
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.soap. soap envelope
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging,
			backend.ecodex.org.DownloadMessageResponse param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			if (messaging != null) {
				emptyEnvelope.getHeader().addChild(
						messaging.getOMElement(
								org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.MY_QNAME, factory));
			}
			if (param != null)
				emptyEnvelope.getBody().addChild(
						param.getOMElement(backend.ecodex.org.DownloadMessageResponse.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/**
	 * Wrapdownload message.
	 *
	 * @return the backend.ecodex.org. download message response
	 */
	private backend.ecodex.org.DownloadMessageResponse wrapdownloadMessage() {
		backend.ecodex.org.DownloadMessageResponse wrappedElement = new backend.ecodex.org.DownloadMessageResponse();
		return wrappedElement;
	}

	/**
	 * To envelope.
	 *
	 * @param factory the factory
	 * @return the org.apache.axiom.soap. soap envelope
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
		return factory.getDefaultEnvelope();
	}

	/**
	 * From om.
	 *
	 * @param param the param
	 * @param type the type
	 * @param extraNamespaces the extra namespaces
	 * @return the java.lang. object
	 * @throws AxisFault the axis fault
	 */
	private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type,
			java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault
	{
		try {
			if (backend.ecodex.org.SendRequestURL.class.equals(type)) {
				return backend.ecodex.org.SendRequestURL.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.FaultDetail.class.equals(type)) {
				return backend.ecodex.org.FaultDetail.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			if (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.class.equals(type)) {
				return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.SendRequest.class.equals(type)) {
				return backend.ecodex.org.SendRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.FaultDetail.class.equals(type)) {
				return backend.ecodex.org.FaultDetail.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			if (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.class.equals(type)) {
				return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.ListPendingMessagesRequest.class.equals(type)) {
				return backend.ecodex.org.ListPendingMessagesRequest.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.ListPendingMessagesResponse.class.equals(type)) {
				return backend.ecodex.org.ListPendingMessagesResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.FaultDetail.class.equals(type)) {
				return backend.ecodex.org.FaultDetail.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.DownloadMessageRequest.class.equals(type)) {
				return backend.ecodex.org.DownloadMessageRequest.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.DownloadMessageResponse.class.equals(type)) {
				return backend.ecodex.org.DownloadMessageResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (backend.ecodex.org.FaultDetail.class.equals(type)) {
				return backend.ecodex.org.FaultDetail.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			if (org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.class.equals(type)) {
				return org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
		return null;
	}

	/**
	 * Gets the envelope namespaces.
	 *
	 * @param env the env
	 * @return the envelope namespaces
	 */
	private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
		java.util.Map returnMap = new java.util.HashMap();
		java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}

	/**
	 * Creates the axis fault.
	 *
	 * @param e the e
	 * @return the org.apache.axis2. axis fault
	 */
	private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
		org.apache.axis2.AxisFault f;
		Throwable cause = e.getCause();
		if (cause != null) {
			f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
		} else {
			f = new org.apache.axis2.AxisFault(e.getMessage());
		}
		return f;
	}
}//end of class

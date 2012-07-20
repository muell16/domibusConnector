/**
 * BackendServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package eu.ecodex.connector.generated;

/*
 *  BackendServiceStub java implementation
 */

public class BackendServiceStub extends org.apache.axis2.client.Stub implements BackendService {
	protected org.apache.axis2.description.AxisOperation[] _operations;

	// hashmaps to keep the fault mapping
	private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
	private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
	private java.util.HashMap faultMessageMap = new java.util.HashMap();

	private static int counter = 0;

	private static synchronized java.lang.String getUniqueSuffix() {
		// reset the counter if it is greater than 99999
		if (counter > 99999) {
			counter = 0;
		}
		counter = counter + 1;
		return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
	}

	private void populateAxisService() throws org.apache.axis2.AxisFault {

		// creating the Service with a unique name
		_service = new org.apache.axis2.description.AxisService("BackendService" + getUniqueSuffix());
		addAnonymousOperations();

		// creating the operations
		org.apache.axis2.description.AxisOperation __operation;

		_operations = new org.apache.axis2.description.AxisOperation[4];

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessageWithReference"));
		_service.addOperation(__operation);

		_operations[0] = __operation;

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage"));
		_service.addOperation(__operation);

		_operations[1] = __operation;

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend", "listPendingMessages"));
		_service.addOperation(__operation);

		_operations[2] = __operation;

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend", "downloadMessage"));
		_service.addOperation(__operation);

		_operations[3] = __operation;

	}

	// populates the faults
	private void populateFaults() {

		faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "sendMessageWithReference"),
		        "eu.ecodex.connector.generated.SendMessageWithReferenceFault");
		faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "sendMessageWithReference"),
		        "eu.ecodex.connector.generated.SendMessageWithReferenceFault");
		faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "sendMessageWithReference"),
		        "backend.ecodex.org.FaultDetail");

		faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "sendMessage"),
		        "eu.ecodex.connector.generated.SendMessageFault");
		faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "sendMessage"),
		        "eu.ecodex.connector.generated.SendMessageFault");
		faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "sendMessage"), "backend.ecodex.org.FaultDetail");

		faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "listPendingMessages"),
		        "eu.ecodex.connector.generated.ListPendingMessagesFault");
		faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "listPendingMessages"),
		        "eu.ecodex.connector.generated.ListPendingMessagesFault");
		faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "listPendingMessages"), "backend.ecodex.org.FaultDetail");

		faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "downloadMessage"),
		        "eu.ecodex.connector.generated.DownloadMessageFault");
		faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "downloadMessage"),
		        "eu.ecodex.connector.generated.DownloadMessageFault");
		faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName(
		        "http://org.ecodex.backend", "FaultDetail"), "downloadMessage"), "backend.ecodex.org.FaultDetail");

	}

	/**
	 * Constructor that takes in a configContext
	 */

	public BackendServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
	        java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
		this(configurationContext, targetEndpoint, false);
	}

	/**
	 * Constructor that takes in a configContext and useseperate listner
	 */
	public BackendServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
	        java.lang.String targetEndpoint, boolean useSeparateListener) throws org.apache.axis2.AxisFault {
		// To populate AxisService
		populateAxisService();
		populateFaults();

		_serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);

		_serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
		_serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

	}

	/**
	 * Default Constructor
	 */
	public BackendServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext)
	        throws org.apache.axis2.AxisFault {

		this(configurationContext, "http://www.ecodex.org/eCODEX");

	}

	/**
	 * Default Constructor
	 */
	public BackendServiceStub() throws org.apache.axis2.AxisFault {

		this("http://www.ecodex.org/eCODEX");

	}

	/**
	 * Constructor taking the target endpoint
	 */
	public BackendServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
		this(null, targetEndpoint);
	}

	/**
	 * Auto generated method signature
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#sendMessageWithReference
	 * @param sendRequestURL13
	 * 
	 * @param messaging14
	 * 
	 * @throws eu.ecodex.connector.generated.SendMessageWithReferenceFault
	 *             :
	 */

	public void sendMessageWithReference(

	backend.ecodex.org.SendRequestURL sendRequestURL13,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging14)

	throws java.rmi.RemoteException

	, eu.ecodex.connector.generated.SendMessageWithReferenceFault {
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0]
			        .getName());
			_operationClient.getOptions().setAction("\"\"");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

			addPropertyToOperationClient(_operationClient,
			        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequestURL13,
			        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend",
			                "sendMessageWithReference")), new javax.xml.namespace.QName("http://org.ecodex.backend",
			                "sendMessageWithReference"));

			env.build();

			// add the children only if the parameter is not null
			if (messaging14 != null) {

				org.apache.axiom.om.OMElement omElementmessaging14 = toOM(messaging14,
				        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend",
				                "sendMessageWithReference")));
				addHeader(omElementmessaging14, env);

			}

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			return;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
				        "sendMessageWithReference"))) {
					// make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
						                "sendMessageWithReference"));
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
						java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
						// message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
						                "sendMessageWithReference"));
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
						        new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						if (ex instanceof eu.ecodex.connector.generated.SendMessageWithReferenceFault) {
							throw (eu.ecodex.connector.generated.SendMessageWithReferenceFault) ex;
						}

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender().cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#startsendMessageWithReference
	 * @param sendRequestURL13
	 * 
	 * @param messaging14
	 */
	public void startsendMessageWithReference(

	backend.ecodex.org.SendRequestURL sendRequestURL13,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging14,

	        final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException {

		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
		        .createClient(_operations[0].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
		        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

		// Style is Doc.

		env = toEnvelope(
		        getFactory(_operationClient.getOptions().getSoapVersionURI()),
		        sendRequestURL13,
		        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessageWithReference")),
		        new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessageWithReference"));

		// add the soap_headers only if they are not null
		if (messaging14 != null) {

			org.apache.axiom.om.OMElement omElementmessaging14 = toOM(messaging14,
			        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend",
			                "sendMessageWithReference")));
			addHeader(omElementmessaging14, env);

		}

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		// Nothing to pass as the callback!!!

		org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
		if (_operations[0].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
			_operations[0].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * Auto generated method signature
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#sendMessage
	 * @param sendRequest16
	 * 
	 * @param messaging17
	 * 
	 * @throws eu.ecodex.connector.generated.SendMessageFault
	 *             :
	 */

	public void sendMessage(

	backend.ecodex.org.SendRequest sendRequest16,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging17)

	throws java.rmi.RemoteException

	, eu.ecodex.connector.generated.SendMessageFault {
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1]
			        .getName());
			_operationClient.getOptions().setAction("\"\"");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

			addPropertyToOperationClient(_operationClient,
			        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequest16,
			        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage")),
			        new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage"));

			env.build();

			// add the children only if the parameter is not null
			if (messaging17 != null) {

				org.apache.axiom.om.OMElement omElementmessaging17 = toOM(messaging17,
				        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage")));
				addHeader(omElementmessaging17, env);

			}

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			return;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
				        "sendMessage"))) {
					// make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "sendMessage"));
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
						java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
						// message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "sendMessage"));
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
						        new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						if (ex instanceof eu.ecodex.connector.generated.SendMessageFault) {
							throw (eu.ecodex.connector.generated.SendMessageFault) ex;
						}

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender().cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#startsendMessage
	 * @param sendRequest16
	 * 
	 * @param messaging17
	 */
	public void startsendMessage(

	backend.ecodex.org.SendRequest sendRequest16,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging17,

	        final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException {

		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
		        .createClient(_operations[1].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
		        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequest16,
		        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage")),
		        new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage"));

		// add the soap_headers only if they are not null
		if (messaging17 != null) {

			org.apache.axiom.om.OMElement omElementmessaging17 = toOM(messaging17,
			        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "sendMessage")));
			addHeader(omElementmessaging17, env);

		}

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		// Nothing to pass as the callback!!!

		org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
		if (_operations[1].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
			_operations[1].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * Auto generated method signature
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#listPendingMessages
	 * @param listPendingMessagesRequest19
	 * 
	 * @throws eu.ecodex.connector.generated.ListPendingMessagesFault
	 *             :
	 */

	public backend.ecodex.org.ListPendingMessagesResponse listPendingMessages(

	backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest19)

	throws java.rmi.RemoteException

	, eu.ecodex.connector.generated.ListPendingMessagesFault {
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2]
			        .getName());
			_operationClient.getOptions().setAction("\"\"");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

			addPropertyToOperationClient(_operationClient,
			        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
			        listPendingMessagesRequest19, optimizeContent(new javax.xml.namespace.QName(
			                "http://org.ecodex.backend", "listPendingMessages")), new javax.xml.namespace.QName(
			                "http://org.ecodex.backend", "listPendingMessages"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
			        .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

			java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
			        backend.ecodex.org.ListPendingMessagesResponse.class, getEnvelopeNamespaces(_returnEnv));

			return (backend.ecodex.org.ListPendingMessagesResponse) object;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
				        "listPendingMessages"))) {
					// make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "listPendingMessages"));
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
						java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
						// message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "listPendingMessages"));
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
						        new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						if (ex instanceof eu.ecodex.connector.generated.ListPendingMessagesFault) {
							throw (eu.ecodex.connector.generated.ListPendingMessagesFault) ex;
						}

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender().cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#startlistPendingMessages
	 * @param listPendingMessagesRequest19
	 */
	public void startlistPendingMessages(

	backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest19,

	final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException {

		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
		        .createClient(_operations[2].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
		        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), listPendingMessagesRequest19,
		        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "listPendingMessages")),
		        new javax.xml.namespace.QName("http://org.ecodex.backend", "listPendingMessages"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
			public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
				try {
					org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

					java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
					        backend.ecodex.org.ListPendingMessagesResponse.class, getEnvelopeNamespaces(resultEnv));
					callback.receiveResultlistPendingMessages((backend.ecodex.org.ListPendingMessagesResponse) object);

				} catch (org.apache.axis2.AxisFault e) {
					callback.receiveErrorlistPendingMessages(e);
				}
			}

			public void onError(java.lang.Exception error) {
				if (error instanceof org.apache.axis2.AxisFault) {
					org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
						        .getQName(), "listPendingMessages"))) {
							// make the fault by reflection
							try {
								java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
								                "listPendingMessages"));
								java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
								java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
								java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
								// message class
								java.lang.String messageClassName = (java.lang.String) faultMessageMap
								        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
								                "listPendingMessages"));
								java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
								java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
								java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
								        new java.lang.Class[] { messageClass });
								m.invoke(ex, new java.lang.Object[] { messageObject });

								if (ex instanceof eu.ecodex.connector.generated.ListPendingMessagesFault) {
									callback.receiveErrorlistPendingMessages((eu.ecodex.connector.generated.ListPendingMessagesFault) ex);
									return;
								}

								callback.receiveErrorlistPendingMessages(new java.rmi.RemoteException(ex.getMessage(),
								        ex));
							} catch (java.lang.ClassCastException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.ClassNotFoundException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.NoSuchMethodException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.reflect.InvocationTargetException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.IllegalAccessException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.InstantiationException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (org.apache.axis2.AxisFault e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							}
						} else {
							callback.receiveErrorlistPendingMessages(f);
						}
					} else {
						callback.receiveErrorlistPendingMessages(f);
					}
				} else {
					callback.receiveErrorlistPendingMessages(error);
				}
			}

			public void onFault(org.apache.axis2.context.MessageContext faultContext) {
				org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
				        .getInboundFaultFromMessageContext(faultContext);
				onError(fault);
			}

			public void onComplete() {
				try {
					_messageContext.getTransportOut().getSender().cleanup(_messageContext);
				} catch (org.apache.axis2.AxisFault axisFault) {
					callback.receiveErrorlistPendingMessages(axisFault);
				}
			}
		});

		org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
		if (_operations[2].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
			_operations[2].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * Auto generated method signature
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#downloadMessage
	 * @param downloadMessageRequest21
	 * 
	 * @throws eu.ecodex.connector.generated.DownloadMessageFault
	 *             :
	 */

	public backend.ecodex.org.DownloadMessageResponse downloadMessage(

	backend.ecodex.org.DownloadMessageRequest downloadMessageRequest21)

	throws java.rmi.RemoteException

	, eu.ecodex.connector.generated.DownloadMessageFault {
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3]
			        .getName());
			_operationClient.getOptions().setAction("\"\"");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

			addPropertyToOperationClient(_operationClient,
			        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), downloadMessageRequest21,
			        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "downloadMessage")),
			        new javax.xml.namespace.QName("http://org.ecodex.backend", "downloadMessage"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
			        .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

			java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
			        backend.ecodex.org.DownloadMessageResponse.class, getEnvelopeNamespaces(_returnEnv));

			return (backend.ecodex.org.DownloadMessageResponse) object;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
				        "downloadMessage"))) {
					// make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "downloadMessage"));
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
						java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
						// message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap
						        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "downloadMessage"));
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
						        new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						if (ex instanceof eu.ecodex.connector.generated.DownloadMessageFault) {
							throw (eu.ecodex.connector.generated.DownloadMessageFault) ex;
						}

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender().cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see eu.ecodex.connector.generated.BackendService#startdownloadMessage
	 * @param downloadMessageRequest21
	 */
	public void startdownloadMessage(

	backend.ecodex.org.DownloadMessageRequest downloadMessageRequest21,

	final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException {

		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
		        .createClient(_operations[3].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
		        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), downloadMessageRequest21,
		        optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend", "downloadMessage")),
		        new javax.xml.namespace.QName("http://org.ecodex.backend", "downloadMessage"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
			public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
				try {
					org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

					java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
					        backend.ecodex.org.DownloadMessageResponse.class, getEnvelopeNamespaces(resultEnv));
					callback.receiveResultdownloadMessage((backend.ecodex.org.DownloadMessageResponse) object);

				} catch (org.apache.axis2.AxisFault e) {
					callback.receiveErrordownloadMessage(e);
				}
			}

			public void onError(java.lang.Exception error) {
				if (error instanceof org.apache.axis2.AxisFault) {
					org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
						        .getQName(), "downloadMessage"))) {
							// make the fault by reflection
							try {
								java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
								                "downloadMessage"));
								java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
								java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
								java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
								// message class
								java.lang.String messageClassName = (java.lang.String) faultMessageMap
								        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
								                "downloadMessage"));
								java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
								java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
								java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
								        new java.lang.Class[] { messageClass });
								m.invoke(ex, new java.lang.Object[] { messageObject });

								if (ex instanceof eu.ecodex.connector.generated.DownloadMessageFault) {
									callback.receiveErrordownloadMessage((eu.ecodex.connector.generated.DownloadMessageFault) ex);
									return;
								}

								callback.receiveErrordownloadMessage(new java.rmi.RemoteException(ex.getMessage(), ex));
							} catch (java.lang.ClassCastException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.ClassNotFoundException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.NoSuchMethodException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.reflect.InvocationTargetException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.IllegalAccessException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.InstantiationException e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (org.apache.axis2.AxisFault e) {
								// we cannot intantiate the class - throw the
								// original Axis fault
								callback.receiveErrordownloadMessage(f);
							}
						} else {
							callback.receiveErrordownloadMessage(f);
						}
					} else {
						callback.receiveErrordownloadMessage(f);
					}
				} else {
					callback.receiveErrordownloadMessage(error);
				}
			}

			public void onFault(org.apache.axis2.context.MessageContext faultContext) {
				org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
				        .getInboundFaultFromMessageContext(faultContext);
				onError(fault);
			}

			public void onComplete() {
				try {
					_messageContext.getTransportOut().getSender().cleanup(_messageContext);
				} catch (org.apache.axis2.AxisFault axisFault) {
					callback.receiveErrordownloadMessage(axisFault);
				}
			}
		});

		org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
		if (_operations[3].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
			_operations[3].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
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

	private javax.xml.namespace.QName[] opNameArray = null;

	private boolean optimizeContent(javax.xml.namespace.QName opName) {

		if (opNameArray == null) {
			return false;
		}
		for (int i = 0; i < opNameArray.length; i++) {
			if (opName.equals(opNameArray[i])) {
				return true;
			}
		}
		return false;
	}

	// http://www.ecodex.org/eCODEX
	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.SendRequestURL param, boolean optimizeContent)
	        throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.SendRequestURL.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.FaultDetail param, boolean optimizeContent)
	        throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.FaultDetail.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE param, boolean optimizeContent)
	        throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.SendRequest param, boolean optimizeContent)
	        throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.SendRequest.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.ListPendingMessagesRequest param,
	        boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.ListPendingMessagesRequest.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.ListPendingMessagesResponse param,
	        boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.ListPendingMessagesResponse.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.DownloadMessageRequest param, boolean optimizeContent)
	        throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.DownloadMessageRequest.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(backend.ecodex.org.DownloadMessageResponse param, boolean optimizeContent)
	        throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(backend.ecodex.org.DownloadMessageResponse.MY_QNAME,
			        org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
	        backend.ecodex.org.SendRequestURL param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
	        throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(param.getOMElement(backend.ecodex.org.SendRequestURL.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
	        backend.ecodex.org.SendRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
	        throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(param.getOMElement(backend.ecodex.org.SendRequest.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
	        backend.ecodex.org.ListPendingMessagesRequest param, boolean optimizeContent,
	        javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
			        param.getOMElement(backend.ecodex.org.ListPendingMessagesRequest.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
	        backend.ecodex.org.DownloadMessageRequest param, boolean optimizeContent,
	        javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
			        param.getOMElement(backend.ecodex.org.DownloadMessageRequest.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	/**
	 * get the default envelope
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
		return factory.getDefaultEnvelope();
	}

	private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type,
	        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {

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

				return backend.ecodex.org.DownloadMessageRequest.Factory
				        .parse(param.getXMLStreamReaderWithoutCaching());

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

}

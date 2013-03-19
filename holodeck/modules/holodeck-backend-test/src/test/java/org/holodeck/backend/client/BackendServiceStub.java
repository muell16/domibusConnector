/*
 *
 */
package org.holodeck.backend.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

/*
 *  BackendServiceStub java implementation
 */
/**
 * The Class BackendServiceStub.
 */
public class BackendServiceStub extends org.apache.axis2.client.Stub {

	/** The _operations. */
	protected org.apache.axis2.description.AxisOperation[] _operations;
	//hashmaps to keep the fault mapping
	/** The fault exception name map. */
	private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();

	/** The fault exception class name map. */
	private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();

	/** The fault message map. */
	private java.util.HashMap faultMessageMap = new java.util.HashMap();

	/** The counter. */
	private static int counter = 0;

	/**
	 * Gets the unique suffix.
	 *
	 * @return the unique suffix
	 */
	private static synchronized java.lang.String getUniqueSuffix() {
		// reset the counter if it is greater than 99999
		if (counter > 99999) {
			counter = 0;
		}
		counter = counter + 1;
		return java.lang.Long.toString(System.currentTimeMillis()) + "_" + counter;
	}

	/**
	 * Populate axis service.
	 *
	 * @throws AxisFault the axis fault
	 */
	private void populateAxisService() throws org.apache.axis2.AxisFault {
		//creating the Service with a unique name
		_service = new org.apache.axis2.description.AxisService("BackendService" + getUniqueSuffix());
		addAnonymousOperations();
		//creating the operations
		org.apache.axis2.description.AxisOperation __operation;
		_operations = new org.apache.axis2.description.AxisOperation[4];
		__operation = new org.apache.axis2.description.OutInAxisOperation();
		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessageWithReference"));
		_service.addOperation(__operation);
		_operations[0] = __operation;
		__operation = new org.apache.axis2.description.OutInAxisOperation();
		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessage"));
		_service.addOperation(__operation);
		_operations[1] = __operation;
		__operation = new org.apache.axis2.description.OutInAxisOperation();
		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "listPendingMessages"));
		_service.addOperation(__operation);
		_operations[2] = __operation;
		__operation = new org.apache.axis2.description.OutInAxisOperation();
		__operation.setName(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "downloadMessage"));
		_service.addOperation(__operation);
		_operations[3] = __operation;
	}

	//populates the faults
	/**
	 * Populate faults.
	 */
	private void populateFaults() {
		faultExceptionNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.SendMessageWithReferenceFault");
		faultExceptionClassNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.SendMessageWithReferenceFault");
		faultMessageMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.BackendServiceStub$FaultDetail");
		faultExceptionNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.SendMessageFault");
		faultExceptionClassNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.SendMessageFault");
		faultMessageMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.BackendServiceStub$FaultDetail");
		faultExceptionNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.ListPendingMessagesFault");
		faultExceptionClassNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.ListPendingMessagesFault");
		faultMessageMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.BackendServiceStub$FaultDetail");
		faultExceptionNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.DownloadMessageFault");
		faultExceptionClassNameMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.DownloadMessageFault");
		faultMessageMap.put(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "FaultDetail"),
				"org.holodeck.backend.client.BackendServiceStub$FaultDetail");
	}

	/**
	 * Instantiates a new backend service stub.
	 *
	 * @param configurationContext the configuration context
	 * @param targetEndpoint the target endpoint
	 * @throws AxisFault the axis fault
	 */
	public BackendServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
			java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
		this(configurationContext, targetEndpoint, false);
	}

	/**
	 * Instantiates a new backend service stub.
	 *
	 * @param configurationContext the configuration context
	 * @param targetEndpoint the target endpoint
	 * @param useSeparateListener the use separate listener
	 * @throws AxisFault the axis fault
	 */
	public BackendServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
			java.lang.String targetEndpoint, boolean useSeparateListener) throws org.apache.axis2.AxisFault {
		//To populate AxisService
		populateAxisService();
		populateFaults();
		_serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);
		_serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
		_serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
	}

	/**
	 * Instantiates a new backend service stub.
	 *
	 * @param configurationContext the configuration context
	 * @throws AxisFault the axis fault
	 */
	public BackendServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext)
			throws org.apache.axis2.AxisFault {
		this(configurationContext, "http://www.ecodex.org/eCODEX");
	}

	/**
	 * Instantiates a new backend service stub.
	 *
	 * @throws AxisFault the axis fault
	 */
	public BackendServiceStub() throws org.apache.axis2.AxisFault {
		this("http://www.ecodex.org/eCODEX");
	}

	/**
	 * Instantiates a new backend service stub.
	 *
	 * @param targetEndpoint the target endpoint
	 * @throws AxisFault the axis fault
	 */
	public BackendServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
		this(null, targetEndpoint);
	}

	/**
	 * Send message with reference.
	 *
	 * @param sendRequestURL3 the send request ur l3
	 * @param messaging4 the messaging4
	 * @throws RemoteException the remote exception
	 * @throws SendMessageWithReferenceFault the send message with reference fault
	 */
	public void sendMessageWithReference(org.holodeck.backend.client.types.SendRequestURL sendRequestURL3,
			org.holodeck.backend.client.types.MessagingE messaging4) throws java.rmi.RemoteException,
			org.holodeck.backend.client.SendMessageWithReferenceFault
	{
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
			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequestURL3,
					optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/",
							"sendMessageWithReference")));
			env.build();
			// add the children only if the parameter is not null
			if (messaging4 != null) {
				org.apache.axiom.om.OMElement omElementmessaging4 = toOM(messaging4,
						optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/",
								"sendMessageWithReference")));
				addHeader(omElementmessaging4, env);
			}
			//adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);
			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);
			//execute the operation client
			_operationClient.execute(true);
			return;
		} catch (org.apache.axis2.AxisFault f) {
			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
					//make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								.get(faultElt.getQName());
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
						//message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
								new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });
						if (ex instanceof org.holodeck.backend.client.SendMessageWithReferenceFault) {
							throw (org.holodeck.backend.client.SendMessageWithReferenceFault) ex;
						}
						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			_messageContext.getTransportOut().getSender().cleanup(_messageContext);
		}
	}

	/**
	 * Startsend message with reference.
	 *
	 * @param sendRequestURL3 the send request ur l3
	 * @param messaging4 the messaging4
	 * @param callback the callback
	 * @throws RemoteException the remote exception
	 */
	public void startsendMessageWithReference(
			org.holodeck.backend.client.types.SendRequestURL sendRequestURL3,
			org.holodeck.backend.client.types.MessagingE messaging4,
			final org.holodeck.backend.client.BackendServiceCallbackHandler callback) throws java.rmi.RemoteException
	{
		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
				.createClient(_operations[0].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
		addPropertyToOperationClient(_operationClient,
				org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
		//Style is Doc.
		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequestURL3,
				optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessageWithReference")));
		// add the soap_headers only if they are not null
		if (messaging4 != null) {
			org.apache.axiom.om.OMElement omElementmessaging4 = toOM(messaging4,
					optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/",
							"sendMessageWithReference")));
			addHeader(omElementmessaging4, env);
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
		//execute the operation client
		_operationClient.execute(false);
	}

	/**
	 * Send message.
	 *
	 * @param sendRequest6 the send request6
	 * @param messaging7 the messaging7
	 * @throws RemoteException the remote exception
	 * @throws SendMessageFault the send message fault
	 */
	public void sendMessage(org.holodeck.backend.client.types.SendRequest sendRequest6,
			org.holodeck.backend.client.types.MessagingE messaging7) throws java.rmi.RemoteException,
			org.holodeck.backend.client.SendMessageFault
	{
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
			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequest6,
					optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessage")));
			env.build();
			// add the children only if the parameter is not null
			if (messaging7 != null) {
				org.apache.axiom.om.OMElement omElementmessaging7 = toOM(messaging7,
						optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessage")));
				addHeader(omElementmessaging7, env);
			}
			//adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);
			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);
			//execute the operation client
			_operationClient.execute(true);
			return;
		} catch (org.apache.axis2.AxisFault f) {
			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
					//make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								.get(faultElt.getQName());
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
						//message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
								new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });
						if (ex instanceof org.holodeck.backend.client.SendMessageFault) {
							throw (org.holodeck.backend.client.SendMessageFault) ex;
						}
						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			_messageContext.getTransportOut().getSender().cleanup(_messageContext);
		}
	}

	/**
	 * Startsend message.
	 *
	 * @param sendRequest6 the send request6
	 * @param messaging7 the messaging7
	 * @param callback the callback
	 * @throws RemoteException the remote exception
	 */
	public void startsendMessage(org.holodeck.backend.client.types.SendRequest sendRequest6,
			org.holodeck.backend.client.types.MessagingE messaging7,
			final org.holodeck.backend.client.BackendServiceCallbackHandler callback) throws java.rmi.RemoteException
	{
		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
				.createClient(_operations[1].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
		addPropertyToOperationClient(_operationClient,
				org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
		//Style is Doc.
		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), sendRequest6,
				optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessage")));
		// add the soap_headers only if they are not null
		if (messaging7 != null) {
			org.apache.axiom.om.OMElement omElementmessaging7 = toOM(messaging7,
					optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "sendMessage")));
			addHeader(omElementmessaging7, env);
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
		//execute the operation client
		_operationClient.execute(false);
	}

	/**
	 * List pending messages.
	 *
	 * @param listPendingMessagesRequest9 the list pending messages request9
	 * @return the org.holodeck.backend.client. backend service stub. list pending messages response
	 * @throws RemoteException the remote exception
	 * @throws ListPendingMessagesFault the list pending messages fault
	 */
	public org.holodeck.backend.client.types.ListPendingMessagesResponse listPendingMessages(
			org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest9)
			throws java.rmi.RemoteException, org.holodeck.backend.client.ListPendingMessagesFault
	{
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
					listPendingMessagesRequest9, optimizeContent(new javax.xml.namespace.QName(
							"http://org.ecodex.backend/1_0/", "listPendingMessages")));
			//adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);
			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);
			//execute the operation client
			_operationClient.execute(true);
			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
			java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
					org.holodeck.backend.client.types.ListPendingMessagesResponse.class,
					getEnvelopeNamespaces(_returnEnv));
			return (org.holodeck.backend.client.types.ListPendingMessagesResponse) object;
		} catch (org.apache.axis2.AxisFault f) {
			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
					//make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								.get(faultElt.getQName());
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
						//message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
								new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });
						if (ex instanceof org.holodeck.backend.client.ListPendingMessagesFault) {
							throw (org.holodeck.backend.client.ListPendingMessagesFault) ex;
						}
						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			_messageContext.getTransportOut().getSender().cleanup(_messageContext);
		}
	}

	/**
	 * Startlist pending messages.
	 *
	 * @param listPendingMessagesRequest9 the list pending messages request9
	 * @param callback the callback
	 * @throws RemoteException the remote exception
	 */
	public void startlistPendingMessages(
			org.holodeck.backend.client.types.ListPendingMessagesRequest listPendingMessagesRequest9,
			final org.holodeck.backend.client.BackendServiceCallbackHandler callback) throws java.rmi.RemoteException
	{
		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
				.createClient(_operations[2].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
		addPropertyToOperationClient(_operationClient,
				org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
		//Style is Doc.
		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), listPendingMessagesRequest9,
				optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "listPendingMessages")));
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
							org.holodeck.backend.client.types.ListPendingMessagesResponse.class,
							getEnvelopeNamespaces(resultEnv));
					callback.receiveResultlistPendingMessages((org.holodeck.backend.client.types.ListPendingMessagesResponse) object);
				} catch (org.apache.axis2.AxisFault e) {
					callback.receiveErrorlistPendingMessages(e);
				}
			}

			public void onError(java.lang.Exception error) {
				if (error instanceof org.apache.axis2.AxisFault) {
					org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
							//make the fault by reflection
							try {
								java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
										.get(faultElt.getQName());
								java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
								java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
								//message class
								java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt
										.getQName());
								java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
								java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
								java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
										new java.lang.Class[] { messageClass });
								m.invoke(ex, new java.lang.Object[] { messageObject });
								if (ex instanceof org.holodeck.backend.client.ListPendingMessagesFault) {
									callback.receiveErrorlistPendingMessages((org.holodeck.backend.client.ListPendingMessagesFault) ex);
									return;
								}
								callback.receiveErrorlistPendingMessages(new java.rmi.RemoteException(ex.getMessage(),
										ex));
							} catch (java.lang.ClassCastException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.ClassNotFoundException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.NoSuchMethodException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.reflect.InvocationTargetException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.IllegalAccessException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (java.lang.InstantiationException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrorlistPendingMessages(f);
							} catch (org.apache.axis2.AxisFault e) {
								// we cannot intantiate the class - throw the original Axis fault
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
		//execute the operation client
		_operationClient.execute(false);
	}

	/**
	 * Download message.
	 *
	 * @param downloadMessageRequest11 the download message request11
	 * @return the org.holodeck.backend.client. backend service stub. download message response
	 * @throws RemoteException the remote exception
	 * @throws DownloadMessageFault the download message fault
	 */
	public org.holodeck.backend.client.types.DownloadMessageResponse downloadMessage(
			org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest11)
			throws java.rmi.RemoteException, org.holodeck.backend.client.DownloadMessageFault
	{
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
			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), downloadMessageRequest11,
					optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "downloadMessage")));
			//adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);
			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);
			//execute the operation client
			_operationClient.execute(true);
			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
			java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
					org.holodeck.backend.client.types.DownloadMessageResponse.class,
					getEnvelopeNamespaces(_returnEnv));
			return (org.holodeck.backend.client.types.DownloadMessageResponse) object;
		} catch (org.apache.axis2.AxisFault f) {
			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
					//make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								.get(faultElt.getQName());
						java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
						java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
						//message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
						java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
								new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });
						if (ex instanceof org.holodeck.backend.client.DownloadMessageFault) {
							throw (org.holodeck.backend.client.DownloadMessageFault) ex;
						}
						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			_messageContext.getTransportOut().getSender().cleanup(_messageContext);
		}
	}

	/**
	 * Startdownload message.
	 *
	 * @param downloadMessageRequest11 the download message request11
	 * @param callback the callback
	 * @throws RemoteException the remote exception
	 */
	public void startdownloadMessage(
			org.holodeck.backend.client.types.DownloadMessageRequest downloadMessageRequest11,
			final org.holodeck.backend.client.BackendServiceCallbackHandler callback) throws java.rmi.RemoteException
	{
		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
				.createClient(_operations[3].getName());
		_operationClient.getOptions().setAction("\"\"");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
		addPropertyToOperationClient(_operationClient,
				org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
		//Style is Doc.
		env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), downloadMessageRequest11,
				optimizeContent(new javax.xml.namespace.QName("http://org.ecodex.backend/1_0/", "downloadMessage")));
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
							org.holodeck.backend.client.types.DownloadMessageResponse.class,
							getEnvelopeNamespaces(resultEnv));
					callback.receiveResultdownloadMessage((org.holodeck.backend.client.types.DownloadMessageResponse) object);
				} catch (org.apache.axis2.AxisFault e) {
					callback.receiveErrordownloadMessage(e);
				}
			}

			public void onError(java.lang.Exception error) {
				if (error instanceof org.apache.axis2.AxisFault) {
					org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
							//make the fault by reflection
							try {
								java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
										.get(faultElt.getQName());
								java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
								java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
								//message class
								java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt
										.getQName());
								java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
								java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
								java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
										new java.lang.Class[] { messageClass });
								m.invoke(ex, new java.lang.Object[] { messageObject });
								if (ex instanceof org.holodeck.backend.client.DownloadMessageFault) {
									callback.receiveErrordownloadMessage((org.holodeck.backend.client.DownloadMessageFault) ex);
									return;
								}
								callback.receiveErrordownloadMessage(new java.rmi.RemoteException(ex.getMessage(), ex));
							} catch (java.lang.ClassCastException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.ClassNotFoundException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.NoSuchMethodException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.reflect.InvocationTargetException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.IllegalAccessException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (java.lang.InstantiationException e) {
								// we cannot intantiate the class - throw the original Axis fault
								callback.receiveErrordownloadMessage(f);
							} catch (org.apache.axis2.AxisFault e) {
								// we cannot intantiate the class - throw the original Axis fault
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
		//execute the operation client
		_operationClient.execute(false);
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

	/** The op name array. */
	private javax.xml.namespace.QName[] opNameArray = null;

	/**
	 * Optimize content.
	 *
	 * @param opName the op name
	 * @return true, if successful
	 */
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

	/**
	 * To om.
	 *
	 * @param param the param
	 * @param optimizeContent the optimize content
	 * @return the org.apache.axiom.om. om element
	 * @throws AxisFault the axis fault
	 */
	private org.apache.axiom.om.OMElement toOM(org.holodeck.backend.client.types.SendRequestURL param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.holodeck.backend.client.types.SendRequestURL.MY_QNAME,
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
	private org.apache.axiom.om.OMElement toOM(org.holodeck.backend.client.types.FaultDetail param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.holodeck.backend.client.types.FaultDetail.MY_QNAME,
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
	private org.apache.axiom.om.OMElement toOM(org.holodeck.backend.client.types.MessagingE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.holodeck.backend.client.types.MessagingE.MY_QNAME,
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
	private org.apache.axiom.om.OMElement toOM(org.holodeck.backend.client.types.SendRequest param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.holodeck.backend.client.types.SendRequest.MY_QNAME,
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
			org.holodeck.backend.client.types.ListPendingMessagesRequest param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(
					org.holodeck.backend.client.types.ListPendingMessagesRequest.MY_QNAME,
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
			org.holodeck.backend.client.types.ListPendingMessagesResponse param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(
					org.holodeck.backend.client.types.ListPendingMessagesResponse.MY_QNAME,
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
			org.holodeck.backend.client.types.DownloadMessageRequest param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.holodeck.backend.client.types.DownloadMessageRequest.MY_QNAME,
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
			org.holodeck.backend.client.types.DownloadMessageResponse param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			return param.getOMElement(org.holodeck.backend.client.types.DownloadMessageResponse.MY_QNAME,
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
			org.holodeck.backend.client.types.SendRequestURL param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody()
					.addChild(
							param.getOMElement(org.holodeck.backend.client.types.SendRequestURL.MY_QNAME,
									factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/* methods to provide back word compatibility */
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
			org.holodeck.backend.client.types.SendRequest param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(org.holodeck.backend.client.types.SendRequest.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/* methods to provide back word compatibility */
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
			org.holodeck.backend.client.types.ListPendingMessagesRequest param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody()
					.addChild(
							param.getOMElement(
									org.holodeck.backend.client.types.ListPendingMessagesRequest.MY_QNAME,
									factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/* methods to provide back word compatibility */
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
			org.holodeck.backend.client.types.DownloadMessageRequest param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault
	{
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(org.holodeck.backend.client.types.DownloadMessageRequest.MY_QNAME,
							factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	/* methods to provide back word compatibility */
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
			if (org.holodeck.backend.client.types.SendRequestURL.class.equals(type)) {
				return org.holodeck.backend.client.types.SendRequestURL.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.FaultDetail.class.equals(type)) {
				return org.holodeck.backend.client.types.FaultDetail.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.MessagingE.class.equals(type)) {
				return org.holodeck.backend.client.types.MessagingE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.SendRequest.class.equals(type)) {
				return org.holodeck.backend.client.types.SendRequest.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.FaultDetail.class.equals(type)) {
				return org.holodeck.backend.client.types.FaultDetail.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.MessagingE.class.equals(type)) {
				return org.holodeck.backend.client.types.MessagingE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.ListPendingMessagesRequest.class.equals(type)) {
				return org.holodeck.backend.client.types.ListPendingMessagesRequest.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.ListPendingMessagesResponse.class.equals(type)) {
				return org.holodeck.backend.client.types.ListPendingMessagesResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.FaultDetail.class.equals(type)) {
				return org.holodeck.backend.client.types.FaultDetail.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.DownloadMessageRequest.class.equals(type)) {
				return org.holodeck.backend.client.types.DownloadMessageRequest.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.DownloadMessageResponse.class.equals(type)) {
				return org.holodeck.backend.client.types.DownloadMessageResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.FaultDetail.class.equals(type)) {
				return org.holodeck.backend.client.types.FaultDetail.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
			if (org.holodeck.backend.client.types.MessagingE.class.equals(type)) {
				return org.holodeck.backend.client.types.MessagingE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());
			}
		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
		return null;
	}
}

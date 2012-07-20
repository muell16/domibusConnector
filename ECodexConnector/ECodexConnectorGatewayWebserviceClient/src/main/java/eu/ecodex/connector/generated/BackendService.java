
/**
 * BackendService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package eu.ecodex.connector.generated;

/*
 *  BackendService java interface
 */

public interface BackendService {

	/**
	 * Auto generated method signature
	 * 
	 * @param sendRequestURL3
	 * 
	 * @param messaging4
	 * 
	 * @throws eu.ecodex.connector.generated.SendMessageWithReferenceFault
	 *             :
	 */

	public void sendMessageWithReference(

	backend.ecodex.org.SendRequestURL sendRequestURL3,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging4)
	        throws java.rmi.RemoteException

	        , eu.ecodex.connector.generated.SendMessageWithReferenceFault;

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @param sendRequestURL3
	 * 
	 * @param messaging4
	 */
	public void startsendMessageWithReference(

	backend.ecodex.org.SendRequestURL sendRequestURL3,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging4,

	        final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException;

	/**
	 * Auto generated method signature
	 * 
	 * @param sendRequest6
	 * 
	 * @param messaging7
	 * 
	 * @throws eu.ecodex.connector.generated.SendMessageFault
	 *             :
	 */

	public void sendMessage(

	backend.ecodex.org.SendRequest sendRequest6,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging7)
	        throws java.rmi.RemoteException

	        , eu.ecodex.connector.generated.SendMessageFault;

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @param sendRequest6
	 * 
	 * @param messaging7
	 */
	public void startsendMessage(

	backend.ecodex.org.SendRequest sendRequest6,
	        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging7,

	        final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException;

	/**
	 * Auto generated method signature
	 * 
	 * @param listPendingMessagesRequest9
	 * 
	 * @throws eu.ecodex.connector.generated.ListPendingMessagesFault
	 *             :
	 */

	public backend.ecodex.org.ListPendingMessagesResponse listPendingMessages(

	backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest9) throws java.rmi.RemoteException

	, eu.ecodex.connector.generated.ListPendingMessagesFault;

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @param listPendingMessagesRequest9
	 */
	public void startlistPendingMessages(

	backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest9,

	final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException;

	/**
	 * Auto generated method signature
	 * 
	 * @param downloadMessageRequest11
	 * 
	 * @throws eu.ecodex.connector.generated.DownloadMessageFault
	 *             :
	 */

	public backend.ecodex.org.DownloadMessageResponse downloadMessage(

	backend.ecodex.org.DownloadMessageRequest downloadMessageRequest11) throws java.rmi.RemoteException

	, eu.ecodex.connector.generated.DownloadMessageFault;

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @param downloadMessageRequest11
	 */
	public void startdownloadMessage(

	backend.ecodex.org.DownloadMessageRequest downloadMessageRequest11,

	final eu.ecodex.connector.generated.BackendServiceCallbackHandler callback)

	throws java.rmi.RemoteException;

	//
}

/**
 * BackendServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package eu.ecodex.connector.generated;

/**
 * BackendServiceCallbackHandler Callback class, Users can extend this class and
 * implement their own receiveResult and receiveError methods.
 */
public abstract class BackendServiceCallbackHandler {

	protected Object clientData;

	/**
	 * User can pass in any object that needs to be accessed once the
	 * NonBlocking Web service call is finished and appropriate method of this
	 * CallBack is called.
	 * 
	 * @param clientData
	 *            Object mechanism by which the user can pass in user data that
	 *            will be avilable at the time this callback is called.
	 */
	public BackendServiceCallbackHandler(Object clientData) {
		this.clientData = clientData;
	}

	/**
	 * Please use this constructor if you don't want to set any clientData
	 */
	public BackendServiceCallbackHandler() {
		this.clientData = null;
	}

	/**
	 * Get the client data
	 */

	public Object getClientData() {
		return clientData;
	}

	/**
	 * auto generated Axis2 call back method for sendMessageWithReference method
	 * override this method for handling normal response from
	 * sendMessageWithReference operation
	 */
	public void receiveResultsendMessageWithReference() {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from sendMessageWithReference operation
	 */
	public void receiveErrorsendMessageWithReference(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for sendMessage method override
	 * this method for handling normal response from sendMessage operation
	 */
	public void receiveResultsendMessage() {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from sendMessage operation
	 */
	public void receiveErrorsendMessage(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for listPendingMessages method
	 * override this method for handling normal response from
	 * listPendingMessages operation
	 */
	public void receiveResultlistPendingMessages(backend.ecodex.org.ListPendingMessagesResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from listPendingMessages operation
	 */
	public void receiveErrorlistPendingMessages(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for downloadMessage method override
	 * this method for handling normal response from downloadMessage operation
	 */
	public void receiveResultdownloadMessage(backend.ecodex.org.DownloadMessageResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from downloadMessage operation
	 */
	public void receiveErrordownloadMessage(java.lang.Exception e) {
	}

}

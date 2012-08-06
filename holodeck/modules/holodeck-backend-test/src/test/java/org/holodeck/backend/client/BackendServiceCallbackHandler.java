/*
 *
 */
package org.holodeck.backend.client;

/**
 * The Class BackendServiceCallbackHandler.
 */
public abstract class BackendServiceCallbackHandler {

	/** The client data. */
	protected Object clientData;

	/**
	 * Instantiates a new backend service callback handler.
	 *
	 * @param clientData the client data
	 */
	public BackendServiceCallbackHandler(Object clientData) {
		this.clientData = clientData;
	}

	/**
	 * Instantiates a new backend service callback handler.
	 */
	public BackendServiceCallbackHandler() {
		this.clientData = null;
	}

	/**
	 * Gets the client data.
	 *
	 * @return the client data
	 */
	public Object getClientData() {
		return clientData;
	}

	/**
	 * Receive resultsend message with reference.
	 */
	public void receiveResultsendMessageWithReference() {
	}

	/**
	 * Receive errorsend message with reference.
	 *
	 * @param e the e
	 */
	public void receiveErrorsendMessageWithReference(java.lang.Exception e) {
	}

	/**
	 * Receive resultsend message.
	 */
	public void receiveResultsendMessage() {
	}

	/**
	 * Receive errorsend message.
	 *
	 * @param e the e
	 */
	public void receiveErrorsendMessage(java.lang.Exception e) {
	}

	/**
	 * Receive resultlist pending messages.
	 *
	 * @param result the result
	 */
	public void receiveResultlistPendingMessages(
			org.holodeck.backend.client.types.ListPendingMessagesResponse result)
	{
	}

	/**
	 * Receive errorlist pending messages.
	 *
	 * @param e the e
	 */
	public void receiveErrorlistPendingMessages(java.lang.Exception e) {
	}

	/**
	 * Receive resultdownload message.
	 *
	 * @param result the result
	 */
	public void receiveResultdownloadMessage(
			org.holodeck.backend.client.types.DownloadMessageResponse result)
	{
	}

	/**
	 * Receive errordownload message.
	 *
	 * @param e the e
	 */
	public void receiveErrordownloadMessage(java.lang.Exception e) {
	}
}

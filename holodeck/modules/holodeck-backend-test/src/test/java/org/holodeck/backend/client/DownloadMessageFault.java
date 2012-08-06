/*
 *
 */
package org.holodeck.backend.client;

/**
 * The Class DownloadMessageFault.
 */
public class DownloadMessageFault extends java.lang.Exception {

	/** The fault message. */
	private org.holodeck.backend.client.types.FaultDetail faultMessage;

	/**
	 * Instantiates a new download message fault.
	 */
	public DownloadMessageFault() {
		super("DownloadMessageFault");
	}

	/**
	 * Instantiates a new download message fault.
	 *
	 * @param s the s
	 */
	public DownloadMessageFault(java.lang.String s) {
		super(s);
	}

	/**
	 * Instantiates a new download message fault.
	 *
	 * @param s the s
	 * @param ex the ex
	 */
	public DownloadMessageFault(java.lang.String s, java.lang.Throwable ex) {
		super(s, ex);
	}

	/**
	 * Instantiates a new download message fault.
	 *
	 * @param cause the cause
	 */
	public DownloadMessageFault(java.lang.Throwable cause) {
		super(cause);
	}

	/**
	 * Sets the fault message.
	 *
	 * @param msg the new fault message
	 */
	public void setFaultMessage(org.holodeck.backend.client.types.FaultDetail msg) {
		faultMessage = msg;
	}

	/**
	 * Gets the fault message.
	 *
	 * @return the fault message
	 */
	public org.holodeck.backend.client.types.FaultDetail getFaultMessage() {
		return faultMessage;
	}
}

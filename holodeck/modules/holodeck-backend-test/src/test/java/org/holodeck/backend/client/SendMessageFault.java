/*
 *
 */
package org.holodeck.backend.client;

/**
 * The Class SendMessageFault.
 */
public class SendMessageFault extends java.lang.Exception {

	/** The fault message. */
	private org.holodeck.backend.client.types.FaultDetail faultMessage;

	/**
	 * Instantiates a new send message fault.
	 */
	public SendMessageFault() {
		super("SendMessageFault");
	}

	/**
	 * Instantiates a new send message fault.
	 *
	 * @param s the s
	 */
	public SendMessageFault(java.lang.String s) {
		super(s);
	}

	/**
	 * Instantiates a new send message fault.
	 *
	 * @param s the s
	 * @param ex the ex
	 */
	public SendMessageFault(java.lang.String s, java.lang.Throwable ex) {
		super(s, ex);
	}

	/**
	 * Instantiates a new send message fault.
	 *
	 * @param cause the cause
	 */
	public SendMessageFault(java.lang.Throwable cause) {
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

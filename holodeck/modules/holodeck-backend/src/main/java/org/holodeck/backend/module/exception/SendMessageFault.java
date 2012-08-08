/*
 * 
 */
package org.holodeck.backend.module.exception;

/**
 * The Class SendMessageFault.
 */
public class SendMessageFault extends java.lang.Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7749407617039423058L;

	/** The fault message. */
	private backend.ecodex.org.FaultDetail faultMessage;

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
	public void setFaultMessage(backend.ecodex.org.FaultDetail msg) {
		faultMessage = msg;
	}

	/**
	 * Gets the fault message.
	 *
	 * @return the fault message
	 */
	public backend.ecodex.org.FaultDetail getFaultMessage() {
		return faultMessage;
	}
}

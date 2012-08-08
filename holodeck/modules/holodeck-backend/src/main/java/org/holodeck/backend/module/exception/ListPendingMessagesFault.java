/*
 * 
 */
package org.holodeck.backend.module.exception;

/**
 * The Class ListPendingMessagesFault.
 */
public class ListPendingMessagesFault extends java.lang.Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8428617365183557042L;

	/** The fault message. */
	private backend.ecodex.org.FaultDetail faultMessage;

	/**
	 * Instantiates a new list pending messages fault.
	 */
	public ListPendingMessagesFault() {
		super("ListPendingMessagesFault");
	}

	/**
	 * Instantiates a new list pending messages fault.
	 *
	 * @param s the s
	 */
	public ListPendingMessagesFault(java.lang.String s) {
		super(s);
	}

	/**
	 * Instantiates a new list pending messages fault.
	 *
	 * @param s the s
	 * @param ex the ex
	 */
	public ListPendingMessagesFault(java.lang.String s, java.lang.Throwable ex) {
		super(s, ex);
	}

	/**
	 * Instantiates a new list pending messages fault.
	 *
	 * @param cause the cause
	 */
	public ListPendingMessagesFault(java.lang.Throwable cause) {
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

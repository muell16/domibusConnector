/*
 * 
 */
package org.holodeck.backend.service.exception;

import backend.ecodex.org.Code;
import backend.ecodex.org.FaultDetail;

/**
 * The Class SendMessageServiceException.
 */
public class SendMessageServiceException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3117224315509851312L;

	/** The code. */
	private Code code;

	/**
	 * Instantiates a new send message service exception.
	 */
	public SendMessageServiceException() {
		super();
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SendMessageServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param message the message
	 */
	public SendMessageServiceException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param cause the cause
	 */
	public SendMessageServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param code the code
	 */
	public SendMessageServiceException(Code code) {
		super();
		this.code = code;
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param code the code
	 */
	public SendMessageServiceException(String message, Throwable cause, Code code) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param message the message
	 * @param code the code
	 */
	public SendMessageServiceException(String message, Code code) {
		super(message);
		this.code = code;
	}

	/**
	 * Instantiates a new send message service exception.
	 *
	 * @param cause the cause
	 * @param code the code
	 */
	public SendMessageServiceException(Throwable cause, Code code) {
		super(cause);
		this.code = code;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public Code getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(Code code) {
		this.code = code;
	}

	/**
	 * Gets the fault.
	 *
	 * @return the fault
	 */
	public FaultDetail getFault() {
		FaultDetail faultDetail = new FaultDetail();
		faultDetail.setCode(code);
		faultDetail.setMessage(getMessage());
		return faultDetail;
	}
}

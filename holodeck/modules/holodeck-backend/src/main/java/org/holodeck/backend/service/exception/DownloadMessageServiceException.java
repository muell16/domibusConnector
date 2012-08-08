/*
 * 
 */
package org.holodeck.backend.service.exception;

import backend.ecodex.org.Code;
import backend.ecodex.org.FaultDetail;

/**
 * The Class DownloadMessageServiceException.
 */
public class DownloadMessageServiceException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7776392255041070516L;

	/** The code. */
	private Code code;

	/**
	 * Instantiates a new download message service exception.
	 */
	public DownloadMessageServiceException() {
		super();
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public DownloadMessageServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param message the message
	 */
	public DownloadMessageServiceException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param cause the cause
	 */
	public DownloadMessageServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param code the code
	 */
	public DownloadMessageServiceException(Code code) {
		super();
		this.code = code;
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param code the code
	 */
	public DownloadMessageServiceException(String message, Throwable cause, Code code) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param message the message
	 * @param code the code
	 */
	public DownloadMessageServiceException(String message, Code code) {
		super(message);
		this.code = code;
	}

	/**
	 * Instantiates a new download message service exception.
	 *
	 * @param cause the cause
	 * @param code the code
	 */
	public DownloadMessageServiceException(Throwable cause, Code code) {
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

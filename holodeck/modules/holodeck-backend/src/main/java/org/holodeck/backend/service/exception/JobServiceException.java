/*
 * 
 */
package org.holodeck.backend.service.exception;


/**
 * The Class JobServiceException.
 */
public class JobServiceException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3465278371193590261L;

	/**
	 * Instantiates a new job service exception.
	 */
	public JobServiceException() {
		super();
	}

	/**
	 * Instantiates a new job service exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public JobServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new job service exception.
	 *
	 * @param message the message
	 */
	public JobServiceException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new job service exception.
	 *
	 * @param cause the cause
	 */
	public JobServiceException(Throwable cause) {
		super(cause);
	}
}

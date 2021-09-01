package eu.domibus.connector.web.exception;

import org.springframework.security.core.AuthenticationException;

public class UserLoginException extends AuthenticationException {

	public UserLoginException(String explanation) {
		super(explanation);
	}

	public UserLoginException(String msg, Throwable t) {
		super(msg, t);
	}

}

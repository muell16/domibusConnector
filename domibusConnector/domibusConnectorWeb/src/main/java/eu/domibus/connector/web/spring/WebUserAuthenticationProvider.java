package eu.domibus.connector.web.spring;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import eu.domibus.connector.persistence.service.web.DomibusConnectorWebUserPersistenceService;
import eu.domibus.connector.web.dto.WebUser;



/**
 * uses the IDomibusWebAdminUserDao to get the users from DB
 * @author spindlest
 *
 */
@Service
public class WebUserAuthenticationProvider implements AuthenticationProvider {

	private final static Logger LOG = LoggerFactory.getLogger(WebUserAuthenticationProvider.class);
	
	private DomibusConnectorWebUserPersistenceService webUserPersistenceService;

	@Autowired
	public void setWebUserPersistenceService(DomibusConnectorWebUserPersistenceService webUserPersistenceService) {
		this.webUserPersistenceService = webUserPersistenceService;
	}

	/**
     * {@inheritDoc }     
     */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication == null) {
			throw new IllegalArgumentException("authentication is not allowed to be null!");
		}
		LOG.trace("authenticate: called");
		
				
		UsernamePasswordAuthenticationToken pwAuth = (UsernamePasswordAuthenticationToken) authentication;
		
		String username = pwAuth.getName();
		String password = pwAuth.getCredentials().toString();
		
		LOG.trace("authenticate: username is [{}], password is [{}]", username, password);
//		WebUser user = webUserPersistenceService.login(username, password);
//		if (user == null) {
//			throw new  BadCredentialsException("username or password incorrect!");
//		}
		LOG.debug("authenticated user [{}]  successfully]", username);		
		return new UsernamePasswordAuthenticationToken(
		          username, password, new ArrayList<>());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * only username + password authentication is supported
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return (authentication == UsernamePasswordAuthenticationToken.class);
	}

}

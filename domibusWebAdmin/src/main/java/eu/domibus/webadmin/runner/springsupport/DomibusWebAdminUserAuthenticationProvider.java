package eu.domibus.webadmin.runner.springsupport;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import eu.domibus.webadmin.dao.IDomibusWebAdminUserDao;

/**
 * uses the IDomibusWebAdminUserDao to get the users from DB
 * @author spindlest
 *
 */
@Service
public class DomibusWebAdminUserAuthenticationProvider implements AuthenticationProvider {

	private final static Logger LOG = LoggerFactory.getLogger(DomibusWebAdminUserAuthenticationProvider.class);
	
	private IDomibusWebAdminUserDao domibusWebAdminUserDao;

	@Autowired
	public DomibusWebAdminUserAuthenticationProvider(IDomibusWebAdminUserDao domibusWebAdminUserDao) {
		this.domibusWebAdminUserDao = domibusWebAdminUserDao;
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
		try {
			boolean loginSuccess = this.domibusWebAdminUserDao.login(username, password);
			if (loginSuccess == false) {
				throw new  BadCredentialsException("username or password incorrect!");
			}
			LOG.debug("authenticated user [{}]  to [{}]", username, loginSuccess);		
			return new UsernamePasswordAuthenticationToken(
		              username, password, new ArrayList<>());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOG.error("Exception occured with crypto", e); 
			throw new AuthenticationServiceException("Problems with crypto", e);
		}

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

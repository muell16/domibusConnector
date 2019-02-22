package eu.domibus.webadmin.dao.impl;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.webadmin.commons.Util;
import eu.domibus.webadmin.dao.IDomibusWebAdminUserDao;
import eu.domibus.webadmin.model.connector.DomibusWebAdminUser;

@Repository("domibusWebAdminUserDao")
@Transactional(readOnly=true, value="transactionManager")
public class DomibusWebAdminUserDao implements IDomibusWebAdminUserDao, Serializable {

    private static final long serialVersionUID = -8330659798855359673L;
    
    @PersistenceContext //(unitName = "domibus.webadmin")
    private EntityManager em;

    @Override
    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	
        Query q = em.createQuery("from DomibusWebAdminUser m where m.username =:username");
        q.setParameter("username", username);

        DomibusWebAdminUser domibusWebAdminUser = (DomibusWebAdminUser) q.getSingleResult();
        
        String passwordDB = domibusWebAdminUser.getPassword();
        String saltDB = domibusWebAdminUser.getSalt();
        String saltedPasswordDB = saltDB + passwordDB;
        String passwordParamHashed = Util.generatePasswordHashWithSalt(password, saltDB);

        if (passwordParamHashed.equals(saltedPasswordDB)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(readOnly=false, value="transactionManager")
    public void insertUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = Util.getHexSalt();
        String passwordDB = Util.generatePasswordHashWithSaltOnlyPW(password, salt);

        DomibusWebAdminUser domibusWebAdminUser = new DomibusWebAdminUser();
        domibusWebAdminUser.setUsername(username);
        domibusWebAdminUser.setPassword(passwordDB);
        domibusWebAdminUser.setSalt(salt);
        domibusWebAdminUser.setRole("admin");
        em.persist(domibusWebAdminUser);


    }
    
    @Override
    @Transactional(readOnly=false, value="transactionManager")
    public void updateUserPassword(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
    	Query q = em.createQuery("from DomibusWebAdminUser m where m.username =:username");
        q.setParameter("username", username);
        DomibusWebAdminUser domibusWebAdminUser = (DomibusWebAdminUser) q.getSingleResult();	
        
    	String salt = Util.getHexSalt();
        String passwordDB = Util.generatePasswordHashWithSaltOnlyPW(password, salt);

        domibusWebAdminUser.setPassword(passwordDB);
        domibusWebAdminUser.setSalt(salt);
        
        em.merge(domibusWebAdminUser);
    }

    @Override
    @Transactional(readOnly=false, value="transactionManager")
    public void deleteUser(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	Query q = em.createQuery("from DomibusWebAdminUser m where m.username =:username");
        q.setParameter("username", username);
        DomibusWebAdminUser domibusWebAdminUser = (DomibusWebAdminUser) q.getSingleResult();	
        em.remove(domibusWebAdminUser);
    }

    @Override
    public boolean checkIfUserExists(String username) {

        try {
        	Query q = em.createQuery("from DomibusWebAdminUser m where m.username =:username");
            q.setParameter("username", username);

            DomibusWebAdminUser domibusWebAdminUser = (DomibusWebAdminUser) q.getSingleResult();

            return domibusWebAdminUser != null;

        } catch (EmptyResultDataAccessException e) {
            return false;
        }catch (NoResultException e) {
            return false;
        }

    }

}
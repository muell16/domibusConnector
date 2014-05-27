package eu.ecodex.webadmin.dao.impl;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import eu.ecodex.webadmin.commons.Util;
import eu.ecodex.webadmin.dao.IECodexWebAdminUserDao;
import eu.ecodex.webadmin.model.connector.ECodexWebAdminUser;

@Repository
public class ECodexWebAdminUserDao implements IECodexWebAdminUserDao, Serializable {

    private static final long serialVersionUID = -8330659798855359673L;

    @PersistenceContext(unitName = "ecodex.webadmin")
    private EntityManager em;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.dao.impl.IECodexWebAdminUserDao#login(java.lang.String
     * , java.lang.String)
     */
    @Override
    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Query q = em.createQuery("from ECodexWebAdminUser m where m.username=:username");
        q.setParameter("username", username);
        ECodexWebAdminUser eCodexWebAdminUser = (ECodexWebAdminUser) q.getSingleResult();
        String passwordDB = eCodexWebAdminUser.getPassword();
        String saltDB = eCodexWebAdminUser.getSalt();
        String saltedPasswordDB = saltDB + passwordDB;
        String passwordParamHashed = Util.generatePasswordHashWithSalt(password, saltDB);

        if (passwordParamHashed.equals(saltedPasswordDB)) {
            return true;
        } else {
            return false;
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
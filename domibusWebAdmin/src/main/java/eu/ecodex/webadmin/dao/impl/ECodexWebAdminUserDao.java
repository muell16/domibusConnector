package eu.ecodex.webadmin.dao.impl;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import eu.ecodex.webadmin.commons.Util;
import eu.ecodex.webadmin.dao.IECodexWebAdminUserDao;
import eu.ecodex.webadmin.model.connector.ECodexWebAdminUser;

public class ECodexWebAdminUserDao extends JdbcDaoSupport implements IECodexWebAdminUserDao, Serializable {

    private static final long serialVersionUID = -8330659798855359673L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.dao.impl.IECodexWebAdminUserDao#login(java.lang.String
     * , java.lang.String)
     */
    @Override
    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String sql = "select * from ECODEX_WEBADMIN_USER where USERNAME = ?";
        String[] parameter = new String[1];
        parameter[0] = username;
        // Map<String, Object> map = getJdbcTemplate().queryForMap(sql,
        // parameter);

        ECodexWebAdminUser eCodexWebAdminUser = getJdbcTemplate().queryForObject(sql, new Object[] { username },
                new BeanPropertyRowMapper<ECodexWebAdminUser>(ECodexWebAdminUser.class));

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

    @Override
    public void insertUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = Util.getHexSalt();
        String passwordDB = Util.generatePasswordHashWithSaltOnlyPW(password, salt);

        getJdbcTemplate().update(
                "insert into ECODEX_WEBADMIN_USER (USERNAME, PASSWORD, SALT, ROLE) values (?, ?, ?, ?)",
                new Object[] { username, passwordDB, salt, "admin" });

    }

    @Override
    public void deleteUser(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
        getJdbcTemplate().update("delete from ECODEX_WEBADMIN_USER where USERNAME = ?", new Object[] { username });
    }

    @Override
    public boolean checkIfUserExists(String username) {

        try {
            String sql = "select * from ECODEX_WEBADMIN_USER where USERNAME = ?";
            String[] parameter = new String[1];
            parameter[0] = username;

            getJdbcTemplate().queryForObject(sql, new Object[] { username },
                    new BeanPropertyRowMapper<ECodexWebAdminUser>(ECodexWebAdminUser.class));

            return true;

        } catch (EmptyResultDataAccessException e) {
            return false;
        }

    }

}
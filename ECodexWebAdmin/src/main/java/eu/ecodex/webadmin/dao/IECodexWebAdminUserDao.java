package eu.ecodex.webadmin.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public interface IECodexWebAdminUserDao {

    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException,
            SQLException;

}
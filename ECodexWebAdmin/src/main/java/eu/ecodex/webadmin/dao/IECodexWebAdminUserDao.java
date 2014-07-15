package eu.ecodex.webadmin.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IECodexWebAdminUserDao {

    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

    public void insertUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

    public void deleteUser(String username) throws NoSuchAlgorithmException, InvalidKeySpecException;

    public boolean checkIfUserExists(String username);

}
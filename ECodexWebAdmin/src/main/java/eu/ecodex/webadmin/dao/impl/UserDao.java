package eu.ecodex.webadmin.dao.impl;

public class UserDao {

    public static boolean login(String user, String password) {

        if ("admin".equals(user) && "admin".equals(password)) {
            return true;
        } else {
            return false;
        }

    }
}
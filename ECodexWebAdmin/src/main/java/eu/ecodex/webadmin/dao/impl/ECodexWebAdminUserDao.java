package eu.ecodex.webadmin.dao.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import eu.ecodex.webadmin.commons.Util;
import eu.ecodex.webadmin.dao.IECodexWebAdminUserDao;

public class ECodexWebAdminUserDao implements IECodexWebAdminUserDao {

    private ComboPooledDataSource dataSource;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.dao.impl.IECodexWebAdminUserDao#login(java.lang.String
     * , java.lang.String)
     */
    @Override
    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException,
            SQLException {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = con.prepareStatement("select * from ECODEX_WEBADMIN_USER where username= ?");
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String passwordDB = rs.getString("PASSWORD");
            String saltDB = rs.getString("SALT");
            String saltedPasswordDB = saltDB + passwordDB;
            String passwordParamHashed = Util.generatePasswordHashWithSalt(password, saltDB);

            if (passwordParamHashed.equals(saltedPasswordDB)) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    public ComboPooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

}
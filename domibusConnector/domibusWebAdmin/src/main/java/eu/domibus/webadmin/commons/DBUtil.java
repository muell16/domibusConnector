package eu.domibus.webadmin.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBUtil {

    private DataSource connectorDatasource;
    private String connectorErrorMessage;

    public boolean testConnectorDbConnection() {
        try {
            Connection conn = connectorDatasource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select count(*) from DOMIBUS_WEBADMIN_USER");
            rs.next();
            conn.close();
            return true;
        } catch (SQLException e) {
            connectorErrorMessage = e.getMessage();
            return false;
        }
    }


    @Autowired
    public DBUtil(DataSource ds) {
    	this.connectorDatasource = ds;
    }

    public DataSource getConnectorDatasource() {
        return connectorDatasource;
    }

    public void setConnectorDatasource(DataSource connectorDatasource) {
        this.connectorDatasource = connectorDatasource;
    }


    public String getConnectorErrorMessage() {
        return connectorErrorMessage;
    }

    public void setConnectorErrorMessage(String connectorErrorMessage) {
        this.connectorErrorMessage = connectorErrorMessage;
    }


}

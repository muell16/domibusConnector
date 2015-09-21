package eu.ecodex.webadmin.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DBUtil {

    private DataSource connectorDatasource;
    private DataSource gatewayDatasource;
    private String connectorErrorMessage;
    private String gatewayErrorMessage;

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

    public boolean testGatewayDbConnection() {
        try {
            Connection conn = gatewayDatasource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select count(*) from TB_RECEIPT_TRACKING");
            rs.next();
            conn.close();
            return true;
        } catch (SQLException e) {
            gatewayErrorMessage = e.getMessage();
            return false;
        }
    }

    public DataSource getConnectorDatasource() {
        return connectorDatasource;
    }

    public void setConnectorDatasource(DataSource connectorDatasource) {
        this.connectorDatasource = connectorDatasource;
    }

    public DataSource getGatewayDatasource() {
        return gatewayDatasource;
    }

    public void setGatewayDatasource(DataSource gatewayDatasource) {
        this.gatewayDatasource = gatewayDatasource;
    }

    public String getConnectorErrorMessage() {
        return connectorErrorMessage;
    }

    public void setConnectorErrorMessage(String connectorErrorMessage) {
        this.connectorErrorMessage = connectorErrorMessage;
    }

    public String getGatewayErrorMessage() {
        return gatewayErrorMessage;
    }

    public void setGatewayErrorMessage(String gatewayErrorMessage) {
        this.gatewayErrorMessage = gatewayErrorMessage;
    }

}

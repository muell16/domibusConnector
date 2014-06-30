package eu.ecodex.webadmin.commons;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class WebAdminProperties extends JdbcDaoSupport implements Serializable {

    private static final long serialVersionUID = -1113080729567255182L;

    private String connectorDatabaseUrl;
    private String monitoringType;
    private String jmxServerAddress;
    private String jmxServerPort;
    private String restServerAddress;
    private String restServerPort;
    private String restWebContext;
    private String loadError;

    public void loadProperties() {
        String sql = "select * from ECODEX_WEBADMIN_PROPERTIES";
        try {
            List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
            for (Map row : rows) {

                String key = ((String) (row.get("PROPERTIES_KEY")));
                String value = ((String) (row.get("PROPERTIES_VALUE")));
                if ("monitoring.type".equals(key)) {
                    monitoringType = value;
                } else if ("jmx.server.address".equals(key)) {
                    jmxServerAddress = value;
                } else if ("jmx.server.port".equals(key)) {
                    jmxServerPort = value;
                } else if ("rest.server.address".equals(key)) {
                    restServerAddress = value;
                } else if ("rest.server.port".equals(key)) {
                    restServerPort = value;
                } else if ("rest.webcontext".equals(key)) {
                    restWebContext = value;
                }

            }
        } catch (DataAccessException e) {
            logger.error("Unable to load Webadmin Properties: " + e.getStackTrace());
            loadError = e.getMessage();
        }

    }

    public void saveProperty(String key, String value) {
        String sql = "update ECODEX_WEBADMIN_PROPERTIES SET PROPERTIES_VALUE = ? WHERE PROPERTIES_KEY = ?";
        getJdbcTemplate().update(sql, value, key);
    }

    public String getJmxServerAddress() {
        return jmxServerAddress;
    }

    public void setJmxServerAddress(String jmxServerAddress) {
        if (jmxServerAddress != null && jmxServerAddress.toLowerCase().equals("localhost")) {
            jmxServerAddress = "";
        }
        this.jmxServerAddress = jmxServerAddress;
    }

    public String getJmxServerPort() {
        return jmxServerPort;
    }

    public void setJmxServerPort(String jmxServerPort) {
        this.jmxServerPort = jmxServerPort;
    }

    public String getConnectorDatabaseUrl() {
        return connectorDatabaseUrl;
    }

    public void setConnectorDatabaseUrl(String connectorDatabaseUrl) {
        this.connectorDatabaseUrl = connectorDatabaseUrl;
    }

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getRestServerAddress() {
        return restServerAddress;
    }

    public void setRestServerAddress(String restServerAddress) {
        this.restServerAddress = restServerAddress;
    }

    public String getRestServerPort() {
        return restServerPort;
    }

    public void setRestServerPort(String restServerPort) {
        this.restServerPort = restServerPort;
    }

    public String getJmxConnectorString() {
        return "service:jmx:rmi:///jndi/rmi:/" + getJmxServerAddress() + "/:" + getJmxServerPort() + "/jmxrmi";
    }

    public String getRestConnectorString() {
        return "http://" + getRestServerAddress() + ":" + getRestServerPort() + "/" + restWebContext
                + "/services/rest/monitor/";
    }

    public String getRestWebContext() {
        return restWebContext;
    }

    public void setRestWebContext(String restWebContext) {
        this.restWebContext = restWebContext;
    }

    public String getLoadError() {
        return loadError;
    }

    public void setLoadError(String loadError) {
        this.loadError = loadError;
    }

}

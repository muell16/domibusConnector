package eu.ecodex.webadmin.commons;

public class WebAdminProperties {

    private String connectorDatabaseUrl;
    private String jmxServerAddress;
    private String jmxServerPort;

    public String getJmxServerAddress() {
        return jmxServerAddress;
    }

    public void setJmxServerAddress(String jmxServerAddress) {
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

}

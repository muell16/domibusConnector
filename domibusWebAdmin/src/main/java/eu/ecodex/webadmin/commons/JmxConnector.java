package eu.ecodex.webadmin.commons;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxConnector {

    private static MBeanServerConnection mbsc = null;

    public static MBeanServerConnection getJmxServerConnection(String jmxServerAddress, String jmxServerPort,
            boolean forceReconnect) throws IOException {
        JMXServiceURL url;
        if (forceReconnect || mbsc == null) {
            String connection = "service:jmx:rmi:///jndi/rmi:/" + jmxServerAddress + "/:" + jmxServerPort + "/jmxrmi";
            url = new JMXServiceURL(connection);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            mbsc = jmxc.getMBeanServerConnection();
            return mbsc;
        } else {
            return mbsc;
        }
    }

    public static MBeanServerConnection getJmxServerConnection() throws IOException {
        return mbsc;
    }

}

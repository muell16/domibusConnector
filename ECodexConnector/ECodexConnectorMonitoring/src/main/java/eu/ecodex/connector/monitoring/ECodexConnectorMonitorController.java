package eu.ecodex.connector.monitoring;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.XmlWebApplicationContext;

import eu.ecodex.connector.common.ECodexConnectorProperties;

public class ECodexConnectorMonitorController implements ApplicationContextAware, InitializingBean {

    private XmlWebApplicationContext ctx;
    private IECodexConnectorMonitor selectedConnectorMonitor;
    private ECodexConnectorProperties connectorProperties;

    private enum monitoring {
        JMX, REST, DB
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ctx = (XmlWebApplicationContext) context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String monitoringType = checkMonitoringType(connectorProperties.getMonitoringType());

        if (!ctx.containsBean("ecodexConnectorMonitor")) {

            // Load the according monitoring context based on properties set and
            // refresh the context
            String[] locationsMonitoring = new String[1];
            locationsMonitoring[0] = "classpath:spring/context/" + monitoringType.toLowerCase() + "-context.xml";
            String[] locationsCtx = ctx.getConfigLocations();
            ArrayList<String> result = new ArrayList<String>();
            Collections.addAll(result, locationsMonitoring);
            Collections.addAll(result, locationsCtx);
            String[] locationConfigNew = result.toArray(new String[result.size()]);
            ctx.setConfigLocations(locationConfigNew);
            ctx.refresh();

        }

    }

    private String checkMonitoringType(String monitoringType) {
        if (monitoringType == null || monitoringType.isEmpty()) {
            return monitoring.DB.toString();
        } else {
            for (monitoring m : monitoring.values()) {
                if (m.name().equals(monitoringType)) {
                    return m.toString();
                }
            }

            return monitoring.DB.toString();
        }
    }

    public ECodexConnectorProperties getConnectorProperties() {
        return connectorProperties;
    }

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public IECodexConnectorMonitor getSelectedConnectorMonitor() {
        return selectedConnectorMonitor;
    }

    public void setSelectedConnectorMonitor(IECodexConnectorMonitor selectedConnectorMonitor) {
        this.selectedConnectorMonitor = selectedConnectorMonitor;
    }

}

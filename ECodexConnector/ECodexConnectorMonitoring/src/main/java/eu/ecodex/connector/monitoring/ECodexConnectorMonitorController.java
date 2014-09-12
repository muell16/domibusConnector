package eu.ecodex.connector.monitoring;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.monitoring.db.ECodexConnectorMonitorDB;

public class ECodexConnectorMonitorController implements ApplicationContextAware, InitializingBean {

    private ApplicationContext ctx;
    private ECodexConnectorProperties connectorProperties;

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorMonitorController.class);

    private enum monitoring {
        JMX, REST, DB
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ctx = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        monitoring monitoringType = checkMonitoringType(connectorProperties.getMonitoringType());

        // the configured monitoring type decides which beans will be loaded
        // into the applicaiton context.

        if ((monitoringType.equals(monitoring.JMX) && !ctx.containsBean("ecodexConnectorMonitorJMX"))
                || (monitoringType.equals(monitoring.REST) && !ctx.containsBean("ecodexConnectorMonitorREST"))) {
            // if REST or JMX is configured and the corresponding beans are not
            // loaded into the context yet
            // the right configuration file will be loaded into the context and
            // the context re-loaded.
            try {
                XmlWebApplicationContext webctx = (XmlWebApplicationContext) ctx;

                String[] locationsMonitoring = new String[1];
                locationsMonitoring[0] = "classpath:spring/context/" + monitoringType.toString().toLowerCase()
                        + "-context.xml";
                String[] locationsCtx = webctx.getConfigLocations();
                ArrayList<String> result = new ArrayList<String>();
                Collections.addAll(result, locationsMonitoring);
                Collections.addAll(result, locationsCtx);
                String[] locationConfigNew = result.toArray(new String[result.size()]);
                webctx.setConfigLocations(locationConfigNew);
                webctx.refresh();
            } catch (ClassCastException cce) {
                LOGGER.error("ApplicationContext not compatible with XmlWebApplicationContext. Monitoring stays DB!",
                        cce);
                connectorProperties.setMonitoringType(monitoring.DB.toString());
                monitoringType = monitoring.DB;
            }

        }

        if (monitoringType.equals(monitoring.DB) && !ctx.containsBean("ecodexConnectorMonitorDB")) {
            // if DB is configured (or the context is not a web context) the
            // right bean will be loaded manually into the context
            AbstractRefreshableApplicationContext refctx = (AbstractRefreshableApplicationContext) ctx;

            // Creating and registering bean to the container
            BeanDefinition beanDefinition = new RootBeanDefinition(ECodexConnectorMonitorDB.class);
            ECodexConnectorMonitor monitor = (ECodexConnectorMonitor) refctx.getBean("ecodexConnectorMonitor");

            beanDefinition.setAttribute("ecodexConnectorMonitor", monitor);

            BeanDefinitionRegistry factory = (BeanDefinitionRegistry) refctx.getBeanFactory();
            factory.registerBeanDefinition("ecodexConnectorMonitorDB", beanDefinition);
        }

    }

    private monitoring checkMonitoringType(String monitoringType) {
        if (monitoringType == null || monitoringType.isEmpty()) {
            return monitoring.DB;
        } else {
            for (monitoring m : monitoring.values()) {
                if (m.name().equals(monitoringType)) {
                    return m;
                }
            }

            return monitoring.DB;
        }
    }

    public ECodexConnectorProperties getConnectorProperties() {
        return connectorProperties;
    }

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

}

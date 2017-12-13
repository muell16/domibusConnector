package eu.domibus.connector.monitoring;

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

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.monitoring.db.DomibusConnectorMonitorDB;

public class DomibusConnectorMonitorController implements ApplicationContextAware, InitializingBean {

    private ApplicationContext ctx;
    private CommonConnectorProperties connectorProperties;

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorMonitorController.class);

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

        if ((monitoringType.equals(monitoring.JMX) && !ctx.containsBean("domibusConnectorMonitorJMX"))
                || (monitoringType.equals(monitoring.REST) && !ctx.containsBean("domibusConnectorMonitorREST"))) {
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

        if (monitoringType.equals(monitoring.DB) && !ctx.containsBean("domibusConnectorMonitorDB")) {
            // if DB is configured (or the context is not a web context) the
            // right bean will be loaded manually into the context
            try {
                AbstractRefreshableApplicationContext refctx = (AbstractRefreshableApplicationContext) ctx;

                // Creating and registering bean to the container
                BeanDefinition beanDefinition = new RootBeanDefinition(DomibusConnectorMonitorDB.class);
                DomibusConnectorMonitor monitor = (DomibusConnectorMonitor) refctx.getBean("domibusConnectorMonitor");

                beanDefinition.setAttribute("domibusConnectorMonitor", monitor);

                BeanDefinitionRegistry factory = (BeanDefinitionRegistry) refctx.getBeanFactory();
                factory.registerBeanDefinition("domibusConnectorMonitorDB", beanDefinition);
            } catch (ClassCastException cce) {
                LOGGER.error("Application context not compatible with AbstractRefreshableApplicationContext. Monitoring disabled!", cce);
            }
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

    public CommonConnectorProperties getConnectorProperties() {
        return connectorProperties;
    }

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

}

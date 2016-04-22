package eu.domibus.connector.runnable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import eu.domibus.connector.gui.config.DomibusConnectorConfigUI;

public class DomibusConnector {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String connectorProperties = System.getProperty("connector.properties");
        if (!StringUtils.hasText(connectorProperties)) {
            connectorProperties = System.getenv("connector.properties");
        }

        String loggingProperties = System.getProperty("logging.properties");
        if (!StringUtils.hasText(loggingProperties)) {
            loggingProperties = System.getenv("logging.properties");
        }

        if (!StringUtils.hasText(connectorProperties)) {
        	new DomibusConnectorConfigUI();
            throw new RuntimeException(
                    "No connector properties set! Please use the arg -Dconnector.properties='path to the connector properties file'");
        }

        if (!StringUtils.hasText(loggingProperties)) {
            System.setProperty("logging.properties", "classpath:log4j.properties");
        }

        @SuppressWarnings("unused")
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/context/DomibusConnectorRunnableContext.xml");

    }

}

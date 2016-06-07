package eu.domibus.connector.runnable;

import java.io.File;
import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;

public class DomibusConnector {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String connectorProperties = System.getProperty("connector.properties");
        if (!StringUtils.hasText(connectorProperties)) {
            connectorProperties = System.getenv("connector.properties");
        }
        if (!StringUtils.hasText(connectorProperties)) {
            connectorProperties = ConnectorProperties.CONNECTOR_PROPERTIES_FILE_PATH;
        }
        if(StringUtils.hasText(connectorProperties)){
        	System.setProperty("connector.properties", connectorProperties);
        }
        ConnectorProperties.loadConnectorProperties();
        if (!ConnectorProperties.CONNECTOR_PROPERTIES_FILE.exists()) {
//        	DomibusConnectorConfigurator.main(new String[]{});
        	
        	try {
				Process process = new ProcessBuilder(
						"java", "-jar","domibusConnectorConfigurator.jar").start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
//        	Runtime runTime = Runtime.getRuntime();
//			try {
//				Process process = runTime.exec("java -classpath eu.domibus.connector.gui.DomibusConnectorConfigurator");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	new DomibusConnectorConfigUI();
        	System.exit(0);
        }
        
        
        if(!ConnectorProperties.CONNECTOR_PROPERTIES_FILE.exists()){
			ConnectorProperties.loadConnectorProperties();
		}

        String loggingProperties = System.getProperty("logging.properties");
        if (!StringUtils.hasText(loggingProperties)) {
            loggingProperties = System.getenv("logging.properties");
        }


        if (!StringUtils.hasText(loggingProperties)) {
        	DomibusConnector.class.getResource("log4j.properties");
        	File classpathLog4j = new File(DomibusConnector.class.getClassLoader().getResource("log4j.properties").getFile());
        	if(classpathLog4j.exists())
            System.setProperty("logging.properties", classpathLog4j.getAbsolutePath());
        	else{
        		System.setProperty("logging.properties", ConnectorProperties.LOG4J_CONFIG_FILE_PATH);
        	}
        		
        }

        @SuppressWarnings({ "resource" })
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/context/DomibusConnectorRunnableContext.xml");
        
        context.registerShutdownHook();
    }

}

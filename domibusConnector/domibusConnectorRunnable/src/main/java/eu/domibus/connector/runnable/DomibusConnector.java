package eu.domibus.connector.runnable;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;

public class DomibusConnector {

    /**
     * @param args
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) {

    	boolean startWithGUI = false;
    	if(ArrayUtils.contains(args, "-gui")){
    		startWithGUI=true;
    	}
    	
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
        try{
        	ConnectorProperties.loadConnectorProperties();
        }catch(Exception e){
        	
        }
        if (!ConnectorProperties.CONNECTOR_PROPERTIES_FILE.exists() && startWithGUI) {
        	
        	try {
				Process process = new ProcessBuilder(
						"java", "-jar","domibusConnectorConfigurator.jar").start();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	System.exit(0);
        }
        
        
        if(!ConnectorProperties.CONNECTOR_PROPERTIES_FILE.exists()){
        	try{
                ConnectorProperties.loadConnectorProperties();
                }catch(Exception e){
                	throw new RuntimeException("Connector Properties could not be loaded!");
                }
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

        AbstractApplicationContext context = null;
        
        if(startWithGUI){
        	System.out.println("Start Connector with GUI.");
        	context = new ClassPathXmlApplicationContext(
        			"classpath:spring/context/DomibusConnectorRunnableWithGUI.xml");
        }else{
        	System.out.println("Start Connector without GUI");
        	context = new ClassPathXmlApplicationContext(
        			"classpath:spring/context/DomibusConnectorRunnableContext.xml");
        	
        }
        
        context.registerShutdownHook();
    }

}

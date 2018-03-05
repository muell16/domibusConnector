package eu.domibus.connector.test;

import eu.domibus.connector.starter.ConnectorStarter;
import org.junit.BeforeClass;
import org.springframework.context.ConfigurableApplicationContext;

public class ConnectorBackendToGwITCase {

    private static ConfigurableApplicationContext CONNECTOR_APPLICATION_CONTEXT;

    @BeforeClass
    public static void beforeClass() {
        String[] args = new String[] {};
        CONNECTOR_APPLICATION_CONTEXT = ConnectorStarter.runSpringApplication(args);


        //TODO: start clients....

        //TODO: start gw....


    }



}

package eu.domibus.connector.test;

import eu.domibus.connector.starter.ConnectorStarter;
import org.junit.BeforeClass;
import org.springframework.context.ConfigurableApplicationContext;
import test.eu.domibus.connector.gateway.link.testgw.TestGW;

public class ConnectorBackendToGwITCase {

    private static ConfigurableApplicationContext CONNECTOR_APPLICATION_CONTEXT;

    @BeforeClass
    public static void beforeClass() {
        String[] args = new String[] {};
        CONNECTOR_APPLICATION_CONTEXT = ConnectorStarter.runSpringApplication(args);

        //TODO: start clients....



        //TODO: start gw....
        TEST_GW_APPLICATION_CONTEXT = TestGW.startContext(new String[]{
                "server.port=" + portTestGW,
                "connector.delivery.endpoint.address=" + GW_LINK_SERVER_ADDRESS,
        });

    }



}

package eu.domibus.connector.test;

import eu.domibus.connector.starter.ConnectorStarter;
import org.junit.BeforeClass;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;
import test.eu.domibus.connector.gateway.link.testgw.TestGW;

public class ConnectorBackendToGwITCase {

    private static ConfigurableApplicationContext CONNECTOR_APPLICATION_CONTEXT;

    private static ConfigurableApplicationContext TEST_GW_APPLICATION_CONTEXT;

    private static ConfigurableApplicationContext BACKEND_BOB_APPLICATION_CONTEXT;

    private static ConfigurableApplicationContext BACKEND_ALICE_APPLICATION_CONTEXT;


    private static String CONNECTOR_GWLINK_SERVER_ADDRESS;
    private static String TESTGW_SERVER_ADDRESS;

    @BeforeClass
    public static void beforeClass() {

        int portTestGW = SocketUtils.findAvailableTcpPort();
        int portConnector = SocketUtils.findAvailableTcpPort();

        //TODO: configure server addresses

        //TODO: configure backend addresses

        //TODO: prepare testdata....
        String[] args = new String[] {
                "--server.port=" + portConnector,

        };
        CONNECTOR_APPLICATION_CONTEXT = ConnectorStarter.runSpringApplication(args);

        //TODO: start clients....
        //bob
        //alice



        //TODO: start gw....
        TEST_GW_APPLICATION_CONTEXT = TestGW.startContext(new String[]{
                "server.port=" + portTestGW,
                "connector.delivery.endpoint.address=" + CONNECTOR_GWLINK_SERVER_ADDRESS,
        });

    }



}

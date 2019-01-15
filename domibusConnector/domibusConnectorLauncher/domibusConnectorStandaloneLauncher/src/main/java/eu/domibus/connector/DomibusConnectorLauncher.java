package eu.domibus.connector;

import eu.domibus.connector.web.VaadinWebConnectorStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
public class DomibusConnectorLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorLauncher.class);

	public static void main(String[] args) {
        VaadinWebConnectorStarter.main(args); //just call the Vaadin Starter...
    }

}

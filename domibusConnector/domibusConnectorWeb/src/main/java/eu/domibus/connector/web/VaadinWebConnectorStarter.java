package eu.domibus.connector.web;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import eu.domibus.connector.starter.DomibusConnectorStarter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
public class VaadinWebConnectorStarter {

	public static void main(String[] args) {
        //just call the DomibusConnectorStarter...
	    DomibusConnectorStarter.runSpringApplication(args);
    }

}

package eu.domibus.connector.web.viewAreas.configuration;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class EnvironmentConfiguration extends VerticalLayout{

	/**
	 * # The connector is using this webservice address to submit messages to the gateway
		connector.gatewaylink.ws.submissionEndpointAddress=http://127.0.0.1:8080/domibus/services/domibusConnectorSubmissionWebservice

		# Service and action entered here according to the p-modes used.
# Enabled and used messages received with that combination will not be sent to the backend of the connector, 
# but will be answered with evidences after processed in the connector.
connector.test.service=testService1
connector.test.action=tc2Action

# If the connector should use a http proxy for loading the trusted lists  you have to configure
# the proxy values here:
#
#HTTP proxy settings
http.proxy.enabled=true
http.proxy.host=172.30.9.12
http.proxy.port=8080
http.proxy.user=
http.proxy.password=

#HTTPS proxy settings
https.proxy.enabled=true
https.proxy.host=172.30.9.12
https.proxy.port=8080
https.proxy.user=
https.proxy.password=

# Application defined datasource:

# Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# Login username of the database.
spring.datasource.username=tbckenddb

# JDBC URL of the database.
spring.datasource.url=jdbc:oracle:thin:@sjusee:1521:sjusee

# Login password of the database.
spring.datasource.password=tbckenddb

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle10gDialect
	 * 
	 */
	public EnvironmentConfiguration() {
		// connector.gatewaylink.ws.submissionEndpointAddress
		// connector.test.service=testService1
		// connector.test.action=tc2Action
		// #HTTP proxy settings
		// #HTTPS proxy settings
		// Database settings
	}

}

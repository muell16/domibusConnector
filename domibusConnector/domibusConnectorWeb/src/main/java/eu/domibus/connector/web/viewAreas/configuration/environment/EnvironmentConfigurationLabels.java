package eu.domibus.connector.web.viewAreas.configuration.environment;

import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationLabel;

public class EnvironmentConfigurationLabels {

	public static final ConfigurationLabel gatewaySubmissionLinkLabels = new ConfigurationLabel(
			"Gateway submission webservice address",
			"connector.gatewaylink.ws.submissionEndpointAddress",
			"The connector is using this webservice address to submit messages to the gateway.",
			"Example: http://127.0.0.1:8080/domibus/services/domibusConnectorSubmissionWebservice"
			);
	
	public static final ConfigurationLabel connectorTestServiceLabels = new ConfigurationLabel(
			"Service for Connector 2 Connector Tests", 
			"connector.test.service", 
			"This configuration parameter identifies the Service from the PModes which is used to run Connector 2 Connector Tests. ",
			"If this Service in a certain combination with an Action is used when receiving a message, this message will then ",
			"not be transmitted to any backend, but answered with the corresponding evidences directly by the Connector."
			);
	
	public static final ConfigurationLabel connectorTestActionLabels = new ConfigurationLabel(
			"Action for Connector 2 Connector Tests", 
			"connector.test.action", 
			"This configuration parameter identifies the Action from the PModes which is used to run Connector 2 Connector Tests. ",
			"If this Action in a certain combination with a Service is used when receiving a message, this message will then ",
			"not be transmitted to any backend, but answered with the corresponding evidences directly by the Connector."
			);
	
	public static final ConfigurationLabel useHttpProxyLabels = new ConfigurationLabel(
			"Use an HTTP Proxy", 
			"http.proxy.enabled", 
			"This configuration parameter should be checked if your Connector needs to pass a Proxy server for http connections. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpProxyHostLabels = new ConfigurationLabel(
			"HTTP Proxy host", 
			"http.proxy.host", 
			"This configuration parameter should contain the network address of the proxy server. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpProxyPortLabels = new ConfigurationLabel(
			"HTTP Proxy port", 
			"http.proxy.port", 
			"This configuration parameter should contain the port number under which the proxy server can be reached. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpProxyUserLabels = new ConfigurationLabel(
			"HTTP Proxy user", 
			"http.proxy.user", 
			"This configuration parameter should contain the user, if the network proxy server needs authentification. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpProxyPasswordLabels = new ConfigurationLabel(
			"HTTP Proxy password", 
			"http.proxy.password", 
			"This configuration parameter should contain the password, if the network proxy server needs authentification. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel useHttpsProxyLabels = new ConfigurationLabel(
			"Use an HTTPS Proxy", 
			"https.proxy.enabled", 
			"This configuration parameter should be checked if your Connector needs to pass a Proxy server for https connections. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpsProxyHostLabels = new ConfigurationLabel(
			"HTTPS Proxy host", 
			"https.proxy.host", 
			"This configuration parameter should contain the network address of the proxy server. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpsProxyPortLabels = new ConfigurationLabel(
			"HTTPS Proxy port", 
			"https.proxy.port", 
			"This configuration parameter should contain the port number under which the proxy server can be reached. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpsProxyUserLabels = new ConfigurationLabel(
			"HTTPS Proxy user", 
			"https.proxy.user", 
			"This configuration parameter should contain the user, if the network proxy server needs authentification. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel httpsProxyPasswordLabels = new ConfigurationLabel(
			"HTTPS Proxy password", 
			"https.proxy.password", 
			"This configuration parameter should contain the password, if the network proxy server needs authentification. ",
			"This is network environment specific. "
			);
	
	public static final ConfigurationLabel databaseConnectionStringLabels = new ConfigurationLabel(
			"Database connection string (url)", 
			"spring.datasource.url", 
			"JDBC URL of the database.. ",
			"Example MySQL: jdbc:mysql://<database.serverName>:<database.port>/<database.schema.name> ",
			"Example Oracle: jdbc:oracle:thin:@<database.serverName>:<database.port>/<database.oracle.sid> "
			);
	
	public static final ConfigurationLabel databaseUserLabels = new ConfigurationLabel(
			"Database Username", 
			"spring.datasource.username", 
			"Login username to the database. "
			);
	
	public static final ConfigurationLabel databasePasswordLabels = new ConfigurationLabel(
			"Database Password", 
			"spring.datasource.password", 
			"Login password to the database. "
			);
	
	public static final ConfigurationLabel databaseDriverClassNameLabels = new ConfigurationLabel(
			"JDBC Driver Class Name", 
			"spring.datasource.driver-class-name", 
			"Fully qualified name of the JDBC driver. Auto-detected based on the URL by default. ",
			"Example MySQL: com.mysql.jdbc.Driver ",
			"Example Oracle: oracle.jdbc.OracleDriver "
			);
	
	public static final ConfigurationLabel databaseDialectLabels = new ConfigurationLabel(
			"Hibernate Dialect", 
			"spring.jpa.properties.hibernate.dialect", 
			"The Dialect class hibernate needs to interprete queries correctly. ",
			"Example MySQL: org.hibernate.dialect.MySQL5InnoDBDialect ",
			"Example Oracle: org.hibernate.dialect.Oracle10gDialect "
			);

}

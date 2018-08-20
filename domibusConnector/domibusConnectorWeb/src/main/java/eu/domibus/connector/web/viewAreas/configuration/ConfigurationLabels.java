package eu.domibus.connector.web.viewAreas.configuration;

public class ConfigurationLabels {

	public static final ConfigurationLabel gatewaySubmissionLinkLabels = new ConfigurationLabel(
			"Gateway submission webservice address",
			"connector.gatewaylink.ws.submissionEndpointAddress",
			"The connector is using this webservice address to submit messages to the gateway.",
			"Example: http://127.0.0.1:8080/domibus/services/domibusConnectorSubmissionWebservice"
			);
	
	public static class ConfigurationLabel{
		public final String CONFIGURATION_ELEMENT_LABEL;
		public final String PROPERTY_NAME_LABEL;
		public final String[] INFO_LABEL;
		
		public ConfigurationLabel(String tfLabel, String prpNameLabel, String... infoLabel) {
			CONFIGURATION_ELEMENT_LABEL = tfLabel;
			PROPERTY_NAME_LABEL = prpNameLabel;
			INFO_LABEL = infoLabel;
		}
	}
	
}

package eu.domibus.connector.web.areas.configuration.backend;

import eu.domibus.connector.web.areas.configuration.util.ConfigurationLabel;

public class BackendClientInfoLabels {

	public static final ConfigurationLabel backendClientInfoNameLabels = new ConfigurationLabel(
			"Backend Name",
			"DatabaseTableField DOMIBUS_CONNECTOR_BACKEND_INFO.BACKEND_NAME",
			"The name of this backend. This can only be set when the Backend is created and is not editable afterwards. "
			);
	
	public static final ConfigurationLabel backendClientInfoKeyAliasLabels = new ConfigurationLabel(
			"Backend Key Alias",
			"DatabaseTableField DOMIBUS_CONNECTOR_BACKEND_INFO.BACKEND_KEY_ALIAS",
			"The alias of the public certificate of this backend. The certificate must be part of the truststore (see configuration \"Truststore\"). ",
			"The same Alias as within the Truststore must be entered here!"
			);
	
	public static final ConfigurationLabel backendClientInfoDefaultLabels = new ConfigurationLabel(
			"is Default Backend",
			"DatabaseTableField DOMIBUS_CONNECTOR_BACKEND_INFO.BACKEND_DEFAULT",
			"Marks this backend as the default backend. If a message is received and the Service cannot be resolved to a backend, this backend will be chosen. ",
			"As only one backend can be default, all other backends will be unset as default if set."
			);
	
	public static final ConfigurationLabel backendClientInfoEnabledLabels = new ConfigurationLabel(
			"is Backend enabled",
			"DatabaseTableField DOMIBUS_CONNECTOR_BACKEND_INFO.BACKEND_ENABLED",
			"Marks this backend as enabled. If a message is received only enabled backend(s) can be chosen. "
			);
	
	public static final ConfigurationLabel backendClientInfoPushAddressLabels = new ConfigurationLabel(
			"Push Address",
			"DatabaseTableField DOMIBUS_CONNECTOR_BACKEND_INFO.BACKEND_PUSH_ADDRESS",
			"Configures a backend as an active push service backend. ",
			"If a message is received for this backend it is not queued until retrieved, but sent to this service address immediately. "
			);
	
	public static final ConfigurationLabel backendClientInfoDescriptionLabels = new ConfigurationLabel(
			"Description",
			"DatabaseTableField DOMIBUS_CONNECTOR_BACKEND_INFO.BACKEND_DESCRIPTION",
			"Free text field to describe the backend. "
			);
	
	public static final ConfigurationLabel backendClientInfoServicesLabels = new ConfigurationLabel(
			"Backend to Service connection",
			"DatabaseTable DOMIBUS_CONNECTOR_BACK_2_S",
			"This connects backend(s) to Services. If a message is received, the Service of the message may decide what backend the message is sent to. ",
			"Every Service can only be connected to one backend. But each backend can receive multiple Service Messages."
			);
}

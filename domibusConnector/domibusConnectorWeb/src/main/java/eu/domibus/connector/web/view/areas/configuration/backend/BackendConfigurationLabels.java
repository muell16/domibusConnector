package eu.domibus.connector.web.view.areas.configuration.backend;


import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationLabel;

/**
 * 
 * @author riederb
 *
 *	connector.backend.ws.key.key-store.path
	connector.backend.ws.key.key-store.password
	connector.backend.ws.key.private-key.alias
	connector.backend.ws.key.private-key.password
	
	connector.backend.ws.trust.trust-store.path
	connector.backend.ws.trust.trust-store.password
 *
 */
public class BackendConfigurationLabels {

	public static final ConfigurationLabel backendKeyStorePathLabels = new ConfigurationLabel(
			"Signing Keystore",
			"connector.backend.ws.key.key-store.path",
			"The File-Path to the keystore holding the certificate with which the connector signs and decrypts messages from/to the backend(s). ",
			"The path ideally should be absolute and with a \"file:\" prefix. Also \"\\\" should be replaced by \"/\" or \"\\\\\"",
			"Example: file:C:/<anyPath>/connector.jks"
			);
	
	public static final ConfigurationLabel backendKeyStorePasswordLabels = new ConfigurationLabel(
			"Signing Keystore Password",
			"connector.backend.ws.key.key-store.password",
			"The Password of the keystore. "
			);
	
	public static final ConfigurationLabel backendKeyAliasLabels = new ConfigurationLabel(
			"Signing Key Alias",
			"connector.backend.ws.key.private-key.alias",
			"The alias of the private key with which the connector signs and decrypts messages from/to the backend(s). "
			);

	public static final ConfigurationLabel backendKeyPasswordLabels = new ConfigurationLabel(
			"Signing Key Password",
			"connector.backend.ws.key.private-key.password",
			"The Password of the private key. "
			);
	
	public static final ConfigurationLabel backendTrustStorePathLabels = new ConfigurationLabel(
			"Truststore",
			"connector.backend.ws.trust.trust-store.path",
			"The File-Path to the truststore holding all public certificates of all the backends. When receiving a message from a backend, ",
			"the connector validates the signature of it. Also, when sending a message to a backend, the connector encrypts the message with ",
			"the public key of this backend.",
			"The path ideally should be absolute and with a \"file:\" prefix. Also \"\\\" should be replaced by \"/\" or \"\\\\\"",
			"Example: file:C:/<anyPath>/connector.jks"
			);
	
	public static final ConfigurationLabel backendTrustStorePasswordLabels = new ConfigurationLabel(
			"Truststore Password",
			"connector.backend.ws.trust.trust-store.password",
			"The Password of the truststore. "
			);

}

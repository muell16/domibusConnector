package eu.domibus.connector.gui.config.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import eu.domibus.connector.gui.config.properties.Messages;

public class ConnectorProperties {

	
	public static String CONNECTOR_PROPERTIES_DIR_PATH = System.getProperty("user.dir") + File.separator + "conf";
	public static File CONNECTOR_PROPERTIES_DIR = new File(CONNECTOR_PROPERTIES_DIR_PATH);
	public static String CONNECTOR_PROPERTIES_FILE_PATH = CONNECTOR_PROPERTIES_DIR + File.separator + "connector.properties";
	public static File CONNECTOR_PROPERTIES_FILE = new File(CONNECTOR_PROPERTIES_FILE_PATH);
	
	public static String LOG4J_CONFIG_FILE_PATH = CONNECTOR_PROPERTIES_DIR + File.separator + "log4j.properties";
	
	public static final String DATABASE_DIALECT_KEY = Messages.getString("connector.database.dialect.key");
	public static final String DATABASE_DIALECT_LABEL = Messages.getString("connector.database.dialect.label");
	public static final String DATABASE_DRIVERCLASSNAME_KEY = Messages.getString("connector.database.driverClassName.key");
	public static final String DATABASE_DRIVERCLASSNAME_LABEL = Messages.getString("connector.database.driverClassName.label");
	public static final String DATABASE_URL_KEY = Messages.getString("connector.database.url.key");
	public static final String DATABASE_URL_LABEL = Messages.getString("connector.database.url.label");
	public static final String DATABASE_USERNAME_KEY = Messages.getString("connector.database.username.key");
	public static final String DATABASE_USERNAME_LABEL = Messages.getString("connector.database.username.label");
	public static final String DATABASE_PASSWORD_KEY = Messages.getString("connector.database.password.key");
	public static final String DATABASE_PASSWORD_LABEL = Messages.getString("connector.database.password.label");
	
	public static final String C3P0_ACQUIRE_INCREMENT_KEY = Messages.getString("c3p0.acquireIncrement.key");
	public static final String C3P0_ACQUIRE_INCREMENT_LABEL = Messages.getString("c3p0.acquireIncrement.label");
	public static final String C3P0_MIN_POOL_SIZE_KEY = Messages.getString("c3p0.minPoolSize.key");
	public static final String C3P0_MIN_POOL_SIZE_LABEL = Messages.getString("c3p0.minPoolSize.label");
	public static final String C3P0_MAX_POOL_SIZE_KEY = Messages.getString("c3p0.maxPoolSize.key");
	public static final String C3P0_MAX_POOL_SIZE_LABEL = Messages.getString("c3p0.maxPoolSize.label");
	public static final String C3P0_MAX_IDLE_TIME_KEY = Messages.getString("c3p0.maxIdleTime.key");
	public static final String C3P0_MAX_IDLE_TIME_LABEL = Messages.getString("c3p0.maxIdleTime.label");
	
	public static final String STORE_EVIDENCES_KEYSTORE_PATH_KEY = Messages.getString("connector.evidences.keystore.path.key");
	public static final String STORE_EVIDENCES_KEYSTORE_PATH_LABEL = Messages.getString("connector.evidences.keystore.path.label");
	public static final String STORE_EVIDENCES_KEYSTORE_PW_KEY = Messages.getString("connector.evidences.keystore.password.key");
	public static final String STORE_EVIDENCES_KEYSTORE_PW_LABEL = Messages.getString("connector.evidences.keystore.password.label");
	public static final String STORE_EVIDENCES_KEY_ALIAS_KEY = Messages.getString("connector.evidences.key.alias.key");
	public static final String STORE_EVIDENCES_KEY_ALIAS_LABEL = Messages.getString("connector.evidences.key.alias.label");
	public static final String STORE_EVIDENCES_KEY_PW_KEY = Messages.getString("connector.evidences.key.password.key");
	public static final String STORE_EVIDENCES_KEY_PW_LABEL = Messages.getString("connector.evidences.key.password.label");
	
	public static final String STORE_SECURITY_KEYSTORE_PATH_KEY = Messages.getString("connector.security.keystore.path.key");
	public static final String STORE_SECURITY_KEYSTORE_PATH_LABEL = Messages.getString("connector.security.keystore.path.label");
	public static final String STORE_SECURITY_KEYSTORE_PW_KEY = Messages.getString("connector.security.keystore.password.key");
	public static final String STORE_SECURITY_KEYSTORE_PW_LABEL = Messages.getString("connector.security.keystore.password.label");
	public static final String STORE_SECURITY_KEY_ALIAS_KEY = Messages.getString("connector.security.key.alias.key");
	public static final String STORE_SECURITY_KEY_ALIAS_LABEL = Messages.getString("connector.security.key.alias.label");
	public static final String STORE_SECURITY_KEY_PW_KEY = Messages.getString("connector.security.key.password.key");
	public static final String STORE_SECURITY_KEY_PW_LABEL = Messages.getString("connector.security.key.password.label");
	
	public static final String STORE_TRUSTSTORE_PATH_KEY = Messages.getString("java.truststore.path.key");
	public static final String STORE_TRUSTSTORE_PATH_LABEL = Messages.getString("java.truststore.path.label");
	public static final String STORE_TRUSTSTORE_PW_KEY = Messages.getString("java.truststore.password.key");
	public static final String STORE_TRUSTSTORE_PW_LABEL = Messages.getString("java.truststore.password.label");
	
	public static final String GATEWAY_ENDPOINT_KEY = Messages.getString("gateway.endpoint.address.key");
	public static final String GATEWAY_ENDPOINT_LABEL = Messages.getString("gateway.endpoint.address.label");
	public static final String GATEWAY_NAME_KEY = Messages.getString("gateway.name.key");
	public static final String GATEWAY_NAME_LABEL = Messages.getString("gateway.name.label");
	public static final String GATEWAY_ROLE_KEY = Messages.getString("gateway.role.key");
	public static final String GATEWAY_ROLE_LABEL = Messages.getString("gateway.role.label");
	
	public static final String GENERAL_CHECK_MESSAGES_PERIOD_KEY = Messages.getString("check.messages.period.key");
	public static final String GENERAL_CHECK_MESSAGES_PERIOD_LABEL = Messages.getString("check.messages.period.label");
	
	public static final String PROXY_ACTIVE_KEY = Messages.getString("http.proxy.enabled.key");
	public static final String PROXY_ACTIVE_LABEL = Messages.getString("http.proxy.enabled.label");
	public static final String PROXY_HOST_KEY = Messages.getString("http.proxy.host.key");
	public static final String PROXY_HOST_LABEL = Messages.getString("http.proxy.host.label");
	public static final String PROXY_PORT_KEY = Messages.getString("http.proxy.port.key");
	public static final String PROXY_PORT_LABEL = Messages.getString("http.proxy.port.label");
	public static final String PROXY_USERNAME_KEY = Messages.getString("http.proxy.user.key");
	public static final String PROXY_USERNAME_LABEL = Messages.getString("http.proxy.user.label");
	public static final String PROXY_PASSWORD_KEY = Messages.getString("http.proxy.password.key");
	public static final String PROXY_PASSWORD_LABEL = Messages.getString("http.proxy.password.label");
	
	public static final String DYNAMIC_DISCOVERY_ACTIVE_KEY = Messages.getString("dynamic.discovery.active.key");
	public static final String DYNAMIC_DISCOVERY_ACTIVE_LABEL = Messages.getString("dynamic.discovery.active.label");
	public static final String DYNAMIC_DISCOVERY_SML_RESOLVER_KEY = Messages.getString("dynamic.discovery.sml.key");
	public static final String DYNAMIC_DISCOVERY_SML_RESOLVER_LABEL = Messages.getString("dynamic.discovery.sml.label");
	public static final String DYNAMIC_DISCOVERY_COMMUNITY_KEY = Messages.getString("dynamic.discovery.community.key");
	public static final String DYNAMIC_DISCOVERY_COMMUNITY_LABEL = Messages.getString("dynamic.discovery.community.label");
	public static final String DYNAMIC_DISCOVERY_ENVIRONMENT_KEY = Messages.getString("dynamic.discovery.environment.key");
	public static final String DYNAMIC_DISCOVERY_ENVIRONMENT_LABEL = Messages.getString("dynamic.discovery.environment.label");
	public static final String DYNAMIC_DISCOVERY_NORM_ALGORITHM_KEY = Messages.getString("dynamic.discovery.norm.algorithm.key");
	public static final String DYNAMIC_DISCOVERY_NORM_ALGORITHM_LABEL = Messages.getString("dynamic.discovery.norm.algorithm.label");
	
	public static final String SECURITY_ACTIVE_KEY = Messages.getString("security.active.key");
	public static final String SECURITY_ACTIVE_LABEL = Messages.getString("security.active.label");
	public static final String SECURITY_TOKEN_ISSUER_COUNTRY_KEY = Messages.getString("security.token.issuer.country.key");
	public static final String SECURITY_TOKEN_ISSUER_COUNTRY_LABEL = Messages.getString("security.token.issuer.country.label");
	public static final String SECURITY_TOKEN_ISSUER_PROVIDER_KEY = Messages.getString("security.token.issuer.service.provider.key");
	public static final String SECURITY_TOKEN_ISSUER_PROVIDER_LABEL = Messages.getString("security.token.issuer.service.provider.label");
	public static final String SECURITY_TOKEN_ISSUER_AES_KEY = Messages.getString("security.token.issuer.aes.key");
	public static final String SECURITY_TOKEN_ISSUER_AES_LABEL = Messages.getString("security.token.issuer.aes.label");
	public static final String SECURITY_IMPL_CLASSNAME_KEY = Messages.getString("security.impl.classname.key");
	public static final String SECURITY_IMPL_CLASSNAME_LABEL = Messages.getString("security.impl.classname.label");
	
	public static final String EVIDENCES_TOOLKIT_ACTIVE_KEY = Messages.getString("evidences.toolkit.active.key");
	public static final String EVIDENCES_TOOLKIT_ACTIVE_LABEL = Messages.getString("evidences.toolkit.active.label");
	public static final String EVIDENCES_HASH_ALGORITHM_KEY = Messages.getString("evidences.hash.algorithm.key");
	public static final String EVIDENCES_HASH_ALGORITHM_LABEL = Messages.getString("evidences.hash.algorithm.label");
	public static final String EVIDENCES_TIMEOUT_ACTIVE_KEY = Messages.getString("evidences.timeout.active.key");
	public static final String EVIDENCES_TIMEOUT_ACTIVE_LABEL = Messages.getString("evidences.timeout.active.label");
	public static final String EVIDENCES_TIMEOUT_RELAYREMMD_KEY = Messages.getString("evidences.timeout.relayremmd.key");
	public static final String EVIDENCES_TIMEOUT_RELAYREMMD_LABEL = Messages.getString("evidences.timeout.relayremmd.label");
	public static final String EVIDENCES_TIMEOUT_DELIVERY_KEY = Messages.getString("evidences.timeout.delivery.key");
	public static final String EVIDENCES_TIMEOUT_DELIVERY_LABEL = Messages.getString("evidences.timeout.delivery.label");
	public static final String EVIDENCES_TIMEOUT_RETRIEVAL_KEY = Messages.getString("evidences.timeout.retrieval.key");
	public static final String EVIDENCES_TIMEOUT_RETRIEVAL_LABEL = Messages.getString("evidences.timeout.retrieval.label");
	public static final String EVIDENCES_ADDRESS_STREET_KEY = Messages.getString("evidences.address.street.key");
	public static final String EVIDENCES_ADDRESS_STREET_LABEL = Messages.getString("evidences.address.street.label");
	public static final String EVIDENCES_ADDRESS_LOCALITY_KEY = Messages.getString("evidences.address.locality.key");
	public static final String EVIDENCES_ADDRESS_LOCALITY_LABEL = Messages.getString("evidences.address.locality.label");
	public static final String EVIDENCES_ADDRESS_POSTAL_CODE_KEY = Messages.getString("evidences.address.postalcode.key");
	public static final String EVIDENCES_ADDRESS_POSTAL_CODE_LABEL = Messages.getString("evidences.address.postalcode.label");
	public static final String EVIDENCES_ADDRESS_COUNTRY_KEY = Messages.getString("evidences.address.country.key");
	public static final String EVIDENCES_ADDRESS_COUNTRY_LABEL = Messages.getString("evidences.address.country.label");
	
	public static final String OTHER_NBC_IMPL_CLASSNAME_KEY = Messages.getString("other.nbc.impl.classname.key");
	public static final String OTHER_NBC_IMPL_CLASSNAME_LABEL = Messages.getString("other.nbc.impl.classname.label");
	public static final String OTHER_CONTENT_MAPPER_ACTIVE_KEY = Messages.getString("other.content.mapper.active.key");
	public static final String OTHER_CONTENT_MAPPER_ACTIVE_LABEL = Messages.getString("other.content.mapper.active.label");
	public static final String OTHER_CONTENT_MAPPER_IMPL_CLASSNAME_KEY = Messages.getString("other.content.mapper.impl.classname.key");
	public static final String OTHER_CONTENT_MAPPER_IMPL_CLASSNAME_LABEL = Messages.getString("other.content.mapper.impl.classname.label");
	public static final String OTHER_CLUSTERED_ACTIVE_KEY = Messages.getString("other.clustered.active.key");
	public static final String OTHER_CLUSTERED_ACTIVE_LABEL = Messages.getString("other.clustered.active.label");
	public static final String OTHER_MONITORING_TYPE_KEY = Messages.getString("other.monitoring.type.key");
	public static final String OTHER_MONITORING_TYPE_LABEL = Messages.getString("other.monitoring.type.label");
	public static final String OTHER_INCOMING_MSG_DIR_KEY = Messages.getString("other.incoming.msg.dir.key");
	public static final String OTHER_INCOMING_MSG_DIR_LABEL = Messages.getString("other.incoming.msg.dir.label");
	public static final String OTHER_OUTGOING_MSG_DIR_KEY = Messages.getString("other.outgoing.msg.dir.key");
	public static final String OTHER_OUTGOING_MSG_DIR_LABEL = Messages.getString("other.outgoing.msg.dir.label");
	public static final String OTHER_MSG_PROPERTY_FILE_NAME_KEY = Messages.getString("other.msg.property.file.name.key");
	public static final String OTHER_MSG_PROPERTY_FILE_NAME_LABEL = Messages.getString("other.msg.property.file.name.label");
	
	public static String databaseDialectValue;
	public static String databaseDriverClassNameValue;
	public static String databaseUrlValue;
	public static String databaseUsernameValue;
	public static String databasePasswordValue;
	
	public static String c3p0acquireIncrementValue;
	public static String c3p0minPoolSizeValue;
	public static String c3p0maxPoolSizeValue;
	public static String c3p0maxIdleTimeValue;
	
	public static String evidencesKeystorePathValue;
	public static String evidencesKeystorePasswordValue;
	public static String evidencesKeyAliasValue;
	public static String evidencesKeyPasswordValue;
	
	public static String securityKeystorePathValue;
	public static String securityKeystorePasswordValue;
	public static String securityKeyAliasValue;
	public static String securityKeyPasswordValue;
	
	public static String truststorePathValue;
	public static String truststorePasswordValue;
	
	public static String gatewayEndpointValue;
	public static String gatewayNameValue;
	public static String gatewayRoleValue;
	
	public static String checkMessagesPeriod;

	public static boolean proxyEnabled;
	public static String proxyHost;
	public static String proxyPort;
	public static String proxyUser;
	public static String proxyPassword;
		
	public static boolean useDynamicDiscovery;
	public static String dynamicDiscoverySMLResolverAddress;
	public static String dynamicDiscoveryCommunity;
	public static String dynamicDiscoveryEnvironment;
	public static String dynamicDiscoveryNormalisationAlgorithm;

	public static boolean useSecurityToolkit;
	public static String tokenIssuerCountry;
	public static String tokenIssuerServiceProvider;
	public static String tokenIssuerAESValue;
	public static String securityToolkitImplementationClassName;

	public static boolean useEvidencesToolkit;
	public static String hashAlgorithm;
	public static boolean useEvidencesTimeout;
	public static String evidenceRelayREMMDTimeout;
	public static String evidenceDeliveryTimeout;
	public static String evidenceRetrievalTimeout;
	public static String postalAddressStreet;
	public static String postalAddressLocality;
	public static String postalAddressPostalCode;
	public static String postalAddressCountry;

	public static String nationalBackendClientImplementationClassName;
	public static boolean useContentMapper;
	public static String contentMapperImplementaitonClassName;
	public static boolean clustered;
	public static String monitoringType;
	
	public static String incomingMessagesDirectory;
	public static String outgoingMessagesDirectory;
	public static String messagePropertiesFileName = "message.properties";
	
	private final static Properties properties = new Properties();

    public static void loadConnectorProperties() throws Exception{
    	String connectorProperties = System.getProperty("connector.properties");
    	if(connectorProperties!=null && connectorProperties.length()>0){
    		CONNECTOR_PROPERTIES_DIR_PATH = connectorProperties.substring(0, connectorProperties.lastIndexOf(File.separator));
    		CONNECTOR_PROPERTIES_DIR = new File(CONNECTOR_PROPERTIES_DIR_PATH);
    		CONNECTOR_PROPERTIES_FILE_PATH = connectorProperties;
    		CONNECTOR_PROPERTIES_FILE = new File(CONNECTOR_PROPERTIES_FILE_PATH);
    		
    	}
    	
    	FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(CONNECTOR_PROPERTIES_FILE);
        } catch (FileNotFoundException e1) {
        	e1.printStackTrace();
        	throw e1;
        }
        try {
            properties.load(fileInputStream);
        } catch (Exception e1) {
        	e1.printStackTrace();
        	throw e1;
        }

        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        
        setPropertyValues();
        
    }
    
    public static boolean checkDatabaseSettings(){
    	return databaseDialectValue!=null && databaseDriverClassNameValue!=null 
    			&& databaseUrlValue!=null && databaseUsernameValue!=null 
    			&& databasePasswordValue!=null && c3p0acquireIncrementValue!=null
    			&& c3p0maxIdleTimeValue!=null && c3p0maxPoolSizeValue!=null
    			&& c3p0minPoolSizeValue!=null;
    }


	private static void setPropertyValues() {
		databaseDialectValue = properties.getProperty(DATABASE_DIALECT_KEY);
        databaseDriverClassNameValue = properties.getProperty(DATABASE_DRIVERCLASSNAME_KEY);
        databaseUrlValue = properties.getProperty(DATABASE_URL_KEY);
        databaseUsernameValue = properties.getProperty(DATABASE_USERNAME_KEY);
        databasePasswordValue = properties.getProperty(DATABASE_PASSWORD_KEY);
        
        c3p0acquireIncrementValue = properties.getProperty(C3P0_ACQUIRE_INCREMENT_KEY);
        c3p0minPoolSizeValue = properties.getProperty(C3P0_MIN_POOL_SIZE_KEY);
        c3p0maxPoolSizeValue = properties.getProperty(C3P0_MAX_POOL_SIZE_KEY);
        c3p0maxIdleTimeValue = properties.getProperty(C3P0_MAX_IDLE_TIME_KEY);
        
        evidencesKeystorePathValue = properties.getProperty(STORE_EVIDENCES_KEYSTORE_PATH_KEY);
        evidencesKeystorePasswordValue = properties.getProperty(STORE_EVIDENCES_KEYSTORE_PW_KEY);
        evidencesKeyAliasValue = properties.getProperty(STORE_EVIDENCES_KEY_ALIAS_KEY);
        evidencesKeyPasswordValue = properties.getProperty(STORE_EVIDENCES_KEY_PW_KEY);
        
        securityKeystorePathValue = properties.getProperty(STORE_SECURITY_KEYSTORE_PATH_KEY);
        securityKeystorePasswordValue = properties.getProperty(STORE_SECURITY_KEYSTORE_PW_KEY);
        securityKeyAliasValue = properties.getProperty(STORE_SECURITY_KEY_ALIAS_KEY);
        securityKeyPasswordValue = properties.getProperty(STORE_SECURITY_KEY_PW_KEY);
        
        truststorePathValue = properties.getProperty(STORE_TRUSTSTORE_PATH_KEY);
        truststorePasswordValue = properties.getProperty(STORE_TRUSTSTORE_PW_KEY);
        
        gatewayEndpointValue = properties.getProperty(GATEWAY_ENDPOINT_KEY);
        gatewayNameValue = properties.getProperty(GATEWAY_NAME_KEY);
        gatewayRoleValue = properties.getProperty(GATEWAY_ROLE_KEY);
        
        checkMessagesPeriod = properties.getProperty(GENERAL_CHECK_MESSAGES_PERIOD_KEY);
        
        proxyEnabled = Boolean.parseBoolean(properties.getProperty(PROXY_ACTIVE_KEY));
        proxyHost = properties.getProperty(PROXY_HOST_KEY);
        proxyPort = properties.getProperty(PROXY_PORT_KEY);
        proxyUser = properties.getProperty(PROXY_USERNAME_KEY);
        proxyPassword = properties.getProperty(PROXY_PASSWORD_KEY);
        
        useDynamicDiscovery = Boolean.parseBoolean(properties.getProperty(DYNAMIC_DISCOVERY_ACTIVE_KEY));
        dynamicDiscoverySMLResolverAddress = properties.getProperty(DYNAMIC_DISCOVERY_SML_RESOLVER_KEY);
        dynamicDiscoveryCommunity = properties.getProperty(DYNAMIC_DISCOVERY_COMMUNITY_KEY);
        dynamicDiscoveryEnvironment = properties.getProperty(DYNAMIC_DISCOVERY_ENVIRONMENT_KEY);
        dynamicDiscoveryNormalisationAlgorithm = properties.getProperty(DYNAMIC_DISCOVERY_NORM_ALGORITHM_KEY);
        
        useSecurityToolkit = Boolean.parseBoolean(properties.getProperty(SECURITY_ACTIVE_KEY));
        tokenIssuerCountry = properties.getProperty(SECURITY_TOKEN_ISSUER_COUNTRY_KEY);
        tokenIssuerServiceProvider = properties.getProperty(SECURITY_TOKEN_ISSUER_PROVIDER_KEY);
        tokenIssuerAESValue = properties.getProperty(SECURITY_TOKEN_ISSUER_AES_KEY);
        securityToolkitImplementationClassName = properties.getProperty(SECURITY_IMPL_CLASSNAME_KEY);
        
        useEvidencesToolkit = Boolean.parseBoolean(properties.getProperty(EVIDENCES_TOOLKIT_ACTIVE_KEY));
        hashAlgorithm = properties.getProperty(EVIDENCES_HASH_ALGORITHM_KEY);
        useEvidencesTimeout = Boolean.parseBoolean(properties.getProperty(EVIDENCES_TIMEOUT_ACTIVE_KEY));
        evidenceRelayREMMDTimeout = properties.getProperty(EVIDENCES_TIMEOUT_RELAYREMMD_KEY);
        evidenceDeliveryTimeout = properties.getProperty(EVIDENCES_TIMEOUT_DELIVERY_KEY);
        evidenceRetrievalTimeout = properties.getProperty(EVIDENCES_TIMEOUT_RETRIEVAL_KEY);
        postalAddressStreet = properties.getProperty(EVIDENCES_ADDRESS_STREET_KEY);
        postalAddressLocality = properties.getProperty(EVIDENCES_ADDRESS_LOCALITY_KEY);
        postalAddressPostalCode = properties.getProperty(EVIDENCES_ADDRESS_POSTAL_CODE_KEY);
        postalAddressCountry = properties.getProperty(EVIDENCES_ADDRESS_COUNTRY_KEY);
        
        nationalBackendClientImplementationClassName = properties.getProperty(OTHER_NBC_IMPL_CLASSNAME_KEY);
    	useContentMapper = Boolean.parseBoolean(properties.getProperty(OTHER_CONTENT_MAPPER_ACTIVE_KEY));
    	contentMapperImplementaitonClassName = properties.getProperty(OTHER_CONTENT_MAPPER_IMPL_CLASSNAME_KEY);
    	clustered = Boolean.parseBoolean(properties.getProperty(OTHER_CLUSTERED_ACTIVE_KEY));
    	monitoringType = properties.getProperty(OTHER_MONITORING_TYPE_KEY);
    	
    	incomingMessagesDirectory = properties.getProperty(OTHER_INCOMING_MSG_DIR_KEY)!=null&&!properties.getProperty(OTHER_INCOMING_MSG_DIR_KEY).isEmpty()?properties.getProperty(OTHER_INCOMING_MSG_DIR_KEY):null;
    	outgoingMessagesDirectory = properties.getProperty(OTHER_OUTGOING_MSG_DIR_KEY)!=null&&!properties.getProperty(OTHER_OUTGOING_MSG_DIR_KEY).isEmpty()?properties.getProperty(OTHER_OUTGOING_MSG_DIR_KEY):null;
    	messagePropertiesFileName = properties.getProperty(OTHER_MSG_PROPERTY_FILE_NAME_KEY)!=null&&!properties.getProperty(OTHER_MSG_PROPERTY_FILE_NAME_KEY).isEmpty()?properties.getProperty(OTHER_MSG_PROPERTY_FILE_NAME_KEY):messagePropertiesFileName;
	}
	
	private static void putPropertyValues() {
		properties.put(DATABASE_DIALECT_KEY, databaseDialectValue!=null?databaseDialectValue:"");
		properties.put(DATABASE_DRIVERCLASSNAME_KEY, databaseDriverClassNameValue!=null?databaseDriverClassNameValue:"");
		properties.put(DATABASE_URL_KEY, databaseUrlValue!=null?databaseUrlValue:"");
		properties.put(DATABASE_USERNAME_KEY, databaseUsernameValue!=null?databaseUsernameValue:"");
		properties.put(DATABASE_PASSWORD_KEY, databasePasswordValue!=null?databasePasswordValue:"");
		
		properties.put(C3P0_ACQUIRE_INCREMENT_KEY, c3p0acquireIncrementValue!=null?c3p0acquireIncrementValue:"");
		properties.put(C3P0_MIN_POOL_SIZE_KEY, c3p0minPoolSizeValue!=null?c3p0minPoolSizeValue:"");
		properties.put(C3P0_MAX_POOL_SIZE_KEY, c3p0maxPoolSizeValue!=null?c3p0maxPoolSizeValue:"");
		properties.put(C3P0_MAX_IDLE_TIME_KEY, c3p0maxIdleTimeValue!=null?c3p0maxIdleTimeValue:"");
		
		properties.put(STORE_EVIDENCES_KEYSTORE_PATH_KEY, evidencesKeystorePathValue!=null?evidencesKeystorePathValue:"");
		properties.put(STORE_EVIDENCES_KEYSTORE_PW_KEY, evidencesKeystorePasswordValue!=null?evidencesKeystorePasswordValue:"");
		properties.put(STORE_EVIDENCES_KEY_ALIAS_KEY, evidencesKeyAliasValue!=null?evidencesKeyAliasValue:"");
		properties.put(STORE_EVIDENCES_KEY_PW_KEY, evidencesKeyPasswordValue!=null?evidencesKeyPasswordValue:"");
		
		properties.put(STORE_SECURITY_KEYSTORE_PATH_KEY, securityKeystorePathValue!=null?securityKeystorePathValue:"");
		properties.put(STORE_SECURITY_KEYSTORE_PW_KEY, securityKeystorePasswordValue!=null?securityKeystorePasswordValue:"");
		properties.put(STORE_SECURITY_KEY_ALIAS_KEY, securityKeyAliasValue!=null?securityKeyAliasValue:"");
		properties.put(STORE_SECURITY_KEY_PW_KEY, securityKeyPasswordValue!=null?securityKeyPasswordValue:"");
		
		properties.put(STORE_TRUSTSTORE_PATH_KEY, truststorePathValue!=null?truststorePathValue:"");
		properties.put(STORE_TRUSTSTORE_PW_KEY, truststorePasswordValue!=null?truststorePasswordValue:"");
		
		properties.put(GATEWAY_ENDPOINT_KEY, gatewayEndpointValue!=null?gatewayEndpointValue:"");
		properties.put(GATEWAY_NAME_KEY, gatewayNameValue!=null?gatewayNameValue:"");
		properties.put(GATEWAY_ROLE_KEY, gatewayRoleValue!=null?gatewayRoleValue:"");
		
		properties.put(GENERAL_CHECK_MESSAGES_PERIOD_KEY, checkMessagesPeriod!=null?checkMessagesPeriod:"");
		
		properties.put(PROXY_ACTIVE_KEY, Boolean.toString(proxyEnabled));
		properties.put(PROXY_HOST_KEY, proxyHost!=null?proxyHost:"");
		properties.put(PROXY_PORT_KEY, proxyPort!=null?proxyPort:"");
		properties.put(PROXY_USERNAME_KEY, proxyUser!=null?proxyUser:"");
		properties.put(PROXY_PASSWORD_KEY, proxyPassword!=null?proxyPassword:"");
		
		properties.put(DYNAMIC_DISCOVERY_ACTIVE_KEY, Boolean.toString(useDynamicDiscovery));
		properties.put(DYNAMIC_DISCOVERY_SML_RESOLVER_KEY, dynamicDiscoverySMLResolverAddress!=null?dynamicDiscoverySMLResolverAddress:"");
		properties.put(DYNAMIC_DISCOVERY_COMMUNITY_KEY, dynamicDiscoveryCommunity!=null?dynamicDiscoveryCommunity:"");
		properties.put(DYNAMIC_DISCOVERY_ENVIRONMENT_KEY, dynamicDiscoveryEnvironment!=null?dynamicDiscoveryEnvironment:"");
		properties.put(DYNAMIC_DISCOVERY_NORM_ALGORITHM_KEY, dynamicDiscoveryNormalisationAlgorithm!=null?dynamicDiscoveryNormalisationAlgorithm:"");
		
		properties.put(SECURITY_ACTIVE_KEY, Boolean.toString(useSecurityToolkit));
		properties.put(SECURITY_TOKEN_ISSUER_COUNTRY_KEY, tokenIssuerCountry!=null?tokenIssuerCountry:"");
		properties.put(SECURITY_TOKEN_ISSUER_PROVIDER_KEY, tokenIssuerServiceProvider!=null?tokenIssuerServiceProvider:"");
		properties.put(SECURITY_TOKEN_ISSUER_AES_KEY, tokenIssuerAESValue!=null?tokenIssuerAESValue:"");
		properties.put(SECURITY_IMPL_CLASSNAME_KEY, securityToolkitImplementationClassName!=null?securityToolkitImplementationClassName:"");
		
		properties.put(EVIDENCES_TOOLKIT_ACTIVE_KEY, Boolean.toString(useEvidencesToolkit));
		properties.put(EVIDENCES_HASH_ALGORITHM_KEY, hashAlgorithm!=null?hashAlgorithm:"");
		properties.put(EVIDENCES_TIMEOUT_ACTIVE_KEY, Boolean.toString(useEvidencesTimeout));
		properties.put(EVIDENCES_TIMEOUT_RELAYREMMD_KEY, evidenceRelayREMMDTimeout!=null?evidenceRelayREMMDTimeout:"");
		properties.put(EVIDENCES_TIMEOUT_DELIVERY_KEY, evidenceDeliveryTimeout!=null?evidenceDeliveryTimeout:"");
		properties.put(EVIDENCES_TIMEOUT_RETRIEVAL_KEY, evidenceRetrievalTimeout!=null?evidenceRetrievalTimeout:"");
		properties.put(EVIDENCES_ADDRESS_STREET_KEY, postalAddressStreet!=null?postalAddressStreet:"");
		properties.put(EVIDENCES_ADDRESS_LOCALITY_KEY, postalAddressLocality!=null?postalAddressLocality:"");
		properties.put(EVIDENCES_ADDRESS_POSTAL_CODE_KEY, postalAddressPostalCode!=null?postalAddressPostalCode:"");
		properties.put(EVIDENCES_ADDRESS_COUNTRY_KEY, postalAddressCountry!=null?postalAddressCountry:"");
		
		properties.put(OTHER_NBC_IMPL_CLASSNAME_KEY, nationalBackendClientImplementationClassName!=null?nationalBackendClientImplementationClassName:"");
		properties.put(OTHER_CONTENT_MAPPER_ACTIVE_KEY, Boolean.toString(useContentMapper));
		properties.put(OTHER_CONTENT_MAPPER_IMPL_CLASSNAME_KEY, contentMapperImplementaitonClassName!=null?contentMapperImplementaitonClassName:"");
		properties.put(OTHER_CLUSTERED_ACTIVE_KEY, Boolean.toString(clustered));
		properties.put(OTHER_MONITORING_TYPE_KEY, monitoringType!=null?monitoringType:"");
		
		properties.put(OTHER_INCOMING_MSG_DIR_KEY, incomingMessagesDirectory!=null?incomingMessagesDirectory:"");
		properties.put(OTHER_OUTGOING_MSG_DIR_KEY, outgoingMessagesDirectory!=null?outgoingMessagesDirectory:"");
		properties.put(OTHER_MSG_PROPERTY_FILE_NAME_KEY, messagePropertiesFileName!=null?messagePropertiesFileName:"");
	}
	
	public static boolean storeConnectorProperties(){
		if(!CONNECTOR_PROPERTIES_DIR.exists()){
    		CONNECTOR_PROPERTIES_DIR.mkdirs();
    	}
        if (!CONNECTOR_PROPERTIES_FILE.exists()) {
            try {
            	CONNECTOR_PROPERTIES_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        putPropertyValues();
        Set<String> keys = properties.stringPropertyNames();
		
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(CONNECTOR_PROPERTIES_FILE));
			String line;String input = "";
			while ((line = file.readLine()) != null){
				if(line.contains("=")&&!line.startsWith("*")&&!line.startsWith("/")){
					String key = line.substring(0, line.indexOf("="));
					String value = properties.getProperty(key);
					input += key + "=" + value + '\n';
					keys.remove(key);
				}else{
					input += line + '\n';
				}
			}
			file.close();
			
			if(!keys.isEmpty()){
				for(String key:keys){
					String value = properties.getProperty(key);
					input += key + "=" + value + '\n';
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream(CONNECTOR_PROPERTIES_FILE);
	        fileOut.write(input.getBytes());
	        fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        
        return true;
	}

    public static boolean storeOld() {
    	if(!CONNECTOR_PROPERTIES_DIR.exists()){
    		CONNECTOR_PROPERTIES_DIR.mkdirs();
    	}
        if (!CONNECTOR_PROPERTIES_FILE.exists()) {
            try {
            	CONNECTOR_PROPERTIES_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(CONNECTOR_PROPERTIES_FILE);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return false;
        }
        
        putPropertyValues();

        try {
            properties.store(fos, null);
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        return true;
    }


}

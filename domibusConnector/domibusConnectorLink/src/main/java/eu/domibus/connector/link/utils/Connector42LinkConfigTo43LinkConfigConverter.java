package eu.domibus.connector.link.utils;

import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.link.impl.gwwspushplugin.childctx.WsGatewayPluginConfigurationProperties;
import eu.domibus.connector.link.impl.wsbackendplugin.childctx.WsBackendPluginConfigurationProperties;

import java.util.Map;
import java.util.Properties;

/**
 * This class will help, load old properties (e.g. Connector 4.2)
 * and convert it into new properties (eg. Connector 4.3)
 * This one will do it for Link Configuration
 *
 */
public class Connector42LinkConfigTo43LinkConfigConverter {

    public static final String GWL_GW_ADDRESS_OLD_PROP_NAME = "connector.gatewaylink.ws.submissionEndpointAddress";
    public static final String GWL_ENCRYPT_ALIAS_OLD_PROP_NAME = "connector.gatewaylink.ws.encrypt-alias";
    public static final String GWL_TRUST_STORE_PW_OLD_PROP_NAME = "connector.gatewaylink.ws.trust-store.password";
    public static final String GWL_TRUST_STORE_PATH_OLD_PROP_NAME = "connector.gatewaylink.ws.trust-store.path";
    public static final String GWL_KEY_STORE_PW_OLD_PROP_NAM = "connector.gatewaylink.ws.key-store.password";
    public static final String GWL_KEY_STORE_PATH_OLD_PROP_NAME = "connector.gatewaylink.ws.key-store.path";
    public static final String GWL_PRIVATE_KEY_ALIAS_OLD_PROP_NAME = "connector.gatewaylink.ws.private-key.alias";
    public static final String GWL_PRIVATE_KEY_PW_OLD_PROP_NAME = "connector.gatewaylink.ws.private-key.password";

    public static final String BACKEND_TRUST_STORE_PW_OLD_PROP_NAME = "connector.backend.ws.trust.trust-store.password";
    public static final String BACKEND_TRUST_STORE_PATH_OLD_PROP_NAME = "connector.backend.ws.trust.trust-store.path";
    public static final String BACKEND_KEY_STORE_PW_OLD_PROP_NAM = "connector.backend.ws.key.key-store.password";
    public static final String BACKEND_KEY_STORE_PATH_OLD_PROP_NAME = "connector.backend.ws.key.key-store.path";
    public static final String BACKEND_PRIVATE_KEY_ALIAS_OLD_PROP_NAME = "connector.backend.ws.key.private-key.alias";
    public static final String BACKEND_PRIVATE_KEY_PW_OLD_PROP_NAME = "connector.backend.ws.key.private-key.password";


    private final Properties oldProperties;

    public Connector42LinkConfigTo43LinkConfigConverter(Properties oldProperties) {
        this.oldProperties = oldProperties;
    }

    public WsGatewayPluginConfigurationProperties convertGwLinkProperties() {
        WsGatewayPluginConfigurationProperties wsGatewayPluginConfigurationProperties = new WsGatewayPluginConfigurationProperties();
        wsGatewayPluginConfigurationProperties.setGwAddress(getOldRequiredProperty(GWL_GW_ADDRESS_OLD_PROP_NAME));
        wsGatewayPluginConfigurationProperties.setCxfLoggingEnabled(false);

        CxfTrustKeyStoreConfigurationProperties cxfProps = new CxfTrustKeyStoreConfigurationProperties();
        wsGatewayPluginConfigurationProperties.setSoap(cxfProps);
        cxfProps.setEncryptAlias(getOldRequiredProperty(GWL_ENCRYPT_ALIAS_OLD_PROP_NAME));

        StoreConfigurationProperties trustStore = new StoreConfigurationProperties();
        trustStore.setPassword(getOldRequiredProperty(GWL_TRUST_STORE_PW_OLD_PROP_NAME));
        trustStore.setPath(getOldRequiredProperty(GWL_TRUST_STORE_PATH_OLD_PROP_NAME));
        trustStore.setType("JKS");
        cxfProps.setTrustStore(trustStore);

        StoreConfigurationProperties keyStore = new StoreConfigurationProperties();
        keyStore.setType("JKS");
        keyStore.setPassword(getOldRequiredProperty(GWL_KEY_STORE_PW_OLD_PROP_NAM));
        keyStore.setPath(getOldRequiredProperty(GWL_KEY_STORE_PATH_OLD_PROP_NAME));
        cxfProps.setKeyStore(keyStore);

        KeyConfigurationProperties privateKeyConfig = new KeyConfigurationProperties();
        privateKeyConfig.setPassword(getOldRequiredProperty(GWL_PRIVATE_KEY_PW_OLD_PROP_NAME));
        privateKeyConfig.setAlias(getOldRequiredProperty(GWL_PRIVATE_KEY_ALIAS_OLD_PROP_NAME));
        cxfProps.setPrivateKey(privateKeyConfig);
        return wsGatewayPluginConfigurationProperties;

    }

    public WsBackendPluginConfigurationProperties convertBackendLinkProperties() {
        WsBackendPluginConfigurationProperties wsBackendPluginConfigurationProperties = new WsBackendPluginConfigurationProperties();
        wsBackendPluginConfigurationProperties.setCxfLoggingEnabled(false);

        CxfTrustKeyStoreConfigurationProperties cxfProps = new CxfTrustKeyStoreConfigurationProperties();
        wsBackendPluginConfigurationProperties.setSoap(cxfProps);

        StoreConfigurationProperties trustStore = new StoreConfigurationProperties();
        cxfProps.setTrustStore(trustStore);
        trustStore.setPassword(getOldRequiredProperty(BACKEND_TRUST_STORE_PW_OLD_PROP_NAME));
        trustStore.setPath(getOldRequiredProperty(BACKEND_TRUST_STORE_PATH_OLD_PROP_NAME));
        trustStore.setType("JKS");


        StoreConfigurationProperties keyStore = new StoreConfigurationProperties();
        cxfProps.setKeyStore(keyStore);
        keyStore.setType("JKS");
        keyStore.setPassword(getOldRequiredProperty(BACKEND_KEY_STORE_PW_OLD_PROP_NAM));
        keyStore.setPath(getOldRequiredProperty(BACKEND_KEY_STORE_PATH_OLD_PROP_NAME));

        KeyConfigurationProperties privateKeyConfig = new KeyConfigurationProperties();
        cxfProps.setPrivateKey(privateKeyConfig);
        privateKeyConfig.setPassword(getOldRequiredProperty(BACKEND_PRIVATE_KEY_PW_OLD_PROP_NAME));
        privateKeyConfig.setAlias(getOldRequiredProperty(BACKEND_PRIVATE_KEY_ALIAS_OLD_PROP_NAME));

        return wsBackendPluginConfigurationProperties;
    }

    private String getOldRequiredProperty(String oldPropName) {
        if (oldProperties.containsKey(oldPropName)) {
            return oldProperties.get(oldPropName).toString();
        } else {
            throw new IllegalArgumentException(String.format("The provided 'old' properties does not contain the property [%s], which is required!", oldPropName));
        }
    }
    
}
